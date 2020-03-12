package com.txl.wanandroidtv.viewModel

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.txl.commonlibrary.utils.exector.AppExecutors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * citsin
 * 2019-08-02
 */
//private val navItemListViewModelList = SparseArray<NavItemListViewModel>()

//class NavItemListViewModel(val id: Int, private val baseUrl: String = ApiUrl.CONTENT_LIST) : AbsNavItemListVIewModel<NavItem>() {
//    private val TAG = "NavItemListViewModel"
//
//    private val data = MutableLiveData<ResourceBoundary<NavItem>>()
//    private var loadData = false
//
//    init {
//        resetData()
//    }
//
//    /**
//     * 重置数据
//     * */
//    override fun resetData(){
//        currentPage = 1
//        GlobalScope.launch(Dispatchers.Main) {
//            //初始化加载数据
//            data.value = ResourceBoundary(
//                Constants.STATE_LOADING,
//                0
//            )
//            val url = "$baseUrl$id"
//            loadData = true
//            AppExecutors.execNetIo {
//                //从第一页开始
//                val response = ContentRepository.fetchList(url, currentPage, 20)
//                if (!response.state) {
//                    val cv  = getCacheData(url,NavItem::class.java)
//                    if (cv != null) {
//                        data.postValue(ResourceBoundary(
//                            Constants.STATE_LOADED,
//                            Constants.SOURCE_FROM_CACHE,
//                            "",
//                            cv
//                        ))
//                    } else {
//                        data.postValue(ResourceBoundary(
//                            Constants.STATE_ERROR,
//                            0,
//                            response.error!!.description
//                        ))
//                    }
//                } else {
//                    response.data?.let { d ->
//                        saveDate(url,d)
//                        data.postValue(ResourceBoundary(
//                            Constants.STATE_LOADED,
//                            Constants.SOURCE_FROM_CACHE,
//                            "",
//                            d
//                        ))
//                    }
//
//                }
//                loadData = false
//            }
//        }
//    }
//
//
//    fun asLiveData():LiveData<ResourceBoundary<NavItem>>{
//        return data
//    }
//
//    override fun nextPage() {
//        if (loadData) {
//            return
//        }
//        loadData = true
//        currentPage++
//        GlobalScope.launch(Dispatchers.IO) {
//            val url = "${ApiUrl.CONTENT_LIST}$id"
//            GlobalScope.async(Dispatchers.IO) {
//                val response = ContentRepository.fetchList(url, currentPage, 20)
//                if (!response.state) {
//                    data.postValue(ResourceBoundary(
//                        Constants.STATE_ERROR,
//                        0,
//                        response.error!!.description,
//                        null,currentPage
//                    ))
//                } else {
//                    response.data?.let { d ->
//                        data.postValue(ResourceBoundary(
//                            Constants.STATE_LOADED,
//                            Constants.SOURCE_FROM_REMOTE,
//                            "",
//                            d,
//                            currentPage
//                        ))
//                    }
//                }
//                loadData = false
//            }
//        }
//    }
//
//
//    /**
//     * 加载下一页数据
//     */
//    fun nextPage(index: Int) {
//        nextPage()
//    }
//
//    companion object class Factory(private val id: Int) : ViewModelProvider.NewInstanceFactory() {
//
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            var result = navItemListViewModelList.get(id)
//            if (result == null) {
//                result = NavItemListViewModel(id)
//                navItemListViewModelList.put(id, result)
//            }
//            return result as T
//        }
//    }
//
//}
