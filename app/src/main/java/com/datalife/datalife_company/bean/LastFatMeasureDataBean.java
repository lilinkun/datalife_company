package com.datalife.datalife_company.bean;

/**
 * 最后一次检测结果对象
 * Created by LG on 2019/7/24.
 */
public class LastFatMeasureDataBean<T> {
    private T Project6;

    public T getProject6() {
        return Project6;
    }

    public void setProject6(T project6) {
        Project6 = project6;
    }

    @Override
    public String toString() {
        return "LastFatMeasureDataBean{" +
                "Project6=" + Project6 +
                '}';
    }
}