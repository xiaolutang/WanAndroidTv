package com.txl.wanandroidtv.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.home.BannerItemData
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：
 */
class WanAndroidListTitleViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)

    companion object {
        val viewHolderFactory = object : IViewHolderFactory<BaseViewHolder> {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return WanAndroidListTitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false))
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return WanAndroidListItemType.TYPE_TEXT_TITLE
            }
        }
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if (data is String) {
            tvTitle.text = data
        }
    }
}