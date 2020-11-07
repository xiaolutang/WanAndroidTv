package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data
import com.txl.wan_android_data_provider.data.Response
import com.txl.wan_android_data_provider.viewModel.*
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelFactory
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.ui.adpater.ListAdapterFactory
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType
import com.txl.wanandroidtv.utils.RecyclerViewConfigUtils
import kotlinx.android.synthetic.main.fragment_lib_nav_base.*

/**
 * wan android 广场导航
 * */
class SquareNavFragment : BaseNavFragment(), BaseRecyclerFactoryAdapter.OnItemClickListener {
    private lateinit var delegateAdapter: DelegateAdapter

    private var recyclerView: LibTvRecyclerView2? = null
    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_SQUARE, SquareViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_SQUARE, SquareNavItemListViewModel::class.java)
        }

        fun newInstance(category:String):SquareNavFragment{
            val fragment = SquareNavFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY,category)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        super.initView()
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        recyclerView?.setRecycledViewPool(RecyclerViewConfigUtils.viewPool)
        showLoading(0)
        val layoutManager =  VirtualLayoutManager(requireContext())
        recyclerView?.layoutManager = layoutManager
        delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView?.adapter = delegateAdapter
        recyclerView?.setOnFocusSearchFailedListener(object : LibTvRecyclerView2.OnFocusSearchFailedListener{
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

    override fun showLoading(currentPage: Int) {

    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        showProgress(false)
        if (data is Response<*>){
            val result = data.data
            if(result is Data){
                val adapters = ListAdapterFactory.createAdaptersWithType(requireContext(),result.datas, WanAndroidListItemType.TYPE_COMMON)
                delegateAdapter.addAdapters(adapters)
            }
        }
    }

    private fun showProgress(show:Boolean){
        progressBar?.visibility = if(show){ View.VISIBLE}else{
            View.GONE}
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun onItemClick(position: Int, data: Any?) {

    }
}