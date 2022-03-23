package com.bin.youwei.bluetoothchat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bin.youwei.bluetoothchat.adapter.BluetoothSortAdapter;
import com.bin.youwei.bluetoothchat.adapter.BluetoothTypeAdapter;
import com.bin.youwei.bluetoothchat.base.BaseActivity;
import com.bin.youwei.bluetoothchat.bean.BluetoothBean;
import com.bin.youwei.bluetoothchat.bean.BluetoothTypeBean;
import com.bin.youwei.bluetoothchat.bluetooth.Bluetooth;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothClientConnect;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothConnectCallback;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothServiceConnect;
import com.bin.youwei.bluetoothchat.databinding.ActivityMainBinding;
import com.bin.youwei.bluetoothchat.ui.Dialog;
import com.bin.youwei.bluetoothchat.util.Utils;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity<ActivityMainBinding> implements MenuItem.OnMenuItemClickListener, View.OnClickListener {
    private List<BluetoothTypeBean> allList;
    private List<BluetoothDevice> list;
    private BluetoothTypeAdapter bluetoothTypeAdapter;
    private MenuItem menuItem;
    private BluetoothServiceConnect bluetoothService;
    private AlertDialog.Builder dialog;
    private String[] NoneBound;
    private String[] Bound;

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.getInstance().checkBluetoothState()) {
            Utils.getInstance().showSnackbar(binding.getRoot(), "蓝牙功能未启用");
            return;
        }
    }

    @Override
    public void initView(View view) {
        checkPermission();
        Bluetooth.getInstance().init(this);
        Bound = new String[]{getString(R.string.PairCancel), getString(R.string.BluetoothInfo), getString(R.string.ClicenConnection)};
        NoneBound = new String[]{getString(R.string.PAIR), getString(R.string.BluetoothInfo), getString(R.string.ClicenConnection)};
        allList = new ArrayList<>();
        list = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Config.bluetoothType.forEach((action) -> {
            allList.add(new BluetoothTypeBean(action, new ArrayList<>()));
        });
        bluetoothTypeAdapter = new BluetoothTypeAdapter(this, allList, new Click() {
            @Override
            public void onClick(BluetoothDevice device) {
                showAlertDialog(device);
            }
        });
        binding.recyclerView.setAdapter(bluetoothTypeAdapter);
        // 设置侧滑
        /*ItemTouchHelperCallback itemTouchHelperCallBack = new ItemTouchHelperCallback
        (bluetoothAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        itemTouchHelper.getItemOffsets(()->{

        },);*/
        // 禁止组件下拉
        binding.refresh.setEnabled(false);
        // 注册事件订阅
        EventBus.getDefault().register(this);
    }

    @Override
    public void setListener() {
        binding.controlServer.setOnClickListener(this);
    }

    /**
     * EventBus订阅事件回调
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(BluetoothBean bean) {
        Log.d("蓝牙", bean.getState());
        switch (bean.getState()) {
            case BluetoothDevice.ACTION_FOUND:
                // 找到设备,过滤相同设备
                List<BluetoothDevice> collect =
                        allList.get(1).getBluetoothDevices().stream().filter((v) -> !v.getAddress().equals(bean.getDevice().getAddress())).collect(Collectors.toList());
                // 过滤已配对的设备
                if (bean.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    collect.add(bean.getDevice());
                }
                allList.get(1).setBluetoothDevices(collect);
                bluetoothTypeAdapter.updata(allList);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                // 开始查找
                Utils.getInstance().showSnackbar(binding.getRoot(), "开始扫描");
                startScan();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                // 查找完成
                binding.refresh.setRefreshing(false);
                Utils.getInstance().showSnackbar(binding.getRoot(), "查找完成");
                stopScan();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.controlServer:
                Button button = (Button) v;
                String s = button.getText().toString();
                if (s.equals(getString(R.string.serviceConnect))) {
                    // 点击打开的操作
                    button.setText(getString(R.string.serviceClose));
                    binding.refresh.setVisibility(View.INVISIBLE);
                    connectClientBluetooth();
                } else {
                    // 点击关闭的操作
                    button.setText(R.string.serviceConnect);
                    binding.refresh.setVisibility(View.VISIBLE);
                    bluetoothService.close();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.refresh);
        MenuItem discoverable = menu.findItem(R.id.discoverable);
        refresh.setOnMenuItemClickListener(this);
        discoverable.setOnMenuItemClickListener(this);
        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * 服务端开启服务端蓝牙
     */
    private void connectClientBluetooth() {
        bluetoothService = new BluetoothServiceConnect(this, new BluetoothConnectCallback.Service() {
            @Override
            public void success(BluetoothSocket socket) {

            }

            @Override
            public void cancel() {

            }
        });
        bluetoothService.start();
    }

    private void showAlertDialog(BluetoothDevice device) {
        dialog = new AlertDialog.Builder(this);
        String[] item;
        if (Bluetooth.getInstance().getBondState(device.getBondState()).equals(getString(R.string.BOND_NONE))) {
            item = NoneBound;
        } else {
            item = Bound;
        }
        dialog.setItems(item, (DialogInterface dialogInterface, int i) -> {
            String name = item[i];
            if (name.equals(getString(R.string.PairCancel))) {
                if (Bluetooth.getInstance().cancelPairBluetooth(device)) {
                    refreshBluetooth();
                    Utils.getInstance().showSnackbar(binding.getRoot(), "取消了与" + device.getName() + "的配对");
                } else {
                    Utils.getInstance().showSnackbar(binding.getRoot(), "取消与" + device.getName() + "的配对失败");
                }
            } else if (name.equals(getString(R.string.PAIR))) {
                Bluetooth.getInstance().pairBluetooth(device);
            } else if (name.equals(getString(R.string.ClicenConnection))) {
                Bluetooth.getInstance().connectServiceBluetooth(device);
            } else if (name.equals(getString(R.string.BluetoothInfo))) {
                // 底部弹出对话框
                Dialog.getInstance().showBottomInfoDialog(this, name,
                        device.getAddress(), String.valueOf(Bluetooth.getInstance().getBluetoothType(device.getType())),
                        String.valueOf(device.getUuids()), Bluetooth.getInstance().getBondState(device.getBondState()));
            }
        });
        dialog.create().show();
    }

    /**
     * 右上角菜单点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.discoverable:
                // 允许其它蓝牙发现自己
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivity(enabler);
                break;
            case R.id.refresh:
                this.menuItem = menuItem;
                refreshBluetooth();
                break;
        }
        return false;
    }

    /**
     * 刷新蓝牙
     */
    private void refreshBluetooth() {
        if (menuItem.getTitle().equals(getString(R.string.refresh))) {
            Bluetooth.getInstance().scanBluetooth(this);
        } else {
            Bluetooth.getInstance().cancelScanBluetooth(this);
            stopScan();
        }
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        binding.refresh.setRefreshing(false);
        menuItem.setTitle(getString(R.string.refresh));
        menuItem.setIcon(getDrawable(R.drawable.ic_refresh));
    }

    /**
     * 开始扫描
     */
    private void startScan() {
        binding.refresh.setRefreshing(true);
        menuItem.setTitle(getString(R.string.stop));
        menuItem.setIcon(getDrawable(R.drawable.ic_stop));
        for (int i = 0; i < allList.size(); i++) {
            BluetoothTypeBean bean = allList.get(i);
            if (bean.getType().equals(getString(R.string.BOND_BONDED))) {
                allList.get(i).setBluetoothDevices(Bluetooth.getInstance().getBondedDevices().stream().collect(Collectors.toList()));
            }
        }
        bluetoothTypeAdapter.updata(allList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bluetooth.getInstance().cancelScanBluetooth(this);
        // 销毁事件订阅
        EventBus.getDefault().unregister(this);
    }

    public void checkPermission() {
        MainActivityPermissionsDispatcher.requestPermissionsWithPermissionCheck(this);
    }

    /**
     * 用户同意给予权限时运行
     */
    @NeedsPermission({
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.ACCESS_FINE_LOCATION
    })
    public void requestPermissions() {
        Bluetooth.getInstance().scanBluetooth(this);
    }

    /**
     * 申请权限被拒绝时执行
     */
    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void permissionLocation() {
        Snackbar.make(binding.getRoot(), "获取定位失败", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    public interface Click {
        void onClick(BluetoothDevice device);
    }
}