package com.txl.wanandroidtv.ui.adpater

import android.content.Context
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.txl.ui_basic.viewholder.BaseViewHolder
import com.txl.wanandroidtv.R
import com.txl.wan_android_data_provider.bean.home.Article
import com.txl.wan_android_data_provider.bean.home.TopDataWrapper
import com.txl.wanandroidtv.ui.viewholder.*

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
            WanAndroidListItemType.TYPE_TOP->{
                val titleLayoutHelper = SingleLayoutHelper()
                titleLayoutHelper.setMargin(0,context.resources.getDimensionPixelSize(R.dimen.dp_50),0,0)
                val titleAdapter = BaseVLayoutAdapter<Any,BaseViewHolder>(WanAndroidListTitleViewHolder.viewHolderFactory,titleLayoutHelper)
                titleAdapter.appendata("置顶数据")

                val layoutHelper = SingleLayoutHelper()
                layoutHelper.setMargin(0,context.resources.getDimensionPixelSize(R.dimen.dp_50),0,0)
//                layoutHelper.setGap(context.resources.getDimensionPixelSize(R.dimen.dp_50))
                val adapter = BaseVLayoutAdapter<Any,BaseViewHolder>(WanAndroidTopScrollerViewHolder.viewHolderFactory,layoutHelper)
                adapter.appendata(TopDataWrapper(data as List<Article>))
                val adapters = ArrayList<BaseVLayoutAdapter<Any,BaseViewHolder>>()
                adapters.add(titleAdapter)
                adapters.add(adapter)
                return adapters
            }
            WanAndroidListItemType.TYPE_COMMON->{
                val bannerLayoutHelper = GridLayoutHelper(4)
                bannerLayoutHelper.setMargin(0,context.resources.getDimensionPixelSize(R.dimen.dp_50),0,0)
                bannerLayoutHelper.setGap(context.resources.getDimensionPixelSize(R.dimen.dp_50))
                bannerLayoutHelper.setAutoExpand(false)
                val adapter = BaseVLayoutAdapter<Any,BaseViewHolder>(WanAndroidCommonItemViewHolder.viewHolderFactory,bannerLayoutHelper)
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