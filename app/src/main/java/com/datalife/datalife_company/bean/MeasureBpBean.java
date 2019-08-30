package com.datalife.datalife_company.bean;

import java.io.Serializable;

import lib.linktop.obj.LoadBpBean;

/**
 * 血压实体类
 * Created by LG on 2019/7/22.
 */
public class MeasureBpBean implements LoadBpBean,Serializable{

    /**
     *舒张压
     */
    private int sbp;
    /**
     *收缩压
     */
    private int dbp;
    /**
     * 心率
     */
    private int hr;
    private long ts;

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    public int getHr() {
        return hr;
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
        return "Bp{" +
                "sbp=" + sbp +
                ", dbp=" + dbp +
                ", hr=" + hr +
                '}';
    }
}
