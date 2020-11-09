package com.txl.wanandroidtv.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.flexbox.FlexboxLayout;
import com.txl.tvlib.focushandler.IFocusSearchHelper;
import com.txl.tvlib.widget.dynamic.focus.IDynamicFocusViewGroup;
import com.txl.tvlib.widget.dynamic.focus.utils.DynamicFocusHelper;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static com.txl.tvlib.utils.ViewExtKt.viewCanFocus;

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
    public boolean dispatchKeyEvent(KeyEvent event) {

        return super.dispatchKeyEvent( event ) || executeKeyEvent(event);
    }

    private boolean executeKeyEvent(KeyEvent event){
        int direction = View.FOCUS_DOWN;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    direction = View.FOCUS_UP;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    direction = View.FOCUS_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    direction = View.FOCUS_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    direction = View.FOCUS_RIGHT;
                    break;
            }

            View nextFocus = findNextFocusViewBySelf( direction );
            if (nextFocus != null) {
                nextFocus.requestFocus();
                return true;
            };
        }
        return false;
    }

    @Nullable
    private View findNextFocusViewBySelf(int direction) {
        int index = findFocusIndex(findFocus());
        Log.e("DynamicFlexboxLayout","focusSearch index :: "+index+"  is right "+(direction == View.FOCUS_RIGHT));
        if(index != -1){
            if(direction == View.FOCUS_RIGHT){
                if(index != getChildCount()-1){
                    View nextFocus = getChildAt(index+1);
                    nextFocus = findFirstFocusAbleView(nextFocus);
                    Log.e("DynamicFlexboxLayout","focusSearch index :: "+index+"  is right "+nextFocus);
                    return nextFocus;
                }
            }else if(direction == View.FOCUS_LEFT){
                if(index != 0){
                    View nextFocus = getChildAt(index-1);
                    nextFocus = findFirstFocusAbleView(nextFocus);
                    return nextFocus;
                }
            }
        }
        return null;
    }

    @Override
    public View focusSearch(View focused, int direction) {

        int index = findFocusIndex(focused);
        Log.e("DynamicFlexboxLayout","focusSearch index :: "+index+"  is right "+(direction == View.FOCUS_RIGHT));
        if(index != -1){
            if(direction == View.FOCUS_RIGHT){
                if(index != getChildCount()-1){
                    View nextFocus = getChildAt(index+1);
                    nextFocus = findFirstFocusAbleView(nextFocus);
                    Log.e("DynamicFlexboxLayout","focusSearch index :: "+index+"  is right "+nextFocus);
                    if(nextFocus != null){
                        return nextFocus;
                    }
                }
            }else if(direction == View.FOCUS_LEFT){
                if(index != 0){
                    View nextFocus = getChildAt(index-1);
                    nextFocus = findFirstFocusAbleView(nextFocus);
                    if(nextFocus != null){
                        return nextFocus;
                    }
                }
            }
        }
        return super.focusSearch(focused, direction);
    }

    private int findFocusIndex(View focused){
        View f = focused;
        ViewParent parent = focused.getParent();
        while (parent != null){
            if(parent == this){
                return indexOfChild(f);
            }
            f = (View) parent;
            parent = f.getParent();
        }
        return -1;
    }

    private View findFirstFocusAbleView(View targetFocusView) {
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if(targetFocusView instanceof IFocusSearchHelper){
            return findFirstFocusAbleView(((IFocusSearchHelper) targetFocusView).findFirstFocusAbleView());
        }else if (targetFocusView instanceof ViewGroup) {
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View focus = findFirstFocusAbleView(temp.getChildAt(i));
                if (viewCanFocus(focus)) {
                    return focus;
                }
            }
        }
        return null;
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
