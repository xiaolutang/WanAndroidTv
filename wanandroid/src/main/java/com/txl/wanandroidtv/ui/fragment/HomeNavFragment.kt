package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data
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

            //初始化class
            putViewModelClass(CATEGORY_HOME, HomeNavItemListViewModel::class.java)
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

    private var recyclerView: LibTvRecyclerView2? = null

    private var progressBar: ProgressBar? = null


    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        showProgress(false)
        if (data is Response<*>){
            val result = data.data
            if(result is Data){
                val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),result.datas,WanAndroidListItemType.TYPE_COMMON)
                delegateAdapter.addAdapters(adapters)
            }
        }
    }

    private fun showProgress(show:Boolean){
        progressBar?.visibility = if(show){ View.VISIBLE}else{View.GONE}
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun initView() {
        super.initView()
        progressBar = rootView?.findViewById(R.id.progressBar)
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        recyclerView?.setRecycledViewPool(RecyclerViewConfigUtils.viewPool)
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
                viewModel?.nextPage()
                showProgress(true)
                return true
            }

            override fun onTopSearchFailed(): Boolean {
                return false
            }

            override fun onLeftSearchFailed(): Boolean {
                return false
            }
        })
    }

    override fun initData() {
        super.initData()
        if(viewModel is HomeNavItemListViewModel){

            (viewModel as HomeNavItemListViewModel).bannerData.observe(this, Observer<Response<List<BannerItemData>>> {
                showProgress(false)
                if(it.netSuccess()){
                    loadingViewUtils?.showLoadingView(false)
                    val list:List<BannerItemData> = it.data!!
                    val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),list,WanAndroidListItemType.TYPE_BANNER)
                    delegateAdapter.addAdapters(0,adapters)
                }
            })
//            (viewModel as HomeNavItemListViewModel).fetchBannerData()
            (viewModel as HomeNavItemListViewModel).topArticles.observe(this, Observer<Response<List<Article>>> {
                showProgress(false)
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
            viewModel?.nextPage()
            showProgress(true)
        }
    }
}

class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeNavItemListViewModel() as T
    }
}