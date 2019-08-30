package com.datalife.datalife_company.util;

/**
 * Created by LG on 2019/8/7.
 */

public enum MeasureEnum {
    ALL("全部类别",0),
    BP("血压",1),
    BT("体温",2),
    OXY("血氧",3);


    private String measureName;
    private int measureCode;

    MeasureEnum(String num, int resPic) {
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

    public static MeasureEnum getPageByValue(String name) {
        for (MeasureEnum p : values()) {
            if (p.getMeasureName().equals(name))
                return p;
        }
        return null;
    }
}
