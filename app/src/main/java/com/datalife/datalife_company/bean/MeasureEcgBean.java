package com.datalife.datalife_company.bean;

import java.io.Serializable;

import lib.linktop.obj.LoadECGBean;

/**
 * ECG的实体类
 * Created by LG on 2019/7/23.
 */
public class MeasureEcgBean implements Serializable,LoadECGBean {

    private int rrMax;
    private int rrMin;
    private int hr;
    private int hrv;
    private int mood;
    private int br;
    private long duration;
    private long ts;
    private String wave;

    public int getRrMax() {
        return rrMax;
    }

    public void setRrMax(int rrMax) {
        this.rrMax = rrMax;
    }

    public int getRrMin() {
        return rrMin;
    }

    public void setRrMin(int rrMin) {
        this.rrMin = rrMin;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getHrv() {
        return hrv;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getBr() {
        return br;
    }

    public void setBr(int br) {
        this.br = br;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    public String getWave() {
        return wave;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "ECG{" +
                "rrMax=" + rrMax +
                ", rrMin=" + rrMin +
                ", hr=" + hr +
                ", hrv=" + hrv +
                ", mood=" + mood +
                ", br=" + br +
                ", duration=" + duration +
                ", ts=" + ts +
                ", wave='" + wave + '\'' +
                '}';
    }

}
