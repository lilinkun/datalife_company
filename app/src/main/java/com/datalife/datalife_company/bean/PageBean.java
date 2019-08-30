package com.datalife.datalife_company.bean;

/**
 * 服务器返回值pagebean
 * Created by LG on 2018/2/4.
 */
public class PageBean {

    String SizeCount;
    String PageIndex;
    String MaxPage;
    String PageCount;

    public String getSizeCount() {
        return SizeCount;
    }

    public void setSizeCount(String sizeCount) {
        SizeCount = sizeCount;
    }

    public String getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }

    public String getMaxPage() {
        return MaxPage;
    }

    public void setMaxPage(String maxPage) {
        MaxPage = maxPage;
    }

    public String getPageCount() {
        return PageCount;
    }

    public void setPageCount(String pageCount) {
        PageCount = pageCount;
    }


}
