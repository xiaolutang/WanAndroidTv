package com.txl.wanandroidtv.bean.com.besjon.pojo;

import java.util.List;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/30
 * description：导航数据
 * */
public class NavigateArticleListData {
    private List<NavigateCategoryData> data;
    private int errorCode;
    private String errorMsg;

    public List<NavigateCategoryData> getData() {
        return data;
    }

    public void setData(List<NavigateCategoryData> data) {
        this.data = data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
}
