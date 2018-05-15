//
// Created by shiki60215 on 18-5-8.
//

#ifndef MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H
#define MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H


#include "JniBitmap.h"

class GaussianBlurFilter {
public:
    static GaussianBlurFilter *getInstance();

    void startGaussianBlur(JniBitmap *jniBitmap, int radius);

    void unInit();

private:
    static GaussianBlurFilter *instance;
    uint32_t *storedBitmapPixels;

    int mImageWidth;
    int mImageHeight;
};


#endif //MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H
