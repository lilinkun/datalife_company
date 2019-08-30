package com.datalife.datalife_company.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.util.DrawUtil;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.linktop.oauth.MiscUtil;

/**
 * 数据自定义表
 * Created by LG on 2019/7/19.
 */
public class HealthDialProgress extends View {

    private static final String TAG = "HealthDialProgress";
    private Context mContext;

    //圆心坐标
    private Point mCenterPoint;
    private float mRadius;
    private float mTextOffsetPercentInRadius;

    private boolean antiAlias;
    //绘制提示
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;
    private float mHintOffset;

    //测量的类型
    private String mTestType;

    //绘制数值
    private Paint mValuePaint;
    private int mValueColor;
    private float mMaxValue;
    private float mMinValue;
    private float mValue;
    private float mValueSize;
    private float mValueOffset;
    private String mPrecisionFormat;

    //绘制单位
    private Paint mUnitPaint;
    private float mUnitSize;
    private int mUnitColor;
    private float mUnitOffset;
    private CharSequence mUnit;
    //前景圆弧
    private Paint mArcPaint;
    private float mArcWidth;
    private int mDialIntervalDegree;
    private float mStartAngle, mSweepAngle;
    private RectF mRectF;
    //渐变
    private int[] mGradientColors = {Color.WHITE, Color.WHITE, Color.WHITE};

    //当前进度，[0.0f,1.0f]
    private float mPercent;
    //动画时间
    private long mAnimTime;
    //属性动画
    private ValueAnimator mAnimator;

    //背景圆弧
    private Paint mBgArcPaint;
    private int mBgArcColor;

    //刻度线颜色
    private Paint mDialPaint;
    private float mDialWidth;
    private int mDialColor;

    private int mDefaultSize;

