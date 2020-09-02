package com.txl.tvlib.focushandler;

import android.view.View;

import com.txl.tvlib.R;

/**
 * 参考 leanback 对元素焦点进行处理
 * */
public class ViewFocusHandler implements IFocusHandler  {
    private final float _focusScale;
    private static final int DURATION_MS = 150;
    private final boolean _useDimmer;

    public ViewFocusHandler(float focusScale,boolean useDimmer) {
        _focusScale = focusScale;
        _useDimmer = useDimmer;
    }


    public float getFocusScale() {
        return _focusScale;
    }

    @Override
    public void onItemFocused(View view, boolean hasFocus) {
//        view.setSelected(hasFocus);
        getOrCreateAnimator(view).animateFocus(hasFocus, false);
    }



    @Override
    public void onInitializeView(View view) {
        getOrCreateAnimator(view).animateFocus(false, true);
    }

    private FocusAnimator getOrCreateAnimator(View view) {
        FocusAnimator animator = (FocusAnimator) view.getTag(R.id.lb_focus_animator);
        if (animator == null) {
            animator = new FocusAnimator(
                    view, getFocusScale(), _useDimmer, DURATION_MS);
            view.setTag(R.id.lb_focus_animator, animator);
        }
        return animator;
    }
}
