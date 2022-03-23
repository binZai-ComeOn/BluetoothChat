package com.bin.youwei.bluetoothchat.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.bin.youwei.bluetoothchat.R;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by BinYouWei on 2022/1/2
 */
public class Bluetooth {
    private static Bluetooth bluetooth;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothReceiver receiver;
    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public static Bluetooth getInstance() {
        if (bluetooth == null) {
            synchronized (Bluetooth.class) {
                if (bluetooth == null) {
                    bluetooth = new Bluetooth();
                }
            }
        }
        return bluetooth;
    }

    /**
     * 作为客户端连接服务端蓝牙
     *
     * @param device
     */
    public void connectServiceBluetooth(BluetoothDevice device) {
        BluetoothClientConnect bluetoothClient = new BluetoothClientConnect(context, device, new BluetoothConnectCallback.Client() {
            @Override
            public void connectSuccess(BluetoothSocket socket) {
                Log.d("BluetoothSortAdapter", "连接成功:" + socket.getRemoteDevice().getName());
            }

            @Override
            public void connectFailed(String errorMsg) {
                Log.d("BluetoothSortAdapter", "连接失败：" + errorMsg);
            }

            @Override
            public void connectCancel() {
                Log.d("BluetoothSortAdapter", "连接取消");
            }
        });
        bluetoothClient.start();
    }

    /**
     * 获得蓝牙适配器
     *
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    /**
     * 获得已经配对过的设备
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return getBluetoothAdapter().getBondedDevices();
    }

    /**
     * 开始扫描蓝牙
     *
     * @return
     */
    public void scanBluetooth(Context context) {
        context.registerReceiver(getBluetoothReceiver(), BluetoothReceiver.markFilter());
        if (getBluetoothAdapter().isDiscovering()) {
            getBluetoothAdapter().cancelDiscovery();
        }
        getBluetoothAdapter().startDiscovery();
    }

    /**
     * 取消扫描蓝牙
     *
     * @param context
     */
    public void cancelScanBluetooth(Context context) {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            context.unregisterReceiver(getBluetoothReceiver());
        }
    }

    /**
     * 获得一个蓝牙蓝牙接收者
     *
     * @return
     */
    public BluetoothReceiver getBluetoothReceiver() {
        if (receiver == null) {
            receiver = new BluetoothReceiver();
        }
        return receiver;
    }

    /**
     * 转换蓝牙绑定的状态
     *
     * @param bondState
     * @return
     */
    public String getBondState(int bondState) {
        return bondState == BluetoothDevice.BOND_NONE ?
                context.getString(R.string.BOND_NONE) :
                bondState == BluetoothDevice.BOND_BONDING ?
                        context.getString(R.string.BOND_BONDING) :
                        context.getString(R.string.BOND_BONDED);
    }

    /**
     * 蓝牙配对
     *
     * @param device
     */
    public void pairBluetooth(BluetoothDevice device) {
        try {
            //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
            device.createBond();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消蓝牙配对
     *
     * @param device
     */
    public boolean cancelPairBluetooth(BluetoothDevice device) {
        try {
            //如果想要配对的设备，只需要将removeBond改为creatBond
            Method method = BluetoothDevice.class.getMethod("removeBond");
            return  (boolean) method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 转换蓝牙绑定的类型
     *
     * @param type
     * @return
     */
    public String getBluetoothType(int type) {
        return type == BluetoothDevice.DEVICE_TYPE_CLASSIC ?
                context.getString(R.string.DEVICE_TYPE_CLASSIC) :
                type == BluetoothDevice.DEVICE_TYPE_DUAL ?
                        context.getString(R.string.DEVICE_TYPE_DUAL) :
                        type == BluetoothDevice.DEVICE_TYPE_LE ?
                                context.getString(R.string.DEVICE_TYPE_LE) :
                                context.getString(R.string.DEVICE_TYPE_UNKNOWN);
    }
}
