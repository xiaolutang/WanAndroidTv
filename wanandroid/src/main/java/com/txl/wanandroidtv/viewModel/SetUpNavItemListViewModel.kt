package com.txl.wanandroidtv.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.txl.commonlibrary.utils.exector.AppExecutors
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData
import com.txl.wanandroidtv.bean.com.besjon.pojo.setup.Children
import com.txl.wanandroidtv.bean.com.besjon.pojo.setup.JsonRootBean
import com.txl.wanandroidtv.bean.com.besjon.pojo.setup.SetUpData
import com.txl.wanandroidtv.data.DataDriven

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/4/1
 * description：体系
 */
class SetUpNavItemListViewModel : AbsNavItemListViewModel() {

    @Volatile
    private var setUpDataReady = false

    private var setUpRootData:JsonRootBean? = null

    private var currentSetUpData = MutableLiveData<SetUpData>()
    private var currentChild = MutableLiveData<Children>()

    init {
        AppExecutors.execNetIo {
            val result = DataDriven.getSetUpNavData()
            if (result.netSuccess()) {
                val g = Gson()
                val data = g.fromJson(result.data, JsonRootBean::class.java)
                setUpDataReady = true
                setUpRootData = data
                currentSetUpData.postValue(data.data[0])
                currentChild.postValue(data.data[0].children[0])
                getPageData()
            }

        }
    }

    fun setCurrentSetUpData(setUpData: SetUpData){
        currentPage = 0
        currentSetUpData.value = setUpData
        currentChild.value = setUpData.children[0]
    }

    fun setChildPosition(position:Int){
        currentPage = 0
        currentChild.value = currentSetUpData.value!!.children[position]
    }

    override fun getPageData() {
        if (!setUpDataReady || currentChild.value == null) {
            return
        }
        AppExecutors.execNetIo {
            val response = DataDriven.getSetUpNavItemListData(currentPage, currentPage == 0,currentChild.value!!.id)
            if (response.netSuccess()) {
                val g = Gson()
                val result = g.fromJson(response.data, HomeArticleListData::class.java)
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