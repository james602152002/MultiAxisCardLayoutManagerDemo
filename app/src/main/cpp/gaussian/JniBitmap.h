//
// Created by shiki60215 on 18-5-8.
//

#ifndef MULTIAXISCARDLAYOUTMANAGERDEMO_JNIBITMAP_H
#define MULTIAXISCARDLAYOUTMANAGERDEMO_JNIBITMAP_H

#include <android/bitmap.h>

typedef struct {
    uint8_t alpha, red, green, blue;
} ARGB;

class JniBitmap {
public:
    uint32_t *_storedBitmapPixels;
    AndroidBitmapInfo _bitmapInfo;

    JniBitmap() {
        _storedBitmapPixels = NULL;
    }
};

#endif //MULTIAXISCARDLAYOUTMANAGERDEMO_JNIBITMAP_H
