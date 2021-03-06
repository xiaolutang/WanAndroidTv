package com.txl.wanandroidtv.ui.adpater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.txl.ui_basic.adapter.BaseRecyclerAdapter;
import com.txl.ui_basic.viewholder.BaseViewHolder;
import com.txl.wan_android_data_provider.bean.NavItemData;
import com.txl.wanandroidtv.ui.viewholder.NavItemViewHolder;
import com.txl.wanandroidtv.ui.viewholder.NvaItemViewHolderFactory;

import java.util.Collection;

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/1/1
 * description：
 */
public class NavRecyclerAdapter extends BaseRecyclerAdapter<NavItemData, BaseViewHolder> {
    private NvaItemViewHolderFactory nvaItemViewHolderFactory;
    public NavRecyclerAdapter() {

    }

    public NavRecyclerAdapter(Collection<NavItemData> data) {
        super( data );
    }

    @NonNull
    @Override
    public NavItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(nvaItemViewHolderFactory == null){
            nvaItemViewHolderFactory = new NvaItemViewHolderFactory();
        }
        return nvaItemViewHolderFactory.onCreateViewHolder( parent, viewType );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder( position,getData().get( position ) );
    }
}
