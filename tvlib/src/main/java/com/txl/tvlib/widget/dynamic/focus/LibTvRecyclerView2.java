package com.txl.tvlib.widget.dynamic.focus;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.txl.tvlib.focushandler.IFocusSearchHelper;
import com.txl.tvlib.widget.ICheckView;
import com.txl.tvlib.widget.dynamic.focus.utils.DynamicFocusHelper;

import java.util.ArrayList;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/2
 * description：
 */
public class LibTvRecyclerView2 extends RecyclerView implements IDynamicFocusViewGroup{
    private final String TAG = LibTvRecyclerView2.class.getSimpleName();

    /**
     * 当子元素上焦点的时候是否触发选中
     * */
    private boolean childFocusChecked = false;

    private ICheckView.OnCheckedChangeListener mChildOnCheckedChangeListener;

    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private LibTvRecyclerView2.PassThroughHierarchyChangeListener mPassThroughHierarchyChangeListener;

    /**
     * 当前的选中View
     * */
    private Checkable mCheckedView;
    /**
     *当前选中的position
     * */
    private int mCheckedPosition = RecyclerView.INVALID_TYPE;

    private DynamicFocusHelper mDynamicFocusHelper;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mPageChangeListener;

    private OnFocusSearchFailedListener mOnFocusSearchFailedListener = new OnFocusSearchFailedListener() {
        @Override
        public boolean onLeftSearchFailed() {
            return false;
        }

        @Override
        public boolean onRightSearchFailed() {
            return false;
        }

        @Override
        public boolean onTopSearchFailed() {
            return false;
        }

        @Override
        public boolean onBottomSearchFailed() {
            return false;
        }
    };

    public void setOnFocusSearchFailedListener(OnFocusSearchFailedListener onFocusSearchFailedListener) {
        this.mOnFocusSearchFailedListener = onFocusSearchFailedListener;
    }

    public LibTvRecyclerView2(@NonNull Context context) {
        this(context,null);
    }

    public LibTvRecyclerView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LibTvRecyclerView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughHierarchyChangeListener = new LibTvRecyclerView2.PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughHierarchyChangeListener);
        setChildDrawingOrderCallback(new ChildDrawingOrderCallback(){
            @Override
            public int onGetChildDrawingOrder(int childCount, int i){
                final int tempFocusIndex = indexOfChild(getFocusedChild());
                // View 根据Z-order来进行绘制，将焦点元素的z-order与Recycler的最后一个元素进行对换 焦点的order始终最大
                //配合 requestChildFocus 时重新绘制对焦点元素不能放置在最上面进行处理
                if (tempFocusIndex == -1) {
                    return i;
                }
                if (tempFocusIndex == i) {
                    return childCount - 1;
                } else if (i == childCount - 1) {
                    return tempFocusIndex;
                } else {
                    return i;
                }
            }
        });
        mDynamicFocusHelper = new DynamicFocusHelper(this);
        //会导致页面抖动
