//
// Created by shiki60215 on 18-5-8.
//

#include <jni.h>
#include "gaussian/BitmapOperation.h"

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
                                                                                    jobject handle) {
    JniBitmap *jniBitmap = (JniBitmap *)env->GetDirectBufferAddress(handle);
    if(jniBitmap->_storedBitmapPixels == NULL) {
        return;
    }
//    GaussianBlur::
}

}