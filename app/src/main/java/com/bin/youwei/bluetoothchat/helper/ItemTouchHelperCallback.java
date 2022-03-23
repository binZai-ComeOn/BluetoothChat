package com.bin.youwei.bluetoothchat.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by BinYouWei on 2022/1/16
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private RecyclerView.Adapter adapter;

    public ItemTouchHelperCallback(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 指定可以支持的拖放和滑动的方向，上下为拖动（drag），左右为滑动（swipe）
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        Log.d("ItemTouchHelperCallback", "拖动"+viewHolder.getAdapterPosition());
        Log.d("ItemTouchHelperCallback", "拖动1"+viewHolder.getOldPosition());
        Log.d("ItemTouchHelperCallback", "拖动2"+viewHolder.getLayoutPosition());
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = 2 | ItemTouchHelper.LEFT;
            return makeMovementFlags(0,ItemTouchHelper.LEFT);
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
