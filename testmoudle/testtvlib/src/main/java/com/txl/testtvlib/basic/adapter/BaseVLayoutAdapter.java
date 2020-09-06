package com.txl.testtvlib.basic.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.txl.ui_basic.viewholder.BaseViewHolder;
import com.txl.ui_basic.viewholder.IViewHolderFactory;

import java.util.ArrayList;
import java.util.Collection;

public class BaseVLayoutAdapter<T, VH extends BaseViewHolder> extends DelegateAdapter.Adapter<VH> implements BaseViewHolder.OnViewHolderItemClickListener {
    private ArrayList<T> mData = new ArrayList<>();

    private IViewHolderFactory<VH> mViewHolderFactory;
    private LayoutHelper mLayoutHelper;
    private OnItemClickListener mOnItemClickListener;

    public BaseVLayoutAdapter(IViewHolderFactory<VH> mViewHolderFactory, LayoutHelper mLayoutHelper) {
        this.mViewHolderFactory = mViewHolderFactory;
        this.mLayoutHelper = mLayoutHelper;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH  vh =  mViewHolderFactory.onCreateViewHolder(parent,viewType);
        vh.setOnViewHolderItemClickListener(this);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        int type = mViewHolderFactory.getItemViewType(position,getItem(position));
        return type;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.onBindViewHolder(position,getItem(position));
        holder.resetCurrentPosition(position);
    }

    public void removeItem(T data){
        this.mData.remove(data);
        this.notifyDataSetChanged();
    }

    public void appendData(T data) {
        this.mData.add(data);
        this.notifyItemInserted(mData.size()-1);
    }

    public void appendData(Collection<T> data) {
        int originSize = mData.size();
        this.mData.addAll(data);
        notifyItemRangeInserted(originSize,data.size());
    }

    public void clearData(){
        this.mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public T getItem(int position) {
        if (mData != null && getItemCount() > 0 && position < getItemCount()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public void onViewHolderItemClick(View view, int position) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view,position,getItem(position));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    public interface OnItemClickListener{
        <T> void onItemClick(View view, int position, T data);
    }
    public ArrayList<T>  getData(){

        return mData;
    }
}
