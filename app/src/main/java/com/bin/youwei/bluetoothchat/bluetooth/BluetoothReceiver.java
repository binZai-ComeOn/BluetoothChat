package com.bin.youwei.bluetoothchat.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.bin.youwei.bluetoothchat.bean.BluetoothBean;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

/**
 * 监听发现蓝牙的广播
 *
 * Created by BinYouWei on 2022/1/10
 */
public class BluetoothReceiver extends BroadcastReceiver {
    private String TAG = "BlueToothReceiver";

    /**
     * 设置Intent过滤器
     *
     * @return
     */
    public static IntentFilter markFilter() {
        IntentFilter filter = new IntentFilter();
        // 找到设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        // 远程设备的绑定状态发生变化
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 第一次检索远程设备的友好名称，或自上次检索后更改
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        // 远程设备的蓝牙类已更改
        filter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        // 用于在获取远程设备后将其UUID 作为ParcelUuid远程设备的包装进行广播
        filter.addAction(BluetoothDevice.ACTION_UUID);
        // 操作配对请求
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        // 蓝牙状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 开始搜索蓝牙
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // 蓝牙搜索完成
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 本地Adapter的蓝牙扫描模式发生了变化
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        // 本地蓝牙适配器更改蓝牙名称
        filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        // 请求本地蓝牙可被其它蓝牙扫描到
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 显示请求可发现模式的系统活动
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 连接状态改变
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BluetoothDevice.ACTION_FOUND:
                // 找到设备
                Log.d(TAG, "ACTION_FOUND");
                BluetoothDevice device =
                        (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName() != null){
                    EventBus.getDefault().post(new BluetoothBean(BluetoothDevice.ACTION_FOUND,device));
                }
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                // 远程设备的绑定状态发生变化
                Log.d(TAG, "ACTION_BOND_STATE_CHANGED");
                switch (((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)).getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        EventBus.getDefault().post(new BluetoothBean(BluetoothDevice.ACTION_BOND_STATE_CHANGED, BluetoothDevice.BOND_BONDING));
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        EventBus.getDefault().post(new BluetoothBean(BluetoothDevice.ACTION_BOND_STATE_CHANGED, BluetoothDevice.BOND_BONDED));
                        break;
                }
                break;
            case BluetoothDevice.ACTION_NAME_CHANGED:
                // 第一次检索远程设备的友好名称，或自上次检索后更改
                Log.d(TAG, "ACTION_NAME_CHANGED");
                break;
            case BluetoothDevice.ACTION_CLASS_CHANGED:
                // 远程设备的蓝牙类已更改
                Log.d(TAG, "ACTION_CLASS_CHANGED");
                break;
            case BluetoothDevice.ACTION_UUID:
                // 用于在获取远程设备后将其UUID 作为ParcelUuid远程设备的包装进行广播
                Log.d(TAG, "ACTION_UUID");
                break;
            case BluetoothDevice.ACTION_PAIRING_REQUEST:
                // 操作配对请求
                Log.d(TAG, "ACTION_PAIRING_REQUEST");
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                // 蓝牙状态改变
                Log.d(TAG, "ACTION_STATE_CHANGED");
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                // 开始搜索蓝牙
                Log.d(TAG, "ACTION_DISCOVERY_STARTED");
                EventBus.getDefault().post(new BluetoothBean(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                // 蓝牙搜索完成
                Log.d(TAG, "ACTION_DISCOVERY_FINISHED");
                EventBus.getDefault().post(new BluetoothBean(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
                break;
            case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                // 本地Adapter的蓝牙扫描模式发生了变化
                Log.d(TAG, "ACTION_SCAN_MODE_CHANGED");
                break;
            case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
                // 本地蓝牙适配器更改蓝牙名称
                Log.d(TAG, "ACTION_LOCAL_NAME_CHANGED");
                break;
            case BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE:
                // 请求本地蓝牙可被其它蓝牙扫描到
                Log.d(TAG, "ACTION_REQUEST_DISCOVERABLE");
                break;
            case BluetoothAdapter.ACTION_REQUEST_ENABLE:
                // 显示请求可发现模式的系统活动
                Log.d(TAG, "ACTION_REQUEST_ENABLE");
                break;
            case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                // 连接状态改变
                Log.d(TAG, "ACTION_CONNECTION_STATE_CHANGED");
                break;
        }
    }
}
