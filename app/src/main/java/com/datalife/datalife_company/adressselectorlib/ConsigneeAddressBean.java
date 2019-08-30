package com.datalife.datalife_company.adressselectorlib;


import com.datalife.datalife_company.bean.ProvinceBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wepon on 2017/12/4.
 * 数据模型
 */

public class ConsigneeAddressBean implements Serializable {

    private List<ProvinceBean> province;
    private List<ProvinceBean> city;
    private List<ProvinceBean> district;

    public List<ProvinceBean> getProvince() {
        return province;
    }

    public void setProvince(List<ProvinceBean> province) {
        this.province = province;
    }

    public List<ProvinceBean> getCity() {
        return city;
    }

    public void setCity(List<ProvinceBean> city) {
        this.city = city;
    }

    public List<ProvinceBean> getDistrict() {
        return district;
    }

    public void setDistrict(List<ProvinceBean> district) {
        this.district = district;
    }

    public static class AddressItemBean implements Serializable {
        private String i;
        private String n;
        private String p;

        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
}
