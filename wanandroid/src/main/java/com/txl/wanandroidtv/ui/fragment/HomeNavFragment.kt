package com.txl.wanandroidtv.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.com.besjon.pojo.Article
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData
import com.txl.wanandroidtv.ui.adpater.BaseRecyclerFactoryAdapter
import com.txl.wanandroidtv.ui.utils.PageJumpUtils.openPage
import com.txl.wanandroidtv.ui.viewholder.HomeFragmentCommonItemViewHolderFactory
import com.txl.wanandroidtv.ui.widget.HomeGridDividerItemDecoration
import com.txl.wanandroidtv.viewModel.*
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelFactory

/**
 * wan android 首页导航
 * */
const val HOME_SPAN_COUNT = 4
class HomeNavFragment : BaseNavFragment(), BaseRecyclerFactoryAdapter.OnItemClickListener {

    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_HOME, HomeViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_HOME, HomeNavItemListViewModel::class.java)
        }
    }

    private var mAdapter:BaseRecyclerFactoryAdapter<Article>?=null
    override fun createAdapter(): BaseRecyclerFactoryAdapter<*> {
        mAdapter = BaseRecyclerFactoryAdapter(requireContext(), HomeFragmentCommonItemViewHolderFactory())
        mAdapter?.setItemClickListener(this)
        return mAdapter!!
    }

    override fun createViewModel(): AbsNavItemListVIewModel? {
        return ViewModelProvider(this, HomeViewModelFactory()).get(HomeNavItemListViewModel::class.java)
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        super.onDataReady(currentPage, data)
        if (data is HomeArticleListData){
            mAdapter?.appendData(data.data.datas)
        }
    }

    override fun initView() {
        super.initView()
        recyclerView?.layoutManager = GridLayoutManager(requireContext(),HOME_SPAN_COUNT)
        recyclerView?.addItemDecoration(HomeGridDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_50)))
    }

    override fun onItemClick(position: Int, data: Any?) {
        openPage(requireContext(), data)
    }
}

class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeNavItemListViewModel() as T
    }
}