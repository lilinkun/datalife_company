package com.datalife.datalife_company.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.datalife.datalife_company.R;

/**
 * Created by LG on 2019/7/17.
 */

public class PopWindowSexSelectBy implements View.OnClickListener {

    private RelativeLayout exit;
    private Button byGallery;
    private Button byCamera;
    private Button cancel;
    private PopupWindow window;
    private View contentView;
    private View v;
    private RelativeLayout content;
    private Animation bottom_in;
    private Animation bottom_out;
    private Animation close_alpha;
    private Animation show_alpha;
    private Context context;

    public PopWindowSexSelectBy(Context context, View v) {
        this.v = v;
        this.context = context;
        contentView = (View) ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.popwindow_photo_select_by, null);
        byGallery = (Button) contentView.findViewById(R.id.byGallery);
        byCamera = (Button) contentView.findViewById(R.id.byCamera);
        cancel = (Button) contentView.findViewById(R.id.cancel);
        content = (RelativeLayout) contentView.findViewById(R.id.content);

        setLanguage();
        byGallery.setOnClickListener(this);
        byCamera.setOnClickListener(this);
        cancel.setOnClickListener(this);

        exit = (RelativeLayout) contentView.findViewById(R.id.exit);
        exit.setOnClickListener(this);

        bottom_in = AnimationUtils.loadAnimation(context, R.anim.bottom_in);
        bottom_out = AnimationUtils.loadAnimation(context, R.anim.bottom_out);
        close_alpha = AnimationUtils.loadAnimation(context, R.anim.close_alpha);
        show_alpha = AnimationUtils.loadAnimation(context, R.anim.show_alpha);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        window.setFocusable(true);
        window.setTouchable(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    closeWindow();
                    window = null;
                    return true;
                }
                return false;
            }
        });
    }

    private void setLanguage() {
        String mode;
//        mode = SharePerfenceUtils.getInstance(context).getLanuageMode();
//        if (mode.equals("1")) {
//            byGallery.setText("man");
//            byCamera.setText("woman");
//            cancel.setText("cancel");
//        } else {
//
//            byGallery.setText("男");
//            byCamera.setText("女");
//            cancel.setText("取消");
//        }
        byGallery.setText(context.getResources().getString(R.string.my_sex_nan));
        byCamera.setText(context.getResources().getString(R.string.my_sex_nv));
        cancel.setText(context.getResources().getString(R.string.text_change_cancle));

//
    }


    public void show() {
        window.showAtLocation(v, Gravity.CENTER | Gravity.CENTER, 0, 0);
        content.startAnimation(bottom_in);
        exit.startAnimation(show_alpha);
        exit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == byGallery) {
            window.dismiss();
            // Handle clicks for byGallery
            if (listener != null) {
                listener.onByWhat(false);
            }
        } else if (v == byCamera) {
            // Handle clicks for byCamera
            closeWindow();
            if (listener != null) {
                listener.onByWhat(true);
            }
        } else if (v == cancel) {
            // Handle clicks for cancel
            closeWindow();
        } else if (v == exit) {
            // Handle clicks for exit
            closeWindow();
        }
    }

    private ByWhatListener listener = null;

    public interface ByWhatListener {
        public void onByWhat(Boolean isByCamera);
    }

    public void setByWhatListener(ByWhatListener listener) {
        this.listener = listener;
    }

    private void closeWindow() {
        window.dismiss();
    }
}


