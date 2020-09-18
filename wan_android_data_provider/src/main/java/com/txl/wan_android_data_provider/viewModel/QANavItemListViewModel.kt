package com.txl.wan_android_data_provider.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.txl.commonlibrary.utils.exector.AppExecutors

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/4/1
 * description：
 */
class QANavItemListViewModel: AbsNavItemListViewModel() {
    override fun getPageData() {
        AppExecutors.execNetIo {
//            val response = DataDriven.getQANavArticleList(currentPage)
//            if (response.state) {
//                val g = Gson()
//                val result = g.fromJson(response.data, HomeArticleListData::class.java)
//                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "success", result, currentPage)
//                data.postValue(resourceBoundary)
//            } else {
//                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "failed to load data ", null, currentPage)
//                data.postValue(resourceBoundary)
//            }
//            if (resetData) {
//                resetData = false
//            }
//            if (loadData) {
//                loadData = false
//            }
        }
    }
}

class QANavViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QANavItemListViewModel() as T
    }
}