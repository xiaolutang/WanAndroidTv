package com.txl.tvlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * 拥有焦点记忆的RadioGroup
 * */
public class FocusMemoryRadioGroup extends RadioGroup {
    /**
     * 上一个获取焦点的子元素
     * */
    private View mLastFocusView;

    public FocusMemoryRadioGroup(Context context) {
        super(context);
    }

    public FocusMemoryRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public View getLastFocusView() {
        return mLastFocusView;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        mLastFocusView = child;
    }

    @Override
    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        //上一个获取焦点的子元素不等于当前焦点的子元素
        if(mLastFocusView != null && mLastFocusView != focused){
            return mLastFocusView;
        }
        return super.focusSearch(focused,direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        super.addFocusables(views, direction, focusableMode);
    }
}
