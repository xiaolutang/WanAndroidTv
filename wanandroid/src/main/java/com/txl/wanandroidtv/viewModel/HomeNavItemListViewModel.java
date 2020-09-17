package com.txl.wanandroidtv.viewModel;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.txl.commonlibrary.utils.exector.AppExecutors;
import com.txl.wanandroidtv.bean.home.Article;
import com.txl.wanandroidtv.bean.home.BannerItemData;
import com.txl.wanandroidtv.data.DataDriven;
import com.txl.wanandroidtv.data.Response;
import com.txl.weblinkparse.WebLinkParse;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HomeNavItemListViewModel extends AbsNavItemListViewModel {
    private static final String TAG = HomeNavItemListViewModel.class.getSimpleName();
    private volatile boolean resetData = false;
    private volatile boolean loadData = false;
    /**
     * 置顶数据
     * */
    private MutableLiveData<Response<List<Article>>> topArticles = new MutableLiveData<>();

    private MutableLiveData<Response<List<BannerItemData>>> bannerData = new MutableLiveData<>();

    public MutableLiveData<Response<List<Article>>> getTopArticles(){
        return topArticles;
    }

    public MutableLiveData<Response<List<BannerItemData>>> getBannerData() {
        return bannerData;
    }

    public void fetchBannerData(){
        AppExecutors.execNetIo(new Runnable() {
            @Override
            public void run() {
                Response<List<BannerItemData>> response = DataDriven.INSTANCE.getHomeBanner();
                bannerData.postValue(response);
            }
        });
    }

    /**
     * 获取置顶文章
     * */
    public void fetchTopArticleList(){//fixme 多次请求可能有问题，使用workManager来做吧
        AppExecutors.execNetIo(new Runnable() {//fixme 使用rxJava或kotlin链式调用来编写
            @Override
            public void run() {
                Response<List<Article>> response = DataDriven.INSTANCE.getTopArticleList();
                if(response.netSuccess() && response.getData() != null && response.getData().size() != 0){
                    int size = response.getData().size();
                    CountDownLatch countDownLatch = new CountDownLatch(size);
                    for(final Article article :response.getData()){
                        AppExecutors.execNetIo(new Runnable() {
                            @Override
                            public void run() {
                                article.setImagePath(WebLinkParse.getMaxImgAddress(article.getLink()));
                                countDownLatch.countDown();
                            }
                        });

                    }
                    try {
                        countDownLatch.await();
                        topArticles.postValue(response);
                        //缓存并刷新数据
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

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
//                Response<String> response = DataDriven.INSTANCE.getHomeArticleList(getCurrentPage(),true);
//                if(response.getState()){
//                    Gson g = new  Gson();
//                    HomeArticleListData data = g.fromJson(response.getData(), HomeArticleListData.class);
//                    ResourceBoundary<Object>  resourceBoundary = new ResourceBoundary(STATE_LOADED,0,"success",data,getCurrentPage());
//                    getData().postValue(resourceBoundary);
//                }else{
//                    ResourceBoundary<Object>   resourceBoundary = new ResourceBoundary(STATE_LOADED,0,"failed to load data ",null,getCurrentPage());
//                    getData().postValue(resourceBoundary);
//                }
//                if(resetData){
//                    resetData = false;
//                }
//                if(loadData){
//                    loadData = false;
//                }
            }
        });
    }


    @Override
    protected void getPageData() {
        getHomeNavItemListData();
    }
}
