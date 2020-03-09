package com.txl.tvlib.focushandler;

import android.view.View;

/**
 * 辅助LibTvRecyclerView的焦点查找
 * */
public interface IFocusSearchHelper {
    View findFirstFocusAbleView();
    View findLastFocusAbleView();
}
