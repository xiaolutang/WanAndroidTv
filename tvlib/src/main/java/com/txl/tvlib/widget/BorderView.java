package com.txl.tvlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.txl.tvlib.R;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/28
 * description：绘制焦点框的View
 * 因为这个View需要 和父容器保持相同的大小，因此需要在父容器测量子View时特殊处理
 */
public class BorderView extends View {
    private static final String TAG = "BorderView";

    //焦点Drawable
    private Drawable mDrawable;
    private Rect mBorderPaddingRect = new Rect();
    /**
     * 超越当前View的绘制边界
     * */
    private int outBound = 0;

    public BorderView(Context context) {
        this(context,null);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderView);
        outBound =  typedArray.getInt(R.styleable.BorderView_border_bound,0);
        Drawable drawable = ContextCompat.getDrawable(context,R.drawable.border);
        setBorderDrawable(drawable);
//        mDrawable = typedArray.getDrawable()
        typedArray.recycle();
    }

    public void setOutBound(int outBound){
        this.outBound = outBound;
        invalidate();
    }

    public void setBorderDrawable(Drawable drawable){
        mDrawable = drawable;
        if(mDrawable instanceof NinePatchDrawable){
            NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) mDrawable;
            ninePatchDrawable.getPadding(mBorderPaddingRect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawable instanceof NinePatchDrawable){
            mDrawable.setBounds(-outBound - mBorderPaddingRect.left,-outBound - mBorderPaddingRect.top,getWidth()+outBound+  mBorderPaddingRect.right,getHeight()+outBound+  mBorderPaddingRect.bottom);
        }else {
            mDrawable.setBounds(-outBound,-outBound,getWidth()+outBound,getHeight()+outBound);
        }
        mDrawable.draw(canvas);
        Log.d(TAG,"onDraw");
    }
}
