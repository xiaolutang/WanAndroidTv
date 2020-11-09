package com.txl.ui_basic.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.txl.ui_basic.viewholder.BaseViewHolder;
import com.txl.ui_basic.viewholder.IViewHolderFactory;

import java.util.Collection;

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/17
 * description：通过 IViewHolderFactory 创建ViewHolder
 */
public class BaseRecyclerFactoryAdapter<T> extends BaseRecyclerAdapter<T, BaseViewHolder>{
    private IViewHolderFactory<? extends RecyclerView.ViewHolder> mViewHolderFactory;
    private OnItemClickListener mItemClickListener;
    public BaseRecyclerFactoryAdapter(IViewHolderFactory<? extends RecyclerView.ViewHolder> viewHolderFactory) {
        mViewHolderFactory = viewHolderFactory;
    }

    public BaseRecyclerFactoryAdapter(Collection<T> data,IViewHolderFactory<? extends RecyclerView.ViewHolder> viewHolderFactory) {
        super( data );
        mViewHolderFactory = viewHolderFactory;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder = mViewHolderFactory.onCreateViewHolder( parent, viewType );
        holder.setOnViewHolderItemClickListener( (view, position) -> {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(position,getData().get(position));
            }
        } );
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBindViewHolder( position,getData().get( position ) );
    }

    @Override
    public int getItemViewType(int position) {
        return mViewHolderFactory.getItemViewType( position,getData().get( position ) );
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, Object data);
    }
}
