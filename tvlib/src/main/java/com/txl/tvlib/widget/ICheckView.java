package com.txl.tvlib.widget;

import android.widget.Checkable;

public interface ICheckView extends Checkable {
    /**
     * 设置选中监听
     * */
    void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    /**
     * 被包含的是父容器添加的时候调用
     * */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener onCheckedChangeWidgetListener);

    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkable The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(Checkable checkable, boolean isChecked);
    }
}
