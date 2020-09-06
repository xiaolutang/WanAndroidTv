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
class ContentSureWidthViewHolder(itemView:View):BaseViewHolder(itemView) {
    companion object{
        fun onCreateViewHolder(parent: ViewGroup): ContentSureWidthViewHolder{
            return ContentSureWidthViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sure_width_image_and_text,parent,false))
        }
    }


    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        itemView.findViewById<TextView>(R.id.tv_text_item).text = "第${position}个元素"
    }
}