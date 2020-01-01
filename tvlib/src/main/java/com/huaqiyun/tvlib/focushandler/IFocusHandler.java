package com.huaqiyun.tvlib.focushandler;

import android.view.View;

public interface IFocusHandler {
    /**
     * Called when an item gains or loses focus.
     *
     * @param view The view whose focus is changing.
     * @param hasFocus True if focus is gained; false otherwise.
     */
    void onItemFocused(View view, boolean hasFocus);

    /**
     * Called when the view is being created.
     */
    void onInitializeView(View view);
}
