package com.txl.wanandroidtv.ui.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.bean.com.besjon.pojo.Article
import com.txl.wanandroidtv.bean.com.besjon.pojo.NavigateCategoryData
import com.txl.wanandroidtv.ui.viewholder.base.BaseViewHolder
import com.txl.wanandroidtv.ui.viewholder.base.IViewHolderFactory
import com.txl.wanandroidtv.ui.viewholder.base.NavTypeSpec

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/30
 * description：wanAndroid 导航数据显示
 */
class NavigateFlexBoxItemViewHolderFactory:IViewHolderFactory<BaseViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return NavigateFlexBoxItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_navigate_right_flex_box_content,parent,false))
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return NavTypeSpec.ELEMENT_NAVIGATE_FLEX_BOX
    }
}


class NavigateFlexBoxItemViewHolder(itemView:View):BaseViewHolder(itemView){
    private var flexBoxLayout:FlexboxLayout = itemView.findViewById(R.id.flex_box)

    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        flexBoxLayout.removeAllViews()
        if(data is NavigateCategoryData){
            for (article in data.articles){
                flexBoxLayout.addView(getChildView(article,flexBoxLayout.context))
            }
        }
    }

    private fun getChildView(article: Article,context:Context):View{
        val child = LayoutInflater.from(context).inflate(R.layout.item_navigate_right_flex_box_content_child_item,null,false)
        val textView = child.findViewById<TextView>(R.id.tv_content_item_name)
        textView.text = article.title
        return child
    }

}
