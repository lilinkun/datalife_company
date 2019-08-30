package com.datalife.datalife_company.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.datalife.datalife_company.R;

/**
 * 圆形进度
 * Created by LG on 2019/7/19.
 */
public class CircleProgressBar extends View {

    private int max; //最大值
    private int roundColor; //圆形进度条的颜色
    private int roundProgressColor;//圆形进度条进度的颜色
    private int textColor; //字体的颜色
    private float textSize; //字体的大小
    private float roundWidth; //圆的宽度
    private boolean textShow; //是否显示圆
    private double progress; //当前进度
    private Paint mPaint; //画笔
    public static final int STROKE = 0;
    public static final int FILL = 1;


    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化一只笔
        mPaint = new Paint();
        //获取xml当中设置的属性，如果没有设置，则设置一个默认值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        max = typedArray.getInteger(R.styleable.CustomProgressBar_max, 8);
        roundColor = typedArray.getColor(R.styleable.CustomProgressBar_roundColor, Color.TRANSPARENT);
        roundProgressColor = typedArray.getColor(R.styleable.CustomProgressBar_roundProgressColor, Color.BLUE);
        textColor = typedArray.getColor(R.styleable.CustomProgressBar_textColor, Color.GREEN);
        textSize = typedArray.getDimension(R.styleable.CustomProgressBar_textSize, 55);
        roundWidth = typedArray.getDimension(R.styleable.CustomProgressBar_roundWidth, 10);
        textShow = typedArray.getBoolean(R.styleable.CustomProgressBar_textShow, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景圆环
        int center = getWidth() / 2;
        //设置半径
        float radius = center - roundWidth / 2;
        //设置圆圈的颜色
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(roundWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//设置抗锯齿

        //画外圈
        canvas.drawCircle(center, center, radius, mPaint);
        //画进度百分比
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(0);
        //设置字体大小
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT);
        //设置笔帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置文字的摆放方式为居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        //获取当前进度的值
        int percent = (int) (progress / (float) max * 100);
        String strPercent = percent + "%";
        //获取画笔的文字属性，总共有bottom , top , leading , ascent , descent 这个以后会详细讲解
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        if (percent != 0) {
//            canvas.drawText(strPercent, getWidth() / 2,
//                    getWidth() / 2 + (fm.bottom - fm.top) / 2 - fm.bottom, mPaint);
        }
        //画圆弧
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(roundProgressColor);
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //设置笔帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //话进度
        canvas.drawArc(oval, 0, 360 * (float)progress / max, false, mPaint);
    }

    public void setProgress(double progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("进度progress不能小于0");
        }
        if (progress > max) {
            progress = max;
        }

        if (progress >= 4){
            mPaint.setColor(getResources().getColor(R.color.red));
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }
}
