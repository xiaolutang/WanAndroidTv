package com.txl.wanandroidtv.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.txl.commonlibrary.utils.DrawableUtils
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.NavigateArticleListData
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.NavigateCategoryData
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.ui.utils.ThemeUtils
import com.txl.wanandroidtv.ui.viewholder.NavigateFlexBoxItemViewHolderFactory
import com.txl.wan_android_data_provider.viewModel.NavigateNavItemListViewModel
import com.txl.wan_android_data_provider.viewModel.NavigateNavViewModelFactory
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelClass
import com.txl.wan_android_data_provider.viewModel.ViewModelContainer.putViewModelFactory

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

        fun newInstance(category:String):NavigateNavFragment{
            val fragment = NavigateNavFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY,category)
            fragment.arguments = bundle
            return fragment
        }
    }

    var leftNavRecyclerView:LibTvRecyclerView2?  = null
    var refreshLayout:SmartRefreshLayout?  = null
    var contentNavRecyclerView: LibTvRecyclerView2?  = null

    private var leftNavigateAdapter: BaseRecyclerFactoryAdapter<NavigateCategoryData>? = null
    private var rightContentNavigateAdapter: BaseRecyclerFactoryAdapter<NavigateCategoryData>? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_navigate_nav
    }

    override fun initView() {
        leftNavRecyclerView =  findViewById(R.id.tv_recycler_left_nav)
        leftNavRecyclerView?.openFocusDynamic(true)
//        leftNavRecyclerView?.setChildFocusListener { position, child -> contentNavRecyclerView?.smoothScrollToPositionAndTop(position) }
        refreshLayout =  findViewById(R.id.fragment_lib_smart_refresh_layout)
        refreshLayout?.setPadding(resources.getDimensionPixelSize(R.dimen.dp_90),resources.getDimensionPixelSize(R.dimen.dp_20),resources.getDimensionPixelSize(R.dimen.dp_90),resources.getDimensionPixelSize(R.dimen.dp_20))
        contentNavRecyclerView =  findViewById(R.id.fragment_lib_recycler)

        leftNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        leftNavigateAdapter = BaseRecyclerFactoryAdapter( LeftItemViewHolderFactory())
        leftNavRecyclerView?.adapter = leftNavigateAdapter

        contentNavRecyclerView?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        contentNavRecyclerView?.openFocusDynamic(false)
        rightContentNavigateAdapter = BaseRecyclerFactoryAdapter( NavigateFlexBoxItemViewHolderFactory())
        contentNavRecyclerView?.adapter = rightContentNavigateAdapter
//        contentNavRecyclerView?.setChildFocusListener{ position, child ->
//            leftNavRecyclerView?.setCheckedPosition(position)
//        }
    }


    override fun onDataReady(currentPage: Int, data: Any?) {
        loadingViewUtils?.showLoadingView(false)
        refreshLayout?.finishLoadMore()
        if(data is NavigateArticleListData){
            leftNavigateAdapter?.appendCollectData(data.data)
            rightContentNavigateAdapter?.appendCollectData(data.data)
        }
    }

    override fun showLoading(currentPage: Int) {
        if(currentPage == 0){
            loadingViewUtils?.showLoadingView(true)
        }else{
            refreshLayout?.autoLoadMore()
        }
    }
}

class LeftItemViewHolderFactory: IViewHolderFactory<BaseViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return LeftItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_navigate_left_nav,parent,false))
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return 0
    }

}

class LeftItemViewHolder(view: View): BaseViewHolder(view) {
    private var radioButton:RadioButton = view.findViewById(R.id.tv_navigate_left_nav)

    init {
        val states = arrayOfNulls<IntArray>(3)
        states[0] = intArrayOf(android.R.attr.state_focused)
        states[1] = intArrayOf(android.R.attr.state_checked)
        states[2] = intArrayOf()
        val themeColor = ThemeUtils.getThemeColor(radioButton.context)
        val white = radioButton.resources.getColor(R.color.white)
        val colors = intArrayOf(white, themeColor,white)
        radioButton.setTextColor(ColorStateList(states,colors))
        val radius = itemView.resources.getDimensionPixelSize(R.dimen.dp_30)
        val bgDrawable = DrawableUtils.makeFramelessStateListDrawable(Color.TRANSPARENT,themeColor,themeColor,0f)
        itemView.background = bgDrawable
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is NavigateCategoryData){
            radioButton.text = data.name
        }
    }
}