package com.txl.wanandroidtv.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.txl.wanandroidtv.bean.NavItemData
import com.txl.wanandroidtv.bean.com.besjon.pojo.Article
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData
import com.txl.wanandroidtv.ui.adpater.BaseRecyclerFactoryAdapter
import com.txl.wanandroidtv.ui.adpater.NavRecyclerAdapter
import com.txl.wanandroidtv.ui.viewholder.HomeFragmentCommonItemViewHolderFactory
import com.txl.wanandroidtv.viewModel.AbsNavItemListVIewModel
import com.txl.wanandroidtv.viewModel.HomeNavItemListViewModel

/**
 * wan android 首页导航
 * */
class HomeNavFragment : BaseNavFragment() {
    private var mAdapter:BaseRecyclerFactoryAdapter<Article>?=null
    override fun createAdapter(): BaseRecyclerFactoryAdapter<*> {
        mAdapter = BaseRecyclerFactoryAdapter(requireContext(), HomeFragmentCommonItemViewHolderFactory())
        return mAdapter!!
    }

    override fun createViewModel(): AbsNavItemListVIewModel<*>? {
        return ViewModelProvider(this, HomeViewModelFactory()).get(HomeNavItemListViewModel::class.java)
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        super.onDataReady(currentPage, data)
        if (data is HomeArticleListData){
            mAdapter?.appendData(data.data.datas)
        }
    }
}

class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeNavItemListViewModel() as T
    }
}