package com.txl.testtvlib.testrecyclerview.item.center.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.txl.testtvlib.R
import com.txl.ui_basic.adapter.BaseRecyclerFactoryAdapter
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/4
 * description：
 */
class RecyclerViewHorizontalViewHolder(itemView:View):BaseViewHolder(itemView) {
    private var recyclerView:RecyclerView = itemView.findViewById(R.id.recycler_item_content)
    init {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                super.getItemOffsets(outRect, itemPosition, parent)
                outRect.left = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.top = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.right = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.bottom = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
            }
        })
        itemView.findViewById<TextView>(R.id.tv_title).text = "水平滚动的LibTvRecyclerView2"
    }

    companion object{
        fun onCreateViewHolder(parent: ViewGroup): RecyclerViewHorizontalViewHolder{
            return RecyclerViewHorizontalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_recycler_view,parent,false))
        }
    }


    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        val  adapter = BaseRecyclerFactoryAdapter<String>(object :IViewHolderFactory<ContentSureWidthViewHolder> {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentSureWidthViewHolder {
                return ContentSureWidthViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        })
        for (i in 0..20){
            adapter.appendData("$i")
        }
        recyclerView.adapter = adapter
    }
}