/**
  * Copyright 2020 bejson.com 
  */
package com.txl.wanandroidtv.bean.com.besjon.pojo.setup;
import com.txl.wanandroidtv.bean.com.besjon.pojo.Data;

import java.util.List;

/**
 * Auto-generated: 2020-04-01 18:22:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private List<SetUpData> data;
    private int errorCode;
    private String errorMsg;
    public void setData(List<SetUpData> data) {
         this.data = data;
     }
     public List<SetUpData> getData() {
         return data;
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