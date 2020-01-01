package com.huaqiyun.tvlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import androidx.leanback.widget.VerticalGridView;

/**
 * 使HorizontalGridView具有类似RadioGroup的功能
 * */
public class RadioGroupVerticalGridView extends VerticalGridView {
    private static final String TAG = RadioGroupVerticalGridView.class.getSimpleName();
    private Checkable _checkedView;

    private boolean mProtectFromCheckedChange = false;

    private CheckedStateTracker _childOnCheckedChangeListener;

    private PassThroughHierarchyChangeListener _passThroughListener;

    public RadioGroupVerticalGridView(Context context) {
        super(context);
        init();
    }

    public RadioGroupVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioGroupVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        _childOnCheckedChangeListener = new CheckedStateTracker();
        _passThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(_passThroughListener);
    }



    /**
     * Register a callback to be invoked when a child is added to or removed
     * from this view.
     *
     * @param listener the callback to invoke on hierarchy change
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        _passThroughListener.mOnHierarchyChangeListener = listener;
    }

    public void focusPosition(int position){
        try {
            setSelectedPositionSmooth(position);
            View targetView = getLayoutManager().findViewByPosition(position);
            targetView.requestFocus();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSelectedPositionSmoothWithCheckState(int position, boolean checked){
        setSelectedPositionSmooth(position);
        View targetView = getLayoutManager().findViewByPosition(position);
        BaseCustomCardView baseCustomCardView = findCardView(targetView);
        if(baseCustomCardView != null && !baseCustomCardView.isSelected()){
            baseCustomCardView.setChecked(checked);
        }
    }

    private BaseCustomCardView findCardView(View root){
        if(root instanceof BaseCustomCardView){
            return (BaseCustomCardView) root;
        }
        if(root instanceof ViewGroup){
            int childCount = ((ViewGroup) root).getChildCount();
            for (int i=0; i<childCount;i++){
                BaseCustomCardView result = findCardView(((ViewGroup) root).getChildAt(i));
                if(result != null){
                    return result;
                }
            }
        }
        return null;
    }

    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioGroupVerticalGridView.this && child instanceof ICheckView) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((ICheckView) child).setOnCheckedChangeWidgetListener(_childOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioGroupVerticalGridView.this && child instanceof ICheckView) {
                ((ICheckView) child).setOnCheckedChangeWidgetListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    private class CheckedStateTracker implements ICheckView.OnCheckedChangeListener{
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkable The compound button view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(Checkable checkable, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if(_checkedView != checkable && _checkedView != null){
                setCheckedStateForView(_checkedView, false);
            }
            mProtectFromCheckedChange = false;

            setCheckedView(checkable);
        }
    }

    private void setCheckedView(Checkable checkable) {
        _checkedView = checkable;
    }

    private void setCheckedStateForView(Checkable checkedView, boolean checked) {
        if(checkedView != null){
            checkedView.setChecked(checked);
        }
    }
}
