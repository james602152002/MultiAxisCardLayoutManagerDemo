package com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
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
        int dp_margin = IPhone6ScreenResizeUtil.getPxValue(20);
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (holder instanceof HorizontalCardViewHolder) {
            MultiAxisCardAdapter adapter = (MultiAxisCardAdapter) parent.getAdapter();
            int[] bounds = adapter.getHorizontalCardsLeftmostRightMostBounds(position);
            outRect.left = bounds[0] == position ? (dp_margin << 1) : dp_margin;
            outRect.top = 0;
            outRect.right = bounds[1] == position ? (dp_margin << 1) : dp_margin;
            outRect.bottom = bounds[1] == adapter.getItemCount() - 1 ? (dp_margin << 1) : 0;
        } else {
            final int title_v_margin = IPhone6ScreenResizeUtil.getPxValue(30);
            outRect.left = dp_margin << 1;
            outRect.top = position != 0 ? title_v_margin : IPhone6ScreenResizeUtil.getPxValue(100);
            outRect.right = dp_margin << 1;
            outRect.bottom = title_v_margin;
        }
    }
}
