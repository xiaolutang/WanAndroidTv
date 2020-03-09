package com.txl.tvlib.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;

import com.txl.commonlibrary.utils.AndroidLogWrapperUtil;

import org.jetbrains.annotations.Nullable;

public class FocusFixVerticalGridView extends VerticalGridView {
    /**
     * 向左容器内连续查找焦点
     * */
    private boolean focusLeftSearch = true;
    /**
     * 向右容器内连续查找焦点
     * */
    private boolean focusRightSearch = true;

    public FocusFixVerticalGridView(Context context) {
        super(context);
    }

    public FocusFixVerticalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusFixVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View focusSearch(int direction) {
        return super.focusSearch(direction);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        View searchView  = super.focusSearch(focused,direction);
        if(searchView != focused){
            boolean isMyChild = checkViewIsMyChild(searchView);
            if (isMyChild) return searchView;
        }
        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager == null){
            return searchView;
        }
        try {
            if(direction == View.FOCUS_DOWN){
                //向下寻找焦点，优先在容器内部查找
                int selectedPosition = getSelectedPosition();
                int targetPosition = selectedPosition +1;
//                AndroidLogWrapperUtil.d("FocusFixVerticalGridView","focusSearch size "+getAdapter().getItemCount() +" target = "+targetPosition);
                //在向下寻找焦点的时候，如果当前recyclerView还有子元素，优先查找recycler中的
                View result = findFocusDownCandidateView(focused,targetPosition);
//                AndroidLogWrapperUtil.d("FocusFixVerticalGridView","result is null  selectedPosition is "+searchView.getParent().getClass().getSimpleName()  );
                if(result != null){
                    return result;
                }
            }else if(direction == View.FOCUS_RIGHT && focusRightSearch){
                //向右需要查找到下一排的左侧第一个元素
                int selectedPosition = getSelectedPosition();
                int targetPosition = selectedPosition +1;
                AndroidLogWrapperUtil.d("FocusFixVerticalGridView","focusSearch right selectedPosition : "+selectedPosition+" targetPosition: "+targetPosition+"  "+getAdapter().getItemCount());
                for (;targetPosition < getAdapter().getItemCount();targetPosition++){
                    View result = layoutManager.findViewByPosition(targetPosition);
                    result = findFirstFocusAbleView(result);
                    if(result != null){
                        return result;
                    }
                }
            }else if (direction == View.FOCUS_LEFT && focusLeftSearch){
                //向左需要查找到上一排的最后一个元素
                int selectedPosition = getSelectedPosition();
                int targetPosition = selectedPosition -1;
                AndroidLogWrapperUtil.d("FocusFixVerticalGridView","focusSearch left selectedPosition : "+selectedPosition+" targetPosition: "+targetPosition+"  "+getAdapter().getItemCount());
                for (;targetPosition >= 0;targetPosition--){
                    View result = layoutManager.findViewByPosition(targetPosition);
                    result = finLastFocusAbleView(result);
                    if(result != null){
                        return result;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return searchView;
    }

    public void setFocusLeftSearch(boolean focusLeftSearch) {
        this.focusLeftSearch = focusLeftSearch;
    }

    public void setFocusRightSearch(boolean focusRightSearch) {
        this.focusRightSearch = focusRightSearch;
    }

    private View findFirstFocusAbleView(View targetFocusView){
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if(targetFocusView instanceof ViewGroup){
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = 0 ; i<childCount; i++){
                View focus = findFirstFocusAbleView(temp.getChildAt(i));
                if(viewCanFocus(focus)){
                    return focus;
                }
            }
        }
        return null;
    }

    private View finLastFocusAbleView(View targetFocusView){
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if(targetFocusView instanceof ViewGroup){
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = childCount-1 ; i>=0; i--){
                View focus = findFirstFocusAbleView(temp.getChildAt(i));
                if(viewCanFocus(focus)){
                    return focus;
                }
            }
        }
        return null;
    }

    private boolean viewCanFocus(View targetFocusView) {
        if (targetFocusView != null && targetFocusView.isFocusable() && targetFocusView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    /**
     * @param targetFocusView 即将获取焦点的View
     * @return true即将获取焦点的View是当前ViewGroup的直接或间接子元素
     * */
    @Nullable
    private boolean checkViewIsMyChild(View targetFocusView) {
        ViewParent parent = null;
        View temp = targetFocusView;
        while (temp != null){
            parent = temp.getParent();
            if(parent == this){//理论上从RecyclerView中搜索焦点一定能够找到这个。
                return true;
            }
            if(parent instanceof View){//这样子来找有点耗时,有条件的时候优化
                temp = (View) parent;
            }else {
                break;
            }
        }
        return false;
    }


    /**
     * 当前RecyclerView中向下查找合适的可获取焦点的View
     * */
    private View findFocusDownCandidateView(View focusView, int targetPosition){
        if(getAdapter().getItemCount() > targetPosition && targetPosition != RecyclerView.NO_POSITION){
            LayoutManager layoutManager = getLayoutManager();
            if(layoutManager == null){
                return null;
            }
            View result = layoutManager.findViewByPosition(targetPosition);
            if(result != null && result.getTop() == focusView.getTop()){//不能找在同一排的View
                return findFocusDownCandidateView(focusView,targetPosition+1);
            }
            return result;

        }
        return null;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
    }
}
