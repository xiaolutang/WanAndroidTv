package com.txl.wanandroidtv.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;
import com.txl.tvlib.widget.dynamic.focus.IDynamicFocusViewGroup;
import com.txl.tvlib.widget.dynamic.focus.utils.DynamicFocusHelper;

import java.util.ArrayList;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/31
 * description：
 */
public class DynamicFlexboxLayout extends FlexboxLayout implements IDynamicFocusViewGroup {
    private DynamicFocusHelper dynamicFocusUtils;
    public DynamicFlexboxLayout(Context context) {
        super(context);
        init();
    }

    public DynamicFlexboxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        dynamicFocusUtils = new DynamicFocusHelper(this);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(!dynamicFocusUtils.addFocusables(views,direction,focusableMode)){
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        dynamicFocusUtils.requestChildFocus(child, focused);
        super.requestChildFocus(child, focused);
    }

    @Override
    public void clearFocus() {
        dynamicFocusUtils.clearFocus();
        super.clearFocus();
    }

    @Override
    public void clearChildFocus(View child) {
        dynamicFocusUtils.clearChildFocus();
        super.clearChildFocus(child);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        dynamicFocusUtils.requestFocus(direction, previouslyFocusedRect);
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode) {
        return dynamicFocusUtils.dispatchAddFocusables(views, direction, focusableMode);
    }

    @Override
    public void openFocusDynamic(boolean open) {
        dynamicFocusUtils.setOpenDynamic(open);
    }
}
