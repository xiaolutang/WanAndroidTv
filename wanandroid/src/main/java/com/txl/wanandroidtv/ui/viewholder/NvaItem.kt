package com.txl.wanandroidtv.ui.viewholder

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.NavItemData
import com.txl.wanandroidtv.ui.utils.ThemeUtils

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/9
 * description：主页导航按钮
 */
class NvaItemViewHolderFactory: IViewHolderFactory<NavItemViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_nav, parent, false)
        return NavItemViewHolder(itemView)
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return 0
    }
}

class NavItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
    val radioTitle = itemView.findViewById<RadioButton>(R.id.tv_nav_item)

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is NavItemData){
            radioTitle.text = data.title
            val states = arrayOfNulls<IntArray>(3)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf(android.R.attr.state_checked)
            states[2] = intArrayOf()
            val themeColor = ThemeUtils.getThemeColor(radioTitle.context)
            val white = radioTitle.resources.getColor(R.color.white)
            val colors = intArrayOf(themeColor,white ,white)
            radioTitle.setTextColor(ColorStateList(states,colors))
            val radius = itemView.resources.getDimensionPixelSize(R.dimen.dp_30)
//            val bgDrawable = DrawableUtils.makeFramelessStateListDrawable(Color.TRANSPARENT,themeColor,themeColor,radius.toFloat())
            itemView.background = ContextCompat.getDrawable(radioTitle.context,R.drawable.nav_item_bg)
        }
    }
}