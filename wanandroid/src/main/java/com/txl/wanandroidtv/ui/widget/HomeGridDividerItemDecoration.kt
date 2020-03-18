package com.txl.wanandroidtv.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/18
 * description：
 */
class HomeGridDividerItemDecoration(private var decorationWidth: Int = 0): RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager
        val adapter = parent.adapter ?: return
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val count = adapter.itemCount
            //grid会根据自身的宽度来自己分散摆放因此不需要左右的间距
            var left = 0
            var right = 0
            var top = decorationWidth
            var bottom = 0
            if(position % spanCount == 0){//最左边
                left = 0
            }
            if(position < spanCount){//最上边
                top = 0
            }
            if(position % spanCount == spanCount-1){//最右边
                right = 0
            }
            if(position + spanCount >= count){//最下边
                bottom = 0
            }
            outRect.set(left,top, right, bottom)
        }
    }
}