package com.datalife.datalife_company.interf;

import android.graphics.drawable.Drawable;

/**
 * Created by LG on 2018/1/17.
 */

public interface onTitleBarSetListener {
    void setRightDrawable(int resId);
    void setRightDrawable(Drawable drawable);
    void setRightText(int resId);
    void setRightText(String text);
    void setRightDrawableVisibility(int visibility);
    void setRightTextVisibility(int visibility);
    void setVisible(int visible);
}