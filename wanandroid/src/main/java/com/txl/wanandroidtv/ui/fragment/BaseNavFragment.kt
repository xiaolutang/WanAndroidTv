package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView
import com.txl.ui_basic.fragment.BaseFragment
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.ui.adpater.BaseRecyclerFactoryAdapter
import com.txl.wanandroidtv.viewModel.*
import java.util.*
import kotlin.Deprecated as Deprecated1

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/11
 * description：抽象的导航Fragment
 */
const val CATEGORY = "category"
abstract class BaseNavFragment : BaseFragment(), OnLoadMoreListener {
    private var viewModel: AbsNavItemListVIewModel? = null

    var category = CATEGORY_HOME
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            category = it.getString(CATEGORY,CATEGORY_HOME)
        }
    }

    override fun initData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel(){

        viewModel = createViewModel()
        viewModel?.data?.observe(viewLifecycleOwner,object: Observable(), Observer<ResourceBoundary<*>> {
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

    private fun createViewModel(): AbsNavItemListVIewModel?{
        return ViewModelProvider(this, ViewModelContainer.getViewModelFactory(category)).get(ViewModelContainer.getViewModelClass(category))
    }

    /**
     * @param currentPage 当前第几页数据在加载
     * */
    abstract fun showLoading(currentPage:Int);

    abstract fun onDataReady(currentPage:Int,data :Any?)

    fun onLoadDataError(currentPage:Int){}

    override fun onDetach() {
        super.onDetach()
        viewModel?.resetData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel?.nextPage()
    }
}