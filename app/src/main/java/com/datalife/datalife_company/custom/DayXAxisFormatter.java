package com.datalife.datalife_company.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by LG on 2018/3/17.
 */

public class DayXAxisFormatter implements IAxisValueFormatter
{

    protected String[] mDays = new String[]{
            "0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22","24"
    };

    public DayXAxisFormatter() {
        // maybe do something here or provide parameters in constructor

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        float percent = value / axis.mAxisRange;
        return mDays[(int) (mDays.length * percent)];
    }
}
