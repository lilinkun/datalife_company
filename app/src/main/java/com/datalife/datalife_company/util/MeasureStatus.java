package com.datalife.datalife_company.util;

/**
 * Created by LG on 2019/8/7.
 */
public enum MeasureStatus {
    ALL("全部状态",2),
    NORMAL("正常",0),
    ABNORMAL("异常",1);

    private String measureName;
    private int measureCode;

    MeasureStatus(String num, int resPic) {
        this.measureCode = resPic;
        this.measureName = num;
    }

    public String getMeasureName() {
        return measureName;
    }

    public void setMeasureName(String measureName) {
        this.measureName = measureName;
    }

    public int getMeasureCode() {
        return measureCode;
    }

    public void setMeasureCode(int measureCode) {
        this.measureCode = measureCode;
    }

    public static MeasureStatus getPageByValue(String name) {
        for (MeasureStatus p : values()) {
            if (p.getMeasureName().equals(name))
                return p;
        }
        return null;
    }

}
