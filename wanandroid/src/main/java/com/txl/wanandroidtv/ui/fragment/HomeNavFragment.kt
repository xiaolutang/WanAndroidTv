package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.com.besjon.pojo.Article
import com.txl.wanandroidtv.bean.com.besjon.pojo.HomeArticleListData
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
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
            putViewModelFactory(CATEGORY_SQUARE, SquareViewModelFactory())
            putViewModelFactory(CATEGORY_QUESTION_AND_ANSWER, QANavViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_HOME, HomeNavItemListViewModel::class.java)
            putViewModelClass(CATEGORY_SQUARE, SquareNavItemListViewModel::class.java)
            putViewModelClass(CATEGORY_QUESTION_AND_ANSWER, QANavItemListViewModel::class.java)
        }

        fun newInstance(category:String):HomeNavFragment{
            val fragment = HomeNavFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY,category)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mAdapter: BaseRecyclerFactoryAdapter<Article>?=null

    var adapter: BaseRecyclerFactoryAdapter<*>? = null

    var recyclerView: LibTvRecyclerView2? = null
        private set

    var smartRefreshLayout: SmartRefreshLayout? = null

    private fun createAdapter(): BaseRecyclerFactoryAdapter<*> {
        mAdapter = BaseRecyclerFactoryAdapter( HomeFragmentCommonItemViewHolderFactory())
        mAdapter?.setItemClickListener(this)
        return mAdapter!!
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        smartRefreshLayout?.finishLoadMore()
        if (data is HomeArticleListData){
            mAdapter?.appendData(data.data.datas)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun initView() {
        smartRefreshLayout = rootView?.findViewById(R.id.fragment_lib_smart_refresh_layout)
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        smartRefreshLayout?.setEnableAutoLoadMore(true)//是否启用列表惯性滑动到底部时自动加载更多
        smartRefreshLayout?.setOnLoadMoreListener(this)
        showLoading(0)
        //暂时不考虑阿里的VLayout 不需要那么复杂
        adapter = createAdapter()
        recyclerView?.adapter = adapter
//        recyclerView?.focusSearchFailedListener = object: LibTvRecyclerView.OnFocusSearchFailedListener {
//            override fun onFocusSearchFailed(
//                    currentFocusView: View?,
//                    viewPosition: Int,
//                    direct: Int
//            ) {
//                //常规列表有4个元素一行，所以减4
//                if((direct == View.FOCUS_DOWN || direct == View.FOCUS_RIGHT) && adapter != null && viewPosition+1 >= adapter!!.itemCount-4 && viewPosition != RecyclerView.NO_POSITION){
//                    smartRefreshLayout?.autoLoadMore()
////                    viewModel?.nextPage()
//                }
//            }
//        }
        recyclerView?.layoutManager = GridLayoutManager(requireContext(),HOME_SPAN_COUNT)
        recyclerView?.addItemDecoration(HomeGridDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_50)))
    }

    override fun onItemClick(position: Int, data: Any?) {
        openPage(requireContext(), data)
    }

    override fun showLoading(currentPage:Int){
        if(currentPage == 0){
            loadingViewUtils?.showLoadingView(true)
        }else{
            smartRefreshLayout?.autoLoadMore()
        }
    }
}

class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeNavItemListViewModel() as T
    }
}