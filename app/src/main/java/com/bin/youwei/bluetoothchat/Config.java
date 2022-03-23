package com.bin.youwei.bluetoothchat;

import android.Manifest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by BinYouWei on 2022/1/2
 */
public class Config {

    /**
     * 蓝牙的UUID
     */
    public static final UUID BluetoothUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    /**
     * 蓝牙扫描周期
     */
    public static final int BLUETOOTH_SCAN_CYCLE = 5000;
    /**
     * 蓝牙类型
     */
    public static final List<String> bluetoothType = Arrays.asList("已配对","未配对");
}
