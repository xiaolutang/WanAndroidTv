package com.txl.wanandroidtv.ui.adpater;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.txl.wanandroidtv.bean.NavItemData;
import com.txl.wanandroidtv.ui.viewholder.NavItemViewHolder;
import com.txl.wanandroidtv.ui.viewholder.NvaItemViewHolderFactory;
import com.txl.wanandroidtv.ui.viewholder.base.BaseViewHolder;
import com.txl.wanandroidtv.ui.viewholder.base.IViewHolderFactory;

import java.util.Collection;

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/17
 * description：通过 IViewHolderFactory 创建ViewHolder
 */
public class BaseRecyclerFactoryAdapter<T> extends BaseRecyclerAdapter<T, BaseViewHolder>{
    private IViewHolderFactory mViewHolderFactory;
    public BaseRecyclerFactoryAdapter(Context context,IViewHolderFactory viewHolderFactory) {
        super( context );
        mViewHolderFactory = viewHolderFactory;
    }

    public BaseRecyclerFactoryAdapter(Collection<T> data, Context context,IViewHolderFactory viewHolderFactory) {
        super( data, context );
        mViewHolderFactory = viewHolderFactory;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mViewHolderFactory.onCreateViewHolder( parent, viewType );
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder( position,getData().get( position ) );
    }

    @Override
    public int getItemViewType(int position) {
        return mViewHolderFactory.getItemViewType( position,getData().get( position ) );
    }
}
