package com.txl.tvlib.widget.focus.shake;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * View无法找到焦点元素的抖动动画
 * */
public class ViewShakeAnimation implements IFocusShake{
    //定义一个横向抖动的动画
    private ObjectAnimator translationAnimatorX;
    private ObjectAnimator translationAnimatorY;
    private static final int DURATION = 500;

    /**
     * 抖动偏差 单位px
     * */
    private int offset;

    public ViewShakeAnimation(View animationView) {
        this(animationView,4);
        setHorizontalShakeAnimator(animationView);
        setVerticalShakeAnimator(animationView);
    }

    /**
     * @param animationView  要使用动画的View
     * @param offset 抖动偏差 单位dp
     * */
    public ViewShakeAnimation(View animationView, int offset) {
        if(offset > 0){
            this.offset = convertDpToPixel(animationView.getContext(),offset);
        }
        setHorizontalShakeAnimator(animationView);
        setVerticalShakeAnimator(animationView);
    }

    private void setVerticalShakeAnimator(View newView) {
        //动画种类：X轴平移，后面的值为移动参数，正值为右，负值为左（Y轴正值为下，负值为上）
        translationAnimatorY = ObjectAnimator.ofFloat(newView, "translationY", 0f, offset, 0f, -offset, 0f);
        //用于控制动画快慢节奏，此处使用系统自带的线性Interpolator（匀速），此外还有各种变速Interpolator
        translationAnimatorY.setInterpolator(new LinearInterpolator());
        //设置动画重复次数，ValueAnimator.INFINITE即-1表示用于一直重复
        translationAnimatorY.setRepeatCount(ValueAnimator.RESTART);
        translationAnimatorY.setRepeatCount(1);
        translationAnimatorY.setDuration(DURATION);
    }

    private void setHorizontalShakeAnimator(View newView) {
        //动画种类：X轴平移，后面的值为移动参数，正值为右，负值为左（Y轴正值为下，负值为上）
        translationAnimatorX = ObjectAnimator.ofFloat(newView, "translationX", 0f, offset, 0f, -offset, 0f);
        //用于控制动画快慢节奏，此处使用系统自带的线性Interpolator（匀速），此外还有各种变速Interpolator
        translationAnimatorX.setInterpolator(new LinearInterpolator());
        //设置动画重复次数，ValueAnimator.INFINITE即-1表示用于一直重复
        translationAnimatorX.setRepeatCount(ValueAnimator.RESTART);
        translationAnimatorX.setRepeatCount(1);
        translationAnimatorX.setDuration(DURATION);
    }

    //开始横向抖动动画的方法，非外部调用
    @Override
    public void startHorizontalShakeAnimator() {
        //此处判断动画是否已经在run，若是则不重新调用start方法，避免重复触发导致抖动的不流畅
        if (translationAnimatorX != null && !translationAnimatorX.isRunning()) {
            //结束纵向动画，非本身横向动画
            endVerticalAnimator();
            translationAnimatorX.start();
        }
    }

    @Override
    public void startVerticalShakeAnimator() {
        //此处判断动画是否已经在run，若是则不重新调用start方法，避免重复触发导致抖动的不流畅
        if (translationAnimatorY != null && !translationAnimatorY.isRunning()) {
            //结束纵向动画，非本身横向动画
            endHorizontalAnimator();
            translationAnimatorY.start();
        }
    }

    private void endHorizontalAnimator() {
        if (translationAnimatorX != null) {
            //结束纵向动画，调用end()动画会到动画周期的最后一帧然后停止，调用cancel()动画会停止时间轴，停止在中间状态
            translationAnimatorX.end();
        }
    }

    private void endVerticalAnimator() {
        if (translationAnimatorY != null) {
            //结束纵向动画，调用end()动画会到动画周期的最后一帧然后停止，调用cancel()动画会停止时间轴，停止在中间状态
            translationAnimatorY.end();
        }
    }

    @Override
    public void startAnimation(int direction){
        if(direction == View.FOCUS_UP || direction == View.FOCUS_DOWN){
            startVerticalShakeAnimator();
        }else {
            //左右动画
            startHorizontalShakeAnimator();
        }
    }

    private int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
