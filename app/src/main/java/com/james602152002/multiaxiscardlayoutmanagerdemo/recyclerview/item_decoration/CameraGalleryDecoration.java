package com.james602152002.multiaxiscardlayoutmanagerdemo.recyclerview.item_decoration;

import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

public class CameraGalleryDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int pos = parent.getChildAdapterPosition(view);
            final int count = parent.getAdapter().getItemCount();
            final short margin = (short) IPhone6ScreenResizeUtil.getPxValue(30);
            final short semi_margin = (short) (margin >> 1);
            final int span_index = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            switch (span_index) {
                case 0:
                    outRect.left = margin;
                    outRect.right = semi_margin;
                    break;
                default:
                    outRect.left = semi_margin;
                    outRect.right = margin;
                    break;
            }
            switch (pos % 2) {
                case 0:
                    if (pos == count - 1 || pos == count - 2) {
                        outRect.bottom = margin;
                    } else {
                        outRect.bottom = semi_margin;
                    }
                    break;
                default:
                    if (pos == count - 1) {
                        outRect.bottom = margin;
                    } else {
                        outRect.bottom = semi_margin;
                    }
                    break;
            }
            if (pos / 2 == 0) {
                outRect.top = margin;
            } else {
                outRect.top = semi_margin;
            }
        }
    }
}
