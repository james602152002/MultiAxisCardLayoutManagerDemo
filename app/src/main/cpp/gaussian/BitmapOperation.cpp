//
// Created by shiki60215 on 18-5-8.
//

#include <cstring>
#include <android/bitmap.h>
#include "BitmapOperation.h"

jobject BitmapOperation::jniStoreBitmapData(JNIEnv *env, jobject obj, jobject bitmap) {
    AndroidBitmapInfo bitmapInfo;
    uint32_t *storeBitmapPixels = NULL;
    if (AndroidBitmap_getInfo(env, bitmap, &bitmapInfo) < 0) {
        return NULL;
    }

    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return NULL;
    }

    //
    //read pixels of bitmap into native memory :
    //

    void *bitmapPixels;

    if (AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels) < 0) {
        return NULL;
    }
    uint32_t *src = (uint32_t *) bitmapPixels;
    int pixelsCount = bitmapInfo.width * bitmapInfo.height;
    storeBitmapPixels = new uint32_t[pixelsCount];
    memcpy(storeBitmapPixels, src, sizeof(uint32_t) * pixelsCount);
    AndroidBitmap_unlockPixels(env, bitmap);
    JniBitmap *jniBitmap = new JniBitmap();
    jniBitmap->_bitmapInfo = bitmapInfo;
    jniBitmap->_storedBitmapPixels = storeBitmapPixels;
    return env->NewDirectByteBuffer(jniBitmap, 0);
}

jobject BitmapOperation::jniGetBitmapFromStoredBitmapData(JNIEnv *env, jobject obj,
                                                          jobject handle) {
    JniBitmap *jniBitmap = (JniBitmap *) env->GetDirectBufferAddress(handle);
    if (jniBitmap->_storedBitmapPixels == NULL) {
        return NULL;
    }

    //
    //creating a new bitmap to put the pixels into it - using Bitmap Bitmap.createBitmap (int width, int height, Bitmap.Config config) :
    //
    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls,
                                                            "createBitmap",
                                                            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass,
                                                                   "valueOf",
                                                                   "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass,
                                                       valueOfBitmapConfigFunction, configName);
    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls, createBitmapFunction,
                                                    jniBitmap->_bitmapInfo.width,
                                                    jniBitmap->_bitmapInfo.height, bitmapConfig);

    void *bitmapPixels;
    if (AndroidBitmap_lockPixels(env, newBitmap, &bitmapPixels) < 0) {
        return NULL;
    }

    uint32_t *newBitmapPixels = (uint32_t *) bitmapPixels;
    int pixelsCount = jniBitmap->_bitmapInfo.width * jniBitmap->_bitmapInfo.height;
    memcpy(newBitmapPixels, jniBitmap->_storedBitmapPixels,
           sizeof(uint32_t) * pixelsCount);
    AndroidBitmap_unlockPixels(env, newBitmap);
    return newBitmap;
}