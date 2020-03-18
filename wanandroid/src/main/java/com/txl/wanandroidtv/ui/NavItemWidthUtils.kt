package com.txl.wanandroidtv.ui

import android.content.Context
import com.txl.wanandroidtv.R
import org.jetbrains.anko.displayMetrics

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/18
 * description：通过比例计算元素宽度
 */
object NavItemWidthUtils {
    /**
     * @param col 有多少行
     * @param itemPadding 元素之间间距
     * */
    fun getItemWidth(context: Context,col:Int,itemPadding:Int):Float{
        //主界面左右各有90dp的间距
        val canUseWidth = context.displayMetrics.widthPixels - context.resources.getDimensionPixelSize(R.dimen.dp_90) * 2
        val totalPadding = itemPadding * (col -1f)
        return (canUseWidth -  totalPadding) /col
    }
}