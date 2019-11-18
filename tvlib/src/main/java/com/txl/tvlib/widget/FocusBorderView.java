package com.txl.tvlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import static com.txl.tvlib.widget.TvRecyclerView.DEBUG;

public class FocusBorderView extends View {

    private static final String TAG = "TvRecyclerView.FB";

    private TvRecyclerView mTvRecyclerView;
    private final Scroller mScroller;

    private float mScaleX;
    private float mScaleY;

    private boolean mIsDrawGetFocusAnim;
    private boolean mIsClicked;

    private int mLeftFocusBoundWidth;
    private int mTopFocusBoundWidth;
    private int mRightFocusBoundWidth;
    private int mBottomFocusBoundWidth;


    public FocusBorderView(Context context) {
        super(context);
        mScroller = new Scroller(context);

        mIsDrawGetFocusAnim = false;
        mIsClicked = false;
        mLeftFocusBoundWidth = 0;
        mTopFocusBoundWidth = 0;
        mRightFocusBoundWidth = 0;
        mBottomFocusBoundWidth = 0;
        mScaleX = 0;
        mScaleY = 0;
    }

    public TvRecyclerView getTvRecyclerView() {
        return mTvRecyclerView;
    }

    public void setTvRecyclerView(TvRecyclerView tvRecyclerView) {
        if (mTvRecyclerView == null) {
            mTvRecyclerView = tvRecyclerView;
        }
    }

    public void setSelectPadding(int left, int top, int right, int bottom) {
        mLeftFocusBoundWidth = left;
        mTopFocusBoundWidth = top;
        mRightFocusBoundWidth = right;
        mBottomFocusBoundWidth = bottom;
    }

    public void startFocusAnim() {
        if (mTvRecyclerView != null) {
            mTvRecyclerView.setLayerType(View.LAYER_TYPE_NONE, null);
            View v = mTvRecyclerView.getSelectedView();
            if (v != null) {
                if (DEBUG) {
                    Log.d(TAG, "startFocusAnim: start focus animation");
                }
                mIsDrawGetFocusAnim = true;
                mScroller.abortAnimation();
                mScroller.startScroll(0, 0, 100, 100, 245);
                invalidate();
            }
        }
    }

    public void dismissGetFocus() {
        mIsDrawGetFocusAnim = false;
    }

    public void dismissDraw() {
        mScroller.abortAnimation();
    }

