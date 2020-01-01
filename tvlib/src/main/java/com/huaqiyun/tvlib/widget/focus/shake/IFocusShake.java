package com.huaqiyun.tvlib.widget.focus.shake;

/**
 * 无法查找到对应焦点元素的抖动动画抽取
 * */
public interface IFocusShake {
    void startHorizontalShakeAnimator();

    void startVerticalShakeAnimator();

    void startAnimation(int direction);
}
