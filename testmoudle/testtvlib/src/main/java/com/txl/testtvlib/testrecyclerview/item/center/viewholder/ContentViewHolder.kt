package com.txl.testtvlib.testrecyclerview.item.center.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.txl.testtvlib.R
import com.txl.ui_basic.viewholder.BaseViewHolder

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/4
 * description：
 */
class ContentViewHolder(itemView:View):BaseViewHolder(itemView) {
    companion object{
        fun onCreateViewHolder(parent: ViewGroup): ContentViewHolder{
            return ContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_and_text,parent,false))
        }
    }


    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        itemView.findViewById<TextView>(R.id.tv_text_item).text = "我是第${position}个元素"
    }
}