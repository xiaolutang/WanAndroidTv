package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.txl.ui_basic.fragment.BaseFragment
import com.txl.wanandroidtv.viewModel.*
import java.util.*

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/11
 * description：抽象的导航Fragment
 */
const val CATEGORY = "category"
abstract class BaseNavFragment : BaseFragment(), OnLoadMoreListener {
    protected var viewModel: AbsNavItemListViewModel? = null

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


    override fun initView() {
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

    private fun createViewModel(): AbsNavItemListViewModel?{
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