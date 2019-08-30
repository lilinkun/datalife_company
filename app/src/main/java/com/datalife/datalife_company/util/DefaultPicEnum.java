package com.datalife.datalife_company.util;

import com.datalife.datalife_company.R;

/**
 * app默认头像
 * Created by LG on 2019/7/17.
 */
public enum DefaultPicEnum {
    PIC1(R.mipmap.ic_family_mother,"1"),
    PIC2(R.mipmap.ic_family_father,"2"),
    PIC3(R.mipmap.ic_family_boy,"4"),
    PIC4(R.mipmap.ic_family_girl,"3"),
    PIC5(R.mipmap.ic_family_grandma,"6"),
    PIC6(R.mipmap.ic_family_grandpa,"5");

    private int resPic;
    private String num;

    DefaultPicEnum(int resPic, String num) {
        this.resPic = resPic;
        this.num = num;
    }

    public int getResPic() {
        return resPic;
    }

    public void setResPic(int resPic) {
        this.resPic = resPic;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public static DefaultPicEnum getPageByValue(String val) {
        for (DefaultPicEnum p : values()) {
            if (p.getNum().equals(val))
                return p;
        }
        return null;
    }
}
