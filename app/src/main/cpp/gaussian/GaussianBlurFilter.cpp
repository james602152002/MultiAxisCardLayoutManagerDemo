//
// Created by shiki60215 on 18-5-8.
//

#include <cstring>
#include <android/bitmap.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <android/log.h>
#include "GaussianBlurFilter.h"
#include <time.h>
#include <thread>

#define LOG_TAG "cpp"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

struct thread_args {
    int *scl;
    int *tcl;
    int w;
    int h;
    int r;
};

struct thread_args thread_r;
struct thread_args thread_g;
struct thread_args thread_b;

GaussianBlurFilter *GaussianBlurFilter::instance;

GaussianBlurFilter *GaussianBlurFilter::getInstance() {
    if (instance == NULL)
        instance = new GaussianBlurFilter();
    return instance;
}


int *boxesForGauss(double sigma, int n)  // standard deviation, number of boxes
{
    double wIdeal = sqrt((12 * sigma * sigma / n) + 1);  // Ideal averaging filter width
    int wl = (int) floor(wIdeal);
    if (wl % 2 == 0) wl--;
    int wu = wl + 2;

    double mIdeal = (12 * sigma * sigma - n * wl * wl - 4 * n * wl - 3 * n) / (-4 * wl - 4);
    int m = (int) round(mIdeal);
    // var sigmaActual = Math.sqrt( (m*wl*wl + (n-m)*wu*wu - n)/12 );

    int *sizes = new int[n];
    for (int i = 0; i < n; i++) sizes[i] = i < m ? wl : wu;
    return sizes;
}


void boxBlurH_3(int *scl, int *tcl, int w, int h, int r) {
    double iarr = (double) 1 / (r + r + 1);
    for (int i = 0; i < h; i++) {
        int ti = i * w, li = ti, ri = ti + r;
        int fv = scl[ti], lv = scl[ti + w - 1], val = (r + 1) * fv;
        for (int j = 0; j < r; j++) val += scl[ti + j];
        for (int j = 0; j <= r; j++) {
            val += scl[ri++] - fv;
            tcl[ti++] = (int) (val * iarr);
        }
        for (int j = r + 1; j < w - r; j++) {
            val += scl[ri++] - scl[li++];
            tcl[ti++] = (int) (val * iarr);
        }
        for (int j = w - r; j < w; j++) {
            val += lv - scl[li++];
            tcl[ti++] = (int) (val * iarr);
        }
    }
}

void boxBlurT_3(int *scl, int *tcl, int w, int h, int r) {
    double iarr = (double) 1 / (r + r + 1);
    for (int i = 0; i < w; i++) {
        int ti = i, li = ti, ri = ti + r * w;
        int fv = scl[ti], lv = scl[ti + w * (h - 1)], val = (r + 1) * fv;
        for (int j = 0; j < r; j++) val += scl[ti + j * w];
        for (int j = 0; j <= r; j++) {
            val += scl[ri] - fv;
            tcl[ti] = (int) (val * iarr);
            ri += w;
            ti += w;
        }
        for (int j = r + 1; j < h - r; j++) {
            val += scl[ri] - scl[li];
            tcl[ti] = (int) (val * iarr);
            li += w;
            ri += w;
            ti += w;
        }
        for (int j = h - r; j < h; j++) {
            val += lv - scl[li];
            tcl[ti] = (int) (val * iarr);
            li += w;
            ti += w;
        }
    }
}

void boxBlur_3(int *scl, int *tcl, int w, int h, int r) {
    int size = w * h;
    for (int i = 0; i < size; i++) tcl[i] = scl[i];
    boxBlurH_3(tcl, scl, w, h, r);
    boxBlurT_3(scl, tcl, w, h, r);
}

void gaussBlur_3(int *scl, int *tcl, int w, int h, int r) {
    int *bxs = boxesForGauss(r, 3);
    boxBlur_3(scl, tcl, w, h, (bxs[0] - 1) / 2);
    boxBlur_3(tcl, scl, w, h, (bxs[1] - 1) / 2);
    boxBlur_3(scl, tcl, w, h, (bxs[2] - 1) / 2);
}

void *threadGaussianBlur(void *params) {
    gaussBlur_3(((struct thread_args *) params)->scl, ((struct thread_args *) params)->tcl,
                ((struct thread_args *) params)->w, ((struct thread_args *) params)->h,
                ((struct thread_args *) params)->r);
    pthread_exit((void *) 0);
}

