package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
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
abstract class BaseNavFragment : BaseFragment(), OnLoadMoreListener {
    private var viewModel: AbsNavItemListVIewModel? = null
    var smartRefreshLayout: SmartRefreshLayout? = null
    var recyclerView: LibTvRecyclerView? = null
    var adapter:BaseRecyclerFactoryAdapter<*>? = null
    private set

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lib_nav_base
    }

    /**
     * 这个不应该出现在这里，基础导航的公共类应该与ui无关
     * */
    override fun initView() {
        smartRefreshLayout = rootView?.findViewById(R.id.fragment_lib_smart_refresh_layout)
        recyclerView = rootView?.findViewById(R.id.fragment_lib_recycler)
        smartRefreshLayout?.setEnableAutoLoadMore(true)//是否启用列表惯性滑动到底部时自动加载更多
        smartRefreshLayout?.setOnLoadMoreListener(this)
        showLoading(0)
        //暂时不考虑阿里的VLayout 不需要那么复杂
        adapter = createAdapter()
        recyclerView?.adapter = adapter
        recyclerView?.focusSearchFailedListener = object: LibTvRecyclerView.OnFocusSearchFailedListener {
            override fun onFocusSearchFailed(
                    currentFocusView: View?,
                    viewPosition: Int,
                    direct: Int
            ) {
                //常规列表有4个元素一行，所以减4
                if((direct == View.FOCUS_DOWN || direct == View.FOCUS_RIGHT) && adapter != null && viewPosition+1 >= adapter!!.itemCount-4 && viewPosition != RecyclerView.NO_POSITION){
                    smartRefreshLayout?.autoLoadMore()
//                    viewModel?.nextPage()
                }
            }
        }
    }

    /**
     * 这个方法通过其他的方式移除掉
     * */
    @Deprecated()
    abstract fun createAdapter():BaseRecyclerFactoryAdapter<*>

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

    abstract fun createViewModel(): AbsNavItemListVIewModel?

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

    protected open fun onDataReady(currentPage:Int,data :Any?){
        loadingViewUtils?.showLoadingView(false)
        smartRefreshLayout?.finishLoadMore()
    }

    fun onLoadDataError(currentPage:Int){}

    override fun onDetach() {
        super.onDetach()
        viewModel?.resetData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewModel?.nextPage()
    }
}