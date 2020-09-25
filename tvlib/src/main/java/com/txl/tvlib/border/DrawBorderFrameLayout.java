package com.txl.tvlib.border;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.txl.tvlib.R;

/**
 * Created by liutao on 12/16/15.
 */
public class DrawBorderFrameLayout extends FrameLayout {
    private static final String TAG = "DrawBorderFrameLayout";

    private BorderPainter mBorderPainter;

    public DrawBorderFrameLayout(Context context) {
        this(context, null);
    }

    public DrawBorderFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBorderPainter = new BorderPainter(this, R.drawable.select_border);
//        setChildrenDrawingOrderEnabled(true);
    }

//    @Override
//    protected int getChildDrawingOrder(int childCount, int i) {
//        int p = indexOfChild(drawBorderView);
//        if(p == i){
//            return childCount-1;
//        }else if(p>i){
//            return i;
//        }else {
//            return i-1;
//        }
//    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG,"dispatchDraw");
        super.dispatchDraw(canvas);
        mBorderPainter.draw(canvas);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        mBorderPainter.setView(focused);
    }
}

