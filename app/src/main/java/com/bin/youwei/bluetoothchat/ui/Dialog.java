package com.bin.youwei.bluetoothchat.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bin.youwei.bluetoothchat.R;

/**
 * Created by BinYouWei on 2022/1/14
 */
public class Dialog {
    private static Dialog dialog;
    private AlertDialog alertDialog;

    public static Dialog getInstance() {
        if (dialog == null) {
            synchronized (Dialog.class) {
                if (dialog == null) {
                    dialog = new Dialog();
                }
            }
        }
        return dialog;
    }

    public void showBottomInfoDialog(Context context, String name, String address, String type,
                                     String uuid, String bindstate) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom, null, false);
        alertDialog = new AlertDialog.Builder(context).setView(view).create();
        ((TextView) view.findViewById(R.id.name)).setText(name);
        ((TextView) view.findViewById(R.id.address)).setText(address);
        ((TextView) view.findViewById(R.id.type)).setText(type);
        ((TextView) view.findViewById(R.id.uuid)).setText(uuid);
        ((TextView) view.findViewById(R.id.bindstate)).setText(bindstate);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        // 设置背景透明，防止直角显示
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.popupAnimation);

    }

}
