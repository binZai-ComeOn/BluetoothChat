package com.bin.youwei.bluetoothchat.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by BinYouWei on 2022/1/2
 */
public class BluetoothBean {
    private String state;
    private int bondState;
    private BluetoothDevice device;

    /**
     * 无需获得设备对其进行操作时使用
     * @param state
     */
    public BluetoothBean(String state) {
        this.state = state;
    }

    /**
     * 设备配对时使用
     * @param state
     * @param bondState
     */
    public BluetoothBean(String state,int bondState) {
        this.state = state;
        this.bondState = bondState;
    }

    /**
     * 获得设备对其进行操作时使用
     * @param state
     * @param device
     */
    public BluetoothBean(String state, BluetoothDevice device) {
        this.state = state;
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
