package com.txl.tvlib.widget;

import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.txl.tvlib.widget.TvRecyclerView.DEBUG;


public abstract class ModuleLayoutManager extends RecyclerView.LayoutManager implements
        RecyclerView.SmoothScroller.ScrollVectorProvider {

    private static final String TAG = "TvRecyclerView_ML";

    private static final int BASE_ITEM_DEFAULT_SIZE = 380;

    private static final int HORIZONTAL = OrientationHelper.HORIZONTAL;

    private static final int VERTICAL = OrientationHelper.VERTICAL;

    private final static int LAYOUT_START = -1;

    private final static int LAYOUT_END = 1;

    private int mOrientation;
    private final SparseArray<Rect> mItemsRect;

    private int mHorizontalOffset;
    private int mVerticalOffset;

    private final int mNumRowOrColumn;

    private int mOriItemWidth;
    private int mOriItemHeight;
    private int mTotalSize;


    // re-used variable to acquire decor insets from RecyclerView
    private final Rect mDecorInsets = new Rect();

    public ModuleLayoutManager(int rowOrColumnCount, int orientation) {
        this(rowOrColumnCount, orientation, BASE_ITEM_DEFAULT_SIZE,
                BASE_ITEM_DEFAULT_SIZE);
    }

    public ModuleLayoutManager(int rowOrColumnCount, int orientation, int baseItemWidth,
                               int baseItemHeight) {
        mOrientation = orientation;
        mOriItemWidth = baseItemWidth;
        mOriItemHeight = baseItemHeight;
        mNumRowOrColumn = rowOrColumnCount;
        mItemsRect = new SparseArray<>();
    }

    /**
     * reset default item row or column.
     * Avoid the width and height of all items in each row or column
     * more than the width and height of the parent view.
     */
    private void resetItemRowColumnSize() {
        if (mOrientation == HORIZONTAL) {
            mOriItemHeight = (getHeight() - (mNumRowOrColumn - 1) * getRowSpacing())
                             / mNumRowOrColumn;
        } else {
            mOriItemWidth = (getWidth() - (mNumRowOrColumn - 1) * getColumnSpacing())
                             / mNumRowOrColumn;
        }
        if (DEBUG) {
            Log.d(TAG, "resetItemRowColumnSize: OriItemHeight=" + mOriItemHeight
            + "=OriItemWidth=" + mOriItemWidth);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildren(recycler, state);
    }

    private void layoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }

        detachAndScrapAttachedViews(recycler);

        mHorizontalOffset = 0;
        mVerticalOffset = 0;
        mItemsRect.clear();
        resetItemRowColumnSize();
        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int itemsCount = state.getItemCount();
        Rect displayRect = getDisplayRect();
        for (int i = 0; i < itemsCount; i++) {
            View child = recycler.getViewForPosition(i);
            Rect itemRect = calculateViewSizeByPosition(child, i);
            if (!Rect.intersects(displayRect, itemRect)) {
                recycler.recycleView(child);
                break;
            }
            addView(child);
            //calculate width includes margin
            layoutDecoratedWithMargins(child,
                    itemRect.left,
                    itemRect.top,
                    itemRect.right,
                    itemRect.bottom);

            if (mOrientation == HORIZONTAL) {
                mTotalSize = itemRect.right;
            } else {
                mTotalSize = itemRect.bottom;
            }

            // Save the current Bound field data for the item view
            Rect frame = mItemsRect.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(itemRect);
            mItemsRect.put(i, frame);
            if (DEBUG) {
                Log.d(TAG, "fill: pos=" + i + "=frame=" + frame.toString());
            }
        }
    }

    private Rect calculateViewSizeByPosition(View child, int position) {
        if (position >= getItemCount()) {
            throw new IllegalArgumentException("position outside of itemCount position is "
                    + position + " itemCount is " + getItemCount());
        }

        int leftOffset;
        int topOffset;
        Rect childFrame = new Rect();

        calculateItemDecorationsForChild(child, mDecorInsets);
        measureChild(child, getItemWidth(position), getItemHeight(position));
        int itemStartPos = getItemStartIndex(position);
        int childHorizontalSpace = getDecoratedMeasurementHorizontal(child);
        int childVerticalSpace = getDecoratedMeasurementVertical(child);
        int lastPos;
        int topPos;
        if (mOrientation == HORIZONTAL) {
            lastPos = itemStartPos / mNumRowOrColumn;
            topPos = itemStartPos % mNumRowOrColumn;
        } else {
            lastPos = itemStartPos % mNumRowOrColumn;
            topPos = itemStartPos / mNumRowOrColumn;
        }

        if (lastPos == 0) {
            leftOffset = -mDecorInsets.left;
        } else {
            leftOffset = (mOriItemWidth + getChildHorizontalPadding(child)) * lastPos
                    - mDecorInsets.left;
        }

        if (topPos == 0) {
            topOffset = -mDecorInsets.top;
        } else {
            topOffset = (mOriItemHeight + getChildVerticalPadding(child)) * topPos
                    - mDecorInsets.top;
        }

        childFrame.left = leftOffset;
        childFrame.top = topOffset;
        childFrame.right = leftOffset + childHorizontalSpace;
        childFrame.bottom = topOffset + childVerticalSpace;
        return childFrame;
    }

    private Rect getDisplayRect() {
        Rect displayFrame;
        if (mOrientation == HORIZONTAL) {
            displayFrame = new Rect(mHorizontalOffset - getPaddingLeft(), 0,
                    mHorizontalOffset + getHorizontalSpace() + getPaddingRight(), getVerticalSpace());
        } else {
            displayFrame = new Rect(0, mVerticalOffset - getPaddingTop(),
                    getHorizontalSpace(), mVerticalOffset + getVerticalSpace() + getPaddingBottom());
        }
        return displayFrame;
    }

    private int findLastViewLayoutPosition() {
        int lastPos = getChildCount();
        if (lastPos > 0) {
            lastPos = getPosition(getChildAt(lastPos - 1));
        }
        return lastPos;
    }

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state, int dt) {
        if (state.isPreLayout()) {
            return;
        }
        recycleByScrollState(recycler, dt);

        if (dt >= 0) {
            int beginPos = findLastViewLayoutPosition() + 1;
            fillRequireItems(recycler, beginPos);
        } else {
            int endPos = findFirstVisibleItemPosition() + mNumRowOrColumn;
            for (int i = endPos; i >= 0; i--) {
                Rect frame = mItemsRect.get(i);
                if (Rect.intersects(getDisplayRect(), frame)) {
                    if (findViewByPosition(i) != null) {
                        continue;
                    }

                    View scrap = recycler.getViewForPosition(i);
                    addView(scrap, 0);
                    measureChild(scrap, getItemWidth(i), getItemHeight(i));
                    if (mOrientation == HORIZONTAL) {
                        layoutDecoratedWithMargins(scrap,
                                frame.left - mHorizontalOffset,
                                frame.top,
                                frame.right - mHorizontalOffset,
                                frame.bottom);
                    } else {
                        layoutDecoratedWithMargins(scrap,
                                frame.left,
                                frame.top - mVerticalOffset,
                                frame.right,
                                frame.bottom - mVerticalOffset);
                    }
                }
            }
        }
    }

    private void fillRequireItems(RecyclerView.Recycler recycler, int beginPos) {
        int itemCount = getItemCount();
        Rect displayRect = getDisplayRect();
        int rectCount;
        // Re-display the subview that needs to appear on the screen
        for (int i = beginPos; i < itemCount; i++) {
            rectCount = mItemsRect.size();
            Rect frame = mItemsRect.get(i);
            if (i < rectCount && frame != null) {
                if (Rect.intersects(displayRect, frame)) {
                    View scrap = recycler.getViewForPosition(i);
                    addView(scrap);
                    measureChild(scrap, getItemWidth(i), getItemHeight(i));
                    if (mOrientation == HORIZONTAL) {
                        layoutDecoratedWithMargins(scrap,
                                frame.left - mHorizontalOffset,
                                frame.top,
                                frame.right - mHorizontalOffset,
                                frame.bottom);
                    } else {
                        layoutDecoratedWithMargins(scrap,
                                frame.left,
                                frame.top - mVerticalOffset,
                                frame.right,
                                frame.bottom - mVerticalOffset);
                    }
                }
            } else if (rectCount < itemCount) {
                View child = recycler.getViewForPosition(i);
                Rect itemRect = calculateViewSizeByPosition(child, i);
                if (!Rect.intersects(displayRect, itemRect)) {
                    recycler.recycleView(child);
                    return;
                }
                addView(child);
                if (mOrientation == HORIZONTAL) {
                    layoutDecoratedWithMargins(child,
                            itemRect.left - mHorizontalOffset,
                            itemRect.top,
                            itemRect.right - mHorizontalOffset,
                            itemRect.bottom);
                    mTotalSize = itemRect.right;
                } else {
                    layoutDecoratedWithMargins(child,
                            itemRect.left,
                            itemRect.top - mVerticalOffset,
                            itemRect.right,
                            itemRect.bottom - mVerticalOffset);
                    mTotalSize = itemRect.bottom;
                }

                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemRect);
                mItemsRect.put(i, frame);
                if (DEBUG) {
                    Log.d(TAG, "fillRequireItems: new pos=" + i + "=frame=" + frame.toString());
                }
            }
        }
    }

    private boolean recycleByScrollState(RecyclerView.Recycler recycler, int dt) {
        final int childCount = getChildCount();
        ArrayList<Integer> recycleIndexList = new ArrayList<>();
        Rect displayRect = getDisplayRect();
        if (dt >= 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                int pos = getPosition(child);
                Rect frame = mItemsRect.get(pos);
                if (!Rect.intersects(displayRect, frame)) {
                    recycleIndexList.add(i);
                }
            }
        } else {
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                int pos = getPosition(child);
                Rect frame = mItemsRect.get(pos);
                if (!Rect.intersects(displayRect, frame)) {
                    recycleIndexList.add(i);
                }
            }
        }
        if (recycleIndexList.size() > 0) {
            recycleChildren(recycler, dt, recycleIndexList);
            return true;
        }
        return false;
    }

    /**
     * Recycles children between given index.
     *
     * @param dt direction
     * @param recycleIndexList   save need recycle index
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int dt,
                                 ArrayList<Integer> recycleIndexList) {
        int size = recycleIndexList.size();
        if (DEBUG) {
            Log.d(TAG, "recycleChildren: recycler item size=" + size);
        }
        if (dt < 0) {
            for (int i = 0; i < size; i++) {
                int pos = recycleIndexList.get(i);
                removeAndRecycleViewAt(pos, recycler);
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                int pos = recycleIndexList.get(i);
                removeAndRecycleViewAt(pos, recycler);
            }
        }
    }

    public void measureChild(View child, int childWidth, int childHeight) {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        calculateItemDecorationsForChild(child, mDecorInsets);

        final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(),
                getPaddingLeft() + getPaddingRight() +
                        lp.leftMargin + lp.rightMargin, childWidth,
                canScrollHorizontally());
        final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(),
                getPaddingTop() + getPaddingBottom() +
                        lp.topMargin + lp.bottomMargin, childHeight,
                canScrollVertically());

        child.measure(widthSpec, heightSpec);
    }

    @Override
    public boolean canScrollHorizontally() {
        return mOrientation == HORIZONTAL;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dx;
        int maxScrollSpace = mTotalSize - getHorizontalSpace();
        if (mHorizontalOffset + dx < 0) {
            if (Math.abs(dx) > mHorizontalOffset) {
                realOffset = -mHorizontalOffset;
            } else {
                realOffset -= mHorizontalOffset;
            }
        } else if (mItemsRect.size() >= getItemCount() &&
                mHorizontalOffset + dx > maxScrollSpace) {
            realOffset = maxScrollSpace - mHorizontalOffset;
        }
        mHorizontalOffset += realOffset;
        offsetChildrenHorizontal(-realOffset);
        if (mHorizontalOffset != 0) {
            recycleAndFillItems(recycler, state, dx);
        }
        if (DEBUG) {
            Log.d(TAG, "scrollHorizontallyBy: HorizontalOffset=" + mHorizontalOffset
            + "=maxScrollSpace=" + maxScrollSpace);
        }
        return realOffset;
    }

    @Override
    public boolean canScrollVertically() {
        return mOrientation == VERTICAL;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dy;
        int maxScrollSpace = mTotalSize - getVerticalSpace();

        if (mVerticalOffset + dy < 0) {
            if (Math.abs(dy) > mVerticalOffset) {
                realOffset = -mVerticalOffset;
            } else {
                realOffset -= mVerticalOffset;
            }
        } else if (mItemsRect.size() >= getItemCount() &&
                mVerticalOffset + dy > maxScrollSpace) {
            realOffset = maxScrollSpace - mVerticalOffset;
        }
        mVerticalOffset += realOffset;
        offsetChildrenVertical(-realOffset);
        if (mVerticalOffset != 0) {
            recycleAndFillItems(recycler, state, dy);
        }
        if (DEBUG) {
            Log.d(TAG, "scrollVerticallyBy: VerticalOffset=" + mVerticalOffset
                    + "=maxScrollSpace=" + maxScrollSpace);
        }
        return realOffset;
    }

    public int getOrientation() {
        return mOrientation;
    }

    private int getItemWidth(int position) {
        int itemColumnSize = getItemColumnSize(position);
        return itemColumnSize * mOriItemWidth
                + (itemColumnSize - 1) * (mDecorInsets.left + mDecorInsets.right);
    }

    private int getItemHeight(int position) {
        int itemRowSize = getItemRowSize(position);
        return itemRowSize * mOriItemHeight
                + (itemRowSize - 1) * (mDecorInsets.bottom + mDecorInsets.top);
    }

    private int getDecoratedMeasurementHorizontal(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + params.leftMargin
                + params.rightMargin;
    }

    private int getChildHorizontalPadding(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + params.leftMargin
                + params.rightMargin - child.getMeasuredWidth();
    }

    private int getChildVerticalPadding(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredHeight(child) + params.topMargin
                + params.bottomMargin - child.getMeasuredHeight();
    }

    private int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    private int getVerticalSpace() {
        int space = getHeight() - getPaddingTop() - getPaddingBottom();
        return space <= 0 ? getMinimumHeight() : space;
    }

    private int getHorizontalSpace() {
        int space = getWidth() - getPaddingLeft() - getPaddingRight();
        return space <= 0 ? getMinimumWidth() : space;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        assertNotInLayoutOrScroll(null);
        if (orientation == mOrientation) {
            return;
        }
        mOrientation = orientation;
        requestLayout();
    }

    /**
     * Returns the adapter position of the first visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * @return the first visible item position or -1
     */
    int findFirstVisibleItemPosition() {
        final View child = findOneVisibleChild(0, getChildCount(), false, true);
        return child == null ? -1 : getPosition(child);
    }

    /**
     * Returns the adapter position of the last visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     * @return the last visible item position or -1
     */
    int findLastVisibleItemPosition() {
        final View child = findOneVisibleChild(getChildCount() - 1, -1, false, true);
        return child == null ? -1 : getPosition(child);
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        final int direction = calculateScrollDirectionForPosition(targetPosition);
        PointF outVector = new PointF();
        if (direction == 0) {
            return null;
        }
        if (mOrientation == HORIZONTAL) {
            outVector.x = direction;
            outVector.y = 0;
        } else {
            outVector.x = 0;
            outVector.y = direction;
        }
        return outVector;
    }

    private View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                             boolean acceptPartiallyVisible) {
        final int start = getPaddingLeft();
        final int end = getHeight() - getPaddingBottom();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i+=next) {
            final View child = getChildAt(i);
            final int childStart = getDecoratedStart(child);
            final int childEnd = getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (completelyVisible) {
                    if (childStart >= start && childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }

    private int getDecoratedStart(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedTop(view) - params.topMargin;
    }

    private int getDecoratedEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedBottom(view) + params.bottomMargin;
    }

    private int getFirstChildPosition() {
        final int childCount = getChildCount();
        return childCount == 0 ? 0 : getPosition(getChildAt(0));
    }

    private int calculateScrollDirectionForPosition(int position) {
        if (getChildCount() == 0) {
            return LAYOUT_START;
        }
        final int firstChildPos = getFirstChildPosition();
        return position < firstChildPos ? LAYOUT_START : LAYOUT_END;
    }

    protected abstract int getItemStartIndex(int position);

    protected abstract int getItemRowSize(int position);

    protected abstract int getItemColumnSize(int position);

    protected  abstract int getColumnSpacing();

    protected abstract int getRowSpacing();
}