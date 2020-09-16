/**
  * Copyright 2020 bejson.com 
  */
package com.txl.wanandroidtv.bean.com.besjon.pojo;
import com.txl.wanandroidtv.bean.home.Article;

import java.util.List;

/**
 * Auto-generated: 2020-03-12 21:56:12
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private int curPage;
    private List<Article> datas;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    public void setCurPage(int curPage) {
         this.curPage = curPage;
     }
     public int getCurPage() {
         return curPage;
     }

    public void setDatas(List<Article> data) {
         this.datas = data;
     }
     public List<Article> getDatas() {
         return datas;
     }

    public void setOffset(int offset) {
         this.offset = offset;
     }
     public int getOffset() {
         return offset;
     }

    public void setOver(boolean over) {
         this.over = over;
     }
     public boolean getOver() {
         return over;
     }

    public void setPageCount(int pageCount) {
         this.pageCount = pageCount;
     }
     public int getPageCount() {
         return pageCount;
     }

    public void setSize(int size) {
         this.size = size;
     }
     public int getSize() {
         return size;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

}