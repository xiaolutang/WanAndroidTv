package com.txl.wanandroidtv.ui.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.txl.tvlib.widget.dynamic.focus.LibTvRecyclerView2
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wan_android_data_provider.bean.home.Article
import com.txl.wan_android_data_provider.bean.home.TopDataWrapper
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：置顶数据
 */
class WanAndroidTopScrollerViewHolder(private val view: RecyclerView) : BaseViewHolder(view) {
    companion object {
        val viewHolderFactory = object : IViewHolderFactory<BaseViewHolder> {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val recyclerView = LibTvRecyclerView2(parent.context)
                recyclerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                return WanAndroidTopScrollerViewHolder(recyclerView)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return WanAndroidListItemType.TYPE_BANNER
            }
        }
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is TopDataWrapper){
            view.clipChildren = false
            view.clipToPadding = false
            view.isFocusable = false
            view.layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL,false)
            val adapter= BaseRecyclerFactoryAdapter<Article>(object :IViewHolderFactory<BaseViewHolder>{
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                    return WanAndroidTopScrollerItemViewHolder.viewHolderFactory.onCreateViewHolder(parent,viewType)
                }

                override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                    return 0
                }
            })
            adapter.appendCollectData(data.data)
            view.adapter = adapter
        }

    }
}