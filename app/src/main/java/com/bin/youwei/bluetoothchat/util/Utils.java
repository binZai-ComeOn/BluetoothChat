package com.bin.youwei.bluetoothchat.util;

import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.view.View;

import com.bin.youwei.bluetoothchat.bean.BluetoothTypeBean;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Created by BinYouWei on 2022/1/2
 */
public class Utils {
    private static Utils utils;

    public static Utils getInstance() {
        if (utils == null){
            synchronized (Utils.class){
                if (utils == null){
                    utils = new Utils();
                }
            }
        }
        return utils;
    }

    /**
     * 检查蓝牙是否开启
     */
    public boolean checkBluetoothState(){
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        // 是否支持蓝牙模块
        if (defaultAdapter != null) {
            if (defaultAdapter.isEnabled()){
                return true;
            }
        }
        return false;
    }

    public void showSnackbar(View view,String content){
        Snackbar.make(view,content,Snackbar.LENGTH_LONG).show();
    }
}
