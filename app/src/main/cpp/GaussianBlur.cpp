//
// Created by shiki60215 on 18-5-8.
//

#include <jni.h>
#include "gaussian/BitmapOperation.h"
#include "gaussian/GaussianBlurFilter.h"

#define LOG_TAG "cpp"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

extern "C" {
JNIEXPORT jobject JNICALL
Java_com_james602152002_multiaxiscardlayoutmanagerdemo_util_GaussianBlur_jniStoreBitmap(JNIEnv *env,
                                                                                        jobject obj,
                                                                                        jobject bitmap) {
    return BitmapOperation::jniStoreBitmapData(env, obj, bitmap);
}

JNIEXPORT void JNICALL
Java_com_james602152002_multiaxiscardlayoutmanagerdemo_util_GaussianBlur_jniInitSdk(JNIEnv *env,
                                                                                    jobject obj,
                                                                                    jobject handle,
                                                                                    jdouble sigma,
                                                                                    jint radius) {
    JniBitmap *jniBitmap = (JniBitmap *) env->GetDirectBufferAddress(handle);
    if (jniBitmap->_storedBitmapPixels == NULL) {
        return;
    }
    GaussianBlurFilter::getInstance()->startGaussianBlur(jniBitmap, sigma, radius);
}

JNIEXPORT jobject JNICALL
Java_com_james602152002_multiaxiscardlayoutmanagerdemo_util_GaussianBlur_jniGetBitmap(JNIEnv *env,
                                                                                      jobject obj,
                                                                                      jobject handle) {
    return BitmapOperation::jniGetBitmapFromStoredBitmapData(env, obj, handle);
}

JNIEXPORT void JNICALL
Java_com_james602152002_multiaxiscardlayoutmanagerdemo_util_GaussianBlur_unInit(JNIEnv *env,
                                                                                jobject obj) {
    GaussianBlurFilter::getInstance()->unInit();
}
}