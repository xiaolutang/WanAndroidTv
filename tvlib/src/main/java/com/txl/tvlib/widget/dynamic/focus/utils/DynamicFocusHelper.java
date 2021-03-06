package com.txl.tvlib.widget.dynamic.focus.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;


import com.txl.tvlib.widget.dynamic.focus.IDynamicFocusViewGroup;

import java.util.ArrayList;

public class DynamicFocusHelper implements IDynamicFocusHelper {
    private View lastFocusView;
    /**
     * 打开焦点记忆功能
     * */
    private boolean openDynamic = true;

    /**
     * 需要拥有焦点记忆的View
     * */
    private ViewGroup targetView;

    public DynamicFocusHelper(ViewGroup targetView) {
        this.targetView = targetView;
    }

    public void setOpenDynamic(boolean openDynamic) {
        this.openDynamic = openDynamic;
    }

    @Override
    public boolean hasDynamicFocusView() {
        return lastFocusView != null && lastFocusView.getParent() != null;
    }

    @Override
    public boolean addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(!openDynamic){//关闭焦点记忆功能，不进行处理
            return false;
        }
        //当前焦点元素在RecyclerView内部，不走焦点记忆处理逻辑
        if(targetView.getFocusedChild() != null){
            return false;
        }
        //焦点在记录的View上，不进行处理
        if(lastFocusView == null || lastFocusView.hasFocus()){
            return false;
        }
        if(lastFocusView instanceof IDynamicFocusViewGroup && lastFocusView instanceof ViewGroup){//写在
            return ((IDynamicFocusViewGroup) lastFocusView).dispatchAddFocusables(views,direction,focusableMode);
        }
        // !lastFocusView.hasFocus() 处理在焦点在自己身上交给父容器处理
        if(lastFocusView != null && lastFocusView.getParent() != null && lastFocusView.isFocusable()){
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

    /**
     * 在添加可以获取焦点的元素的时候，通过ViewGroup的addFocusables开始调用这个方法
     * 如果记忆的焦点是一个记忆焦点的容器那么向下再次寻找焦点
     * 否则调用addFocusables对可获取焦点的View进行添加
     * 这个方法是层层向下调用的。
     * */
    @Override
    public boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode){
        if(lastFocusView instanceof IDynamicFocusViewGroup && lastFocusView instanceof ViewGroup){
            return ((IDynamicFocusViewGroup) lastFocusView).dispatchAddFocusables(views, direction, focusableMode);
        }
        return addFocusables(views, direction, focusableMode);
    }
}