    public HealthDialProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mDefaultSize = IDatalifeConstant.dipToPx(context, IDatalifeConstant.DEFAULT_SIZE);
        mRectF = new RectF();
        mCenterPoint = new Point();
        initConfig(context, attrs);
        initPaint();
        setValue(mValue);
    }

    private void initConfig(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DialProgress);

        antiAlias = typedArray.getBoolean(R.styleable.DialProgress_antiAlias, true);
        mMaxValue = typedArray.getFloat(R.styleable.DialProgress_maxValue, IDatalifeConstant.DEFAULT_MAX_VALUE);
        mMinValue = typedArray.getFloat(R.styleable.DialProgress_minValue, IDatalifeConstant.DEFAULT_MAX_VALUE);
        mValue = typedArray.getFloat(R.styleable.DialProgress_value, IDatalifeConstant.DEFAULT_VALUE);
        mValueSize = typedArray.getDimension(R.styleable.DialProgress_valueSize, IDatalifeConstant.DEFAULT_VALUE_SIZE);
        mValueColor = typedArray.getColor(R.styleable.DialProgress_valueColor, Color.BLACK);
        mDialIntervalDegree = typedArray.getInt(R.styleable.DialProgress_dialIntervalDegree, 10);
        int precision = typedArray.getInt(R.styleable.DialProgress_precision, 0);
        mPrecisionFormat = IDatalifeConstant.getPrecisionFormat(precision);

        mUnit = typedArray.getString(R.styleable.DialProgress_unit);
        mUnitColor = typedArray.getColor(R.styleable.DialProgress_unitColor, Color.BLACK);
        mUnitSize = typedArray.getDimension(R.styleable.DialProgress_unitSize, IDatalifeConstant.DEFAULT_UNIT_SIZE);

        mHint = typedArray.getString(R.styleable.DialProgress_hint);
        mHintColor = typedArray.getColor(R.styleable.DialProgress_hintColor, Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.DialProgress_hintSize, IDatalifeConstant.DEFAULT_HINT_SIZE);

        mTestType = typedArray.getString(R.styleable.DialProgress_type);

        mArcWidth = typedArray.getDimension(R.styleable.DialProgress_arcWidth, IDatalifeConstant.DEFAULT_ARC_WIDTH);

        mStartAngle = typedArray.getFloat(R.styleable.DialProgress_startAngle, IDatalifeConstant.DEFAULT_START_ANGLE);
        mSweepAngle = typedArray.getFloat(R.styleable.DialProgress_sweepAngle, IDatalifeConstant.DEFAULT_SWEEP_ANGLE);

        mAnimTime = typedArray.getInt(R.styleable.DialProgress_animTime, IDatalifeConstant.DEFAULT_ANIM_TIME);

        mBgArcColor = typedArray.getColor(R.styleable.DialProgress_bgArcColor, Color.GRAY);
        mDialWidth = typedArray.getDimension(R.styleable.DialProgress_dialWidth, 2);
        mDialColor = typedArray.getColor(R.styleable.DialProgress_dialColor, Color.BLUE);

        mRadius = typedArray.getInt(R.styleable.DialProgress_round,250);

        mTextOffsetPercentInRadius = typedArray.getFloat(R.styleable.DialProgress_textOffsetPercentInRadius, 0.33f);

        int gradientArcColors = typedArray.getResourceId(R.styleable.DialProgress_arcColors, 0);
        if (gradientArcColors != 0) {
            try {
                int[] gradientColors = getResources().getIntArray(gradientArcColors);
                if (gradientColors.length == 0) {
                    int color = getResources().getColor(gradientArcColors);
                    mGradientColors = new int[2];
                    mGradientColors[0] = color;
                    mGradientColors[1] = color;
                } else if (gradientColors.length == 1) {
                    mGradientColors = new int[2];
                    mGradientColors[0] = gradientColors[0];
                    mGradientColors[1] = gradientColors[0];
                } else {
                    mGradientColors = gradientColors;
                }
            } catch (Resources.NotFoundException e) {
                throw new Resources.NotFoundException("the give resource not found.");
            }
        }
        typedArray.recycle();
    }

    private void initPaint() {
        mHintPaint = new TextPaint();
        // 设置抗锯齿,会消耗较大资源，绘制图形速度会变慢。
        mHintPaint.setAntiAlias(antiAlias);
        // 设置绘制文字大小
        mHintPaint.setTextSize(mHintSize);
        // 设置画笔颜色
        mHintPaint.setColor(mHintColor);
        // 从中间向两边绘制，不需要再次计算文字
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(antiAlias);
        mValuePaint.setTextSize(mValueSize);
        mValuePaint.setColor(mValueColor);
        mValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mValuePaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new Paint();
        mUnitPaint.setAntiAlias(antiAlias);
        mUnitPaint.setTextSize(mUnitSize);
        mUnitPaint.setColor(mUnitColor);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(antiAlias);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStrokeCap(Paint.Cap.BUTT);

        mBgArcPaint = new Paint();
        mBgArcPaint.setAntiAlias(antiAlias);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mArcWidth);
        mBgArcPaint.setStrokeCap(Paint.Cap.BUTT);
        mBgArcPaint.setColor(mBgArcColor);

        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(antiAlias);
        mDialPaint.setColor(mDialColor);
        mDialPaint.setStrokeWidth(mDialWidth);
    }

    /**
     * 更新圆弧画笔
     */
    private void updateArcPaint() {
        // 设置渐变
        // 渐变的颜色是360度，如果只显示270，那么则会缺失部分颜色
        SweepGradient sweepGradient = new SweepGradient(mCenterPoint.x, mCenterPoint.y, mGradientColors, null);
        mArcPaint.setShader(sweepGradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(IDatalifeConstant.measure(widthMeasureSpec, mDefaultSize),
                IDatalifeConstant.measure(heightMeasureSpec, mDefaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);
        int minSize = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * (int) mArcWidth,
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - 2 * (int) mArcWidth);
//        mRadius = minSize / 2;
        mRadius = DrawUtil.getRound(mContext,mTestType);
        mCenterPoint.x = getMeasuredWidth() / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - mRadius - mArcWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - mArcWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + mArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + mArcWidth / 2;

        mValueOffset = mCenterPoint.y + getBaselineOffsetFromY(mValuePaint);
        mHintOffset = mCenterPoint.y - mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mHintPaint);
        mUnitOffset = mCenterPoint.y + mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mUnitPaint);

        updateArcPaint();
        Log.d(TAG, "onMeasure: 控件大小 = " + "(" + getMeasuredWidth() + ", " + getMeasuredHeight() + ")"
                + ";圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + mRadius
                + ";圆的外接矩形 = " + mRectF.toString());
    }

    private float getBaselineOffsetFromY(Paint paint) {
        return IDatalifeConstant.measureTextHeight(paint) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawArc(canvas);

        if (mTestType.equals("bt")) {
            drawBtDial(canvas);
        }else if (mTestType.equals("bp_sbp")){
            drawBpSbpDial(canvas);
        }else if (mTestType.equals("bp_dbp")){
            drawBpDbpDial(canvas);
        }else if (mTestType.equals("spo2h")){
            drawSpo2hDial(canvas);
        }else if (mTestType.equals("bp")){
            drawBpDial(canvas);
        }
//        drawText(canvas);
    }

    private void drawArc(Canvas canvas) {
        // 绘制背景圆弧
        // 从进度圆弧结束的地方开始重新绘制，优化性能
        float currentAngle = mSweepAngle * mPercent;
        canvas.save();
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        canvas.drawArc(mRectF, currentAngle, mSweepAngle - currentAngle, false, mBgArcPaint);
        // 第一个参数 oval 为 RectF 类型，即圆弧显示区域
        // startAngle 和 sweepAngle  均为 float 类型，分别表示圆弧起始角度和圆弧度数
        // 3点钟方向为0度，顺时针递增
        // 如果 startAngle < 0 或者 > 360,则相当于 startAngle % 360
        // useCenter:如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形
        canvas.drawArc(mRectF, 0, currentAngle, false, mArcPaint);
        canvas.restore();
    }

    private void drawBpSbpDial(Canvas canvas){
        int total = (int) (mSweepAngle / mDialIntervalDegree);
        canvas.save();
        int rest = (int)((total/mMaxValue)*mValue);
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        mDialPaint.setStrokeWidth(4);

        if (rest <= 8*total/21) {
            for (int i = 0; i <= rest-1; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 8*total/21 && rest <= 13*total/21){
            for (int i = 0; i <= 8*total/21 - 3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));

                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            for (int i = 0; i <= rest - 8*total/21-2; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[1]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }else if (rest > 13*total/21 && rest <= total){
            for (int i = 0; i <= 8*total/21 - 3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));

                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            for (int i = 0; i <= 8*total/21 - 4; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_red_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= rest-13*total/21-2; i++) {
                mDialPaint.setColor(mGradientColors[2]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[2]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        for (int i = 0; i <= total-rest-2; i++) {
            mDialPaint.setColor(mContext.getResources().getColor(R.color.round_gray_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        canvas.restore();
    }

    private void drawBpDbpDial(Canvas canvas){
        int total = (int) (mSweepAngle / mDialIntervalDegree);
        canvas.save();
        int rest = (int)((total/mMaxValue)*mValue);
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        mDialPaint.setStrokeWidth(4);

        if (rest <= total/2) {
            for (int i = 0; i <= rest-1; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[1]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }else if (rest > total/2){
            for (int i = 0; i <= total/2 - 3; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_red_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            for (int i = 0; i <= rest - total/2-2; i++) {
                mDialPaint.setColor(mGradientColors[2]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            mDialPaint.setColor(mGradientColors[2]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }
        for (int i = 0; i <= total-rest-2; i++) {
            mDialPaint.setColor(mContext.getResources().getColor(R.color.round_gray_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        canvas.restore();
    }

    private void drawBpDial(Canvas canvas){
        int total = (int) (mSweepAngle / mDialIntervalDegree);
        canvas.save();
        int rest = (int)((total/mMaxValue)*mValue);
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        mDialPaint.setStrokeWidth(4);

        if (rest <= total/6){
            for (int i = 0; i<= rest-1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(getResources().getColor(R.color.grey_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > total/6 && rest <= 2*total/6){
            for (int i = 0; i<= total/7;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= rest-total/6-1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_normal));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(getResources().getColor(R.color.grey_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 2*total/6  && rest <=3 * total /6){
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_normal));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= rest-2*total/6-1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_scarce_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 3*total/6  && rest <=4 * total /6){
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_normal));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_scarce_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= rest-3*total/6-1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_little_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 4*total/6  && rest <=5 * total /6){
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_normal));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_scarce_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_little_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= rest - 4*total/6 - 1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_middle_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 5*total/6  && rest <=6 * total /6){
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_low));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_normal));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_scarce_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_little_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= total/6;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_middle_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            for (int i = 0; i<= rest - 5*total/6 - 1;i++){
                mDialPaint.setColor(getResources().getColor(R.color.color_bp_very_high));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }


        for (int i = 0; i <= total-rest-2; i++) {
            mDialPaint.setColor(mContext.getResources().getColor(R.color.round_gray_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        canvas.restore();

    }

    private void drawSpo2hDial(Canvas canvas){
        int total = (int) (mSweepAngle / mDialIntervalDegree);
        canvas.save();
        int rest = (int)((total/mMaxValue)*mValue);
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        mDialPaint.setStrokeWidth(4);

        if (rest <= 3*total/8) {
            for (int i = 0; i <= rest-1; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }else if (rest > 3*total/8 && rest <= 5*total/8){
            for (int i = 0; i <= 3*total/8-3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= rest - 3*total/8-2; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[1]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }else if (rest > 5*total/8 && rest <= total){
            for (int i = 0; i <= 3*total/8-3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3*total/8-4; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_red_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= rest-5*total/8-2; i++) {
                mDialPaint.setColor(mGradientColors[2]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[2]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth + 15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        for (int i = 0; i <= total-rest-2; i++) {
            mDialPaint.setColor(mContext.getResources().getColor(R.color.round_gray_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        canvas.restore();
    }

    private void drawBtDial(Canvas canvas) {
        int total = (int) (mSweepAngle / mDialIntervalDegree);
        canvas.save();
        int rest = (int)((total/mMaxValue)*mValue);
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        mDialPaint.setStrokeWidth(4);

        if (rest <= 3*total/7) {
            for (int i = 0; i <= rest-1; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[0]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }else if (rest > 3*total/7 && rest <= 4*total/7){
            for (int i = 0; i <= 3*total/7 - 3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));

                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            for (int i = 0; i <= rest - 3*total/7-2; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            mDialPaint.setColor(mGradientColors[1]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);

        }else if (rest > 4*total/7 && rest <= total){
            for (int i = 0; i <= 3*total/7 - 3; i++) {
                mDialPaint.setColor(mGradientColors[0]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_blue_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }


            for (int i = 0; i <= 3*total/7-4; i++) {
                mDialPaint.setColor(mGradientColors[1]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= 3; i++) {
                mDialPaint.setColor(mContext.getResources().getColor(R.color.dial_red_mix));
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }

            for (int i = 0; i <= rest-4*total/7-2; i++) {
                mDialPaint.setColor(mGradientColors[2]);
                canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
                canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
            }
            mDialPaint.setColor(mGradientColors[2]);
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth+15, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        for (int i = 0; i <= total-rest-2; i++) {
            mDialPaint.setColor(mContext.getResources().getColor(R.color.round_gray_bg));
            canvas.drawLine(mCenterPoint.x + mRadius, mCenterPoint.y, mCenterPoint.x + mRadius + mArcWidth, mCenterPoint.y, mDialPaint);
            canvas.rotate(mDialIntervalDegree, mCenterPoint.x, mCenterPoint.y);
        }
        canvas.restore();
    }

    public void setcolor(int[] color){
        this.mGradientColors = color;
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(String.format(mPrecisionFormat, mValue), mCenterPoint.x, mValueOffset, mValuePaint);

        if (mUnit != null) {
            canvas.drawText(mUnit.toString(), mCenterPoint.x, mUnitOffset, mUnitPaint);
        }

        if (mHint != null) {
            canvas.drawText(mHint.toString(), mCenterPoint.x, mHintOffset, mHintPaint);
        }
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    /**
     * 设置当前值
     *
     * @param value
     */
    public void setValue(float value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        float start = mPercent;
        float end = value / mMaxValue;
        startAnimator(start, end, mAnimTime);
    }

    private void startAnimator(float start, float end, long animTime) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(animTime);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                mValue = mPercent * mMaxValue;
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onAnimationUpdate: percent = " + mPercent
                            + ";currentAngle = " + (mSweepAngle * mPercent)
                            + ";value = " + mValue);
                }
                invalidate();
            }
        });
        mAnimator.start();
    }

    public int[] getGradientColors() {
        return mGradientColors;
    }

    public void setGradientColors(int[] gradientColors) {
        mGradientColors = gradientColors;
        updateArcPaint();
    }

    public void reset() {
        startAnimator(mPercent, 0.0f, 1000L);
    }
}
