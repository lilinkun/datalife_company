package com.datalife.datalife_company.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.interf.OnTitleBarClickListener;

/**
 * Created by LG on 2019/7/16.
 */
public class CustomTitleBar extends LinearLayout implements View.OnClickListener{

    private Context context;
    private View view;
    private Resources res;
    private TextView tv_common_title,tv_more;
    private ImageView iv_back,iv_more;
    private LinearLayout ll_back;
    private OnTitleBarClickListener clickListener;

    public CustomTitleBar(Context context) {
        super(context);
        init(context);
    }

    public CustomTitleBar(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.titlebar);

        String back_text=a.getString(R.styleable.titlebar_text_left);
        if(null!=back_text){
//			tv_back.setText(back_text);

            float mLeft_TextSize = a.getFloat(R.styleable.titlebar_left_TextSize, 0);
//			if(mLeft_TextSize > 0)
//				tv_back.setTextSize(mLeft_TextSize);
        }

        tv_common_title.setText(a.getString(R.styleable.titlebar_text_title));
        tv_common_title.setTextColor(a.getColor(R.styleable.titlebar_title_color, res.getColor(R.color.white)));
        tv_more.setText(a.getString(R.styleable.titlebar_text_right));
        tv_more.setTextColor(res.getColor(R.color.white));
        tv_common_title.setTextSize(a.getFloat(R.styleable.titlebar_text_size,20));
//        iv_back.setImageResource(a.getInt(R.styleable.titlebar_img_left,R.drawable.backicon));
        Drawable img=a.getDrawable(R.styleable.titlebar_img_left);
        if(null!=img)
            iv_back.setImageDrawable(img);
        iv_more.setImageDrawable(a.getDrawable(R.styleable.titlebar_img_right));
        int color=a.getColor(R.styleable.titlebar_layout_color, -1);
//		rl_main.setBackgroundColor(a.getColor(R.styleable.viewtitle_layout_color, R.color.bar_green));
        /*if(color==-1){
            rl_main.setBackgroundColor(res.getColor(R.color.title_bar_color));
        }else{
            rl_main.setBackgroundColor(color);
        }*/
        boolean left_visibility = a.getBoolean(R.styleable.titlebar_left_visibility, true);
        if(left_visibility){
            setLeftImageVisibility(View.VISIBLE);
        }else {
            setLeftImageVisibility(View.GONE);
        }
        a.recycle();
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        this.context = context;
        res=context.getResources();
        view = LayoutInflater.from(context).inflate(R.layout.custom_titlebar,null);

        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
        tv_common_title = (TextView) view.findViewById(R.id.tv_headtop);
        tv_more = (TextView) view.findViewById(R.id.tv_head_right);
        iv_back = (ImageView) view.findViewById(R.id.iv_head_left);
        iv_more = (ImageView) view.findViewById(R.id.iv_head_right);

        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        tv_more.setOnClickListener(this);

        this.addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }


    public void setLeftImageVisibility(int visibility){
        ll_back.setVisibility(visibility);
        iv_back.setVisibility(visibility);
    }

    public void setRightTextVisibility(int visibility,int content){
        tv_more.setVisibility(visibility);
        tv_more.setText(context.getString(content));
    }

    public void setRightTextVisibility(int visibility){
        tv_more.setVisibility(visibility);
    }

    public void setRightText(int resId){
        tv_more.setText(context.getString(resId));
    }

    public void setRightText(String text){
        tv_more.setText(text);
    }

    public void setRightDrawable(int resId){
        iv_more.setImageDrawable(context.getResources().getDrawable(resId));
    }

    public void setRightDrawable(Drawable drawable){
        iv_more.setImageDrawable(drawable);
    }

    public void setRightDrawableVisibility(int visibility){
        iv_more.setVisibility(visibility);
    }

    public void SetOnTitleClickListener(OnTitleBarClickListener clickListener){
        this.clickListener=clickListener;
    }

    public void setTitleName(int titleName){
        tv_common_title.setText(context.getString(titleName));
    }

    public void setTitleName(String titleName){
        tv_common_title.setText(titleName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head_left:
                if (clickListener != null){
                    clickListener.onBackClick();
                }
                break;

            case R.id.iv_head_right:
                if (clickListener != null){
                    clickListener.onMoreClick();
                }
                break;
            case R.id.tv_head_right:
                if (clickListener != null){
                    clickListener.onMoreClick();
                }
                break;
        }
    }
}

