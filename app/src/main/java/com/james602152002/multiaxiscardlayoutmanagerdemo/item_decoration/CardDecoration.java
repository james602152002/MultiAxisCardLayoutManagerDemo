package com.james602152002.multiaxiscardlayoutmanagerdemo.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.james602152002.multiaxiscardlayoutmanager.viewholder.HorizontalCardViewHolder;

/**
 * Created by shiki60215 on 18-1-31.
 */

public class CardDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final short margin = 10;
        final int dp_margin = dp2px(view, margin);
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (holder instanceof HorizontalCardViewHolder) {
            outRect.left = dp_margin;
            outRect.right = dp_margin;
        } else {
            outRect.left = dp_margin;
            outRect.right = dp_margin;
        }
        outRect.top = position != 0 ? dp_margin : (dp_margin << 1);
        outRect.bottom = position != parent.getAdapter().getItemCount() - 1 ? dp_margin :
                (dp_margin << 1);
    }

    private int dp2px(View view, float dpValue) {
        return (int) (0.5f + dpValue * view.getResources().getDisplayMetrics().density);
    }

}
