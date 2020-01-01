package com.huaqiyun.tvlib.widget.dynamic.focus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Checkable;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hqy.commonlibrary.utils.ReflectUtils;
import com.hqy.commonlibrary.utils.ViewUtils;
import com.huaqiyun.tvlib.widget.ICheckView;
import com.huaqiyun.tvlib.widget.dynamic.focus.utils.DynamicFocusHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * 为androidTv开发的Recycler库
 */
public class LibTvRecyclerView extends RecyclerView implements IDynamicFocusViewGroup {
    private static final String TAG = LibTvRecyclerView.class.getSimpleName();
    static boolean DEBUG = true;

    /**
     * 向左容器内连续查找焦点
     * */
    private boolean focusLeftSearch = true;
    /**
     * 向右容器内连续查找焦点
     * */
    private boolean focusRightSearch = true;

    /**
     * 焦点始终保持在某一个位置
     */
    public static final int SCROLL_ALIGN = 0;

    /**
     * 在 {@link #SCROLL_ALIGN} 的模式下，焦点的相对滚动方向位置，0 靠近开始的位置，1靠近结束的位置
     */
    private float mScrollAlignOffset = 0.5f;
    /**
     * 默认的滚动方式
     */
    public static final int SCROLL_FOLLOW = 1;
    /**
     * 按页翻滚
     */
    public static final int SCROLL_BY_PAGE = 2;
    /**
     * 滚动方式
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef ({SCROLL_ALIGN,SCROLL_FOLLOW,SCROLL_BY_PAGE})
    public  @interface ScrollMode{}
    @ScrollMode int mScrollMode = SCROLL_FOLLOW;
    /**
     * 当前被选中的位置
     */
    private int mCheckedPosition = RecyclerView.NO_POSITION;

    private HandleFocusScroll mHandleFocusScroll;

    /**
     * 当前焦点View是否全部可见
     */
    private boolean focusViewAllVisible;
    private boolean horizontalViewAllVisible;
    private boolean verticalViewAllVisible;

    /**
     * 焦点的寻址方向
     */
    private int focusSearchDirect;

    /**
     * 是否因焦点查找在滚动过程中
     */
    private boolean isFocusScroll = false;

    final Rect mTempRect = new Rect();


    private CheckedStateTracker mChildOnCheckedChangeListener;

    private PassThroughHierarchyChangeListener mPassThroughHierarchyChangeListener;

    private Checkable mCheckedView;

    private boolean mProtectFromCheckedChange = false;

    private DynamicFocusHelper mDynamicFocusUtils;

