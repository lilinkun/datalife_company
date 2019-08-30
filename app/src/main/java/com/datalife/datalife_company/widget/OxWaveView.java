package com.datalife.datalife_company.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.datalife.datalife_company.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2019/7/19.
 */
public class OxWaveView extends View {

    private static final int OXYGEN_DATA_RED_MAX = 2000000;
    private static final int OXYGEN_DATA_RED_MIN = 50000;

    private Paint mPaint;
    private int CHART_H = 0;
    private int CHART_W = 0;
    private final static int OFFSET_LEFT = 0;
    private final static int OFFSET_OX_DATA = 0;
    private final static int X_INTERVAL = 2;
    private int min_ox_data;
    private int max_ox_data;
    private List<Integer> data_list = new ArrayList<>();
    private List<PointF> plist = new ArrayList<>();
    private boolean drawFinish = true;


    public OxWaveView(Context context) {
        super(context);
        initPaint();
    }

    public OxWaveView(Context context, AttributeSet attributes) {
        super(context, attributes);
        initPaint();
    }

    public OxWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (CHART_H == 0) CHART_W = getWidth();
        if (CHART_H == 0) CHART_H = getHeight();
        drawCurve(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void clear() {
        min_ox_data = 0;
        max_ox_data = 0;
        data_list.clear();
        plist.clear();
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();
//        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setColor(getResources().getColor(R.color.green_wave));
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
    }

    //useful
    private void drawCurve(Canvas canvas) {
        drawFinish = false;
        if (plist.size() >= 2) {
            for (int i = 0; i < plist.size() - 1; i++) {
                PointF pf0 = plist.get(i);
                PointF pf1;
                try {
                    pf1 = plist.get(i + 1);
                } catch (IndexOutOfBoundsException e) {
                    pf1 = plist.get(i);
                }

                canvas.drawLine(pf0.x, pf0.y, pf1.x, pf1.y, mPaint);
            }
        }
        drawFinish = true;
    }

    //useful
    public void preparePoints(int data) {
        if (drawFinish) {
            if (data > OXYGEN_DATA_RED_MIN && data < OXYGEN_DATA_RED_MAX) {
                data_list.add(data);
                if (data_list.size() > CHART_W / X_INTERVAL) {
                    data_list.remove(0);
                    plist.remove(0);
                }
                min_ox_data = data_list.get(0);
                max_ox_data = data_list.get(0);
                for (int i = 0; i < data_list.size(); i++) {
                    Integer integer = data_list.get(i);
                    int dataInt = integer == null ? 0 : integer;
                    if (dataInt < min_ox_data) {
                        min_ox_data = dataInt;
                    }
                    if (dataInt > max_ox_data) {
                        max_ox_data = dataInt;
                    }
                }
                PointF p = new PointF(OFFSET_LEFT + CHART_W, CHART_H - OFFSET_OX_DATA);
                plist.add(p);
                float dp = (max_ox_data - min_ox_data) / (float) (CHART_H - OFFSET_OX_DATA - CHART_H / 10 * 2);
                if (dp == 0) dp = -1;
                for (int i = 0; i < data_list.size(); i++) {
                    plist.get(i).x -= X_INTERVAL;
                    plist.get(i).y = CHART_H - CHART_H / 10 - (data_list.get(i) - min_ox_data) / (dp);
                }

                postInvalidate();
            }
        }
    }
}