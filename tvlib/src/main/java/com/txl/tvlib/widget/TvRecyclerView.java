package com.txl.tvlib.widget;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.txl.tvlib.R;

import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;

/**
 * 从AndroidTv项目引入
 * */
public class TvRecyclerView extends RecyclerView {

    public static final String TAG = "TvRecyclerView";
    static boolean DEBUG = false;
    private static final float DEFAULT_SELECT_SCALE = 1.04f;
    private static final int NORMAL_SCROLL_COMPENSATION_VAL = 45;

    private static final int SCROLL_ALIGN = 0;
    private static final int SCROLL_FOLLOW = 1;
    private static final int SCROLL_NORMAL = 2;

    private final static int PREV_ITEM = 0;
    private final static int NEXT_ITEM = 1;
    private final static int PREV_ROW = 2;
    private final static int NEXT_ROW = 3;

    private static final int DEFAULT_DIRECTION = -1;

    private FocusBorderView mFocusBorderView;

    public void setOnItemFocusChangeListener(OnItemFocusChangeListener onItemFocusChangeListener) {
        this.onItemFocusChangeListener = onItemFocusChangeListener;
    }

    private OnItemFocusChangeListener onItemFocusChangeListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    private Drawable mDrawableFocus;
    public boolean mIsDrawFocusMoveAnim;
    private float mSelectedScaleValue;
    private float mFocusMoveAnimScale;

    private int mSelectedPosition;
    private View mNextFocused;
    private boolean mInLayout;

    private int mFocusFrameLeft;
    private int mFocusFrameTop;
    private int mFocusFrameRight;
    private int mFocusFrameBottom;

    private boolean mReceivedInvokeKeyDown;
    protected View mSelectedItem;
    private OnItemStateListener mItemStateListener;
    private onScrollStateListener mScrollListener;
    private Scroller mScrollerFocusMoveAnim;
    private PendingMoveSmoothScroller mPendingMoveSmoothScroller;
    private int mScrollMode = SCROLL_ALIGN;

    private boolean mIsAutoProcessFocus;
    private int mOrientation;
    private int mDirection;
    private boolean mIsSetItemSelected = false;
    private int mNumRows = 1;
    private boolean mIsNeedMoved = false;
    int mLayerType = View.LAYER_TYPE_SOFTWARE;
    boolean focusOutKeepDrawable=false;


    public TvRecyclerView(Context context) {
        this(context, null);
    }

