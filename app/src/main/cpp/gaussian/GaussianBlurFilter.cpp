//
// Created by shiki60215 on 18-5-8.
//

#include <cstring>
#include <android/bitmap.h>
#include <stdio.h>
#include <android/log.h>
#include <math.h>
#include "GaussianBlurFilter.h"

#define LOG_TAG "cpp"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

GaussianBlurFilter *GaussianBlurFilter::instance;

GaussianBlurFilter *GaussianBlurFilter::getInstance() {
    if (instance == NULL)
        instance = new GaussianBlurFilter();
    return instance;
}

void GaussianBlurFilter::startGaussianBlur(JniBitmap *jniBitmap, double sigma, int radius) {
    storedBitmapPixels = jniBitmap->_storedBitmapPixels;
    mImageWidth = jniBitmap->_bitmapInfo.width;
    mImageHeight = jniBitmap->_bitmapInfo.height;

    int img_size = mImageWidth * mImageHeight;
    if (mImageData_rgb == NULL) {
        mImageData_rgb = new uint32_t[img_size];
    }
    memcpy(mImageData_rgb, storedBitmapPixels, sizeof(uint32_t) * img_size);

    int filter_width = (radius << 1) + 1;
    int filter_size = filter_width * filter_width;
    int *r;
    int *g;
    int *b;

    double *distributeColorRed = new double[filter_size];
    double *distributeColorGreen = new double[filter_size];
    double *distributeColorBlue = new double[filter_size];

    normalizeFilter(sigma, filter_width);

    float lightness = (float) pow(radius, 0.2f);

    for (int h = 0; h < mImageHeight; h++) {
        for (int w = 0; w < mImageWidth; w++) {
            for (int j = 0; j < filter_size; j++) {
                int x = (j % filter_width) + w;
                int y = (j / filter_width) + h;
                if (x >= mImageWidth || y >= mImageHeight) {
                    distributeColorRed[j] = 0;
                    distributeColorGreen[j] = 0;
                    distributeColorBlue[j] = 0;
                    continue;
                }
                int color = storedBitmapPixels[x + y * mImageWidth];
                int _r = (int) (((color & 0xFF0000) >> 16) * lightness);
                int _g = (int) (((color & 0xFF00) >> 8) * lightness);
                int _b = (int) ((color & 0xFF) * lightness);
                r = &_r;
                g = &_g;
                b = &_b;
                distributeColorRed[j] = filter[j] * *r;
                distributeColorGreen[j] = filter[j] * *g;
                distributeColorBlue[j] = filter[j] * *b;
            }
            int _r = generateDistributeColorInPixel(distributeColorRed, filter_size);
            int _g = generateDistributeColorInPixel(distributeColorGreen, filter_size);
            int _b = generateDistributeColorInPixel(distributeColorBlue, filter_size);
            r = &_r;
            g = &_g;
            b = &_b;
            storedBitmapPixels[h * mImageWidth + w] = 0xFF000000 + (*r << 16) + (*g << 8) + *b;
        }
        LOGD("h ========= %d", h);
    }
    delete distributeColorRed;
    distributeColorRed = NULL;
    delete distributeColorGreen;
    distributeColorGreen = NULL;
    delete distributeColorBlue;
    distributeColorBlue = NULL;
}

void GaussianBlurFilter::normalizeFilter(double sigma, int filter_size) {
    filter = new double[filter_size * filter_size];
    int semi_width = (filter_size - 1) >> 1;
    double r, s = 2.0 * sigma * sigma;

    // sum is for normalization
    double sum = 0.0;

    // generating filter_size x filter_size kernel
    int i = 0;
    for (int y = -semi_width; y <= semi_width; y++) {
        for (int x = -semi_width; x <= semi_width; x++) {
            r = sqrt(y * y + x * x);
            filter[i] = (exp(-(r * r) / s)) / (M_PI * s);
            sum += filter[i];
            i++;
        }
    }

    // normalising the Kernel
    i = 0;
    for (int y = 0; y < filter_size; y++) {
        for (int x = 0; x < filter_size; x++) {
            filter[i] /= sum;
            i++;
        }
    }
}

int GaussianBlurFilter::generateDistributeColorInPixel(double *distribute_color, int filter_size) {
    int summation = 0;
    for (int x = 0; x < filter_size; x++) {
        summation += distribute_color[x];
    }
    summation = summation > 255 ? 255 : summation;
    return summation;
}

void GaussianBlurFilter::unInit() {
    if (instance != NULL)
        delete instance;
    instance = NULL;
}
