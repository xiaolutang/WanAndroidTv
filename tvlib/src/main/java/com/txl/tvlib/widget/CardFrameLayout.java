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
import android.widget.FrameLayout;

import androidx.leanback.widget.ShadowOverlayContainer;
import com.txl.tvlib.R;
import com.txl.tvlib.config.TvLibConfig;
import com.txl.tvlib.focushandler.ViewFocusChangeListener;
import com.txl.tvlib.widget.focus.shake.IFocusShake;
import com.txl.tvlib.widget.focus.shake.ViewShakeAnimation;

/**
 * 具有RadioButton的能力，当元素焦点改变时会触发checked,在失去焦点的时候或将check置为false select置为true {@link FocusTracker#onFocusChange}
 * */
public class CardFrameLayout extends FrameLayout implements ICheckView {

    private static final String TAG = CardFrameLayout.class.getSimpleName();

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

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

    public CardFrameLayout(Context context) {
        this(context,null);
    }

    public CardFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CardFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardFrameLayout, defStyleAttr, 0);

        openFocusShake = a.getBoolean(R.styleable.CardFrameLayout_open_shake, true);
        autoAddFocusAnimation = a.getBoolean(R.styleable.CardFrameLayout_auto_add_focus_animation, false);
        mViewBorder.drawBorder = a.getBoolean(R.styleable.CardFrameLayout_hasFocusBorder,TvLibConfig.Companion.getDefaultConfig().getHasSelectBorder());
        mViewBorder.setBorderWidth(a.getDimensionPixelSize(R.styleable.CardFrameLayout_selectBorderWidth, TvLibConfig.Companion.getDefaultConfig().getBorderWidth()));
        mViewBorder.setBorderColor(a.getColor(R.styleable.CardFrameLayout_borderColor,TvLibConfig.Companion.getDefaultConfig().getBorderColor()));
        mFocusChecked = a.getBoolean(R.styleable.CardFrameLayout_focusChecked,true);
        a.recycle();
        _focusTracker = new FocusTracker();
        super.setOnFocusChangeListener(_focusTracker);
        _viewShakeAnimation = new ViewShakeAnimation(this);
        if(autoAddFocusAnimation){
            new ViewFocusChangeListener(this);
        }
        setFocusable(true);
        setWillNotDraw(false);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
//        mViewBorder.drawableBorder(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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
     * @param checked 触发是否被选中 checked 为false 时会将select置为false
     */
    @Override
    public void setChecked(boolean checked) {
        if(_focusChangeCheckedState || checked != _checked){
            _checked = checked;
            refreshDrawableState();
            dispatchSetChecked(checked);
            if(!checked){
                setSelected(false);
            }
            if(_onCheckedChangeListener != null){
                _onCheckedChangeListener.onCheckedChanged(this,checked);
            }
            if(_onCheckedChangeWidgetListener != null){
                _onCheckedChangeWidgetListener.onCheckedChanged(this,checked);
            }
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
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
                    if(findFocus() == CardFrameLayout.this){
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
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this._onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener onCheckedChangeWidgetListener) {
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
            if(hasFocus){//元素获取焦点的时候触发checked
                setChecked(true);
            }else {
                setChecked(false);
                setSelected(true);
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
            if(drawBorder){
                Log.d(TAG,"drawableBorder width: "+getWidth()+" height: "+getHeight());
                //除2.0的原因是绘制会从线的中心点画
                canvas.drawRect(borderWidth/2.0f,borderWidth/2.0f, getWidth()-borderWidth/2.0f,getHeight()-borderWidth/2.0f,paint);
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
