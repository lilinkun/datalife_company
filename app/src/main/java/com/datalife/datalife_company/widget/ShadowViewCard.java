package com.datalife.datalife_company.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.datalife.datalife_company.R;

/**
 * Created by LG on 2019/8/6.
 */
public class ShadowViewCard extends FrameLayout {
    private static final int DEFAULT_VALUE_SHADOW_COLOR = R.color.shadow_default_color;
    private static final int DEFAULT_VALUE_SHADOW_CARD_COLOR = R.color.shadow_card_default_color;
    private static final int DEFAULT_VALUE_SHADOW_ROUND = 0;

    private static final int DEFAULT_VALUE_SHADOW_RADIUS = 10;
    private static final int DEFAULT_VALUE_SHADOW_TOP_HEIGHT = 0;
    private static final int DEFAULT_VALUE_SHADOW_LEFT_HEIGHT = 0;
    private static final int DEFAULT_VALUE_SHADOW_RIGHT_HEIGHT = 5;
    private static final int DEFAULT_VALUE_SHADOW_BOTTOM_HEIGHT = 5;
    private static final int DEFAULT_VALUE_SHADOW_OFFSET_Y = 0;
    private static final int DEFAULT_VALUE_SHADOW_OFFSET_X = DEFAULT_VALUE_SHADOW_TOP_HEIGHT / 3;

    private int shadowRound;
    private int shadowColor;
    private int shadowCardColor;
    private int shadowRadius;
    private int shadowOffsetY;
    private int shadowOffsetX;
    private int shadowTopHeight;
    private int shadowLeftHeight;
    private int shadowRightHeight;
    private int shadowBottomHeight;

    public ShadowViewCard(Context context) {
        this(context, null);
    }

    public ShadowViewCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowViewCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShadowViewCard);
        shadowRound = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowRound, DEFAULT_VALUE_SHADOW_ROUND);
        shadowColor = a.getColor(R.styleable.ShadowViewCard_shadowColor, getResources().getColor(DEFAULT_VALUE_SHADOW_COLOR));
        shadowCardColor = a.getColor(R.styleable.ShadowViewCard_shadowCardColor, getResources().getColor(DEFAULT_VALUE_SHADOW_CARD_COLOR));
        shadowTopHeight = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowTopHeight, dp2px(getContext(), DEFAULT_VALUE_SHADOW_TOP_HEIGHT));
        shadowRightHeight = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowRightHeight, dp2px(getContext(), DEFAULT_VALUE_SHADOW_RIGHT_HEIGHT));
        shadowLeftHeight = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowLeftHeight, dp2px(getContext(), DEFAULT_VALUE_SHADOW_LEFT_HEIGHT));
        shadowBottomHeight = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowBottomHeight, dp2px(getContext(), DEFAULT_VALUE_SHADOW_BOTTOM_HEIGHT));
        shadowOffsetY = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowOffsetY, dp2px(getContext(), DEFAULT_VALUE_SHADOW_OFFSET_Y));
        shadowOffsetX = a.getDimensionPixelSize(R.styleable.ShadowViewCard_shadowOffsetX, dp2px(getContext(), DEFAULT_VALUE_SHADOW_OFFSET_X));
        shadowRadius = a.getInteger(R.styleable.ShadowViewCard_shadowRadius, DEFAULT_VALUE_SHADOW_RADIUS);
        a.recycle();
        setPadding(shadowLeftHeight, shadowTopHeight, shadowRightHeight, shadowBottomHeight);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.WHITE);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setAntiAlias(true);
        float left = shadowLeftHeight;
        float top = shadowTopHeight;
        float right = getWidth() - shadowRightHeight;
        float bottom = getHeight() - shadowBottomHeight;
        shadowPaint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetX, shadowColor);
        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, shadowRound, shadowRound, shadowPaint);
        canvas.save();
        super.dispatchDraw(canvas);
    }

}
