package com.txl.wanandroidtv.utils

import androidx.recyclerview.widget.RecyclerView.RecycledViewPool


/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/15
 * description：
 */
object RecyclerViewConfigUtils {

    /**
     * 全局重用池
     * */
    val viewPool =RecycledViewPool()

    init {
        viewPool.setMaxRecycledViews(0, 10)
    }

}