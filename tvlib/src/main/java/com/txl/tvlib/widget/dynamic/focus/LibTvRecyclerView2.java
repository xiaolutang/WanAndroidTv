package com.txl.tvlib.widget.dynamic.focus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.txl.tvlib.widget.ICheckView;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/2
 * description：
 */
public class LibTvRecyclerView2 extends RecyclerView {
    /**
     * 当子元素上焦点的时候是否触发选中
     * */
    private boolean childFocusChecked = false;

    private ICheckView.OnCheckedChangeListener mChildOnCheckedChangeListener;

    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private LibTvRecyclerView2.PassThroughHierarchyChangeListener mPassThroughHierarchyChangeListener;

    /**
     * 当前的选中View
     * */
    private Checkable mCheckedView;
    /**
     *当前选中的position
     * */
    private int mCheckedPosition = RecyclerView.INVALID_TYPE;


    public LibTvRecyclerView2(@NonNull Context context) {
        this(context,null);
    }

    public LibTvRecyclerView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LibTvRecyclerView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughHierarchyChangeListener = new LibTvRecyclerView2.PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughHierarchyChangeListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughHierarchyChangeListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof ICheckView) {
            final ICheckView button = (ICheckView) child;
            //恢复原来的选中位置
            if (button.isChecked() || (params instanceof RecyclerView.LayoutParams && ((LayoutParams) params).getViewAdapterPosition() == mCheckedPosition && mCheckedPosition != RecyclerView.INVALID_TYPE)) {
                mProtectFromCheckedChange = true;
                if (mCheckedView != child )  {
                    setCheckedStateForView((View) mCheckedView, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedStateForView(child,true);
            }
        }
        super.addView(child, index, params);
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == LibTvRecyclerView2.this && child instanceof ICheckView) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((ICheckView) child).setOnCheckedChangeWidgetListener(mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == LibTvRecyclerView2.this && child instanceof ICheckView) {
                ((ICheckView) child).setOnCheckedChangeWidgetListener(null);
                //为了防止因为ViewHolder重用导致选中位置出现问题每次移除人为的标记为未选中
                ((ICheckView) child).setChecked(false);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }


    private class CheckedStateTracker implements ICheckView.OnCheckedChangeListener{
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkable The compound button view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(Checkable checkable, boolean isChecked) {//这个只能子元素被选中时触发
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if(mCheckedView != checkable){//调用到这里就表示为选中
                setCheckedStateForView((View) mCheckedView, false);
                setCheckedView((View) checkable);
            }
            mProtectFromCheckedChange = false;

        }
    }

    /**
     * 记录当前选中的View
     * */
    private void setCheckedView(View checkedView){
        if(checkedView instanceof ICheckView){
            mCheckedView = (ICheckView) checkedView;
            RecyclerView.LayoutParams params = (LayoutParams) checkedView.getLayoutParams();
            mCheckedPosition = params.getViewAdapterPosition();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checkedView,mCheckedPosition);
            }
        }
    }

    private void setCheckedStateForView(View checkedView, boolean checked) {
        if(checkedView instanceof ICheckView){
            ICheckView checkable = (ICheckView) checkedView;
            checkable.setChecked(checked);
            if(checked){
                setCheckedView(checkedView);
            }
        }

    }


    public interface OnCheckedChangeListener {

        public void onCheckedChanged(LibTvRecyclerView2 group,View checkedView, int position);
    }
}
