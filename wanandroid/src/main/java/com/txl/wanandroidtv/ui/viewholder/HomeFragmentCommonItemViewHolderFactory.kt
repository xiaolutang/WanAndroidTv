package com.txl.wanandroidtv.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.txl.tvlib.widget.CardFrameLayout
import com.txl.ui_basic.adapter.NavTypeSpec
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.home.Article
import com.txl.wanandroidtv.ui.NavItemWidthUtils
import com.txl.wanandroidtv.ui.fragment.HOME_SPAN_COUNT

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
//        val borderWidth = cardFrameLayout.context.resources.getDimensionPixelSize(R.dimen.dp_5)
//        cardFrameLayout.setBorderColor(ThemeUtils.getThemeColor(cardFrameLayout.context))
//        cardFrameLayout.setBorderWidth(borderWidth)
//        cardFrameLayout.setHasSelectBorder(true)

        val width = NavItemWidthUtils.getItemWidth(itemView.context, HOME_SPAN_COUNT,itemView.context.resources.getDimensionPixelSize(R.dimen.dp_50))
        val  height = width * 6 / 5
        var layoutParams = cardFrameLayout.layoutParams
        layoutParams.width = width.toInt()
        cardFrameLayout.layoutParams = layoutParams
        layoutParams = imageIcon.layoutParams

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