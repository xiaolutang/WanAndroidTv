package com.txl.wanandroidtv.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.txl.tvlib.widget.CardFrameLayout
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.com.besjon.pojo.Article
import com.txl.wanandroidtv.ui.NavItemWidthUtils
import com.txl.wanandroidtv.ui.fragment.HOME_SPAN_COUNT
import com.txl.wanandroidtv.ui.utils.ThemeUtils
import com.txl.wanandroidtv.ui.viewholder.base.BaseViewHolder
import com.txl.wanandroidtv.ui.viewholder.base.IViewHolderFactory
import com.txl.wanandroidtv.ui.viewholder.base.NavTypeSpec

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/17
 * description：首页的普通页面数据
 */
class HomeFragmentCommonItemViewHolderFactory: IViewHolderFactory<CommonItemViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonItemViewHolder {
        return CommonItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_common,parent,false))
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return NavTypeSpec.ELEMENT_HOME_COMMON
    }
}

class CommonItemViewHolder(itemView: View) : BaseViewHolder(itemView){
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    private val tvPublishData = itemView.findViewById<TextView>(R.id.tv_publish_data)
    private val imageIcon = itemView.findViewById<ImageView>(R.id.image_icon)
    private val cardFrameLayout = itemView as  CardFrameLayout

    init {
        cardFrameLayout.setBorderColor(ThemeUtils.getThemeColor(cardFrameLayout.context))
        cardFrameLayout.setBorderWidth(cardFrameLayout.context.resources.getDimensionPixelSize(R.dimen.dp_5))
        cardFrameLayout.setHasSelectBorder(true)
        val  layoutParams = imageIcon.layoutParams
        val width = NavItemWidthUtils.getItemWidth(itemView.context, HOME_SPAN_COUNT,itemView.context.resources.getDimensionPixelSize(R.dimen.dp_50))
        val  height = width * 6 / 5
        layoutParams.width = width.toInt()
        layoutParams.height = height.toInt()
        imageIcon.layoutParams = layoutParams
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is Article){
            tvTitle.text = data.title
            tvPublishData.text = data.niceDate
            Glide.with(imageIcon).load(data.envelopePic).placeholder(R.drawable.image_place_holder).into(imageIcon)
        }
    }
}