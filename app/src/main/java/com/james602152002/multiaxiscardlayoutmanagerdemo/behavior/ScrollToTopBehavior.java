package com.james602152002.multiaxiscardlayoutmanagerdemo.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class ScrollToTopBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> implements View.OnTouchListener {

    private float dest_y;
    private FloatingActionButton actionButton;
    private ObjectAnimator transitionAnimator;
    private int parent_height;
    private float action_btn_height;
    private boolean show = false;
    private RecyclerView.OnScrollListener listener;

    public ScrollToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        actionButton = child;
        if (target instanceof SmartRefreshLayout || target instanceof RecyclerView) {
            return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof SmartRefreshLayout) {
            final RecyclerView recyclerView = (RecyclerView) ((SmartRefreshLayout) target).getChildAt(0);
            initListener(recyclerView, child, dy);
        } else if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            initListener(recyclerView, child, dy);
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    private void initListener(RecyclerView recyclerView, FloatingActionButton child, int dy) {
        if (listener == null) {
            recyclerView.setOnTouchListener(this);
            listener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!recyclerView.canScrollVertically(-1)) {
                            ScrollToTopBehavior.this.show = false;
                            setFloatingBtnTransition(0);
                        }
                    }
                }
            };
            recyclerView.addOnScrollListener(listener);
        }
        if (dy < 0 && recyclerView.canScrollVertically(-1)) {
            toggleFloatingBtn(child, true, dy);
        } else if (dy > 0 && recyclerView.canScrollVertically(1)) {
            toggleFloatingBtn(child, false, dy);
        }
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, FloatingActionButton child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        final int dest_x = parent.getWidth() - child.getWidth() - lp.rightMargin;
        parent_height = parent.getHeight();
        action_btn_height = child.getHeight();
        dest_y = parent_height - action_btn_height - lp.bottomMargin;
        child.setTranslationX(dest_x);
        child.setTranslationY(parent_height);
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    private void toggleFloatingBtn(View child, boolean show) {
        child.setClickable(true);
        if (transitionAnimator != null || (show && actionButton.getTranslationY() == dest_y)
                || (!show && actionButton.getTranslationY() == parent_height)) {
            return;
        }
        final float width = parent_height - dest_y;
        final float current_ratio = 1 - ((child.getTranslationY() - dest_y) / width);
        transitionAnimator = ObjectAnimator.ofFloat(this, "floatingBtnTransition", current_ratio, show ? 1 : 0);
        transitionAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                transitionAnimator.removeListener(this);
                transitionAnimator = null;
            }
        });
        transitionAnimator.setDuration(300);
        transitionAnimator.start();
    }

    private void toggleFloatingBtn(View child, boolean show, int dy) {
        this.show = show;
        child.setClickable(true);
        final float width = parent_height - dest_y;
        float ratio = 1 - ((child.getTranslationY() + dy - dest_y) / width);
        if (ratio > 1)
            ratio = 1;
        if (ratio < 0)
            ratio = 0;
        setFloatingBtnTransition(ratio);
    }

    private void setFloatingBtnTransition(float ratio) {
        actionButton.setAlpha(ratio);
        final float width = parent_height - dest_y;
        actionButton.setTranslationY((dest_y - parent_height) * ratio + parent_height);
        ratio = (parent_height - actionButton.getTranslationY() - action_btn_height) / (width - action_btn_height);
        if (ratio < 0)
            ratio = 0;
        actionButton.setRotation((1 - ratio) * 180);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                toggleFloatingBtn(actionButton, show);
                break;
        }
        return false;
    }


}
