package com.txl.wanandroidtv.ui.viewholder

import android.view.View
import android.view.ViewGroup
import com.txl.wanandroidtv.ui.viewholder.base.BaseViewHolder
import com.txl.wanandroidtv.ui.viewholder.base.IViewHolderFactory

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/17
 * description：首页的普通页面数据
 */
class HomeFragmentCommonItemViewHolderFactory: IViewHolderFactory<CommonItemViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonItemViewHolder {
//        CommonItemViewHolder;
    }

    override fun <T : Any?> getItemViewType(position: Int, data: T): Int {
        return 0
    }
}

class CommonItemViewHolder(itemView: View) : BaseViewHolder(itemView){

}