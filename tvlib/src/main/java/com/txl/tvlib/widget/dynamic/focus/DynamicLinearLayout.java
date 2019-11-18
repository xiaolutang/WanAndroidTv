package com.txl.tvlib.widget.dynamic.focus;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.txl.tvlib.widget.dynamic.focus.utils.DynamicFocusUtils;

import java.util.ArrayList;

/**
 * 具有焦点记忆功能的LinearLayout,暂时先这样命名，后面可以考虑加入焦点优先级功能
 * */
public class DynamicLinearLayout extends LinearLayout implements IDynamicFocusViewGroup{
    private DynamicFocusUtils dynamicFocusUtils;

    public DynamicLinearLayout(Context context) {
        super(context);
        init();
    }

    public DynamicLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DynamicLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        dynamicFocusUtils = new DynamicFocusUtils();
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
}
