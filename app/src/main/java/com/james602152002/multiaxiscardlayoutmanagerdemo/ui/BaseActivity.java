package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.view.Window;
import android.widget.FrameLayout;

import com.kelin.translucentbar.library.TranslucentBarManager;

/**
 * Created by shiki60215 on 18-3-5.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTransparentBar();
        initWindow();
    }

    private void initTransparentBar() {
        TranslucentBarManager manager = new TranslucentBarManager(this);
        manager.transparent(this);
    }

    private void initWindow() {
        final Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setAllowEnterTransitionOverlap(true);
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            window.setSharedElementExitTransition(new ChangeBounds());
            window.setSharedElementEnterTransition(new ChangeBounds());

//            Fade fade = new Fade();
//            fade.setMode(Visibility.MODE_IN);
//            fade.setDuration(500);
//            fade.excludeTarget(R.id.appbar, true);
//            fade.excludeTarget(android.R.id.statusBarBackground, true);
//            fade.excludeTarget(android.R.id.navigationBarBackground, true);

//            getWindow().setEnterTransition(fade);
//            getWindow().setExitTransition(fade);
        }
    }

    protected final void initToolBar(Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
            if (toolbar.getLayoutParams() instanceof FrameLayout.LayoutParams)
                ((FrameLayout.LayoutParams) toolbar.getLayoutParams()).topMargin = getStatusBarHeight();
            if (toolbar.getLayoutParams() instanceof ConstraintLayout.LayoutParams)
                ((ConstraintLayout.LayoutParams) toolbar.getLayoutParams()).topMargin = getStatusBarHeight();
        } else {
            getSupportActionBar().hide();
        }
    }

    protected final int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
