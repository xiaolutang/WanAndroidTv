package com.txl.tvlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;

import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.ShadowOverlayContainer;

import com.txl.tvlib.R;
import com.txl.tvlib.focushandler.ViewFocusChangeListener;
import com.txl.tvlib.widget.focus.shake.IFocusShake;
import com.txl.tvlib.widget.focus.shake.ViewShakeAnimation;

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

    private boolean openFocusShake = true;

    private FocusTracker _focusTracker;

    private IFocusShake _viewShakeAnimation;

    private ViewBorder mViewBorder = new ViewBorder();

    private ShakeAnimationRunnable shakeAnimationRunnable;

    /**
     * 自动处理焦点的缩放效果,在leanback封装的位置不要使用，leanback会自己添加recycler的item焦点效果
     * */
    private boolean autoAddFocusAnimation = false;

    private OnViewSelectChangeListener _onViewSelectChangeListener;

    /**
     * 焦点变化立即更改checked状态
     * */
    private boolean mFocusChecked = true;
    /**
     * 失去焦点的时候触发选中效果 当mFocusChecked 为false setCheckedWhenFocusLost才会生效
     * */
    private boolean setCheckedWhenFocusLost = false;

    public BaseCustomCardView(Context context) {
        this(context,null);
    }

    public BaseCustomCardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseCustomCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardFrameLayout, defStyleAttr, 0);

        openFocusShake = a.getBoolean(R.styleable.CardFrameLayout_open_shake, true);
        autoAddFocusAnimation = a.getBoolean(R.styleable.CardFrameLayout_auto_add_focus_animation, false);
        mViewBorder.drawBorder = a.getBoolean(R.styleable.CardFrameLayout_hasFocusBorder,false);
        mViewBorder.setBorderWidth(a.getDimensionPixelSize(R.styleable.CardFrameLayout_selectBorderWidth,10));
        mViewBorder.setBorderColor(a.getColor(R.styleable.CardFrameLayout_borderColor,Color.WHITE));
        mFocusChecked = a.getBoolean(R.styleable.CardFrameLayout_focusChecked,true);
        a.recycle();
        _focusTracker = new FocusTracker();
        super.setOnFocusChangeListener(_focusTracker);
        _viewShakeAnimation = new ViewShakeAnimation(this);
        if(autoAddFocusAnimation){
            new ViewFocusChangeListener(this);
        }
        setFocusable(true);
        setFocusableInTouchMode( true );
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFocused()){
            mViewBorder.drawableBorder(canvas);
        }
    }

    public void setViewShakeAnimation(IFocusShake iFocusShake){
        _viewShakeAnimation = iFocusShake;
    }


    public void setAutoAddFocusAnimation(boolean autoAddFocusAnimation){
        if(this.autoAddFocusAnimation == autoAddFocusAnimation){
            return;
        }
        this.autoAddFocusAnimation = autoAddFocusAnimation;
        if(autoAddFocusAnimation){
            new ViewFocusChangeListener(this);
        }else {
            setOnFocusChangeListener(null);
        }
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

    public void setOnViewSelectChangeListener(OnViewSelectChangeListener onViewSelectChangeListener) {
        this._onViewSelectChangeListener = onViewSelectChangeListener;
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
        if(_onViewSelectChangeListener != null && isSelected() != selected){
            _onViewSelectChangeListener.onViewSelect(selected);
        }
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
            dispatchSetChecked(checked);
            refreshDrawableState();

            if(_onCheckedChangeListener != null){
                _onCheckedChangeListener.onCheckedChanged(this,checked);
            }
            if(_onCheckedChangeWidgetListener != null){
                _onCheckedChangeWidgetListener.onCheckedChanged(this,checked);
            }
        }
    }

    @Override
    public View focusSearch(final int direction) {
        View focusView =  super.focusSearch(direction);
        Log.d(TAG,"focusSearch nextFocusView is null ? "+focusView);
        if(openFocusShake && focusView == null || focusView == this){
            if(focusView != null && focusView.getParent() instanceof ShadowOverlayContainer){
                _viewShakeAnimation = new ViewShakeAnimation((View) focusView.getParent());
            }
            if(shakeAnimationRunnable != null){
                shakeAnimationRunnable.cancel = true;
                removeCallbacks(shakeAnimationRunnable);
                shakeAnimationRunnable = null;
            }
            shakeAnimationRunnable = new ShakeAnimationRunnable() {
                @Override
                void doAnimation() {
                    if(findFocus() == BaseCustomCardView.this){
                        _viewShakeAnimation.startAnimation(direction);
                    }
                }
            };
            postDelayed(shakeAnimationRunnable,200);
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
        if(!isChecked()){
            setChecked(!_checked);
        }
    }

    @Override
    public void setOnCheckedChangeListener(ICheckView.OnCheckedChangeListener onCheckedChangeListener) {
        this._onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void setOnCheckedChangeWidgetListener(ICheckView.OnCheckedChangeListener onCheckedChangeWidgetListener) {
        this._onCheckedChangeWidgetListener = onCheckedChangeWidgetListener;
    }

    @Override
    public boolean performClick() {
        if(!isFocused()){
            requestFocus();
        }
        setCheckedWhenFocusLost = true;
        setChecked(false);
        return super.performClick();
    }

    public void setFocusChecked(boolean focusChecked) {
        mFocusChecked = focusChecked;
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
            if(mFocusChecked){
                setChecked(!hasFocus);
            }
            if(!mFocusChecked){
                if(hasFocus && isChecked()){
                    setChecked(false);
                    setCheckedWhenFocusLost = true;
                }
                if(setCheckedWhenFocusLost && !hasFocus){
                    setChecked(true);
                    setCheckedWhenFocusLost = false;
                }
            }
            if(focusChangeListener != null){
                focusChangeListener.onFocusChange(v,hasFocus);
            }
        }
    }

    public static abstract class OnViewSelectChangeListener{
        public abstract void onViewSelect(boolean select);
    }

    public void setBorderColor(int color){
        mViewBorder.setBorderColor(color);
    }

    public void setBorderWidth(int width){
        mViewBorder.setBorderWidth(width);
    }

    public void setHasSelectBorder(boolean hasFocusBorder){
        mViewBorder.drawBorder = hasFocusBorder;
    }

    private class ViewBorder{
        boolean drawBorder = false;
        int borderWidth = 5;
        int borderColor = Color.WHITE;
        Paint paint = new Paint();

        ViewBorder() {
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(borderWidth);
            paint.setColor(borderColor);
        }

        void drawableBorder(Canvas canvas){
            Log.d(TAG,"drawableBorder  "+drawBorder);
            if(drawBorder){
                Log.d(TAG,"drawableBorder");
                canvas.drawRect(0f,0f, getWidth(),getHeight(),paint);
            }
        }

        void setBorderWidth(int borderWidth){
            this.borderWidth = borderWidth;
            paint.setStrokeWidth(borderWidth);
        }

        void setBorderColor(int borderColor){
            this.borderColor = borderColor;
            paint.setColor(borderColor);
        }
    }

    private static abstract class ShakeAnimationRunnable implements Runnable{
        boolean cancel = false;

        @Override
        public void run() {
            if(!cancel){
                doAnimation();
            }
        }

        abstract void doAnimation();
    }
}
