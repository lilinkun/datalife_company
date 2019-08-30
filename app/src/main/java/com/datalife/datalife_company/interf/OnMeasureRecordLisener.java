package com.datalife.datalife_company.interf;

/**
 * Created by LG on 2019/7/24.
 */
public interface OnMeasureRecordLisener {

    public void onBtIntent(int Date, int pageIndex, String startDate, String finishDate);

    public void onSpo2hIntent(int Date, int pageIndex, String startDate, String finishDate);

    public void onHrIntent(int Date, int pageIndex, String startDate, String finishDate);

    public void onEcgIntent(int date, int pageIndex);

    public void onBpIntent(int Date, int pageIndex, String startDate, String finishDate);
}
