package com.txl.wan_android_data_provider.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.txl.commonlibrary.utils.exector.AppExecutors
import com.txl.wan_android_data_provider.data.DataDriven

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/26
 * description：
 */
class SquareNavItemListViewModel: AbsNavItemListViewModel() {

    override fun getPageData() {
        getSquareNavItemListData()
    }

    private fun getSquareNavItemListData() {
        AppExecutors.execNetIo {
            val response = DataDriven.getSquareArticleList(currentPage)
            if(response.netSuccess()){
                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "success", response, currentPage)
                data.postValue(resourceBoundary)
            }else{

            }
        }
    }
}

class SquareViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SquareNavItemListViewModel() as T
    }
}
