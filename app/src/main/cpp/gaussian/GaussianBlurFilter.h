//
// Created by shiki60215 on 18-5-8.
//

#ifndef MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H
#define MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H


#include "JniBitmap.h"

class GaussianBlurFilter {
public:
    static GaussianBlurFilter *getInstance();
    void startGaussianBlur(JniBitmap *jniBitmap);
    void normalizeFilter(double sigma, int filter_size);
    int generateDistributeColorInPixel(double *distribute_color, int filter_width);
private:
    static GaussianBlurFilter *instance;
    uint32_t *storedBitmapPixels;
    uint32_t *mImageData_rgb;
    double *filter;

    int mImageWidth;
    int mImageHeight;
};


#endif //MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H