//        addOnLayoutChangeListener( new View.OnLayoutChangeListener(){
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                postDelayed( new Runnable() {
//                    @Override
//                    public void run() {
//                        View f = findFocus();
//                        if(f != null){
//                            makeViewHorizontalCenter( f);
//                            makeViewVerticalCenter( f );
//                            f.clearFocus();
//                            f.requestFocus();
//                        }
//
//                    }
//                } ,64);
//            }
//        } );
    }

    public void openFocusDynamic(boolean open){
        mDynamicFocusHelper.setOpenDynamic(open);
    }


    public void bindViewPager(ViewPager viewPager){
        if(mViewPager != null){
            mViewPager.removeOnPageChangeListener(mPageChangeListener);
        }
        mViewPager = viewPager;
        mPageChangeListener = new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCheckedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(!mDynamicFocusHelper.addFocusables(views,direction,focusableMode)){
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    public void clearFocus() {
        mDynamicFocusHelper.clearFocus();
        super.clearFocus();
    }

    @Override
    public void clearChildFocus(View child) {
        mDynamicFocusHelper.clearChildFocus();
        super.clearChildFocus(child);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        mDynamicFocusHelper.requestFocus(direction, previouslyFocusedRect);
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode) {
        return mDynamicFocusHelper.dispatchAddFocusables(views, direction, focusableMode);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        mDynamicFocusHelper.requestChildFocus(child, focused);
        //bug 解决焦点View被其它子View覆盖的问题
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout( changed, l, t, r, b );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        View focusView = FocusFinder.getInstance().findNextFocus(this,focused,direction);
        return  focusView != null? focusView : super.focusSearch(focused,direction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event){
        if (!canScroll()) {
            if (isFocused() && event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
                View currentFocused = findFocus();
                if (currentFocused == this) currentFocused = null;
                View nextFocused = FocusFinder.getInstance().findNextFocus(this,
                        currentFocused, View.FOCUS_DOWN);
                return nextFocused != null
                        && nextFocused != this
                        && nextFocused.requestFocus(View.FOCUS_DOWN);
            }
            return false;
        }
        boolean handled = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    handled = verticalScroll(View.FOCUS_UP) || mOnFocusSearchFailedListener.onTopSearchFailed();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    handled = verticalScroll(View.FOCUS_DOWN) || mOnFocusSearchFailedListener.onBottomSearchFailed();
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    handled = horizontalScroll(View.FOCUS_LEFT) || mOnFocusSearchFailedListener.onLeftSearchFailed();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    handled = horizontalScroll(View.FOCUS_RIGHT) || mOnFocusSearchFailedListener.onRightSearchFailed();
                    break;
            }
        }

        return handled;
    }

    private boolean canScroll(){
        return canScrollVertically(-1) || canScrollVertically(1) || canScrollHorizontally(-1) || canScrollHorizontally(1);
    }

    private boolean horizontalScroll(int direction){
        boolean right = direction == View.FOCUS_RIGHT;
        View currentFocus = findFocus();
        int realCanUseWidth = getWidth() - getPaddingEnd() - getPaddingStart();
        if(currentFocus.getWidth() < realCanUseWidth){
            scrollBy(right?currentFocus.getWidth()/2:-currentFocus.getWidth()/2,0);//原来焦点在中间，因此移动1/2应该没有什么问题
        }else {
            int dealt = currentFocus.getWidth() - realCanUseWidth + realCanUseWidth/2 + 6;
            scrollBy(right?dealt:-dealt,0);
        }


        View nextFocus = findNextFocusViewJumpRow(currentFocus,direction);
        if(nextFocus != null){
            makeViewHorizontalCenter(nextFocus);
            makeViewVerticalCenter(nextFocus);
            final View n = nextFocus;
            post(new Runnable() {
                @Override
                public void run() {
                    n.requestFocus(View.FOCUS_DOWN);
                }
            });
        }
        return nextFocus != null && nextFocus != this;
    }

    private int getAdapterPositionByView(LibTvRecyclerView2 root,View view) {
        if (view == null || !checkViewIsMyChild(view)) {
            return NO_POSITION;
        }
        View dirChild = view;
        ViewParent parent = view.getParent();
        while (parent != root && parent != null) {
            dirChild = (View) parent;
            parent = dirChild.getParent();
        }
        LayoutParams params = (LayoutParams) dirChild.getLayoutParams();
        if (params == null || params.isItemRemoved()) {
            // when item is removed, the position value can be any value.
            return NO_POSITION;
        }
        return params.getViewAdapterPosition();
    }

    /**
     * @param targetView 需要被检查的目标View
     * @return true即将获取焦点的View是当前ViewGroup的直接或间接子元素
     */
    private boolean checkViewIsMyChild(View targetView) {
        ViewParent parent = null;
        View temp = targetView;
        while (temp != null) {
            parent = temp.getParent();
            if (parent == this) {//理论上从RecyclerView中搜索焦点一定能够找到这个。
                return true;
            }
            if (parent instanceof View) {//这样子来找有点耗时,有条件的时候优化
                temp = (View) parent;
            } else {
                break;
            }
        }
        return false;
    }

    /***
     * 左右按键跨行寻找下一个可以获取焦点的View
     * */
    private View findNextFocusViewJumpRow(View currentFocus,int direct){
        boolean isRight = direct == View.FOCUS_RIGHT;
        //原来向右左向
        int position = isRight?(getAdapterPositionByView(this,currentFocus)+1):(getAdapterPositionByView(this,currentFocus)-1);
        return isRight?searchRightFocus(getLayoutManager(),position):searchLeftFocus(getLayoutManager(), position);
    }


    private View searchLeftFocus(LayoutManager layoutManager, int targetPosition) {
        View result;
        for (; targetPosition >= 0; targetPosition--) {
            result = layoutManager.findViewByPosition(targetPosition);
            result = findLastFocusAbleView(result);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 向右查找焦点
     * */
    private View searchRightFocus(LayoutManager layoutManager, int targetPosition) {
        if(layoutManager==null){
            return null;
        }
        View result;
        for (; targetPosition < getAdapter().getItemCount(); targetPosition++) {
            result = layoutManager.findViewByPosition(targetPosition);
            result = findFirstFocusAbleView(result);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private View findFirstFocusAbleView(View targetFocusView) {
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if(targetFocusView instanceof IFocusSearchHelper){
            return findFirstFocusAbleView(((IFocusSearchHelper) targetFocusView).findFirstFocusAbleView());
        }else if (targetFocusView instanceof ViewGroup) {
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View focus = findFirstFocusAbleView(temp.getChildAt(i));
                if (viewCanFocus(focus)) {
                    return focus;
                }
            }
        }
        return null;
    }

    private View findLastFocusAbleView(View targetFocusView) {
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if(targetFocusView instanceof IFocusSearchHelper){
            return findLastFocusAbleView(((IFocusSearchHelper) targetFocusView).findLastFocusAbleView());
        }else if (targetFocusView instanceof ViewGroup) {
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = childCount-1; i >= 0; i--) {
                View focus = findLastFocusAbleView(temp.getChildAt(i));
                if (viewCanFocus(focus)) {
                    return focus;
                }
            }
        }
        return null;
    }

    private boolean viewCanFocus(View targetFocusView) {
        return targetFocusView != null && targetFocusView.isFocusable() && targetFocusView.getVisibility() == View.VISIBLE;
    }

    private boolean verticalScroll(int direction){
        boolean down = direction == View.FOCUS_DOWN;
//        if(!canScrollVertically(down?1:-1)){
//            return false;
//        }
        View currentFocus = findFocus();
        int realCanUseHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        if(currentFocus.getHeight() < realCanUseHeight){
            scrollBy(0,down?currentFocus.getHeight()/2:-currentFocus.getHeight()/2);//原来焦点在中间，因此移动1/2应该没有什么问题
        }else {
            int dealt = currentFocus.getHeight() - realCanUseHeight + realCanUseHeight/2 + 6;
            scrollBy(0,down?dealt:-dealt);
        }


        View nextFocus = FocusFinder.getInstance().findNextFocus(this,currentFocus,direction);
        if(nextFocus != null){
            makeViewVerticalCenter(nextFocus);
            final View n = nextFocus;
            post(new Runnable() {
                @Override
                public void run() {
                    n.requestFocus(View.FOCUS_DOWN);
                }
            });
        }
        return nextFocus != null && nextFocus != this;
    }

    private void makeViewVerticalCenter(View view){
        if(view == null){
            return;
        }
        if(view.getHeight() > (getHeight() - getPaddingTop() - getPaddingBottom())){//这个时候顶部对齐
            scrollBy(0, currentViewTop(view));
            return;
        }
        float offsetTop = currentViewTop(view) + view.getHeight()/2.0f;//fixme 当view的父容器超出recyclerView的范围会不会导致计算有问题?
        float middle = getHeight()/2.0f;
        offsetTop = offsetTop - middle;
        Log.d(TAG,"makeViewVerticalCenter 偏差："+offsetTop);
        scrollBy(0, (int) (offsetTop));
    }

    private void makeViewHorizontalCenter(View view){
        float offsetLeft = currentViewLeft(view) + view.getWidth()/2.0f;
        float middle = getWidth()/2.0f;
        offsetLeft = offsetLeft - middle;
        Log.d(TAG,"makeViewHorizontalCenter 偏差："+offsetLeft);
        scrollBy((int) offsetLeft, 0);
    }

    private int currentViewTop(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        int top = view.getTop();
        while (parent != null  && parent != this){
            top = top + parent.getTop();
            parent = (ViewGroup) parent.getParent();
        }
        return top;
    }

    private int currentViewLeft(View view){
        ViewGroup parent = (ViewGroup) view.getParent();
        int left = view.getLeft();
        while (parent != null  && parent != this){
            left = left + parent.getLeft();
            parent = (ViewGroup) parent.getParent();
        }
        return left;
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughHierarchyChangeListener.mOnHierarchyChangeListener = listener;
    }

    public void setCheckedPosition(final int position){

        scrollToPosition(position);
        post(new Runnable() {
            @Override
            public void run() {
                LayoutManager layoutManager = getLayoutManager();
                if(layoutManager != null){
                    final View targetView = layoutManager.findViewByPosition(position);
                    if(targetView instanceof ICheckView){
                        ((ICheckView) targetView).setChecked(true);
                        makeViewHorizontalCenter(targetView);
                        makeViewVerticalCenter(targetView);
//                        targetView.requestFocus();
                    }
                }
            }
        });
        mCheckedPosition = position;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof ICheckView) {
            final ICheckView button = (ICheckView) child;
            //恢复原来的选中位置
            if (button.isChecked() || (params instanceof RecyclerView.LayoutParams && ((LayoutParams) params).getViewAdapterPosition() == mCheckedPosition && mCheckedPosition != RecyclerView.INVALID_TYPE)) {
                mProtectFromCheckedChange = true;
                if (mCheckedView != child )  {
                    setCheckedStateForView((View) mCheckedView, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedStateForView(child,true);
            }
        }
        super.addView(child, index, params);
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == LibTvRecyclerView2.this && child instanceof ICheckView) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((ICheckView) child).setOnCheckedChangeWidgetListener(mChildOnCheckedChangeListener);
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
            if (parent == LibTvRecyclerView2.this && child instanceof ICheckView) {
                ((ICheckView) child).setOnCheckedChangeWidgetListener(null);
                //为了防止因为ViewHolder重用导致选中位置出现问题每次移除人为的标记为未选中
                ((ICheckView) child).setChecked(false);
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
        public void onCheckedChanged(Checkable checkable, boolean isChecked) {//这个只能子元素被选中时触发
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if(mCheckedView != checkable){//调用到这里就表示为选中
                setCheckedStateForView((View) mCheckedView, false);
                setCheckedView((View) checkable);
            }
            mProtectFromCheckedChange = false;

        }
    }

    /**
     * 记录当前选中的View
     * */
    private void setCheckedView(View checkedView){
        if(checkedView instanceof ICheckView){
            mCheckedView = (ICheckView) checkedView;
            //选中的View能够正常获取焦点
            mDynamicFocusHelper.requestChildFocus(checkedView, null);
            RecyclerView.LayoutParams params = (LayoutParams) checkedView.getLayoutParams();
            mCheckedPosition = params.getViewAdapterPosition();
            if(mViewPager != null){
                mViewPager.setCurrentItem(mCheckedPosition);
            }
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checkedView,mCheckedPosition);
            }
        }
    }

    private void setCheckedStateForView(View checkedView, boolean checked) {
        if(checkedView instanceof ICheckView){
            ICheckView checkable = (ICheckView) checkedView;
            checkable.setChecked(checked);
            if(checked){
                setCheckedView(checkedView);
            }
        }

    }


    public interface OnCheckedChangeListener {

        void onCheckedChanged(LibTvRecyclerView2 group,View checkedView, int position);
    }

    public interface OnFocusSearchFailedListener{
        /**
         * @return true 代表本次事件被外部处理了，RecyclerView在相关子View不处理KeyEvent事件的情况下会自己进行处理
         * */
        boolean onLeftSearchFailed();
        /**
         * @return true 代表本次事件被外部处理了，RecyclerView在相关子View不处理KeyEvent事件的情况下会自己进行处理
         * */
        boolean onRightSearchFailed();
        /**
         * @return true 代表本次事件被外部处理了，RecyclerView在相关子View不处理KeyEvent事件的情况下会自己进行处理
         * */
        boolean onTopSearchFailed();
        /**
         * @return true 代表本次事件被外部处理了，RecyclerView在相关子View不处理KeyEvent事件的情况下会自己进行处理
         * */
        boolean onBottomSearchFailed();
    }
}
