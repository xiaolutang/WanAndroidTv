package com.txl.wanandroidtv.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.txl.wanandroidtv.viewModel.AbsNavItemListVIewModel
import com.txl.wanandroidtv.viewModel.HomeNavItemListViewModel

/**
 * wan android 首页导航
 * */
class HomeNavFragment : BaseNavFragment() {
    override fun createViewModel(): AbsNavItemListVIewModel<*>? {
         return ViewModelProvider(this,HomeViewModelFactory()).get(HomeNavItemListViewModel::class.java)
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        super.onDataReady(currentPage, data)
    }
}

class HomeViewModelFactory:ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeNavItemListViewModel() as T
    }
}