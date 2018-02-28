package com.james602152002.multiaxiscardlayoutmanagerdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by shiki60215 on 18-2-28.
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
}
