package com.txl.wanandroidtv.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView
import com.txl.ui_basic.fragment.BaseFragment
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.viewModel.*
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelFactory

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/27
 * description：wanAndroid导航table
 */
class NavigateNavFragment: BaseFragment() {

    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_NAVIGATE, NavigateNavViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_NAVIGATE, NavigateNavItemListViewModel::class.java)
        }
    }

    var leftNavRecyclerView:LibTvRecyclerView?  = null
    var refreshLayout:SmartRefreshLayout?  = null
    var contentNavRecyclerView:LibTvRecyclerView?  = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_navigate_nav
    }

    override fun initView() {
        leftNavRecyclerView =  findViewById(R.id.tv_recycler_left_nav)
        refreshLayout =  findViewById(R.id.fragment_lib_smart_refresh_layout)
        contentNavRecyclerView =  findViewById(R.id.fragment_lib_recycler)

        leftNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//        val adapter = BaseRecyclerFactoryAdapter(requireContext(),null)
    }

    override fun initData() {

    }
}