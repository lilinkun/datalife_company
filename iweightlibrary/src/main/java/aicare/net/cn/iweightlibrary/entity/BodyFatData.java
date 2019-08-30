package aicare.net.cn.iweightlibrary.entity;

import java.io.Serializable;

public class BodyFatData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日期
     */
    private String date;

    /**
     * 时间
     */
    private String time;
    /**
     * 体重
     */
    private double weight;
    /**
     * 身体质量指数
     */
    private double bmi;
    /**
     * 体脂率 body fat rate
     */
    private double bfr;
    /**
     * 皮下脂肪率 Subcutaneous fat rate
     */
    private double sfr;
    /**
     * 内脏脂肪指数
     */
    private int uvi;
    /**
     * 肌肉率 Rate of muscle
     */
    private double rom;
    /**
     * 基础代谢率 basal metabolic rate
     */
    private double bmr;
    /**
     * 骨骼质量 Bone Mass
     */
    private double bm;
    /**
     * 水含量
     */
    private double vwc;
    /**
     * 身体年龄 physical bodyAge
     */
    private int bodyAge;
    /**
     * 蛋白率 protein percentage
     */
    private double pp;
    /**
     * 用户编号
     */
    private int number;
    /**
     * 性别
     */
    private int sex;
    /**
     * 年龄
     */
    private int age;
    /**
     * 身高
     */
    private float height;
    /**
     * 阻抗值
     */
    private int adc;

    /**
     * 去脂体重
     */
    private double dbw;

    public BodyFatData() {
    }

    public BodyFatData(String date, String time, double weight, double bmi, double bfr, double sfr, int uvi, double rom, double bmr, double bm, double vwc, int bodyAge, double pp, int number, int sex, int age, float height, int adc) {
        this.date = date;
        this.time = time;
        this.weight = weight;
        this.bmi = bmi;
        this.bfr = bfr;
        this.sfr = sfr;
        this.uvi = uvi;
        this.rom = rom;
        this.bmr = bmr;
        this.bm = bm;
        this.vwc = vwc;
        this.bodyAge = bodyAge;
        this.pp = pp;
        this.number = number;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.adc = adc;
        this.dbw = weight - bfr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBfr() {
        return bfr;
    }

    public void setBfr(double bfr) {
        this.bfr = bfr;
    }

    public double getSfr() {
        return sfr;
    }

    public void setSfr(double sfr) {
        this.sfr = sfr;
    }

    public int getUvi() {
        return uvi;
    }

    public void setUvi(int uvi) {
        this.uvi = uvi;
    }

    public double getRom() {
        return rom;
    }

    public void setRom(double rom) {
        this.rom = rom;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public double getBm() {
        return bm;
    }

    public void setBm(double bm) {
        this.bm = bm;
    }

    public double getVwc() {
        return vwc;
    }

    public void setVwc(double vwc) {
        this.vwc = vwc;
    }

    public int getBodyAge() {
        return bodyAge;
    }

    public void setBodyAge(int bodyAge) {
        this.bodyAge = bodyAge;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAdc() {
        return adc;
    }

    public void setAdc(int adc) {
        this.adc = adc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getDbw() {
        return dbw;
    }

    public void setDbw(double dbw) {
        this.dbw = dbw;
    }

    @Override
    public String toString() {
        return "BodyFatData [date=" + date + ", time=" + time + ", weight=" + weight
                + ", bmi=" + bmi + ", 体脂率bfr=" + bfr + ", 皮下脂肪率sfr=" + sfr + ", 内脏脂肪指数uvi="
                + uvi + ", 肌肉率 rom=" + rom + ", 基础代谢率bmr=" + bmr + ", 骨骼质量bm=" + bm
                + ", 水含量vwc=" + vwc + ", 身体年龄bodyAge=" + bodyAge + ", 蛋白率pp=" + pp + ", number="
                + number + ", 性别sex=" + sex + ", 年龄age=" + age + ", 身高height=" + height + ", 阻抗值adc=" + adc + ", 去脂体重dbw=" + dbw +  "]";
    }
}
