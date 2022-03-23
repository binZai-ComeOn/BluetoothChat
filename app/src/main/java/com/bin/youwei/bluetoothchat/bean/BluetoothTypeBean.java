package com.bin.youwei.bluetoothchat.bean;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * 蓝牙类型实体类
 */
public class BluetoothTypeBean {
    private String type;
    private List<BluetoothDevice> bluetoothDevices;

    public BluetoothTypeBean(String type, List<BluetoothDevice> bluetoothDevices) {
        this.type = type;
        this.bluetoothDevices = bluetoothDevices;
    }

    public String getType() {
        return type;
    }

    public BluetoothTypeBean setType(String type) {
        this.type = type;
        return this;
    }

    public List<BluetoothDevice> getBluetoothDevices() {
        return bluetoothDevices;
    }

    public BluetoothTypeBean setBluetoothDevices(List<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
        return this;
    }
}
