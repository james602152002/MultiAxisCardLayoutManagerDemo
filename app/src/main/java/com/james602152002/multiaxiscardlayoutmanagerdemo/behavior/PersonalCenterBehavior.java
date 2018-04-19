package com.james602152002.multiaxiscardlayoutmanagerdemo.behavior;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.james602152002.multiaxiscardlayoutmanagerdemo.R;
import com.james602152002.multiaxiscardlayoutmanagerdemo.util.IPhone6ScreenResizeUtil;

public class PersonalCenterBehavior extends CoordinatorLayout.Behavior {

    private AppBarLayout.OnOffsetChangedListener offsetChangedListener;
    private float ratio = 0;
    private SimpleDraweeView avatar;
    private final int min_width, screen_width, min_height, max_height;

    public PersonalCenterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        min_width = max_height = IPhone6ScreenResizeUtil.getPxValue(400);
        min_height = IPhone6ScreenResizeUtil.getPxValue(80);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screen_width = IPhone6ScreenResizeUtil.getCurrentScreenWidth();
        } else {
            screen_width = (int) (IPhone6ScreenResizeUtil.getCurrentScreenWidth() + context.getResources().getDisplayMetrics().density * 30);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) dependency;
            initAppBarListener(appBarLayout);
            ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin = 0;
            final float percentage = (1 + ratio);
            updateChild(child, percentage);
            //Fix cardview api 19 margin problem
            final int margin_offset = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                    0 : (int) (child.getContext().getResources().getDisplayMetrics().density * 15  * 1.5f * (1 - percentage));
            child.setTranslationY(dependency.getBottom() - (child.getHeight() >> 1) * percentage - margin_offset);
        }
    }

    private void updateChild(View child, float percentage) {
        if (avatar == null) {
            avatar = child.findViewById(R.id.avatar);
        }
        avatar.setAlpha((float) Math.exp((1 - percentage) * -10));
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height = (int) (percentage * (max_height - min_height) + min_height);
        lp.width = (int) ((1 - percentage) * (screen_width - min_width) + min_width);
        child.requestLayout();
    }


    private void initAppBarListener(AppBarLayout appBarLayout) {
        if (offsetChangedListener == null) {
            offsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    ratio = (float) verticalOffset / appBarLayout.getTotalScrollRange();
                }
            };
            appBarLayout.addOnOffsetChangedListener(offsetChangedListener);
        }
    }

}
