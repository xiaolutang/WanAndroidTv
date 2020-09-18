package com.txl.wanandroidtv.ui.fragment

import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.HomeArticleListData
import kotlinx.android.synthetic.main.fragment_setup_nav.*

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/4/1
 * description：体系导航
 */
class SetUpNavFragment:BaseNavFragment() {
    override fun showLoading(currentPage: Int) {
        if(currentPage == 0){
            loadingViewUtils?.showLoadingView(true)
        }else{
            smart_refresh_layout?.autoLoadMore()
        }
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        smart_refresh_layout?.finishLoadMore()
        if (data is HomeArticleListData){
//            mAdapter?.appendData(data.data.datas)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_setup_nav
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}