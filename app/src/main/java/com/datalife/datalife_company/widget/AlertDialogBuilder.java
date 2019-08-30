package com.datalife.datalife_company.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.datalife.datalife_company.R;

/**
 * Created by LG on 2019/7/19.
 */
public final class AlertDialogBuilder extends AlertDialog.Builder {

    public AlertDialogBuilder(Context context) {
        this(context, R.style.AppDialogStyle);
    }

    public AlertDialogBuilder(Context context, int theme) {
        super(context, theme);
    }
}