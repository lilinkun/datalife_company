package com.datalife.datalife_company.custom;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by LG on 2018/3/21.
 */

public class MyMarkerView  extends MarkerView {

    private TextView tvContent;
    private Handler myHandler;

    public MyMarkerView(Context context, int layoutResource, Handler handler) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.myHandler = handler;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
//            String str = Utils.formatNumber(e.getY(), 0, true);
            String str = String.valueOf(e.getY());
            tvContent.setText("" + str);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putFloat("x",e.getX());
            message.setData(bundle);
            message.what = IDatalifeConstant.MESSAGEWHAT_CHART;
            myHandler.sendMessage(message);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
