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

//    void normalizeFilter(double sigma, int filter_size);
//
//    int generateDistributeColorInPixel(double *distribute_color, int filter_width);

    void unInit();

private:
    static GaussianBlurFilter *instance;
    uint32_t *storedBitmapPixels;
//    uint32_t *mImageData_rgb;
//    double *filter;

    int mImageWidth;
    int mImageHeight;

//    void *threadGaussianBlur(void *params);
//
//    void gaussBlur_3(int* scl, int* tcl, int w, int h, int r);
//
//    void boxBlur_3(int* scl,int* tcl,int w,int h, int r);
//
//    void boxBlurH_3(int *scl, int *tcl, int w, int h, int r);
//
//    void boxBlurT_3(int *scl, int *tcl, int w, int h, int r);
//
//    int *boxesForGauss(double sigma, int n);
};


#endif //MULTIAXISCARDLAYOUTMANAGERDEMO_GAUSSIANBLURFILTER_H
