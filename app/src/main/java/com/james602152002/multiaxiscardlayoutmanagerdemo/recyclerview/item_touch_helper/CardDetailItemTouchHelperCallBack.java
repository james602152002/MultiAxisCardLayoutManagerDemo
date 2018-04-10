package com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_touch_helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class CardDetailItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private ItemMoveAdapter mAdapter;

    public CardDetailItemTouchHelperCallBack(ItemMoveAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int drag_flag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipe_flag = ItemTouchHelper.LEFT;
        return makeMovementFlags(drag_flag,swipe_flag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return 1.5f;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return defaultValue * 100;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.swipe(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
//            return;
        Log.i("", "on child draw");
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        recyclerView.scrollBy(0,(int)dY / 100);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.i("", "select changed");
        super.onSelectedChanged(viewHolder, actionState);
    }
}
