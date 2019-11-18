package com.txl.tvlib.widget.dynamic.focus.utils;

import android.graphics.Rect;
import android.view.View;

import com.txl.tvlib.widget.dynamic.focus.IDynamicFocusViewGroup;

import java.util.ArrayList;

public class DynamicFocusUtils implements IDynamicFocusViewGroup {
    private View lastFocusView;

    @Override
    public boolean addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(lastFocusView != null && !lastFocusView.hasFocus() && lastFocusView.getParent() != null){
           views.add(lastFocusView);
           return true;
        }
        return false;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        lastFocusView = child;
    }

    @Override
    public void clearFocus() {
        lastFocusView = null;
    }

    @Override
    public void clearChildFocus() {
        lastFocusView = null;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        lastFocusView = null;
        return false;
    }
}
