package com.txl.wanandroidtv.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 列表抽象的ViewModel,对列表接口数据进行缓存和提前进行处理。
 * 因为列表导航的接口和返回数据都有可能不同因此这里不限制数据的类型。建议使用 实现 数据类型实现 Parcelable 接口
 * 这样可以使用默认实现好了的本地缓存和读取
 * */
abstract class AbsNavItemListViewModel:ViewModel(),IPageViewModel {
    val data = MutableLiveData<ResourceBoundary<Any>>()

    @Volatile
    protected var resetData = false
    @Volatile
    protected var loadData = false

    /**
     * 当前加载的第几页数据
     * */
    var currentPage = 0
        protected set

    init {
        this.resetData()
    }

    override fun resetData() {
        currentPage = 0
        getPageData()
    }

    override fun nextPage() {
        if (loadData || resetData) { //重置或者 正在加载数据的时候不能进行获取下一页的数据
            return
        }
        loadData = true
        currentPage++
        getPageData()
    }

    override fun getViewModelData(): MutableLiveData<ResourceBoundary<Any>> {
        return data
    }

    protected abstract fun getPageData()
}