    public void startClickAnim() {
        if (mTvRecyclerView != null) {
            mTvRecyclerView.setLayerType(View.LAYER_TYPE_NONE, null);
            View v = null;
            int indexChild = mTvRecyclerView.getSelectedPosition();
            if (indexChild >= 0 && indexChild < mTvRecyclerView.getAdapter().getItemCount()) {
                v = mTvRecyclerView.getSelectedView();
            }

            if (v != null) {
                if (DEBUG) {
                    Log.d(TAG, "startClickAnim: start click animation");
                }
                mIsClicked = true;
                mScroller.abortAnimation();
                mScroller.startScroll(0, 0, 100, 100, 200);
                invalidate();
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            float scaleValue = mTvRecyclerView.getSelectedScaleValue();
            if (mIsDrawGetFocusAnim) { // calculate scale when get focus animation
                mScaleX = ((scaleValue - 1) * mScroller.getCurrX()) / 100 + 1;
                mScaleY = ((scaleValue - 1) * mScroller.getCurrY()) / 100 + 1;
            } else if (mIsClicked) {  // calculate scale when key down animation
                mScaleX = scaleValue - ((scaleValue - 1) * mScroller.getCurrX()) / 100;
                mScaleY = scaleValue - ((scaleValue - 1) * mScroller.getCurrY()) / 100;
            }
            invalidate();
        } else {
            if (mIsDrawGetFocusAnim) {
                mIsDrawGetFocusAnim = false;
                if (mTvRecyclerView != null) {
                    mTvRecyclerView.setLayerType(mTvRecyclerView.mLayerType, null);
                    invalidate();
                }
            } else if (mIsClicked) {
                mIsClicked = false;
                if (mTvRecyclerView != null) {
                    mTvRecyclerView.setLayerType(mTvRecyclerView.mLayerType, null);
                    invalidate();
                }
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mTvRecyclerView != null&&(mTvRecyclerView.focusOutKeepDrawable || mTvRecyclerView.hasFocus())) {
            drawGetFocusOrClickScaleAnim(canvas);
            drawFocusMoveAnim(canvas);
            drawFocus(canvas);
        }
    }

    private void drawGetFocusOrClickScaleAnim(Canvas canvas) {
        if (mIsDrawGetFocusAnim || mIsClicked) {
            if (DEBUG) {
                Log.d(TAG, "drawGetFocusOrClickScaleAnim: ==isClicked=" + mIsClicked
                + "=GetFocusAnim=" + mIsDrawGetFocusAnim);
            }
            View itemView = mTvRecyclerView.getSelectedView();
            if (itemView == null) {
                return;
            }

            int itemWidth = itemView.getWidth();
            int itemHeight = itemView.getHeight();

            int[] location = new int[2];
            itemView.getLocationInWindow(location);
            int[] drawLocation = new int[2];
            getLocationInWindow(drawLocation);

            // draw focus image
            Drawable drawableFocus = mTvRecyclerView.getDrawableFocus();
            if (drawableFocus != null) {
                int focusWidth = itemWidth + mLeftFocusBoundWidth + mRightFocusBoundWidth;
                int focusHeight = itemHeight + mTopFocusBoundWidth + mBottomFocusBoundWidth;

                canvas.save();
                canvas.translate(location[0] - mLeftFocusBoundWidth,
                        location[1] - drawLocation[1] - mTopFocusBoundWidth);
                canvas.scale(mScaleX, mScaleY, itemWidth / 2, itemHeight / 2);

                drawableFocus.setBounds(0, 0, focusWidth, focusHeight);
                drawableFocus.draw(canvas);
                canvas.restore();
            }

            // draw item view
            canvas.save();
            canvas.translate(location[0], location[1]);
            canvas.scale(mScaleX, mScaleY, itemWidth / 2, itemHeight / 2);
            itemView.draw(canvas);
            canvas.restore();
        }
    }

    private void drawFocusMoveAnim(Canvas canvas) {
        if (mTvRecyclerView.mIsDrawFocusMoveAnim) {
            if (DEBUG) {
                Log.d(TAG, "drawFocusMoveAnim: ==============");
            }
            mScroller.abortAnimation();
            View curView = mTvRecyclerView.getSelectedView();
            View nextView = mTvRecyclerView.getNextFocusView();

            if (nextView != null && curView != null) {
                int[] locationDrawLayout = new int[2];
                this.getLocationInWindow(locationDrawLayout);

                int[] location = new int[2];
                nextView.getLocationInWindow(location);
                int nextLeft = location[0];
                int nextTop = location[1] - locationDrawLayout[1];
                int nextWidth = nextView.getWidth();
                int nextHeight = nextView.getHeight();

                curView.getLocationInWindow(location);
                int curLeft = location[0];
                int curTop = location[1] - locationDrawLayout[1];
                int curWidth = curView.getWidth();
                int curHeight = curView.getHeight();

                float animScale = mTvRecyclerView.getFocusMoveAnimScale();
                float focusLeft = curLeft + (nextLeft - curLeft) * animScale;
                float focusTop = curTop + (nextTop - curTop) * animScale;
                float focusWidth = curWidth + (nextWidth - curWidth) * animScale;
                float focusHeight = curHeight + (nextHeight - curHeight) * animScale;

                // draw focus image
                Drawable drawableFocus = mTvRecyclerView.getDrawableFocus();
                float scaleValue = mTvRecyclerView.getSelectedScaleValue();
                if (drawableFocus != null) {
                    canvas.save();
                    canvas.translate(
                            focusLeft - (scaleValue - 1) / 2 * focusWidth,
                            focusTop - (scaleValue - 1) / 2 * focusHeight);
                    canvas.scale(scaleValue, scaleValue, 0, 0);

                    drawableFocus.setBounds(
                            0 - mLeftFocusBoundWidth,
                            0 - mTopFocusBoundWidth,
                            (int) (focusWidth + mRightFocusBoundWidth),
                            (int) (focusHeight + mBottomFocusBoundWidth));
                    drawableFocus.draw(canvas);
                    canvas.restore();
                }

                // draw next item view
                canvas.save();
                canvas.translate(
                        focusLeft - (scaleValue - 1) / 2 * focusWidth,
                        focusTop - (scaleValue - 1) / 2 * focusHeight);
                canvas.scale(
                        (scaleValue * focusWidth) / nextWidth,
                        (scaleValue * focusHeight) / nextHeight,
                        0,
                        0);
                canvas.saveLayerAlpha(new RectF(0, 0, getWidth(), getHeight()),
                        (int) (0xFF * animScale), Canvas.ALL_SAVE_FLAG);
                nextView.draw(canvas);
                canvas.restore();
                canvas.restore();

                // draw current item view
                canvas.save();
                canvas.translate(
                        focusLeft - (scaleValue - 1) / 2 * focusWidth,
                        focusTop - (scaleValue - 1) / 2 * focusHeight);
                canvas.scale(
                        (scaleValue * focusWidth) / curWidth,
                        (scaleValue * focusHeight) / curHeight,
                        0,
                        0);
                canvas.saveLayerAlpha(new RectF(0, 0, getWidth(), getHeight()),
                        (int) (0xFF * (1 - animScale)), Canvas.ALL_SAVE_FLAG);
                curView.draw(canvas);
                canvas.restore();
                canvas.restore();
            }
        }
    }

    private void drawFocus(Canvas canvas) {
        if (!mIsDrawGetFocusAnim && !mTvRecyclerView.mIsDrawFocusMoveAnim && !mIsClicked) {
            View itemView = mTvRecyclerView.getSelectedView();
            if (itemView != null) {
                int[] itemLocation = new int[2];
                itemView.getLocationInWindow(itemLocation);
                Log.i(TAG, "drawFocus: ===itemLocationX===" + itemLocation[0] +
                        "===itemLocationY==" + itemLocation[1]);

                int itemWidth = itemView.getWidth();
                int itemHeight = itemView.getHeight();
                float scaleValue = mTvRecyclerView.getSelectedScaleValue();
                float itemPositionX = itemLocation[0] - (scaleValue - 1) / 2 * itemWidth;
                float itemPositionY = itemLocation[1] - (scaleValue - 1) / 2 * itemHeight;
                Log.i(TAG, "drawFocus: ======itemPositionX=====" + itemPositionX +
                        "===itemPositionY===" + itemPositionY);

                //draw focus image
                Drawable drawableFocus = mTvRecyclerView.getDrawableFocus();
                int drawWidth = itemWidth + mLeftFocusBoundWidth + mRightFocusBoundWidth;
                int drawHeight = itemHeight + mTopFocusBoundWidth + mBottomFocusBoundWidth;
                float drawPositionX = itemPositionX - scaleValue * mLeftFocusBoundWidth;
                float drawPositionY = itemPositionY - scaleValue * mTopFocusBoundWidth;
                Log.i(TAG, "drawFocus: ===drawPositionX==" + drawPositionX +
                        "===drawPositionY===" + drawPositionY);

                if (drawableFocus != null) {
                    canvas.save();
                    canvas.translate(drawPositionX, drawPositionY);
                    canvas.scale(scaleValue, scaleValue, 0, 0);
                    drawableFocus.setBounds(0, 0, drawWidth, drawHeight);
                    drawableFocus.draw(canvas);
                    canvas.restore();
                }

                // draw item view
                canvas.save();
                canvas.translate(itemPositionX, itemPositionY);
                canvas.scale(scaleValue, scaleValue, 0, 0);
                itemView.draw(canvas);
                canvas.restore();
            }
        }
    }
}
