package com.txl.wanandroidtv.ui.fragment

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
abstract class SquareNavFragment : BaseNavFragment(), BaseRecyclerFactoryAdapter.OnItemClickListener {

    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_SQUARE, SquareViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_SQUARE, SquareNavItemListViewModel::class.java)
        }
    }


//    private var mAdapter:BaseRecyclerFactoryAdapter<Article>?=null
//    override fun createAdapter(): BaseRecyclerFactoryAdapter<*> {
//        mAdapter = BaseRecyclerFactoryAdapter(requireContext(), HomeFragmentCommonItemViewHolderFactory())
//        mAdapter?.setItemClickListener(this)
//        return mAdapter!!
//    }
//
//    override fun createViewModel(): AbsNavItemListVIewModel? {
//        return ViewModelProvider(this, SquareViewModelFactory()).get(SquareNavItemListViewModel::class.java)
//    }
//
//    override fun showLoading(currentPage: Int) {
//        if(currentPage == 0){
//            loadingViewUtils?.showLoadingView(true)
//        }else{
//            smartRefreshLayout?.autoLoadMore()
//        }
//    }
//
//    override fun onDataReady(currentPage: Int, data: Any?) {
//        super.onDataReady(currentPage, data)
//        if (data is HomeArticleListData){
//            mAdapter?.appendData(data.data.datas)
//        }
//    }
//
//    override fun getLayoutRes(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun initView() {
//        super.initView()
//        recyclerView?.layoutManager = GridLayoutManager(requireContext(), HOME_SPAN_COUNT)
//        recyclerView?.addItemDecoration(HomeGridDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_50)))
//    }
//
//    override fun onItemClick(position: Int, data: Any?) {
//        openPage(requireContext(), data)
//    }
}