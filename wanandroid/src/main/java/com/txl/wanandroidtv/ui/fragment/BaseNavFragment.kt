package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.ui.adpater.NavRecyclerAdapter
import com.txl.wanandroidtv.viewModel.*
import java.util.*

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/11
 * description：抽象的导航Fragment
 */
abstract class BaseNavFragment : BaseFragment(), OnLoadMoreListener {
    lateinit var viewModel: AbsNavItemListVIewModel<*>
    var smartRefreshLayout: SmartRefreshLayout? = null
    var recyclerView: LibTvRecyclerView? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    override fun initView() {
        smartRefreshLayout = rootView?.findViewById(R.id.fragment_lib_smart_refresh_layout)
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        smartRefreshLayout?.setEnableAutoLoadMore(true)//是否启用列表惯性滑动到底部时自动加载更多
        smartRefreshLayout?.setOnLoadMoreListener(this)
        showLoading(0)
        recyclerView?.layoutManager = GridLayoutManager(requireContext(),4)
        recyclerView?.adapter = NavRecyclerAdapter(requireContext())
    }

    override fun initData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    protected fun initViewModel(){

        viewModel = HomeNavItemListViewModel()
        viewModel.data.observe(viewLifecycleOwner,object: Observable(), Observer<ResourceBoundary<*>> {
            override fun onChanged(t: ResourceBoundary<*>?) {
                if(t != null){
                    when(t.state){
                        STATE_LOADED->{
                            onDataReady(t.currentPage,t.data)
                        }
                        STATE_ERROR->{
                            onLoadDataError(t.currentPage)
                        }
                        STATE_LOADING->{
                            showLoading(t.currentPage);
                        }
                    }
                }else{

                }
            }
        })
    }

    /**
     * @param currentPage 当前第几页数据在加载
     * */
    fun showLoading(currentPage:Int){
        if(currentPage == 0){
            loadingViewUtils?.showLoadingView(true)
        }else{
            smartRefreshLayout?.autoLoadMore()
        }
    }

    fun onDataReady(currentPage:Int,data :Any?){
        loadingViewUtils?.showLoadingView(false)
    }

    fun onLoadDataError(currentPage:Int){}

    override fun onDetach() {
        super.onDetach()
        viewModel.resetData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel.nextPage()
    }
}