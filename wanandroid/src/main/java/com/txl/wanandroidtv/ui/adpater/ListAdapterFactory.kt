package com.txl.wanandroidtv.ui.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.ui_basic.viewholder.IViewHolderFactory
import com.txl.wanandroidtv.R
import com.txl.wanandroidtv.ui.viewholder.WanAndroidBannerItemViewHolder

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：
 */
object ListAdapterFactory {
    fun createAdaptersWithType(context: Context, data: Collection<Any>, type: Int):List<BaseVLayoutAdapter<Any,BaseViewHolder>>{
        when(type){
            WanAndroidListItemType.TYPE_BANNER->{
                val bannerLayoutHelper = GridLayoutHelper(2)
                bannerLayoutHelper.setMargin(0,context.resources.getDimensionPixelSize(R.dimen.dp_50),0,0)
                bannerLayoutHelper.setGap(context.resources.getDimensionPixelSize(R.dimen.dp_50))
                val adapter = BaseVLayoutAdapter<Any,BaseViewHolder>(WanAndroidBannerItemViewHolder.viewHolderFactory,bannerLayoutHelper)
                adapter.appendCollectionData(data)
                val adapters = ArrayList<BaseVLayoutAdapter<Any,BaseViewHolder>>()
                adapters.add(adapter)
                return adapters
            }
            else->{
                return ArrayList<BaseVLayoutAdapter<Any,BaseViewHolder>>()
            }
        }

    }
}