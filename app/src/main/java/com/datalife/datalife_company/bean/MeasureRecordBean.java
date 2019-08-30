package com.datalife.datalife_company.bean;

import java.io.Serializable;

/**
 * 检测历史数据
 * Created by LG on 2019/7/24.
 */
public class MeasureRecordBean implements Serializable {
        String CheckId;
        String Member_Id;
        String user_id;
        String Machine_Id;
        String ProjectId;
        String CheckValue1;
        String CheckValue2;
        String CheckValue3;
        String CheckValue4;
        String CheckValue5;
        String CheckValue6;
        String CheckValue7;
        String CheckValue8;
        String CheckValue9;
        String CheckValue10;
        String CheckValue11;
        String CheckValue12;
        String CheckValue13;
        String Project_Json;
        String CreateDate;
        String Machine_Sn;

    public String getCheckId() {
        return CheckId;
    }

    public void setCheckId(String checkId) {
        CheckId = checkId;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMachine_Id() {
        return Machine_Id;
    }

    public void setMachine_Id(String machine_Id) {
        Machine_Id = machine_Id;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getCheckValue1() {
        return CheckValue1;
    }

    public void setCheckValue1(String checkValue1) {
        CheckValue1 = checkValue1;
    }

    public String getCheckValue2() {
        return CheckValue2;
    }

    public void setCheckValue2(String checkValue2) {
        CheckValue2 = checkValue2;
    }

    public String getCheckValue3() {
        return CheckValue3;
    }

    public void setCheckValue3(String checkValue3) {
        CheckValue3 = checkValue3;
    }

    public String getCheckValue4() {
        return CheckValue4;
    }

    public void setCheckValue4(String checkValue4) {
        CheckValue4 = checkValue4;
    }

    public String getCheckValue5() {
        return CheckValue5;
    }

    public void setCheckValue5(String checkValue5) {
        CheckValue5 = checkValue5;
    }

    public String getCheckValue6() {
        return CheckValue6;
    }

    public void setCheckValue6(String checkValue6) {
        CheckValue6 = checkValue6;
    }

    public String getCheckValue7() {
        return CheckValue7;
    }

    public void setCheckValue7(String checkValue7) {
        CheckValue7 = checkValue7;
    }

    public String getCheckValue8() {
        return CheckValue8;
    }

    public void setCheckValue8(String checkValue8) {
        CheckValue8 = checkValue8;
    }

    public String getCheckValue9() {
        return CheckValue9;
    }

    public void setCheckValue9(String checkValue9) {
        CheckValue9 = checkValue9;
    }

    public String getCheckValue10() {
        return CheckValue10;
    }

    public void setCheckValue10(String checkValue10) {
        CheckValue10 = checkValue10;
    }

    public String getCheckValue11() {
        return CheckValue11;
    }

    public void setCheckValue11(String checkValue11) {
        CheckValue11 = checkValue11;
    }

    public String getCheckValue12() {
        return CheckValue12;
    }

    public void setCheckValue12(String checkValue12) {
        CheckValue12 = checkValue12;
    }

    public String getCheckValue13() {
        return CheckValue13;
    }

    public void setCheckValue13(String checkValue13) {
        CheckValue13 = checkValue13;
    }

    public String getProject_Json() {
        return Project_Json;
    }

    public void setProject_Json(String project_Json) {
        Project_Json = project_Json;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getMachine_Sn() {
        return Machine_Sn;
    }

    public void setMachine_Sn(String machine_Sn) {
        Machine_Sn = machine_Sn;
    }

    @Override
    public String toString() {
        return "MeasureRecordBean{" +
                "CheckId='" + CheckId + '\'' +
                ", Member_Id='" + Member_Id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", Machine_Id='" + Machine_Id + '\'' +
                ", ProjectId='" + ProjectId + '\'' +
                ", CheckValue1='" + CheckValue1 + '\'' +
                ", CheckValue2='" + CheckValue2 + '\'' +
                ", CheckValue3='" + CheckValue3 + '\'' +
                ", CheckValue4='" + CheckValue4 + '\'' +
                ", CheckValue5='" + CheckValue5 + '\'' +
                ", CheckValue6='" + CheckValue6 + '\'' +
                ", CheckValue7='" + CheckValue7 + '\'' +
                ", CheckValue8='" + CheckValue8 + '\'' +
                ", CheckValue9='" + CheckValue9 + '\'' +
                ", CheckValue10='" + CheckValue10 + '\'' +
                ", CheckValue11='" + CheckValue11 + '\'' +
                ", CheckValue12='" + CheckValue12 + '\'' +
                ", CheckValue13='" + CheckValue13 + '\'' +
                ", Project_Json='" + Project_Json + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                ", Machine_Sn='" + Machine_Sn + '\'' +
                '}';
    }
}
