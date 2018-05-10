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

    public GaussianBlur initSdk(double sigma, int radius) {
        if (mByteBuffer != null)
            jniInitSdk(mByteBuffer,sigma, radius);
        return this;
    }

    public Bitmap getBitmap() {
        if (mByteBuffer == null)
            return null;
        return jniGetBitmap(mByteBuffer);
    }

    public native void unInit();

    private native ByteBuffer jniStoreBitmap(Bitmap bitmap);

    private native void jniInitSdk(ByteBuffer byteBuffer, double sigma, int radius);

    private native Bitmap jniGetBitmap(ByteBuffer mByteBuffer);
}
