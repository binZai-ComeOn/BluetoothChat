package com.bin.youwei.bluetoothchat.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bin.youwei.bluetoothchat.MainActivity;
import com.bin.youwei.bluetoothchat.R;
import com.bin.youwei.bluetoothchat.bean.BluetoothBean;
import com.bin.youwei.bluetoothchat.bean.BluetoothTypeBean;

import java.util.List;

/**
 * Created by BinYouWei on 2022/1/2
 *
 * 蓝牙分类适配器
 */
public class BluetoothTypeAdapter extends RecyclerView.Adapter<BluetoothTypeAdapter.MyViewHolder> {
    private Context context;
    private List<BluetoothTypeBean> lists;
    private MainActivity.Click click;

    public BluetoothTypeAdapter(Context context, List<BluetoothTypeBean> lists,MainActivity.Click click) {
        this.context = context;
        this.lists = lists;
        this.click = click;
    }

    public void updata(List<BluetoothTypeBean> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_type,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BluetoothTypeBean bean = lists.get(position);
        holder.type.setText(bean.getType());
        holder.bluetooths.setLayoutManager(new LinearLayoutManager(context));
        holder.bluetooths.setAdapter(new BluetoothSortAdapter(context, bean.getBluetoothDevices(),new MainActivity.Click(){
            @Override
            public void onClick(BluetoothDevice device) {
                click.onClick(device);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return lists != null ? lists.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView type;
        private RecyclerView bluetooths;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            bluetooths = itemView.findViewById(R.id.bluetooths);
        }
    }
}
