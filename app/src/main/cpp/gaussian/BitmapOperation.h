//
// Created by shiki60215 on 18-5-8.
//

#ifndef MULTIAXISCARDLAYOUTMANAGERDEMO_BITMAPOPERATION_H
#define MULTIAXISCARDLAYOUTMANAGERDEMO_BITMAPOPERATION_H


#include <jni.h>
#include <android/bitmap.h>
#include "JniBitmap.h"

class BitmapOperation {
public:
    static jobject jniStoreBitmapData(JNIEnv *env, jobject obj, jobject bitmap);
    static jobject jniGetBitmapFromStoredBitmapData(
            JNIEnv *env, jobject obj, jobject handle);
};


#endif //MULTIAXISCARDLAYOUTMANAGERDEMO_BITMAPOPERATION_H
