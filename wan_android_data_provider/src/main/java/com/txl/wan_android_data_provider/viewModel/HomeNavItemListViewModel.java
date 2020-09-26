package com.txl.wan_android_data_provider.viewModel;


import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tencent.mmkv.MMKV;
import com.txl.commonlibrary.utils.Md5Utils;
import com.txl.commonlibrary.utils.StringUtils;
import com.txl.commonlibrary.utils.exector.AppExecutors;
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data;
import com.txl.wan_android_data_provider.bean.home.Article;
import com.txl.wan_android_data_provider.bean.home.BannerItemData;
import com.txl.wan_android_data_provider.data.DataDriven;
import com.txl.wan_android_data_provider.data.Response;
import com.txl.weblinkparse.WebLinkParse;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.txl.wan_android_data_provider.viewModel.ResourceBoundaryKt.STATE_ERROR;
import static com.txl.wan_android_data_provider.viewModel.ResourceBoundaryKt.STATE_LOADED;

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

    private Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            Response<List<BannerItemData>> response = DataDriven.INSTANCE.getHomeBanner();
            bannerData.postValue(response);
        }
    };
    private FutureTask<Object> bannerTask = new FutureTask<Object>(bannerRunnable,null);

    private Runnable TopArticleRunnable = new Runnable() {
        @Override
        public void run() {
            Response<List<Article>> response = DataDriven.INSTANCE.getTopArticleList();
            if(response.netSuccess() && response.getData() != null && response.getData().size() != 0){
                int size = response.getData().size();
                final CountDownLatch countDownLatch = new CountDownLatch(size);
                for(final Article article :response.getData()){
                    if(!StringUtils.INSTANCE.isNetUrl(article.getLink())){
                        countDownLatch.countDown();
                        continue;
                    }
                    AppExecutors.execNetIo(new Runnable() {
                        @Override
                        public void run() {
                            article.setImagePath(getMaxImageAddress(article.getLink()));
                            countDownLatch.countDown();
                        }
                    });

                }
                try {
                    countDownLatch.await();
                    topArticles.postValue(response);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private FutureTask<Object> topArticleTask = new FutureTask<Object>(TopArticleRunnable,null);

    public void fetchBannerData(){
        AppExecutors.execNetIo(bannerTask);
    }

    /**
     * 获取置顶文章
     * */
    public void fetchTopArticleList(){
        AppExecutors.execNetIo(topArticleTask);
    }

    @Override
    public void resetData() {
        resetData = true;
        setCurrentPage(0);
        AppExecutors.execNetIo(new Runnable() {
            @Override
            public void run() {
                try {
                    fetchBannerData();
                    bannerTask.get();
                    fetchTopArticleList();
                    topArticleTask.get();
                    getHomeNavItemListData();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
                Response<Data> response = DataDriven.INSTANCE.getHomeArticleList(getCurrentPage());
                if(response.netSuccess()){
                    if(response.getData()!= null && response.getData().getDatas() != null){
                        int size = response.getData().getDatas().size();
                        final CountDownLatch countDownLatch = new CountDownLatch(size);
                        for(final Article article :response.getData().getDatas()){
                            if(!StringUtils.INSTANCE.isNetUrl(article.getLink())){
                                countDownLatch.countDown();
                                continue;
                            }
                            AppExecutors.execDiskIo(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG,"执行解析之前  "+article.getLink()+" 总数 ：： "+countDownLatch.getCount());
                                    article.setImagePath(getMaxImageAddress(article.getLink()));
                                    Log.d(TAG,"执行解析之后  "+article.getLink()+" 总数 ：： "+countDownLatch.getCount());
                                    countDownLatch.countDown();
                                }
                            });

                        }
                        try {
                            countDownLatch.await();
                            Log.d(TAG,"抛出数据 size ::  "+size+" 总数 ：： "+countDownLatch.getCount());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            ResourceBoundary<Object> resourceBoundary = new ResourceBoundary<Object>(STATE_LOADED, 0, "success", response, getCurrentPage());
                            if (getCurrentPage() <= 2){
                                getData().postValue(resourceBoundary);
                            }

                        }
                    }
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

    private String getMaxImageAddress(String linkUrl){
        String md5 = Md5Utils.MD5(linkUrl);
        String imgpath = MMKV.defaultMMKV().decodeString(md5);
        if(!TextUtils.isEmpty(imgpath)){
            return imgpath;
        }
        imgpath = WebLinkParse.getMaxImgAddress(linkUrl);
        if(!TextUtils.isEmpty(imgpath)){
            MMKV.defaultMMKV().encode(md5,imgpath);
        }
        return imgpath;
    }
}
