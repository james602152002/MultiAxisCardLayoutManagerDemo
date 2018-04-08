package com.james602152002.multiaxiscardlayoutmanagerdemo.util;

import android.content.res.Resources;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

public class IPhone6ScreenResizeUtil {
    private static short currentScreenWidth, currentScreenHeight;
    /**
     * @核心
     */
    private static float text_adjust_ratio = 1f;
    private static float screen_ratio = 1f;
    private static float verticalRatio = 1f;
    private static float iPhone20PTSize = 0;
    private static float iPhone24PTSize = 0;
    private static float iPhone28PTSize = 0;
    private static float iPhone32PTSize = 0;
    private static int commonDividerGap = 0;
    private static int commonTextToTextGap = 0;
    private static int commonSymmetryTextToTextGap = 0;
    private static int commonRemarkHeight = 0;
    private static int commonLargerTitleBottomGap = 0;
    private static short commonShadowHeight = 0;
    private static short rect_width = 0;
    // 误差比例
//    private static final float deviationRatio = 865.647f / 1120.5f;
    private static final float deviationRatio = 865.647f / 1200.5f;
    private static final float ratioPxPt = 16f / 12f * deviationRatio;

    public static void install() {
        final float iphoneScreenWidth = 750f;
        final float iphoneScreenHeight = 1334f;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        currentScreenWidth = (short) displayMetrics.widthPixels;
        currentScreenHeight = (short) displayMetrics.heightPixels;
        displayMetrics = null;
        verticalRatio = (float) currentScreenHeight / iphoneScreenHeight;
        screen_ratio = (float) currentScreenWidth / iphoneScreenWidth;
        text_adjust_ratio = screen_ratio;
    }

    /**
     * @字体大小
     */
    public static void adjustTextSize(TextView textView, float designedPtValue) {
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, designedPtValue * text_adjust_ratio * ratioPxPt);
        }
    }

    /**
     * @字体大小
     */
    public static void adjustPaintTextSize(Paint paint, float designedPtValue) {
        if (paint != null) {
            paint.setTextSize(designedPtValue * text_adjust_ratio * ratioPxPt);
        }
    }

    /**
     * @数组类型更换字体大小
     */
    public static void adjustTextArrSize(float designedPtValue, TextView... textView) {
        if (textView != null) {
            for (TextView text : textView) {
                adjustTextSize(text, designedPtValue);
            }
        }
        textView = null;
    }

    /**
     * @数组类型更换IconFont大小
     */
    public static void adjustIconFontSize(TextView textView, float designedPxValue) {
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, designedPxValue * text_adjust_ratio * 1.1f);
        }
    }

    /**
     * @字体大小默认20PT
     */
    public static void set20PT_TextSize(TextView textView) {
        if (iPhone20PTSize == 0)
            iPhone20PTSize = 20f * text_adjust_ratio * ratioPxPt;
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iPhone20PTSize);
        }
    }

    /**
     * @字体大小默认24PT
     */
    public static void set24PT_TextSize(TextView textView) {
        if (iPhone24PTSize == 0)
            iPhone24PTSize = 24f * text_adjust_ratio * ratioPxPt;
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iPhone24PTSize);
        }
    }


    public static float get24PT_TextSize() {
        if (iPhone24PTSize == 0)
            iPhone24PTSize = 24f * text_adjust_ratio * ratioPxPt;
        return iPhone24PTSize;
    }


    /**
     * @字体大小默认28PT
     */
    public static void set28PT_TextSize(TextView textView) {
        if (iPhone28PTSize == 0)
            iPhone28PTSize = 28f * text_adjust_ratio * ratioPxPt;
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iPhone28PTSize);
        }
    }

    public static float get28PT_TextSize() {
        if (iPhone28PTSize == 0)
            iPhone28PTSize = 28f * text_adjust_ratio * ratioPxPt;
        return iPhone28PTSize;
    }

    /**
     * @字体大小默认32PT
     */
    public static void set32PT_TextSize(TextView textView) {
        if (iPhone32PTSize == 0)
            iPhone32PTSize = 32f * text_adjust_ratio * ratioPxPt;
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iPhone32PTSize);
        }
    }

    /**
     * @获取通用分割线与屏幕的距离
     */
    public static int getCommonDividerGap() {
        if (commonDividerGap == 0)
            commonDividerGap = (int) (30f * screen_ratio);
        return commonDividerGap;
    }

    /**
     * @获取通用对称状态下文字与文字的距离
     */
    public static int getCommonSymmetryTextGapSize() {
        if (commonSymmetryTextToTextGap == 0)
            commonSymmetryTextToTextGap = (int) (7f * text_adjust_ratio);
        return commonSymmetryTextToTextGap;
    }

    /**
     * @获取通用文字与文字的距离
     */
    public static int getCommonTextGapSize() {
        if (commonTextToTextGap == 0)
            commonTextToTextGap = (int) (14f * text_adjust_ratio);
        return commonTextToTextGap;
    }

    /**
     * @获取通用标题文字与内容文字的距离
     */
    public static int getCommonLargerTitleBottomGap() {
        if (commonLargerTitleBottomGap == 0)
            commonLargerTitleBottomGap = (int) (23f * screen_ratio);
        return commonLargerTitleBottomGap;
    }

    public static int getCommonRemarkHeight() {
        if (commonRemarkHeight == 0)
            commonRemarkHeight = (int) (166f * screen_ratio);
        return commonRemarkHeight;
    }

    public static short getCommonShadowHeight() {
        if (commonShadowHeight == 0)
            commonShadowHeight = (short) (16f * screen_ratio);
        return commonShadowHeight;
    }

    public static short getCommonRectWidth() {
        if (rect_width == 0)
            rect_width = (short) (12f * screen_ratio);
        return rect_width;
    }

    public static short getCurrentScreenWidth() {
        return currentScreenWidth;
    }

    public static short getCurrentScreenHeight() {
        return currentScreenHeight;
    }

    public static int getPxValue(float pxValue) {
        return (int) (pxValue * screen_ratio);
    }

    public static int getVerticalPxValue(float pxValue) {
        return (int) (pxValue * verticalRatio);
    }

    public static int getPtToPxValue(float ptValue) {
        return (int) (ptValue * text_adjust_ratio * ratioPxPt);
    }
}
