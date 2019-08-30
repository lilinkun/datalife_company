package com.datalife.datalife_company.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by LG on 2019/7/18.
 */
public class DrawUtil {
    public static double getTextHeight(float frontSize) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return Math.ceil(fm.descent - fm.top) + 2;
    }

    public static float getTextWidth(float frontSize, String text) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        return paint.measureText(text);

    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        return new int[]{displayWidth, displayHeight};
    }

    public static int getScreenWidthSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        return displayHeight;
    }

    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }


    public static int sp2px(Context context, float spValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

    /**
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public static float getScaleTextWidth(TextPaint itemsPaint, String string) {
        // TODO Auto-generated method stub
        return itemsPaint.measureText(string);
    }

    public static final int getRound(Context context, String testType) {
        int s = getScreenSize(context)[0];
        double sa;
        if (testType.equals("bp_dbp")) {
            sa = s / 4;
        } else if (testType.equals("bp_sbp")) {
            sa = s / 2.5;
        } else {
            sa = s / 2.16;
        }
        return (int) sa / 2;
    }

    public static final int getDbpRound(Context context) {
        int s = getScreenSize(context)[0];
        double sa = s / 2.16;
        return (int) sa / 2;
    }
}



