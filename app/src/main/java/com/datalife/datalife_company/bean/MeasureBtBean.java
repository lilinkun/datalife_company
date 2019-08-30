package com.datalife.datalife_company.bean;

import lib.linktop.obj.LoadBtBean;

/**
 * 温度bean
 * Created by LG on 2019/7/22.
 */
public class MeasureBtBean implements LoadBtBean {
    private long ts = 0;
    private double temp = 0.0d;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Bt{" +
                "ts=" + ts +
                ", temp=" + temp +
                '}';
    }
}
