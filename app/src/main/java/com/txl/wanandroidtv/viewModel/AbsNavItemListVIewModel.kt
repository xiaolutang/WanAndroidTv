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
abstract class AbsNavItemListVIewModel<T>:ViewModel() {
    private val data = MutableLiveData<ResourceBoundary<T>>()
    protected val mmkv = MMKV.defaultMMKV()
    /**
     * 当前加载的第几页数据
     * */
    var currentPage = 1
        protected set
    /**
     * 初始化数据
     * */
    abstract fun resetData()
    /**
     * 获取下一页的数据
     * */
    abstract fun nextPage()

    /**
     * 保存数据
     * @param key 在做数据缓存的时候默认对key取md5值，
     * @param data 需要保存的序列化 数据
    * */
    protected open fun saveDate(key:String,data:Parcelable){
        mmkv.encode(Md5Utils.MD5(key), data)
    }

    /**
     * 获取
     * @param key 在做数据缓存的时候默认对key取md5值，
     * @param result 需要返回数据的class类型
     * */
    protected open fun <R : Parcelable> getCacheData(key:String,result:Class<R>):R?{
        return mmkv.decodeParcelable(Md5Utils.MD5(key),result)
    }
}