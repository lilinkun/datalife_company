package com.datalife.datalife_company.bean;

/**
 * Created by LG on 2019/8/28.
 */
public class TypeBean {

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TypeBean{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