    public TvRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setAttributeSet(attrs);
        // 解决问题: 当需要选择的item没有显示在屏幕上, 需要滑动让item显示出来.
        // 这时需要调整item的位置, 并且item获取焦点
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsSetItemSelected) {
                    if (DEBUG) {
                        Log.d(TAG, "onScrolled: mSelectedPosition=" + mSelectedPosition);
                    }
                    mIsSetItemSelected = false;
                    int firstVisiblePos = getFirstVisiblePosition();
                    mNextFocused = getChildAt(mSelectedPosition - firstVisiblePos);
                    if (mNextFocused != null) {
                        if (DEBUG) {
                            Log.d(TAG, "onScrolled: start adjust scroll distance");
                        }
                        if (mIsAutoProcessFocus) {
                            scrollToViewToCenter(mNextFocused);
                        } else {
                            mIsNeedMoved = true;
                            mNextFocused.requestFocus();
                        }
                    }
                }
            }
        });
    }

    private void init() {
        mScrollerFocusMoveAnim = new Scroller(getContext());
        mIsDrawFocusMoveAnim = false;
        mReceivedInvokeKeyDown = false;
        mSelectedPosition = 0;
        mNextFocused = null;
        mInLayout = false;
        mSelectedScaleValue = DEFAULT_SELECT_SCALE;
        mIsAutoProcessFocus = true;

        int p=getContext().getResources().getDimensionPixelOffset(R.dimen.focuse_border_padding);
        mFocusFrameLeft = p;
        mFocusFrameTop = p;
        mFocusFrameRight = p;
        mFocusFrameBottom = p;
        mOrientation = HORIZONTAL;
    }

    private void setAttributeSet(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.TvRecyclerView);
            mScrollMode = typeArray.getInteger(R.styleable.TvRecyclerView_scrollMode, SCROLL_ALIGN);
            focusOutKeepDrawable=typeArray.getBoolean(R.styleable.TvRecyclerView_focusOutKeepDrawable,false);
            final Drawable drawable = typeArray.getDrawable(R.styleable.TvRecyclerView_focusDrawable);
            if (drawable != null) {
                setFocusDrawable(drawable);
            }

            mSelectedScaleValue = typeArray.getFloat(R.styleable.TvRecyclerView_focusScale, DEFAULT_SELECT_SCALE);
            mIsAutoProcessFocus = typeArray.getBoolean(R.styleable.TvRecyclerView_isAutoProcessFocus, true);
            if (!mIsAutoProcessFocus) {
                mSelectedScaleValue = 1.0f;
                setChildrenDrawingOrderEnabled(true);
            }
            typeArray.recycle();
        }
        if (mIsAutoProcessFocus) {
            // set TvRecyclerView process Focus
            setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        }
    }

    private void addFlyBorderView(Context context) {
        if (mFocusBorderView == null) {
            mFocusBorderView = new FocusBorderView(context);
            searchTintContextHostActivity(context).getWindow().addContentView(mFocusBorderView,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mFocusBorderView.setSelectPadding(mFocusFrameLeft, mFocusFrameTop,
                    mFocusFrameRight, mFocusFrameBottom);
        }
    }
    public static Activity searchTintContextHostActivity(Context context)
    {
        Context hostContext=context;
        while(hostContext instanceof ContextWrapper)
        {
            if(hostContext instanceof Activity)
                return (Activity) hostContext;
            hostContext=((ContextWrapper)context).getBaseContext();
        }
        return  null;
    }
    public static void openDEBUG() {
        DEBUG = true;
    }

    public int getFirstVisiblePosition() {
        int firstVisiblePos = -1;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisiblePos = ((LinearLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            } else if (layoutManager instanceof ModuleLayoutManager) {
                firstVisiblePos = ((ModuleLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            }
        }
        return firstVisiblePos;
    }

    public int getLastVisiblePosition() {
        int lastVisiblePos = -1;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisiblePos = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
            } else if (layoutManager instanceof ModuleLayoutManager) {
                lastVisiblePos = ((ModuleLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
            }
        }
        return lastVisiblePos;
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            mOrientation = manager.getOrientation();
            mNumRows = manager.getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            mOrientation = ((LinearLayoutManager)layoutManager).getOrientation();
            mNumRows = 1;
        } else if (layoutManager instanceof ModuleLayoutManager) {
            mOrientation = ((ModuleLayoutManager)layoutManager).getOrientation();
        }
        Log.i(TAG, "setLayoutManager: orientation==" + mOrientation);
        super.setLayoutManager(layoutManager);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        mDirection = direction;
        return super.focusSearch(focused, direction);
    }

    /**
     * note: if you set the property of isAutoProcessFocus is false, the listener will be invalid
     * @param listener itemStateListener
     */
    public void setOnItemStateListener(OnItemStateListener listener) {
        mItemStateListener = listener;
    }

    public void setOnScrollStateListener(onScrollStateListener listener) {
        mScrollListener = listener;
    }

    public void setSelectedScale(float scale) {
        if (scale >= 1.0f) {
            mSelectedScaleValue = scale;
        }
    }

    public void setIsAutoProcessFocus(boolean isAuto) {
        mIsAutoProcessFocus = isAuto;
        if (!isAuto) {
            mSelectedScaleValue = 1.0f;
            setChildrenDrawingOrderEnabled(true);
        } else {
            if (mSelectedScaleValue == 1.0f) {
                mSelectedScaleValue = DEFAULT_SELECT_SCALE;
            }
        }
    }

    public void setFocusDrawable(Drawable focusDrawable) {
        mDrawableFocus = focusDrawable;
    }

    public void setScrollMode(int mode) {
        mScrollMode = mode;
    }

    /**
     * When call this method, you must ensure that the location of the view has been inflate
     * @param position selected item position
     */
    public void setItemSelected(int position) {
        if (mSelectedPosition == position) {
            return;
        }
        if (position >= getAdapter().getItemCount()) {
            position = getAdapter().getItemCount() - 1;
        }
        int firstPos = getChildAdapterPosition(getChildAt(0));
        int lastPos = getChildAdapterPosition(getChildAt(getChildCount() - 1));
        if (DEBUG) {
            Log.d(TAG, "setItemSelected: first=" + firstPos + "=last=" + lastPos
                    + "=pos=" + position);
        }
        if (position >= firstPos && position <= lastPos) {
            mNextFocused = getChildAt(position - firstPos);
            if (mIsAutoProcessFocus && !mIsDrawFocusMoveAnim) {
                scrollToView(mNextFocused, true);
            } else {
                mNextFocused.requestFocus();
            }
        } else {
            mIsSetItemSelected = true;
            mSelectedPosition = position;
            scrollToPosition(position);
        }
    }

    @Override
    public boolean isInTouchMode() {
        boolean result = super.isInTouchMode();
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !result);
        } else {
            return result;
        }
    }

    /**
     * fix issue: not have focus box when change focus
     * @param child child view
     * @param focused the focused view
     */
    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (mSelectedPosition < 0) {
            mSelectedPosition = getAdapterPositionByView(child);
        }
        if (mIsAutoProcessFocus) {
            requestFocus();
        } else {
            int position = getAdapterPositionByView(focused);
            if ((mSelectedPosition != position || mIsNeedMoved) && !mIsSetItemSelected) {
                mSelectedPosition = position;
                mSelectedItem = focused;
                int distance = getNeedScrollDistance(focused);
                if (mIsNeedMoved && mScrollMode != SCROLL_FOLLOW) {
                    distance = getFollowDistance(focused);
                }
                mIsNeedMoved = false;
                if (distance != 0) {
                    if (DEBUG) {
                        Log.d(TAG, "requestChildFocus: scroll distance=" + distance);
                    }
                    smoothScrollView(distance);
                }
            }
        }
        if (DEBUG) {
            Log.d(TAG, "requestChildFocus: SelectPos=" + mSelectedPosition);
        }
    }

    private int getAdapterPositionByView(View view) {
        if (view == null) {
            return NO_POSITION;
        }
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params == null || params.isItemRemoved()) {
            // when item is removed, the position value can be any value.
            return NO_POSITION;
        }
        return params.getViewAdapterPosition();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mIsAutoProcessFocus) {
            if (DEBUG) {
                Log.d(TAG, "onFinishInflate: add fly border view");
            }
            mLayerType = getLayerType();
            if(!isInEditMode())
                addFlyBorderView(getContext());
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (DEBUG) {
            Log.d(TAG, "onFocusChanged: gainFocus==" + gainFocus);
        }
        if (mItemStateListener != null) {
            if (mSelectedItem == null) {
                mSelectedItem = getChildAt(mSelectedPosition - getFirstVisiblePosition());
            }
            mItemStateListener.onItemViewFocusChanged(gainFocus, mSelectedItem,
                    mSelectedPosition);
        }
        if (mFocusBorderView == null) {
            return;
        }
        mFocusBorderView.setTvRecyclerView(this);
        if (gainFocus) {
            mFocusBorderView.bringToFront();
        }
        if (mSelectedItem != null) {
            if (gainFocus) {
                mSelectedItem.setSelected(true);
            } else {
                mSelectedItem.setSelected(false);
            }
            if (gainFocus && !mInLayout) {
                if(mSelectedItem.isFocusable())
                    mFocusBorderView.startFocusAnim();
                else if(!focusOutKeepDrawable)
                {
                    removeFocusDraw();
                }

            }
        }
        if (!gainFocus&&!focusOutKeepDrawable) {
            mFocusBorderView.dismissGetFocus();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility==View.GONE)
        {
            removeFocusDraw();
        }
        super.setVisibility(visibility);

    }



    public void removeFocusDraw()
    {
        mFocusBorderView.dismissDraw();
        mFocusBorderView.dismissGetFocus();
        mFocusBorderView.invalidate();
    }

    public void invalidateFocusDraw()
    {
        mFocusBorderView.invalidate();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int focusIndex = indexOfChild(mSelectedItem);
        if (focusIndex < 0) {
            return i;
        }
        if (i < focusIndex) {
            return i;
        } else if (i < childCount - 1) {
            return focusIndex + childCount - 1 - i;
        } else {
            return focusIndex;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInLayout = true;
        super.onLayout(changed, l, t, r, b);

        // fix issue: when start anim the FocusView location error in AutoProcessFocus mode
        Adapter adapter = getAdapter();
        if (adapter != null && mSelectedPosition >= adapter.getItemCount()) {
            mSelectedPosition = adapter.getItemCount() - 1;
        }
        int selectPos = mSelectedPosition - getFirstVisiblePosition();
        selectPos = selectPos < 0 ? 0 : selectPos;
        mSelectedItem = getChildAt(selectPos);
        mInLayout = false;
        if (DEBUG) {
            Log.d(TAG, "onLayout: selectPos=" + selectPos + "=SelectedItem=" + mSelectedItem);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mFocusBorderView != null && mFocusBorderView.getTvRecyclerView() != null) {
            if (DEBUG) {
                Log.d(TAG, "dispatchDraw: Border view invalidate.");
            }
            mFocusBorderView.invalidate();
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("super_data");
        super.onRestoreInstanceState(superData);
        setItemSelected(bundle.getInt("select_pos", 0));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable("super_data", superData);
        bundle.putInt("select_pos", mSelectedPosition);
        return bundle;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            if (mSelectedItem == null) {
                mSelectedItem = getChildAt(mSelectedPosition);
            }
            try {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mNextFocused = FocusFinder.getInstance().findNextFocus(this, mSelectedItem, View.FOCUS_LEFT);
                } else if (keyCode == KEYCODE_DPAD_RIGHT) {
                    mNextFocused = FocusFinder.getInstance().findNextFocus(this, mSelectedItem, View.FOCUS_RIGHT);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mNextFocused = FocusFinder.getInstance().findNextFocus(this, mSelectedItem, View.FOCUS_UP);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mNextFocused = FocusFinder.getInstance().findNextFocus(this, mSelectedItem, View.FOCUS_DOWN);
                }
            } catch (Exception e) {
                Log.e(TAG, "dispatchKeyEvent: get next focus item error: " + e.getMessage());
                mNextFocused = null;
            }
            if (DEBUG) {
                Log.d(TAG, "dispatchKeyEvent: mNextFocused=" + mNextFocused +
                        "=nextPos=" + getChildAdapterPosition(mNextFocused));
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean isFindNextFocusView(int keyCode) {
        if (!mIsAutoProcessFocus) {
            return false;
        }
        int movement = getMovement(keyCode);
        if (movement == NEXT_ITEM) {
            if (!hasCreatedLastItem()) {
                processPendingMovement(true);
                return true;
            }
        } else if (movement == PREV_ITEM) {
            if (!hasCreatedFirstItem()) {
                processPendingMovement(false);
                return true;
            }
        }
        return false;
    }

    private void processPendingMovement(boolean forward) {
        if (forward ? hasCreatedLastItem() : hasCreatedFirstItem()) {
            return;
        }
        if (mPendingMoveSmoothScroller == null) {
            // Stop existing scroller and create a new PendingMoveSmoothScroller.
            stopScroll();
            PendingMoveSmoothScroller linearSmoothScroller = new PendingMoveSmoothScroller(
                    getContext(), forward ? 1 : -1);
            getLayoutManager().startSmoothScroll(linearSmoothScroller);
            if (linearSmoothScroller.isRunning()) {
                mPendingMoveSmoothScroller = linearSmoothScroller;
            }
        } else {
            if (forward) {
                mPendingMoveSmoothScroller.increasePendingMoves();
            } else {
                mPendingMoveSmoothScroller.decreasePendingMoves();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (processMoves(keyCode)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                mReceivedInvokeKeyDown = true;
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER: {
                if (mReceivedInvokeKeyDown) {
                    if ((getAdapter() != null) && (mSelectedItem != null)) {
                        if (mItemStateListener != null) {
                            if (mFocusBorderView != null){
                                mFocusBorderView.startClickAnim();
                            }
                            mItemStateListener.onItemViewClick(mSelectedItem, mSelectedPosition);
                        }
                        else if(onItemClickListener!=null)
                            onItemClickListener.onItemViewClick(mSelectedItem, mSelectedPosition);
                    }
                    mReceivedInvokeKeyDown = false;
                    if (mIsAutoProcessFocus) {
                        return true;
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void computeScroll() {
        if (mScrollerFocusMoveAnim.computeScrollOffset()) {
            if (mIsDrawFocusMoveAnim) {
                mFocusMoveAnimScale = ((float) (mScrollerFocusMoveAnim.getCurrX())) / 100;
            }
            postInvalidate();
        } else {
            if (mIsDrawFocusMoveAnim) {
                mIsDrawFocusMoveAnim = false;
                updateSelectPositionInLayout(mNextFocused);
                setLayerType(mLayerType, null);
                postInvalidate();
                if (mItemStateListener != null) {
                    mItemStateListener.onItemViewFocusChanged(true, mSelectedItem,
                            mSelectedPosition);
                }
                else if(onItemFocusChangeListener!=null)
                {
                    onItemFocusChangeListener.onItemViewFocusChanged(true, mSelectedItem,
                            mSelectedPosition);
                }
            }
        }
    }

    private void updateSelectPositionInLayout(View nextView) {
        if (nextView != null) {
            mSelectedItem = nextView;
            mSelectedPosition = getChildAdapterPosition(mSelectedItem);
        }
    }

    @Override
    public int getChildAdapterPosition(@NonNull View child) {
        try {
            return super.getChildAdapterPosition(child);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean processMoves(int keyCode) {
        if (mNextFocused == null) {
            // fix issue: When the childView just fills the display area, it can't slide
            if (isFindNextFocusView(keyCode)) {
                return true;
            } else if (mIsAutoProcessFocus) { // scroll start or end
                notifyScrollState(keyCode);
                mIsDrawFocusMoveAnim = false;
            }
            if (DEBUG) {
                Log.d(TAG, "processMoves: error");
            }
            return false;
        } else {
            if (mIsDrawFocusMoveAnim) {
                updateSelectPositionInLayout(mNextFocused);
            }
            scrollToView(mNextFocused, true);
            return true;
        }
    }

    private void scrollToViewToCenter(View view) {
        int scrollDistance = getFollowDistance(view);
        if (DEBUG) {
            Log.d(TAG, "scrollToViewToCenter: scrollDistance==" + scrollDistance);
        }
        if (scrollDistance != 0) {
            smoothScrollView(scrollDistance);
        }

        startFocusMoveAnim();
    }

    private void scrollToView(View view, boolean smooth) {
        int scrollDistance = getNeedScrollDistance(view);
        if (DEBUG) {
            Log.d(TAG, "scrollToView: scrollDistance==" + scrollDistance);
        }
        if (scrollDistance != 0) {
            if (smooth) {
                smoothScrollView(scrollDistance);
            } else {
                scrollToView(scrollDistance);
            }
        }
        startFocusMoveAnim();
    }

    private void notifyScrollState(int keyCode) {
        if (mScrollListener != null) {
            if (mOrientation == HORIZONTAL) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mScrollListener.onScrollEnd(mSelectedItem);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mScrollListener.onScrollStart(mSelectedItem);
                }
            } else {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mScrollListener.onScrollEnd(mSelectedItem);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mScrollListener.onScrollStart(mSelectedItem);
                }
            }
        }
    }

    private int getNeedScrollDistance(View focusView) {
        switch (mScrollMode) {
            case SCROLL_ALIGN:
            default:
                return getAlignDistance(focusView);
            case SCROLL_FOLLOW:
                return getFollowDistance(focusView);
            case SCROLL_NORMAL:
                return getNormalDistance(focusView);
        }
    }

    private int getAlignDistance(View view) {
        int scrollDistance = 0;
        boolean isVisible = isVisibleChild(view);
        boolean isHalfVisible = isHalfVisibleChild(view);
        if (isHalfVisible || !isVisible) {
            scrollDistance = getScrollPrimary(view);
        }
        return scrollDistance;
    }

    private int getFollowDistance(View view) {
        int scrollDistance = 0;
        boolean isOver = isOverHalfScreen(view);
        if (isOver) {
            scrollDistance = getScrollPrimary(view);
        }
        return scrollDistance;
    }

    private int getNormalDistance(View view) {
        if (!mIsAutoProcessFocus) {
            return 0;
        }
        int scrollDistance = 0;
        boolean isVisible = isVisibleChild(view);
        boolean isHalfVisible = isHalfVisibleChild(view);
        if (isHalfVisible || !isVisible) {
            scrollDistance = getNormalScrollDistance(view);
        }
        return scrollDistance;
    }

    private int getClientSize() {
        if (mOrientation == HORIZONTAL) {
            return getWidth() - getPaddingLeft() - getPaddingRight();
        } else {
            return getHeight() - getPaddingTop() - getPaddingBottom();
        }
    }

    private int getScrollPrimary(View view) {
        int distance=0;
        if(view==null)
            return distance;
        if (mOrientation == HORIZONTAL) {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_UP || mDirection == View.FOCUS_DOWN) {
                    return 0;
                }
            }
            distance = view.getLeft() +
                    view.getWidth() / 2 - getClientSize() / 2;
        } else {
            if (mDirection != DEFAULT_DIRECTION) {
                if (mDirection == View.FOCUS_LEFT || mDirection == View.FOCUS_RIGHT) {
                    return 0;
                }
            }
            distance = view.getTop() +
                    view.getHeight() / 2 - getClientSize() / 2;
        }
        return distance;
    }

    private void smoothScrollView(int scrollDistance) {
        if (mOrientation == HORIZONTAL) {
            smoothScrollBy(scrollDistance, 0);
        } else {
            smoothScrollBy(0, scrollDistance);
        }
    }

    private void scrollToView(int scrollDistance) {
        if (mOrientation == HORIZONTAL) {
            scrollBy(scrollDistance, 0);
        } else {
            scrollBy(0, scrollDistance);
        }
    }

    private int getNormalScrollDistance(View view) {
        int distance = 0;

        int viewMin = getDecoratedStart(view);
        int viewMax = getDecoratedEnd(view);

        View firstView = null;
        View lastView = null;
        int paddingLow = getPaddingLow();
        int clientSize = getClientSize();
        int maxValue = paddingLow + clientSize - NORMAL_SCROLL_COMPENSATION_VAL;
        if (viewMin < paddingLow) {
            firstView = view;
        } else if (viewMax > maxValue) {
            lastView = view;
        }
        if (firstView != null) {
            distance = getDecoratedStart(firstView) - paddingLow - NORMAL_SCROLL_COMPENSATION_VAL;
        } else if (lastView != null) {
            distance = getDecoratedEnd(lastView) - maxValue;
        }

        return distance;
    }

    private int getPaddingLow() {
        if (mOrientation == HORIZONTAL) {
            return getPaddingLeft();
        } else {
            return getPaddingTop();
        }
    }

    private int getDecoratedStart(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        if (mOrientation == VERTICAL) {
            return getLayoutManager().getDecoratedTop(view) - params.topMargin;
        } else {
            return getLayoutManager().getDecoratedLeft(view) - params.leftMargin;
        }
    }

    private int getDecoratedEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        if (mOrientation == VERTICAL) {
            return getLayoutManager().getDecoratedBottom(view) + params.bottomMargin;
        } else {
            return getLayoutManager().getDecoratedRight(view) + params.rightMargin;
        }
    }

    private boolean hasCreatedLastItem() {
        int count = getLayoutManager().getItemCount();
        return count == 0 || findViewHolderForAdapterPosition(count - 1) != null;
    }

    private boolean hasCreatedFirstItem() {
        int count = getLayoutManager().getItemCount();
        return count == 0 || findViewHolderForAdapterPosition(0) != null;
    }

    private int getMovement(int keyCode) {
        int movement = View.FOCUS_LEFT;

        if (mOrientation == HORIZONTAL) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    movement = PREV_ITEM;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    movement = NEXT_ITEM;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    movement = PREV_ROW;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    movement = NEXT_ROW;
                    break;
            }
        } else if (mOrientation == VERTICAL) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    movement = PREV_ROW;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    movement = NEXT_ROW;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    movement = PREV_ITEM;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    movement = NEXT_ITEM;
                    break;
            }
        }

        return movement;
    }

    private boolean isHalfVisibleChild(View child) {
        if (child != null) {
            Rect ret = new Rect();
            boolean isVisible = child.getLocalVisibleRect(ret);
            if (mOrientation == HORIZONTAL) {
                return isVisible && (ret.width() < child.getWidth());
            } else {
                return isVisible && (ret.height() < child.getHeight());
            }
        }
        return false;
    }

    private boolean isVisibleChild(View child) {
        if (child != null) {
            Rect ret = new Rect();
            return child.getLocalVisibleRect(ret);
        }
        return false;
    }

    private boolean isOverHalfScreen(View child) {
        Rect ret = new Rect();
        child.getGlobalVisibleRect(ret);
        int size = getClientSize();
        if (mOrientation == HORIZONTAL) {
            if (ret.right > size / 2 || ret.left < size / 2) {
                return true;
            }
        } else {
            if (ret.top < size / 2 || ret.bottom > size / 2) {
                return true;
            }
        }
        return false;
    }

    private void startFocusMoveAnim() {
        mScrollerFocusMoveAnim.abortAnimation();
        if (mFocusBorderView != null) {
            mFocusBorderView.dismissDraw();
            setLayerType(View.LAYER_TYPE_NONE, null);
            mIsDrawFocusMoveAnim = true;
            if (mItemStateListener != null) {
                mItemStateListener.onItemViewFocusChanged(false, mSelectedItem,
                        mSelectedPosition);
            }
            else if(onItemFocusChangeListener!=null)
            {
                onItemFocusChangeListener.onItemViewFocusChanged(false, mSelectedItem,
                        mSelectedPosition);
            }
            mScrollerFocusMoveAnim.startScroll(0, 0, 100, 100, 200);
            invalidate();
        } else {
            Log.d(TAG, "startFocusMoveAnim: mFocusBorderView is null");
        }
    }

    /**
     * When the TvRecyclerView width is determined, the returned position is correct
     * @return selected view position
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    View getSelectedView() {
        return mSelectedItem;
    }

    public float getSelectedScaleValue() {
        return mSelectedScaleValue;
    }

    public Drawable getDrawableFocus() {
        return mDrawableFocus;
    }

    public View getNextFocusView() {
        return mNextFocused;
    }

    public float getFocusMoveAnimScale() {
        return mFocusMoveAnimScale;
    }

    public void setSelectPadding(int left, int top, int right, int bottom) {
        mFocusFrameLeft = left;
        mFocusFrameTop = top;
        mFocusFrameRight = right;
        mFocusFrameBottom = bottom;

        if (mFocusBorderView != null) {
            mFocusBorderView.setSelectPadding(mFocusFrameLeft, mFocusFrameTop,
                    mFocusFrameRight, mFocusFrameBottom);
        }
    }


    /**
     * The SmoothScroller that remembers pending DPAD keys and consume pending keys
     * during scroll.
     */
    private final class PendingMoveSmoothScroller extends LinearSmoothScroller {
        private int mPendingMoves;
        private int mMaxPendingMoves = 10;

        PendingMoveSmoothScroller(Context context, int initialPendingMoves) {
            super(context);
            mPendingMoves = initialPendingMoves;
            int targetPos = mSelectedPosition;
            if (mPendingMoves > 0) {
                targetPos += mNumRows;
                int maxPos = getAdapter().getItemCount() - 1;
                if (targetPos > maxPos) {
                    targetPos = maxPos;
                }
            } else {
                targetPos -= mNumRows;
                if (targetPos < 0) {
                    targetPos = 0;
                }
            }
            setTargetPosition(targetPos);
        }

        void increasePendingMoves() {
            if (mPendingMoves < mMaxPendingMoves) {
                mPendingMoves++;
            }
        }

        void decreasePendingMoves() {
            if (mPendingMoves > -mMaxPendingMoves) {
                mPendingMoves--;
            }
        }

        @Override
        protected void updateActionForInterimTarget(SmoothScroller.Action action) {
            if (mPendingMoves == 0) {
                return;
            }
            super.updateActionForInterimTarget(action);
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            if (mPendingMoves == 0) {
                return null;
            }
            int direction = mPendingMoves < 0 ? -1 : 1;
            if (mOrientation == HORIZONTAL) {
                return new PointF(direction, 0);
            } else {
                return new PointF(0, direction);
            }
        }

        @Override
        protected void onStop() {
            // if we hit wall, need clear the remaining pending moves.
            mPendingMoves = 0;
            mPendingMoveSmoothScroller = null;
            int targetPosition = getTargetPosition();
            View targetView = findViewByPosition(targetPosition);
            Log.i(TAG, "PendingMoveSmoothScroller onStop: targetPos=" + targetPosition
                    + "==targetView=" + targetView);
            if (targetView == null) {
                super.onStop();
                return;
            }
            if (mSelectedPosition != targetPosition) {
                mSelectedPosition = targetPosition;
            }
            if (!mIsAutoProcessFocus) {
                targetView.requestFocus();
            } else {
                mNextFocused = targetView;
                scrollToView(targetView, true);
            }
            super.onStop();
        }
    }


    public interface OnItemStateListener extends OnItemClickListener,OnItemFocusChangeListener
    {
    }

    public interface OnItemFocusChangeListener
    {
        void onItemViewFocusChanged(boolean gainFocus, View view, int position);
    }

    public interface OnItemClickListener
    {
        void onItemViewClick(View view, int position);
    }

    /**
     * Only works in isAutoProcessFocus attribute is true
     */
    public interface onScrollStateListener {
        void onScrollEnd(View view);
        void onScrollStart(View view);
    }
}
