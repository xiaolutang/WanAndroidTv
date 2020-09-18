package com.txl.wanandroidtv.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.home.BannerItemData
import com.txl.wanandroidtv.ui.adpater.WanAndroidListItemType
import com.txl.wanandroidtv.ui.widget.RatioImageView

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：
 */
class WanAndroidBannerItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private val imageIcon = itemView.findViewById<RatioImageView>(R.id.image_icon)
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    /**
     * 发布时间
     * */
    private val tvPublishData = itemView.findViewById<TextView>(R.id.tv_publish_data)

    companion object {
        val viewHolderFactory = object : IViewHolderFactory<BaseViewHolder> {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return WanAndroidBannerItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner, parent, false))
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return WanAndroidListItemType.TYPE_BANNER
            }
        }
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if (data is BannerItemData) {
            //如何处理Glide的对象回收问题？
            Glide.with(imageIcon).load(data.imagePath).placeholder(R.drawable.wan_android).into(imageIcon)
            tvTitle.text = data.title
        }
    }
}