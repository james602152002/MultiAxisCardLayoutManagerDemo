package com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.james602152002.multiaxiscardlayoutmanager.adapter.MultiAxisCardAdapter;
import com.james602152002.multiaxiscardlayoutmanager.viewholder.HorizontalCardViewHolder;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

/**
 * Created by shiki60215 on 18-1-31.
 */

public class MultiAxisCardDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        final short margin = 10;
//        final int dp_margin = dp2px(view, margin);
        int dp_margin = IPhone6ScreenResizeUtil.getPxValue(20);
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (holder instanceof HorizontalCardViewHolder) {
            MultiAxisCardAdapter adapter = (MultiAxisCardAdapter) parent.getAdapter();
            int[] bounds = adapter.getHorizontalCardsLeftmostRightMostBounds(position);
            Log.i("", "position =============  " + position);
            outRect.left = bounds[0] == position ?  (dp_margin << 1):dp_margin;
            outRect.top = 0;
            outRect.right = bounds[1] == position ? (dp_margin << 1):dp_margin;
            outRect.bottom = 0;
        } else {
            outRect.left = dp_margin;
            outRect.top = position != 0 ? IPhone6ScreenResizeUtil.getPxValue(2) : IPhone6ScreenResizeUtil.getPxValue(40);
            outRect.right = dp_margin;
            outRect.bottom = position != parent.getAdapter().getItemCount() - 1 ? IPhone6ScreenResizeUtil.getPxValue(2) :
                    dp_margin;
        }
    }
}
