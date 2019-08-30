package com.datalife.datalife_company.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by LG on 2018/3/17.
 */

public class MonthXAxisFormatter implements IAxisValueFormatter
{

    protected String[] mMonths = new String[]{
            "1", "4", "7", "10", "13", "16", "19", "21", "24", "27", "30"
    };

    public MonthXAxisFormatter() {
        // maybe do something here or provide parameters in constructor

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        float percent = value / axis.mAxisRange;
        return mMonths[(int) (mMonths.length * percent)];
    }
}