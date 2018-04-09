package com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

/**
 * Created by shiki60215 on 18-3-7.
 */

public class CardDetailDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int last_position = parent.getAdapter().getItemCount() - 1;
        final int child_position = parent.getChildAdapterPosition(view);
        final int margin = IPhone6ScreenResizeUtil.getPxValue(20);
        if (child_position == 0) {
            outRect.top = IPhone6ScreenResizeUtil.getPxValue(60);
            outRect.bottom = child_position == last_position ? margin << 1 : margin;
        } else if (outRect.bottom == last_position) {
            outRect.top = margin;
            outRect.bottom = margin << 1;
        } else {
            outRect.top = margin;
            outRect.bottom = margin;
        }
        outRect.left = margin;
        outRect.right = margin;
    }
}
