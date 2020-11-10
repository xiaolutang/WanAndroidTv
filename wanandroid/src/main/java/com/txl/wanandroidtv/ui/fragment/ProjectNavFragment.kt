package com.txl.wanandroidtv.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.RadioButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.txl.commonlibrary.utils.DrawableUtils
import com.txl.tvlib.widget.ICheckView
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.NavigateArticleListData
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.NavigateCategoryData
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data
import com.txl.wan_android_data_provider.data.Response
import com.txl.wanandroidtv.ui.utils.ThemeUtils
import com.txl.wanandroidtv.ui.viewholder.NavigateFlexBoxItemViewHolderFactory
import com.txl.wan_android_data_provider.viewModel.NavigateNavItemListViewModel
import com.txl.wan_android_data_provider.viewModel.NavigateNavViewModelFactory
import com.txl.wan_android_data_provider.viewModel.ProjectNavViewModelFactory
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelFactory
import com.txl.wanandroidtv.ui.adpater.ListAdapterFactory
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/27
 * description：项目
 */
class ProjectNavFragment: BaseNavFragment() {

    companion object{
        init {
            //初始化factory
            putViewModelFactory(CATEGORY_PROJECT, ProjectNavViewModelFactory())

            //初始化class
            putViewModelClass(CATEGORY_PROJECT, NavigateNavItemListViewModel::class.java)
        }

        fun newInstance(category:String):ProjectNavFragment{
            val fragment = ProjectNavFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY,category)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var leftNavRecyclerView:LibTvRecyclerView2?  = null

    private var contentNavRecyclerView: LibTvRecyclerView2?  = null

    private var leftNavigateAdapter: BaseRecyclerFactoryAdapter<NavigateCategoryData>? = null
    private var rightContentNavigateAdapter: BaseRecyclerFactoryAdapter<NavigateCategoryData>? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_navigate_nav
    }

    override fun initView() {
        leftNavRecyclerView =  findViewById(R.id.tv_recycler_left_nav)
        leftNavRecyclerView?.setOnCheckedChangeListener(object : LibTvRecyclerView2.OnCheckedChangeListener {

            override fun onCheckedChanged(group: LibTvRecyclerView2?, checkedView: View?, position: Int) {
                contentNavRecyclerView?.scrollToPosition(position)
            }
        })
        leftNavRecyclerView?.setFocusSearchJumpRow(false)
        leftNavRecyclerView?.openFocusDynamic(true)
//        leftNavRecyclerView?.setChildFocusListener { position, child -> contentNavRecyclerView?.smoothScrollToPositionAndTop(position) }
        contentNavRecyclerView =  findViewById(R.id.fragment_lib_recycler)

        leftNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        leftNavigateAdapter = BaseRecyclerFactoryAdapter( LeftItemViewHolderFactory())
        leftNavRecyclerView?.adapter = leftNavigateAdapter

        contentNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        contentNavRecyclerView?.openFocusDynamic(false)
//        contentNavRecyclerView?.setFocusSearchJumpRow(false)
        rightContentNavigateAdapter = BaseRecyclerFactoryAdapter( NavigateFlexBoxItemViewHolderFactory())
        contentNavRecyclerView?.adapter = rightContentNavigateAdapter
//        contentNavRecyclerView?.setChildFocusListener{ position, child ->
//            leftNavRecyclerView?.setCheckedPosition(position)
//        }
        super.initView()
    }


    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        if (data is Response<*>){
            val result = data.data
            if(result is List<*>){
                if(result[0] is NavigateCategoryData){
                    leftNavigateAdapter?.appendCollectData(result as MutableCollection<NavigateCategoryData>?)
                    rightContentNavigateAdapter?.appendCollectData(result as MutableCollection<NavigateCategoryData>?)
                }
            }
        }
    }

    override fun showLoading(currentPage: Int) {
        if(currentPage == 0){
            loadingViewUtils?.showLoadingView(true)
        }else{

        }
    }
}
