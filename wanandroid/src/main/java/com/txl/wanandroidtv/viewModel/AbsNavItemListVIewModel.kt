package com.txl.wanandroidtv.viewModel

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tencent.mmkv.MMKV
import com.txl.commonlibrary.utils.Md5Utils

/**
 * 列表抽象的ViewModel,对列表接口数据进行缓存和提前进行处理。
 * 因为列表导航的接口和返回数据都有可能不同因此这里不限制数据的类型。建议使用 实现 数据类型实现 Parcelable 接口
 * 这样可以使用默认实现好了的本地缓存和读取
 * */
abstract class AbsNavItemListVIewModel:ViewModel(),IPageViewModel {
    val data = MutableLiveData<ResourceBoundary<Any>>()
    protected val mmkv = MMKV.defaultMMKV()
    /**
     * 当前加载的第几页数据
     * */
    var currentPage = 1
        protected set

    init {
        this.resetData()
    }
}