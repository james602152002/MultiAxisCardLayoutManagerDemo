//
// Created by shiki60215 on 18-5-8.
//

#include <cstring>
#include "BitmapOperation.h"

jobject BitmapOperation::jniStoreBitmapData(JNIEnv *env, jobject obj, jobject bitmap) {
    AndroidBitmapInfo bitmapInfo;
    uint32_t *storeBitmapPixels = NULL;
    if (AndroidBitmap_getInfo(env, obj, &bitmapInfo) < 0) {
        return NULL;
    }

    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return NULL;
    }

    //
    //read pixels of bitmap into native memory :
    //

    void *bitmapPixels;

    if (AndroidBitmap_lockPixels(env, obj, &bitmapPixels) < 0) {
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