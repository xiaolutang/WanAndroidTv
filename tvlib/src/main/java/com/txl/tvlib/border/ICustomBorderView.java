package com.txl.tvlib.border;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/13
 * description：
 */
public interface ICustomBorderView {
    /**
     * 是否自己绘制焦点,返回true 全局的焦点border不会被绘制
     * */
    boolean drawBorderBySelf();

    /**
     * 返回小于0的的值表示不自己进行独立设置，返回大于0的值才可以
     * 格式 1.1 表示 x,y方向都进行1.1倍缩放
     * 格式 1.1,1.2  表示x方向进行1.1倍缩放；y 方向上进行1.2倍缩放
     * */
    String focusScale();

    /**
     * 是否拥有焦点变化的缩放 animation
     * */
    boolean hasFocusAnimation();
}
