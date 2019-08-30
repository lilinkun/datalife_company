package aicare.net.cn.iweightlibrary.entity;

import java.io.Serializable;

/**
 * Created by Suzy on 2017/2/13.
 */

public class WeightData implements Serializable {

    private boolean isStable;//是否稳定
    private double weight;//体重
    private double temp = Double.MAX_VALUE;//温度(AC03的称才支持)

    public WeightData(boolean isStable, double weight, double temp) {
        this.isStable = isStable;
        this.weight = weight;
        this.temp = temp;
    }

    public boolean isStable() {
        return isStable;
    }

    public double getWeight() {
        return weight;
    }

    public double getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return "WeightData{" +
                "isStable=" + isStable +
                ", weight=" + weight +
                ", temp=" + temp +
                '}';
    }
}
