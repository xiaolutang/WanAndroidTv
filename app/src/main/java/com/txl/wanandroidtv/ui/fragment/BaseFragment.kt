package com.txl.wanandroidtv.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.ui.utils.LoadingViewUtils

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/11
 * description：封装统一的加载圈效果
 * todo 拆分加载圈成接口，重新配置加载效果
 */
abstract class BaseFragment : Fragment() {
    var rootView:View? = null
    var loadingViewUtils: LoadingViewUtils? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(needLoadingView()){
            rootView = inflater.inflate(R.layout.lib_root_frame_layout,container,false)
            loadingViewUtils = applyLoadingView()
            val viewGroup = rootView as ViewGroup
            inflater.inflate(getLayoutRes(),viewGroup,true)
            viewGroup.addView(loadingViewUtils?.loadingRootView)
        }else{
            rootView = inflater.inflate(getLayoutRes(),container,false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    protected open fun applyLoadingView():LoadingViewUtils{
        return LoadingViewUtils(requireContext())
    }

    /***
     * 获取布局资源的id
     * */
    abstract fun getLayoutRes():Int

    /**
     * 初始化View
     * */
    abstract fun initView()

    /**
     * 初始化数据
     * */
    abstract fun initData()

    /**
     * 是否需要加载圈
     * */
    protected open fun needLoadingView():Boolean{
        return true
    }
}