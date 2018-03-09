package com.james602152002.multiaxiscardlayoutmanagerdemo.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.view.Window;

/**
 * Created by shiki60215 on 18-3-5.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    private void initWindow() {
        final Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setAllowEnterTransitionOverlap(true);
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            window.setSharedElementExitTransition(new ChangeBounds());
            window.setSharedElementEnterTransition(new ChangeBounds());
//            window.setEnterTransition(null);
//            window.setExitTransition(null);
        }

        window.getDecorView().setBackgroundColor(0);
    }
}
