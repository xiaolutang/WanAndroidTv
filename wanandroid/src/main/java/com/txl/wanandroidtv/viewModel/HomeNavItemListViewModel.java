package com.txl.wanandroidtv.viewModel;

import com.google.gson.Gson;
import com.txl.commonlibrary.utils.exector.AppExecutors;
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData;
import com.txl.wanandroidtv.data.DataDriven;
import com.txl.wanandroidtv.data.Response;

import static com.txl.wanandroidtv.viewModel.ResourceBoundaryKt.STATE_LOADED;

public class HomeNavItemListViewModel extends AbsNavItemListViewModel {
    private volatile boolean resetData = false;
    private volatile boolean loadData = false;
    @Override
    public void resetData() {
        resetData = true;
        setCurrentPage(0);
        getHomeNavItemListData();
    }

    @Override
    public void nextPage() {
        if(loadData || resetData){//重置或者 正在加载数据的时候不能进行获取下一页的数据
            return;
        }
        loadData = true;
        setCurrentPage(getCurrentPage()+1);
        getHomeNavItemListData();
    }

    private void getHomeNavItemListData(){
        AppExecutors.execNetIo(new Runnable() {
            @Override
            public void run() {
                Response<String> response = DataDriven.INSTANCE.getHomeArticleList(getCurrentPage(),true);
                if(response.getState()){
                    Gson g = new  Gson();
                    HomeArticleListData data = g.fromJson(response.getData(), HomeArticleListData.class);
                    ResourceBoundary<Object>  resourceBoundary = new ResourceBoundary(STATE_LOADED,0,"success",data,getCurrentPage());
                    getData().postValue(resourceBoundary);
                }else{
                    ResourceBoundary<Object>   resourceBoundary = new ResourceBoundary(STATE_LOADED,0,"failed to load data ",null,getCurrentPage());
                    getData().postValue(resourceBoundary);
                }
                if(resetData){
                    resetData = false;
                }
                if(loadData){
                    loadData = false;
                }
            }
        });
    }


    @Override
    protected void getPageData() {
        getHomeNavItemListData();
    }
}
