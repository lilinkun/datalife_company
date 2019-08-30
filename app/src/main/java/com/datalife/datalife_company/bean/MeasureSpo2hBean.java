package com.datalife.datalife_company.bean;

import lib.linktop.obj.LoadSPO2HBean;

/**
 * spo2h实体类
 * Created by LG on 2019/7/22.
 */
public class MeasureSpo2hBean implements LoadSPO2HBean{

    private int value = 0;
    private int hr = 0;
    private long ts = 0L;

    public int getHr() {
        return hr;
    }

    @Override
    public void setValue(int i) {
        this.value = i;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "SPO2H{" +
                "spo2h=" + value +
                ", hr=" + hr +
                ", ts=" + ts +
                '}';
    }
}
