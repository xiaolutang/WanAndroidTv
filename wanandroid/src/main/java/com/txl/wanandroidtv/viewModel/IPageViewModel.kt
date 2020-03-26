package com.txl.wanandroidtv.viewModel

import androidx.lifecycle.MutableLiveData

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/12
 * description：列表页的数据ViewModel接口定义
 * eg.进行列表页网络请求
 * 第一次获取或重置 这个数据使用 resetData()
 * 余下的使用nextPage()
 */
interface IPageViewModel {
    /**
     * 初始化数据
     * */
    fun resetData()
    /**
     * 获取下一页的数据
     * */
    fun nextPage()

    fun getViewModelData(): MutableLiveData<ResourceBoundary<Any>>
}