package com.txl.testtvlib.testrecyclerview.item.center.viewholder

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
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
class RecyclerViewViewHolder(itemView:View):BaseViewHolder(itemView) {
    private var recyclerView:RecyclerView = itemView.findViewById(R.id.recycler_item_content)

    companion object{
        fun onCreateViewHolder(parent: ViewGroup): RecyclerViewViewHolder{
            return RecyclerViewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_recycler_view,parent,false))
        }
    }


    override fun <T : Any?> onBindViewHolder(position: Int, data: T) {
        super.onBindViewHolder(position, data)
        val  adapter = BaseRecyclerFactoryAdapter<String>(object :IViewHolderFactory<ContentViewHolder> {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
                return ContentViewHolder.onCreateViewHolder(parent)
            }

            override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
                return 0
            }
        })
        for (i in 0..20){
            adapter.appendData("$i")
        }
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context,4)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {


            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                super.getItemOffsets(outRect, itemPosition, parent)
                outRect.left = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.top = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.right = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
                outRect.bottom = parent.context.resources.getDimensionPixelSize(R.dimen.dp_5)
            }
        })
        recyclerView.adapter = adapter
    }
}