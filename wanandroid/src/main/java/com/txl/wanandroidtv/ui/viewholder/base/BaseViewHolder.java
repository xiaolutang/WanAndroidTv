package com.txl.wanandroidtv.ui.viewholder.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    protected String TAG = getClass().getSimpleName();
    protected int currentPosition;
    private OnViewHolderItemClickListener mOnViewHolderItemClickListener;

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.mOnViewHolderItemClickListener = onViewHolderItemClickListener;
    }

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if(mOnViewHolderItemClickListener != null){
                mOnViewHolderItemClickListener.onViewHolderItemClick(v,currentPosition);
            }
        });
    }

    /**
     * 用于更新数据
     * @param data 更新ui所需要的数据
     * */
    public <T> void onBindViewHolder(int position,T data) {
        currentPosition = position;
    }

    public void resetCurrentPosition(int position ){
        currentPosition = position;
    }

    public interface OnViewHolderItemClickListener{
        void onViewHolderItemClick(View view, int position);
    }
}
