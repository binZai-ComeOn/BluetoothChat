package com.bin.youwei.bluetoothchat.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.bin.youwei.bluetoothchat.Config;
import com.bin.youwei.bluetoothchat.R;

import java.io.IOException;

/**
 * 开启服务端
 */
public class BluetoothServiceConnect extends Thread{
    private final BluetoothServerSocket serverSocket;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothConnectCallback.Service callback;

    public BluetoothServiceConnect(Context context,BluetoothConnectCallback.Service callback) {
        // 获得蓝牙适配器
        bluetoothAdapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        BluetoothServerSocket socket = null;
        try {
            // 参数为 SDP 记录的服务名称与UUID
            socket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(context.getString(R.string.app_name), Config.BluetoothUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = socket;
        this.callback = callback;
    }

    public void run() {
        BluetoothSocket socket = null;
        // 循环监听，直到出现异常或返回socket
        while (true){
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if (socket != null){
                // 连接成功后做的操作
                this.callback.success(socket);
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 关闭服务端
     */
    public void close(){
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
