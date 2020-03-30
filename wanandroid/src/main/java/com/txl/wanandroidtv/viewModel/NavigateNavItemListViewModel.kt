package com.txl.wanandroidtv.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.txl.commonlibrary.utils.exector.AppExecutors
import com.txl.wanandroidtv.bean.com.besjon.pojo.NavigateArticleListData
import com.txl.wanandroidtv.data.DataDriven

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/27
 * description：
 */
class NavigateNavItemListViewModel: AbsNavItemListVIewModel() {
    override fun getPageData() {
        AppExecutors.execNetIo {
            val response = DataDriven.getNavigateArticleList()
            if (response.state) {
                val g = Gson()
                val result = g.fromJson(response.data, NavigateArticleListData::class.java)
                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "success", result, currentPage)
                data.postValue(resourceBoundary)
            } else {
                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "failed to load data ", null, currentPage)
                data.postValue(resourceBoundary)
            }
            if (resetData) {
                resetData = false
            }
            if (loadData) {
                loadData = false
            }
        }
    }

}

class NavigateNavViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NavigateNavItemListViewModel() as T
    }
}