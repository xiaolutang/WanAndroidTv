package com.txl.testtvlib.testrecyclerview.item.center.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.txl.testtvlib.R
import com.txl.ui_basic.viewholder.BaseViewHolder

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/3
 * description：
 */
class TitleViewHolder(itemView:View):BaseViewHolder(itemView) {
    companion object{
        fun onCreateViewHolder(parent: ViewGroup): TitleViewHolder{
            return TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.test_recycler_item_center_title,parent,false))
        }
    }

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        if(data is String){
            itemView.findViewById<RadioButton>(R.id.rb_title).text = data
        }
    }
}