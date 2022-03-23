package com.bin.youwei.bluetoothchat.adapter;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.youwei.bluetoothchat.MainActivity;
import com.bin.youwei.bluetoothchat.R;
import com.bin.youwei.bluetoothchat.bluetooth.Bluetooth;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothClientConnect;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothConnectCallback;
import com.bin.youwei.bluetoothchat.bluetooth.BluetoothServiceConnect;
import com.bin.youwei.bluetoothchat.ui.Dialog;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by BinYouWei on 2022/1/2
 * <p>
 * 蓝牙列表适配器
 */
public class BluetoothSortAdapter extends RecyclerView.Adapter<BluetoothSortAdapter.MyViewHolder> {
    private Context context;
    private List<BluetoothDevice> lists;
    private Resources resources;
    private MainActivity.Click click;

    public BluetoothSortAdapter(Context context, List<BluetoothDevice> lists, MainActivity.Click click) {
        this.context = context;
        this.lists = lists;
        this.click = click;
        resources = context.getResources();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BluetoothDevice device = lists.get(position);
        holder.name.setText(device.getName());
        holder.state.setText(Bluetooth.getInstance().getBondState(device.getBondState()));
        holder.body.setOnClickListener((v) -> {
            click.onClick(device);
        });
    }

    @Override
    public int getItemCount() {
        return lists != null ? lists.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView state;
        private ConstraintLayout body;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            state = itemView.findViewById(R.id.state);
            body = itemView.findViewById(R.id.body);
        }
    }
}
