package com.txl.wanandroidtv.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.com.besjon.pojo.NavigateCategoryData
import com.txl.wanandroidtv.ui.adpater.BaseRecyclerFactoryAdapter
import com.txl.wanandroidtv.ui.viewholder.base.BaseViewHolder
import com.txl.wanandroidtv.ui.viewholder.base.IViewHolderFactory
import com.txl.wanandroidtv.viewModel.AbsNavItemListVIewModel
import com.txl.wanandroidtv.viewModel.NavigateNavItemListViewModel
import com.txl.wanandroidtv.viewModel.NavigateNavViewModelFactory
import com.txl.wanandroidtv.viewModel.ViewModelContainer
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wanandroidtv.viewModel.ViewModelContainer.putViewModelFactory

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/27
 * description：wanAndroid导航table
 */
class NavigateNavFragment: BaseNavFragment() {

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

    var leftNavigateAdapter:BaseRecyclerFactoryAdapter<NavigateCategoryData>? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_navigate_nav
    }

    override fun initView() {
        leftNavRecyclerView =  findViewById(R.id.tv_recycler_left_nav)
        leftNavRecyclerView?.setChildFocusListener { position, child -> contentNavRecyclerView?.smoothScrollToPosition(position) }
        refreshLayout =  findViewById(R.id.fragment_lib_smart_refresh_layout)
        contentNavRecyclerView =  findViewById(R.id.fragment_lib_recycler)

        leftNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        leftNavigateAdapter = BaseRecyclerFactoryAdapter(requireContext(),LeftItemViewHolderFactory())

    }

    override fun createAdapter(): BaseRecyclerFactoryAdapter<*> {
        return BaseRecyclerFactoryAdapter<Any>(requireContext(),object :IViewHolderFactory<BaseViewHolder>{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun onDataReady(currentPage: Int, data: Any?) {
        super.onDataReady(currentPage, data)

    }

    override fun createViewModel(): AbsNavItemListVIewModel? {
        val viewModel = ViewModelProvider(this).get(ViewModelContainer.getViewModelClass(CATEGORY_NAVIGATE))
        if(viewModel is AbsNavItemListVIewModel){
            return viewModel
        }
        return null
    }
}

class LeftItemViewHolderFactory:IViewHolderFactory<BaseViewHolder>{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return LeftItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_navigate_left_nav,parent,false))
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return 0
    }

}

class LeftItemViewHolder(view: View): BaseViewHolder(view) {
    var radioButton:RadioButton = view.findViewById(R.id.tv_navigate_left_nav)

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is NavigateCategoryData){
            radioButton.text = data.name
        }
    }
}