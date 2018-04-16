package com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

public class SvgCardDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int pos = parent.getChildAdapterPosition(view);
        final short h_margin = (short) IPhone6ScreenResizeUtil.getPxValue(30);
        final short v_margin = (short) IPhone6ScreenResizeUtil.getPxValue(40);
        final short semi_v_margin = (short) (v_margin >> 1);
        outRect.left = h_margin;
        outRect.top = pos == 0 ? v_margin : semi_v_margin;
        outRect.right = h_margin;
        outRect.bottom = pos == parent.getAdapter().getItemCount() - 1 ? v_margin : semi_v_margin;
    }
}
