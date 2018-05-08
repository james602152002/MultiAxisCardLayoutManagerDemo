package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public class GaussianBlur {

    private static ByteBuffer mByteBuffer = null;

    static {
        System.loadLibrary("GaussianBlur");
    }

    public GaussianBlur storeBitmap(Bitmap bitmap) {
        mByteBuffer = jniStoreBitmap(bitmap);
        return this;
    }

    public GaussianBlur initSdk() {
        if (mByteBuffer != null)
            jniInitSdk(mByteBuffer);
        return this;
    }

    private native ByteBuffer jniStoreBitmap(Bitmap bitmap);

    private native void jniInitSdk(ByteBuffer byteBuffer);
}
