package com.txl.wanandroidtv.ui.viewholder.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface IViewHolderFactory<VH extends BaseViewHolder> {
    VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
    <T> int getItemViewType(int position, T data);
}
