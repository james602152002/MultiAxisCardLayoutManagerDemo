package com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CardDetailItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private ItemMoveAdapter mAdapter;

    public CardDetailItemTouchHelperCallBack(ItemMoveAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int drag_flag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(drag_flag,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
