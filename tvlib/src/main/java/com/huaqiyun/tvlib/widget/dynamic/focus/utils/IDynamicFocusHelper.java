package com.huaqiyun.tvlib.widget.dynamic.focus.utils;

import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

/**
 * 定义焦点记忆的ViewGroup需要重写的方法，
 * 可以直接使用{@link  DynamicFocusHelper}里面对应的方法来进行处理，或者自己实现
 * */
public interface IDynamicFocusHelper {

    boolean hasDynamicFocusView();

    /**
     * ViewGroup默认有实现，
     * 要实现自己的焦点记忆参考leanback的RecyclerView
     * @return true 自己处理可获取焦点元素的添加
     * */
    boolean addFocusables(ArrayList<View> views, int direction, int focusableMode);

    /**
     * ViewGroup有默认的实现
     * 在这个位置需要记忆当前获取焦点的子元素
     * */
    void requestChildFocus(View child, View focused);

    /**
     * 清除对应的记录
     * */
    void clearFocus();

    void clearChildFocus();

    /**
     * 自身获取焦点的时候需要清除对应的焦点记忆
     * */
    boolean requestFocus(int direction, Rect previouslyFocusedRect);

    /**
     * 在添加可以获取焦点的元素的时候，通过ViewGroup的addFocusables开始调用这个方法
     * 如果记忆的焦点是一个记忆焦点的容器那么向下再次寻找焦点
     * 否则调用addFocusables对可获取焦点的View进行添加
     * 这个方法是层层向下调用的。
     * */
    boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode);
}
