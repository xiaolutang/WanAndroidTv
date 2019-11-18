package com.txl.tvlib.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import androidx.leanback.widget.BaseCardView;

import com.txl.tvlib.R;
import com.txl.tvlib.utils.ViewShakeAnimation;

/**
 * 增加这个类为了方便以后的功能扩展
 * */
public class BaseCustomCardView extends BaseCardView implements ICheckView {

    private static final String TAG = BaseCustomCardView.class.getSimpleName();

    private boolean _checked;

    /**
     * 不管当前状态强制更改选中状态
     * */
    private boolean _focusChangeCheckedState = true;

    /**
     * 选中监听
     * */
    private OnCheckedChangeListener _onCheckedChangeListener;
    /**
     * 通知父容器被选中
     * */
    private OnCheckedChangeListener _onCheckedChangeWidgetListener;

    private FocusTracker _focusTracker;

    private ViewShakeAnimation _viewShakeAnimation;

    public BaseCustomCardView(Context context) {
        super(context);
        init();
    }

    public BaseCustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        _focusTracker = new FocusTracker();
        super.setOnFocusChangeListener(_focusTracker);
        _viewShakeAnimation = new ViewShakeAnimation(this);
    }


    /**
     * Register a callback to be invoked when focus of this view changed.
     *
     * @param l The callback that will run.
     */
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        _focusTracker.focusChangeListener = l;
    }

    /**
     * Returns the focus-change callback registered for this view.
     *
     * @return The callback, or null if one is not registered.
     */
    @Override
    public OnFocusChangeListener getOnFocusChangeListener() {
        return _focusTracker.focusChangeListener;
    }

    /**
     * Sets the Selected state of this Card. This can trigger changes in the
     * card layout, resulting in views to become visible or hidden. A card is
     * normally set to Selected state when it receives input focus.
     *
     * @param selected True if the card is Selected, or false otherwise.
     * @see #isSelected()
     */
    @Override
    public void setSelected(boolean selected) {
//        if (selected && !isSelected()) {
//            setChecked(true);
//        }
        super.setSelected(selected);
    }

    /**
     * Change the checked state of the view
     *
     * @param checked The new checked state
     */
    @Override
    public void setChecked(boolean checked) {
        if(_focusChangeCheckedState || checked != _checked){
            _checked = checked;
            refreshDrawableState();
            dispatchSetChecked(checked);
            if(_onCheckedChangeListener != null){
                _onCheckedChangeListener.onCheckedChanged(this,checked);
            }
            if(_onCheckedChangeWidgetListener != null){
                _onCheckedChangeWidgetListener.onCheckedChanged(this,checked);
            }
        }
    }

    @Override
    public View focusSearch(int direction) {
        View focusView =  super.focusSearch(direction);
        Log.d(TAG,"focusSearch nextFocusView is null ? "+focusView);
        if(focusView == null || focusView == this){
            _viewShakeAnimation.startAnimation(direction);
        }
        return focusView;
    }

    private void dispatchSetChecked(boolean checked) {
        int childCount = getChildCount();
        for (int i=0;i<childCount;i++){
            View child = getChildAt(i);
            if(child instanceof Checkable){
                ((Checkable) child).setChecked(checked);
            }
        }
    }

    /**
     * @return The current checked state of the view
     */
    @Override
    public boolean isChecked() {
        return _checked;
    }

    /**
     * Change the checked state of the view to the inverse of its current state
     */
    @Override
    public void toggle() {
        setChecked(!_checked);
    }

    @Override
    public void setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener onCheckedChangeListener) {
        this._onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void setOnCheckedChangeWidgetListener(ICheckView.OnCheckedChangeListener onCheckedChangeWidgetListener) {
        this._onCheckedChangeWidgetListener = onCheckedChangeWidgetListener;
    }

    private class FocusTracker implements OnFocusChangeListener{
        private OnFocusChangeListener focusChangeListener;
        /**
         * Called when the focus state of a view has changed.
         *
         * @param v        The view whose state has changed.
         * @param hasFocus The new focus state of v.
         */
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setChecked(!hasFocus);
            if(focusChangeListener != null){
                focusChangeListener.onFocusChange(v,hasFocus);
            }
        }
    }
}