void GaussianBlurFilter::startGaussianBlur(JniBitmap *jniBitmap, int radius) {
    storedBitmapPixels = jniBitmap->_storedBitmapPixels;
    mImageWidth = jniBitmap->_bitmapInfo.width;
    mImageHeight = jniBitmap->_bitmapInfo.height;

    int img_size = mImageWidth * mImageHeight;

    pthread_t r_id, g_id, b_id;

    struct timeval tv;
    gettimeofday(&tv, NULL);

    int start_time = (int) tv.tv_sec;

    int *distributeColorRed = new int[mImageHeight * mImageWidth];
    int *distributeColorGreen = new int[mImageHeight * mImageWidth];
    int *distributeColorBlue = new int[mImageHeight * mImageWidth];
    int *_distributeColorRed = new int[mImageHeight * mImageWidth];
    int *_distributeColorGreen = new int[mImageHeight * mImageWidth];
    int *_distributeColorBlue = new int[mImageHeight * mImageWidth];
    for (int i = 0; i < mImageHeight * mImageWidth; i++) {
        int color = storedBitmapPixels[i];
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0xFF00) >> 8;
        int b = color & 0xFF;
        distributeColorRed[i] = r;
        distributeColorGreen[i] = g;
        distributeColorBlue[i] = b;
    }

    thread_r.scl = distributeColorRed;
    thread_r.tcl = _distributeColorRed;
    thread_r.w = mImageWidth;
    thread_r.h = mImageHeight;
    thread_r.r = radius;
    thread_g.scl = distributeColorGreen;
    thread_g.tcl = _distributeColorGreen;
    thread_g.w = mImageWidth;
    thread_g.h = mImageHeight;
    thread_g.r = radius;
    thread_b.scl = distributeColorBlue;
    thread_b.tcl = _distributeColorBlue;
    thread_b.w = mImageWidth;
    thread_b.h = mImageHeight;
    thread_b.r = radius;

    pthread_create(&r_id, NULL, threadGaussianBlur, (void *) &thread_r);
    pthread_create(&g_id, NULL, threadGaussianBlur, (void *) &thread_g);
    pthread_create(&b_id, NULL, threadGaussianBlur, (void *) &thread_b);
    pthread_join(r_id, NULL);
    pthread_join(g_id, NULL);
    pthread_join(b_id, NULL);

    for (int i = 0; i < img_size; i++) {
        storedBitmapPixels[i] =
                0xFF000000 + (_distributeColorRed[i] << 16) + (_distributeColorGreen[i] << 8) +
                _distributeColorBlue[i];
    }

    gettimeofday(&tv, NULL);
    LOGD("r = %d, w = %d, h = %d, time spent %ds", radius, mImageWidth, mImageHeight,
         ((int) tv.tv_sec - start_time));


    delete distributeColorRed;
    distributeColorRed = NULL;
    delete distributeColorGreen;
    distributeColorGreen = NULL;
    delete distributeColorBlue;
    distributeColorBlue = NULL;
    delete _distributeColorRed;
    _distributeColorRed = NULL;
    delete _distributeColorGreen;
    _distributeColorGreen = NULL;
    delete _distributeColorBlue;
    _distributeColorBlue = NULL;
}

//void GaussianBlurFilter::normalizeFilter(double sigma, int filter_size) {
//    filter = new double[filter_size * filter_size];
//    int semi_width = (filter_size - 1) >> 1;
//    double r, s = 2.0 * sigma * sigma;
//
//    // sum is for normalization
//    double sum = 0.0;
//
//    // generating filter_size x filter_size kernel
//    int i = 0;
//    for (int y = -semi_width; y <= semi_width; y++) {
//        for (int x = -semi_width; x <= semi_width; x++) {
//            r = sqrt(y * y + x * x);
//            filter[i] = (exp(-(r * r) / s)) / (M_PI * s);
//            sum += filter[i];
//            i++;
//        }
//    }
//
//    // normalising the Kernel
//    i = 0;
//    for (int y = 0; y < filter_size; y++) {
//        for (int x = 0; x < filter_size; x++) {
//            filter[i] /= sum;
//            i++;
//        }
//    }
//}
//
//int GaussianBlurFilter::generateDistributeColorInPixel(double *distribute_color, int filter_size) {
//    int summation = 0;
//    for (int x = 0; x < filter_size; x++) {
//        summation += distribute_color[x];
//    }
//    summation = summation > 255 ? 255 : summation;
//    return summation;
//}

void GaussianBlurFilter::unInit() {
    if (instance != NULL)
        delete instance;
    instance = NULL;
}
