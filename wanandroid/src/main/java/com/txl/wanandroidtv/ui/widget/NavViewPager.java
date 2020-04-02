package com.txl.wanandroidtv.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/4/2
 * description：
 */
public class NavViewPager extends ViewPager {
    public NavViewPager(@NonNull Context context) {
        super(context);
    }

    public NavViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if(arrowScroll(direction)){
            return null;
        }
        return super.focusSearch(focused, direction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(hasFocus()){
            return super.dispatchKeyEvent(event);
        }

        View focused = findFocus();
        if(focused != null){
            return focused.dispatchKeyEvent(event);
        }
        return false;
    }
}
