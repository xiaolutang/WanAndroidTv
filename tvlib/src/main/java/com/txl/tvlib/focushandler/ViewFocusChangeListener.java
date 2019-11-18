package com.txl.tvlib.focushandler;

import android.view.View;

public class ViewFocusChangeListener implements View.OnFocusChangeListener {
    /**
     * 默认获取焦点的时候缩放1.1倍
     * */
    private float focusScale;

    private View.OnFocusChangeListener _chainedListener;
    private IFocusHandler _focusHandler;

    public ViewFocusChangeListener(View targetView) {
        this(targetView,1.1f);
    }

    public ViewFocusChangeListener(View targetView,float focusScale) {
        this.focusScale = focusScale;
        _chainedListener = targetView.getOnFocusChangeListener();
        _focusHandler = new ViewFocusHandler(focusScale,false);
        _focusHandler.onInitializeView(targetView);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (_focusHandler != null) {
            _focusHandler.onItemFocused(v, hasFocus);
        }
        if (_chainedListener != null) {
            _chainedListener.onFocusChange(v, hasFocus);
        }
    }
}
