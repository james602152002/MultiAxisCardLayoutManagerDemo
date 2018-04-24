package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;

public class SmoothScrollUtil {

    public static void smoothScrollToTop(final RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView != null && recyclerView.getLayoutManager().isSmoothScrolling()) {
                        recyclerView.scrollToPosition(0);
                    }
                }
            },1500);
        }
    }
}
