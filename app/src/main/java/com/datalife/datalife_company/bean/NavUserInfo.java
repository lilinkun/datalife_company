package com.datalife.datalife_company.bean;

import java.io.Serializable;

/**
 * Created by LG on 2019/7/17.
 */

public class NavUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    String username;
    String userface;
    String sex;
    String height;
    String weight;
    String age;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserface() {
        return userface;
    }

    public void setUserface(String userface) {
        this.userface = userface;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "NavUserInfo{" +
                "username='" + username + '\'' +
                ", userface='" + userface + '\'' +
                ", sex='" + sex + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", age=" + age +
                '}';
    }
}

