package com.datalife.datalife_company.bean;

/**
 * Created by LG on 2019/8/28.
 */
public class MeasureErrorListBean {

    private String Member_Id;
    private String Member_Name;
    private String Member_Phone;
    private String ProjectName;
    private String Project_Id;
    private String user_id;
    private String user_name;
    private String Check_Result;
    private String CreateDate;
    private String Check_Id;
    private String Check_Result_Name;
    private String Check_Value;

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public void setMember_Name(String member_Name) {
        Member_Name = member_Name;
    }

    public String getMember_Phone() {
        return Member_Phone;
    }

    public void setMember_Phone(String member_Phone) {
        Member_Phone = member_Phone;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(String project_Id) {
        Project_Id = project_Id;
    }

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

    public String getCheck_Result() {
        return Check_Result;
    }

    public void setCheck_Result(String check_Result) {
        Check_Result = check_Result;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCheck_Id() {
        return Check_Id;
    }

    public void setCheck_Id(String check_Id) {
        Check_Id = check_Id;
    }

    public String getCheck_Result_Name() {
        return Check_Result_Name;
    }

    public void setCheck_Result_Name(String check_Result_Name) {
        Check_Result_Name = check_Result_Name;
    }

    public String getCheck_Value() {
        return Check_Value;
    }

    public void setCheck_Value(String check_Value) {
        Check_Value = check_Value;
    }

    @Override
    public String toString() {
        return "MeasureErrorListBean{" +
                "Member_Id='" + Member_Id + '\'' +
                ", Member_Name='" + Member_Name + '\'' +
                ", Member_Phone='" + Member_Phone + '\'' +
                ", ProjectName='" + ProjectName + '\'' +
                ", Project_Id='" + Project_Id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", Check_Result='" + Check_Result + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                ", Check_Id='" + Check_Id + '\'' +
                ", Check_Result_Name='" + Check_Result_Name + '\'' +
                ", Check_Value='" + Check_Value + '\'' +
                '}';
    }
}
