package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.HomeArticleListData
import com.txl.wan_android_data_provider.bean.home.Article
import com.txl.wan_android_data_provider.bean.home.BannerItemData
import com.txl.wan_android_data_provider.data.Response
import com.txl.wanandroidtv.ui.adpater.BaseVLayoutAdapter
import com.txl.wanandroidtv.ui.adpater.ListAdapterFactory
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType
import com.txl.wanandroidtv.ui.utils.PageJumpUtils.openPage
import com.txl.wanandroidtv.utils.RecyclerViewConfigUtils
import com.txl.wan_android_data_provider.viewModel.*
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelFactory


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

    private lateinit var delegateAdapter: DelegateAdapter

    var bannerAdapter: BaseVLayoutAdapter<List<BannerItemData>, BaseViewHolder>? = null

    var recyclerView: LibTvRecyclerView2? = null
        private set

    var smartRefreshLayout: SmartRefreshLayout? = null

    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        smartRefreshLayout?.finishLoadMore()
        if (data is Response<*>){
            val result = data.data
            if(result is Data){
                val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),result.datas,WanAndroidListItemType.TYPE_COMMON)
                delegateAdapter.addAdapters(adapters)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun initView() {
        super.initView()
        smartRefreshLayout = rootView?.findViewById(R.id.fragment_lib_smart_refresh_layout)
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        recyclerView?.setRecycledViewPool(RecyclerViewConfigUtils.viewPool)
        smartRefreshLayout?.setEnableAutoLoadMore(true)//是否启用列表惯性滑动到底部时自动加载更多
        smartRefreshLayout?.setOnLoadMoreListener(this)
        showLoading(0)
        val layoutManager =  VirtualLayoutManager(requireContext())
        recyclerView?.layoutManager = layoutManager
        delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView?.adapter = delegateAdapter
        recyclerView?.setOnFocusSearchFailedListener(object :LibTvRecyclerView2.OnFocusSearchFailedListener{
            override fun onRightSearchFailed(): Boolean {
                return false
            }

            override fun onBottomSearchFailed(): Boolean {
                smartRefreshLayout?.autoLoadMore()
                return true
            }

            override fun onTopSearchFailed(): Boolean {
                return false
            }

            override fun onLeftSearchFailed(): Boolean {
                return false
            }
        })
        smartRefreshLayout?.setOnLoadMoreListener(object :OnLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                viewModel?.nextPage()
                Log.d(TAG,"onLoadMore")
            }
        })
    }

    override fun initData() {
        super.initData()
        if(viewModel is HomeNavItemListViewModel){

            (viewModel as HomeNavItemListViewModel).bannerData.observe(this, Observer<Response<List<BannerItemData>>> {
                if(it.netSuccess()){
                    loadingViewUtils?.showLoadingView(false)
                    val list:List<BannerItemData> = it.data!!
                    val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),list,WanAndroidListItemType.TYPE_BANNER)
                    delegateAdapter.addAdapters(0,adapters)
                }
            })
//            (viewModel as HomeNavItemListViewModel).fetchBannerData()
            (viewModel as HomeNavItemListViewModel).topArticles.observe(this, Observer<Response<List<Article>>> {
                if(it.netSuccess()){
                    loadingViewUtils?.showLoadingView(false)
                    val list:List<Article> = it.data!!
                    val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),list,WanAndroidListItemType.TYPE_TOP)
                    delegateAdapter.addAdapters(adapters)
                }
            })
//            (viewModel as HomeNavItemListViewModel).fetchTopArticleList()
        }
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