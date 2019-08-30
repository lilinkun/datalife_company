package com.datalife.datalife_company.bean;

import java.io.Serializable;

/**
 * Created by LG on 2019/7/16.
 */

public class LoginBean implements Serializable {

   /* private String user_id;
    private String user_name;
    private String mobile;
    private String user_status;
    private String create_date;
    private String lastLogin_date;
    private String lastLogin_ip;
    private String thisLogin_date;
    private String thisLogin_ip;
    private String HeadPic;
    private String viplevel;
    private String nikeName;
    private String Name;
    private String openid;
    private String unionid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLastLogin_date() {
        return lastLogin_date;
    }

    public void setLastLogin_date(String lastLogin_date) {
        this.lastLogin_date = lastLogin_date;
    }

    public String getLastLogin_ip() {
        return lastLogin_ip;
    }

    public void setLastLogin_ip(String lastLogin_ip) {
        this.lastLogin_ip = lastLogin_ip;
    }

    public String getThisLogin_date() {
        return thisLogin_date;
    }

    public void setThisLogin_date(String thisLogin_date) {
        this.thisLogin_date = thisLogin_date;
    }

    public String getThisLogin_ip() {
        return thisLogin_ip;
    }

    public void setThisLogin_ip(String thisLogin_ip) {
        this.thisLogin_ip = thisLogin_ip;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", user_status='" + user_status + '\'' +
                ", create_date='" + create_date + '\'' +
                ", lastLogin_date='" + lastLogin_date + '\'' +
                ", lastLogin_ip='" + lastLogin_ip + '\'' +
                ", thisLogin_date='" + thisLogin_date + '\'' +
                ", thisLogin_ip='" + thisLogin_ip + '\'' +
                ", HeadPic='" + HeadPic + '\'' +
                ", viplevel='" + viplevel + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", Name='" + Name + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }*/

    private String user_id;
    private String user_name;
    private String mobile;
    private String user_status;
    private String create_date;
    private String viplevel;
    private String HeadPic;
    private String openid;
    private String unionid;
    private String nikeName;
    private String Name;
    private String Store_Id;
    private String Store_Name;
    private String Store_Address;
    private String Responsibility_Name;
    private String Phone;
    private String Certificates;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStore_Id() {
        return Store_Id;
    }

    public void setStore_Id(String store_Id) {
        Store_Id = store_Id;
    }

    public String getStore_Name() {
        return Store_Name;
    }

    public void setStore_Name(String store_Name) {
        Store_Name = store_Name;
    }

    public String getStore_Address() {
        return Store_Address;
    }

    public void setStore_Address(String store_Address) {
        Store_Address = store_Address;
    }

    public String getResponsibility_Name() {
        return Responsibility_Name;
    }

    public void setResponsibility_Name(String responsibility_Name) {
        Responsibility_Name = responsibility_Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCertificates() {
        return Certificates;
    }

    public void setCertificates(String certificates) {
        Certificates = certificates;
    }

    @Override
    public String toString() {
        return "StoreBean{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", user_status='" + user_status + '\'' +
                ", create_date='" + create_date + '\'' +
                ", viplevel='" + viplevel + '\'' +
                ", HeadPic='" + HeadPic + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", Name='" + Name + '\'' +
                ", Store_Id='" + Store_Id + '\'' +
                ", Store_Name='" + Store_Name + '\'' +
                ", Store_Address='" + Store_Address + '\'' +
                ", Responsibility_Name='" + Responsibility_Name + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Certificates='" + Certificates + '\'' +
                '}';
    }

}

