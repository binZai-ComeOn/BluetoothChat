package com.bin.youwei.bluetoothchat.bluetooth;

import android.bluetooth.BluetoothSocket;

/**
 * Created by BinYouWei on 2022/1/2
 */
public class BluetoothConnectCallback {

    public interface Service{

        void success(BluetoothSocket socket);

        void cancel();
    }

    public interface Client{

        void connectSuccess(BluetoothSocket socket);

        void connectFailed(String errorMsg);

        void connectCancel();
    }
}