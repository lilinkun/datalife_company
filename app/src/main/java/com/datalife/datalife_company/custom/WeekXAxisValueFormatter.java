package com.datalife.datalife_company.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by LG on 2018/3/17.
 */

public class WeekXAxisValueFormatter implements IAxisValueFormatter
{

    protected String[] mWeeks = new String[]{
            "周一", "周二", "周三", "周四", "周五", "周六", "周天"
    };

    public WeekXAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        float percent = value / axis.mAxisRange;
        return mWeeks[(int) (mWeeks.length * percent)];
    }
}