    public LibTvRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public LibTvRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LibTvRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setFocusable(false);
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughHierarchyChangeListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughHierarchyChangeListener);
        mDynamicFocusUtils = new DynamicFocusHelper();
        mDynamicFocusUtils.setOpenDynamic(false);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughHierarchyChangeListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
    }

    public void setFocusLeftSearch(boolean focusLeftSearch) {
        this.focusLeftSearch = focusLeftSearch;
    }

    public void setFocusRightSearch(boolean focusRightSearch) {
        this.focusRightSearch = focusRightSearch;
    }

    public void setScrollMode(@ScrollMode int scrollMode) {
        this.mScrollMode = scrollMode;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        LayoutManager layoutManager = getLayoutManager();
        Adapter adapter = getAdapter();
        if (layoutManager == null || adapter == null || adapter.getItemCount() == 0) {
            return super.focusSearch(focused, direction);
        }
        View result = null;
        try {

            int selectedPosition = getAdapterPositionByView(this,focused);

            if (direction == View.FOCUS_RIGHT && focusRightSearch) {
                super.focusSearch(focused, FOCUS_DOWN);
                if(selectedPosition+1 == adapter.getItemCount()){
                    return getParent().focusSearch(focused,direction);
                }
                //向右需要查找到下一排的左侧第一个元素
                int targetPosition = selectedPosition + 1;
                result = searchRightFocus(layoutManager, targetPosition);
            } else if (direction == View.FOCUS_LEFT && focusLeftSearch) {
                super.focusSearch(focused, FOCUS_UP);
                if(selectedPosition == 0){
                    return getParent().focusSearch(focused,direction);
                }
                //向左需要查找到上一排的最后一个元素
                int targetPosition = selectedPosition - 1;
                result = searchLeftFocus(layoutManager, targetPosition);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) return result;
        return super.focusSearch(focused, direction);
    }

    @org.jetbrains.annotations.Nullable
    private View searchLeftFocus(LayoutManager layoutManager, int targetPosition) {
        View result;
        for (; targetPosition >= 0; targetPosition--) {
            result = layoutManager.findViewByPosition(targetPosition);
            result = finLastFocusAbleView(result);
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

    private View finLastFocusAbleView(View targetFocusView) {
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if (targetFocusView instanceof ViewGroup) {
            ViewGroup temp = (ViewGroup) targetFocusView;
            int childCount = temp.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View focus = findLastFocusAbleView(temp.getChildAt(i));
                if (viewCanFocus(focus)) {
                    return focus;
                }
            }
        }
        return null;
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

    /**
     * 当前RecyclerView中向下查找合适的可获取焦点的View
     */
    private View findFocusDownCandidateView(View focusView, int targetPosition) {
        if (getAdapter().getItemCount() > targetPosition && targetPosition != RecyclerView.NO_POSITION) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null) {
                return null;
            }
            View result = layoutManager.findViewByPosition(targetPosition);
            if (result != null && result.getTop() == focusView.getTop()) {//不能找在同一排的View
                return findFocusDownCandidateView(focusView, targetPosition + 1);
            }
            return result;

        }
        return null;
    }

    private View findFirstFocusAbleView(View targetFocusView) {
        if (viewCanFocus(targetFocusView)) return targetFocusView;
        if (targetFocusView instanceof ViewGroup) {
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
        if (targetFocusView instanceof ViewGroup) {
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isFocusScroll) {
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN: {
                    focusSearchDirect = FOCUS_DOWN;
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_UP: {
                    focusSearchDirect = FOCUS_UP;
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    focusSearchDirect = FOCUS_LEFT;
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    focusSearchDirect = FOCUS_RIGHT;
                    break;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        mDynamicFocusUtils.requestChildFocus(child, focused);
        getFocusRect(child, focused);
        focusViewAllVisible = isFocusViewAllVisible();
        if (DEBUG) {
            Log.d(TAG, "requestChildFocus focusViewAllVisible :: " + focusViewAllVisible);
        }
        super.requestChildFocus(child, focused);
        isFocusScroll = true;
        scrollByMode(child, focused);
        isFocusScroll = false;
    }

    private boolean isFocusViewAllVisible() {
        if (DEBUG) {
            Log.d(TAG, "isFocusViewAllVisible mTempRect params is top:" + mTempRect.top + " left:" + mTempRect.left + " right:" + mTempRect.right + " bottom:" + mTempRect.bottom + " height :: " + getHeight() + "  width :: " + getWidth() + "  focusSearchDirect:: " + focusSearchDirect);
        }
        switch (focusSearchDirect) {
            case FOCUS_LEFT:
            case FOCUS_UP: {
                horizontalViewAllVisible = mTempRect.left >= 0;
                verticalViewAllVisible = mTempRect.top >= 0;
                return horizontalViewAllVisible && verticalViewAllVisible;
            }
            case FOCUS_RIGHT:
            case FOCUS_DOWN: {
                horizontalViewAllVisible = mTempRect.right <= getWidth();
                verticalViewAllVisible = mTempRect.bottom <= getHeight();
                return horizontalViewAllVisible && verticalViewAllVisible;
            }
        }
        return true;
    }

    private boolean scrollByMode(View child, View focused) {
        boolean handle = false;
        getFocusRect(child, focused);
        if (DEBUG) {
            Log.d(TAG, "mTempRect params is top:" + mTempRect.top + " left:" + mTempRect.left + " right:" + mTempRect.right + " bottom:" + mTempRect.bottom);
        }
        if (mHandleFocusScroll != null) {
            Boolean b = ReflectUtils.reflectPrivateField(this, getClass().getSuperclass(), "mFirstLayoutComplete");
            b = b == null ? false : b;
            handle = mHandleFocusScroll.handleFocusScroll(this, child, mTempRect, true, b);
        }
        if (handle) {
            return true;
        }

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            if (DEBUG) {
                Log.e(TAG, "not handle scroll by the layoutManager: layoutManager is null");
            }
            return false;
        }
        switch (mScrollMode) {
            case SCROLL_ALIGN: {
                if (layoutManager instanceof LinearLayoutManager) {
                    int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                    if (orientation == LinearLayoutManager.VERTICAL) {
                        float baseLine = getHeight() * mScrollAlignOffset;
                        float temp = mTempRect.top + mTempRect.height() * mScrollAlignOffset;
                        float offset = temp - baseLine;
                        if (DEBUG) {
                            Log.d(TAG, "scroll vertical offset is :" + offset);
                        }
                        smoothScrollBy(0, (int) offset);
                    } else {
                        float baseLine = getWidth() * mScrollAlignOffset;
                        float temp = mTempRect.left + mTempRect.width() * mScrollAlignOffset;
                        float offset = temp - baseLine;
                        if (DEBUG) {
                            Log.d(TAG, "scroll HORIZONTAL offset is :" + offset);
                        }
                        smoothScrollBy((int) offset, 0);
                    }
                    handle = true;
                } else {
                    if (DEBUG) {
                        Log.e(TAG, "not handle scroll by the layoutManager: " + layoutManager.getClass().getSimpleName());
                    }
                    handle = false;
                }
                break;
            }
            case SCROLL_BY_PAGE: {
                handle = scrollByPage();
                break;
            }
            case SCROLL_FOLLOW:{
                requestFixFocusPosition(focused);
                handle = true;
                break;
            }

        }
        return handle;
    }

    private void requestFixFocusPosition(View focused){
        ViewParent parent = focused.getParent();
        View child = focused;
        while (parent != null){
            if(parent instanceof LibTvRecyclerView){
                int position = getAdapterPositionByView((LibTvRecyclerView) parent,focused);
                LayoutManager layoutManager = ((LibTvRecyclerView) parent).getLayoutManager();
                if(layoutManager instanceof GridLayoutManager){
                    int count = ((GridLayoutManager) layoutManager).getSpanCount();
                    if(count > position){
                        ((LibTvRecyclerView) parent).smoothScrollBy(child.getLeft(),child.getTop());
                    }else {
                        break;
                    }
                }else if(layoutManager instanceof LinearLayoutManager){
                    if(position == 0 ){
                        ((LibTvRecyclerView) parent).smoothScrollBy(child.getLeft(),child.getTop());
                    }else {
                        break;
                    }
                }
            }
            if(parent instanceof View){
                child = (View) parent;
            }
            parent = parent.getParent();
        }
    }

    private boolean scrollByPage() {
        if (DEBUG) {
            Log.d(TAG, "scrollByPage :: " + focusViewAllVisible);
        }
        if (focusViewAllVisible) {
            return true;
        }
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (focusSearchDirect == FOCUS_DOWN || focusSearchDirect == FOCUS_RIGHT) {
                smoothScrollBy(horizontalViewAllVisible ? 0 : mTempRect.left, verticalViewAllVisible ? 0 : mTempRect.top);
            } else if (focusSearchDirect == FOCUS_UP || focusSearchDirect == FOCUS_LEFT) {
                smoothScrollBy(horizontalViewAllVisible ? 0 : mTempRect.right - getWidth(), verticalViewAllVisible ? 0 : mTempRect.bottom - getHeight());
            }
        }
        return true;
    }

    private void getFocusRect(View child, View focused) {
        View rectView = (focused != null) ? focused : child;
        mTempRect.set(0, 0, rectView.getWidth(), rectView.getHeight());
        // get item decor offsets w/o refreshing. If they are invalid, there will be another
        // layout pass to fix them, then it is LayoutManager's responsibility to keep focused
        // View in viewport.
        final ViewGroup.LayoutParams focusedLayoutParams = rectView.getLayoutParams();
        if (focusedLayoutParams instanceof LayoutParams) {
            // if focused child has item decors, use them. Otherwise, ignore.
            final LayoutParams lp = (LayoutParams) focusedLayoutParams;
            final Boolean insetsDirty = ReflectUtils.reflectPrivateField(lp, LayoutParams.class, "mInsetsDirty");
            final Rect insets = ReflectUtils.reflectPrivateField(lp, LayoutParams.class, "mDecorInsets");
            if (insetsDirty != null && insets != null) {
                if (!insetsDirty) {
                    mTempRect.left -= insets.left;
                    mTempRect.right += insets.right;
                    mTempRect.top -= insets.top;
                    mTempRect.bottom += insets.bottom;
                    if (DEBUG) {
                        Log.d(TAG, "mTempRect params is reset");
                    }
                }
            }
        }
        if (DEBUG) {
            Log.d(TAG, "mTempRect params is top:" + mTempRect.top + " left:" + mTempRect.left + " right:" + mTempRect.right + " bottom:" + mTempRect.bottom);
        }
        if (focused != null) {
            //计算出焦点View相对于自身的偏差
            offsetDescendantRectToMyCoords(focused, mTempRect);
            if (DEBUG) {
                Log.d(TAG, "mTempRect params is top:" + mTempRect.top + " left:" + mTempRect.left + " right:" + mTempRect.right + " bottom:" + mTempRect.bottom);
            }
//            offsetRectIntoDescendantCoords(child, mTempRect);
        }
    }

    private int getAdapterPositionByView(LibTvRecyclerView root,View view) {
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

    public interface HandleFocusScroll {
        boolean handleFocusScroll(LibTvRecyclerView parent, @NonNull View child, @NonNull Rect rect, boolean immediate, boolean focusedChildVisible);
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == LibTvRecyclerView.this && child instanceof ICheckView) {
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
            if (parent == LibTvRecyclerView.this && child instanceof ICheckView) {
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
            if(mCheckedView != checkable && mCheckedView != null){
                setCheckedStateForView(mCheckedView, false);
            }
            if(isChecked && checkable instanceof View){
                mCheckedPosition = getAdapterPositionByView(LibTvRecyclerView.this,(View) checkable);
            }
            mProtectFromCheckedChange = false;

            setCheckedView(checkable);
        }
    }

    private void setCheckedView(Checkable checkable) {
        mCheckedView = checkable;
    }

    private void setCheckedStateForView(Checkable checkedView, boolean checked) {
        if(checkedView != null){
            checkedView.setChecked(checked);
        }
    }


    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if(!mDynamicFocusUtils.addFocusables(views,direction,focusableMode)){
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    public void clearFocus() {
        mDynamicFocusUtils.clearFocus();
        super.clearFocus();
    }

    @Override
    public void clearChildFocus(View child) {
        mDynamicFocusUtils.clearChildFocus();
        super.clearChildFocus(child);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        mDynamicFocusUtils.requestFocus(direction, previouslyFocusedRect);
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode) {
        return mDynamicFocusUtils.dispatchAddFocusables(views, direction, focusableMode);
    }
}
