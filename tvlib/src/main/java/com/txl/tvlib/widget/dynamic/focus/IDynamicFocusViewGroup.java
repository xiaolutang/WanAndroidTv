package com.txl.tvlib.widget.dynamic.focus;

import android.view.View;

import java.util.ArrayList;

public interface IDynamicFocusViewGroup {
    boolean dispatchAddFocusables(ArrayList<View> views, int direction, int focusableMode);
    void openFocusDynamic(boolean open);
}
