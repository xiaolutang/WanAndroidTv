package com.txl.wanandroidtv.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/16
 * description：具有比例的ImageView
 */
public class RatioImageView extends ImageView {
    private float ratio = 16f/9f;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hms = heightMeasureSpec;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY){
            int height = (int) (MeasureSpec.getSize(widthMeasureSpec)/ratio);
            hms = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, hms);
    }

    public void setRatio(float ratio){
        this.ratio = ratio;
        requestLayout();
    }
}
