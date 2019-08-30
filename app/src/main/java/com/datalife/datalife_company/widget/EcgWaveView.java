package com.datalife.datalife_company.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.datalife.datalife_company.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2019/7/19.
 */

public class EcgWaveView extends View {

    /**
     * 平均每秒上报的波形数据点数。
     */
//    private final static int DATA_PRE_SECOND = 530;
    private final static int DATA_PRE_SECOND = 512;

    /**
     * X轴时间基准（走纸速度）
     * pagerSpeed = 1: 25mm/s 25毫米（小格）每秒
     * pagerSpeed = 2 50mm/s  50毫米（小格）每秒
     */
    private int pagerSpeed = 1; // 1 ;2

    /**
     * Y轴增益
     * calibration = 0.5  5mm/mV 波形增幅5毫米左右
     * calibration = 1.0 10mm/mV 波形增幅10毫米左右
     * calibration = 2.0 20mm/mV 波形增幅20毫米左右
     */
    private float calibration = 1f;// 0.5;1 ;2

    /**
     * 存放波形点的容器
     */
    private final List<Integer> dataList = new ArrayList<>();

    /**
     * 波形图画笔
     */
    private Paint mWavePaint;

    private int mWavePaintColor = Color.WHITE;
    private float mWaveStrokeWidth = 1f;
    private boolean scrollable = false;

    /**
     * 当前屏幕波形满屏时最大的总点数。
     */
    private int allDataSize;
    private float totalLattices;
    private float dataSpacing;
    private float mViewWidth;
    private float mViewHalfHeight;
    private float xS;

    public EcgWaveView(Context context) {
        super(context);
        initPaint();
    }

    public EcgWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initPaint();
    }

    public EcgWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EcgWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTypedArray(context, attrs);
        initPaint();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EcgWaveView);
        mWavePaintColor = typedArray.getColor(R.styleable.EcgWaveView_waveColor, Color.WHITE);
        mWaveStrokeWidth = typedArray.getDimension(R.styleable.EcgWaveView_waveStrokeWidth, 1f);
        scrollable = typedArray.getBoolean(R.styleable.EcgWaveView_scrollable, false);
        typedArray.recycle();
    }

    private void initPaint() {
        mWavePaint = new Paint();
        mWavePaint.setColor(mWavePaintColor);
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStrokeWidth(mWaveStrokeWidth);
        mWavePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = getWidth();
        mViewHalfHeight = getHeight() / 2.0f;
        xS = EcgBackgroundView.xS;//控件每毫米的像素宽
        totalLattices = EcgBackgroundView.totalLattices;//平均总格子数
        final float dataPerLattice = DATA_PRE_SECOND / (25.0f * pagerSpeed);//每格波形数据点数
        allDataSize = (int) (totalLattices * dataPerLattice);
        dataSpacing = xS / dataPerLattice;//每个数据点间距。
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (scrollable) {
            widthMeasureSpec = (int) ((2 + dataList.size()) * dataSpacing);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int size = dataList.size();
        if (size >= 2) {
            for (int i = 0; i < size - 1; i++) {
                float x1 = getPointX(i);
                float y1 = getPointY(i);
                float x2 = getPointX(i + 1);
                float y2 = getPointY(i + 1);
                canvas.drawLine(x1, y1, x2, y2, mWavePaint);
            }
        }
        super.onDraw(canvas);
    }

    private float getPointX(int i) {
        if (scrollable) return dataSpacing * i;
        return mViewWidth - (dataSpacing * (dataList.size() - 1 - i));
    }

    private float getPointY(int i) {
        try {
            return (float) (mViewHalfHeight + dataList.get(i) * 18.3 / 128 * xS / 100 * calibration);
        } catch (IndexOutOfBoundsException e) {
            return getPointX(i - 1);
        }
    }

    public synchronized void preparePoint(int data) {
        dataList.add(data);
        if (dataList.size() > allDataSize) dataList.remove(0);
        postInvalidate();
    }

    public void preparePoints(List<Integer> dataList) {
        if (!dataList.isEmpty()) {
            this.dataList.addAll(dataList);
            requestLayout();
        }
    }

    public void clear() {
        dataList.clear();
        postInvalidate();
    }

    /**
     * 设置增益
     *
     * @param calibration 增益值
     */
    public void setCalibration(float calibration) {
        this.calibration = calibration;
        if (!dataList.isEmpty()) postInvalidate();
    }

    /**
     * 设置时间基准（走纸速度）
     *
     * @param pagerSpeed 时间基准（走纸速度）值
     */
    public void setPagerSpeed(int pagerSpeed) {
        this.pagerSpeed = pagerSpeed;
        final float dataPerLattice = DATA_PRE_SECOND / (25.0f * this.pagerSpeed);//每格波形数据点数
        allDataSize = (int) (totalLattices * dataPerLattice);
        dataSpacing = xS / dataPerLattice;//每个数据点间距。
        if (!dataList.isEmpty()) postInvalidate();
    }
}

