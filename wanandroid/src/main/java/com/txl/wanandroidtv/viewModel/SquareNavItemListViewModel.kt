package com.txl.wanandroidtv.viewModel

import com.google.gson.Gson
import com.txl.commonlibrary.utils.exector.AppExecutors
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData
import com.txl.wanandroidtv.data.DataDriven.getSquareArticleList

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/26
 * description：
 */
class SquareNavItemListViewModel: AbsNavItemListVIewModel<Any>() {
    @Volatile
    private var resetData = false
    @Volatile
    private var loadData = false

    override fun resetData() {
        resetData = true
        currentPage = 0
        getSquareNavItemListData()
    }

    override fun nextPage() {
        if (loadData || resetData) { //重置或者 正在加载数据的时候不能进行获取下一页的数据
            return
        }
        loadData = true
        currentPage += 1
        getSquareNavItemListData()
    }

    private fun getSquareNavItemListData() {
        AppExecutors.execNetIo {
            val (state, data1) = getSquareArticleList(currentPage, currentPage == 0)
            if (state) {
                val g = Gson()
                val result = g.fromJson(data1, HomeArticleListData::class.java)
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