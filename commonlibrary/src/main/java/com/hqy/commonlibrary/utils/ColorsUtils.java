package com.hqy.commonlibrary.utils;

import android.content.res.ColorStateList;

public class ColorsUtils {
    public static ColorStateList createColorStateList(int normalColor, int checkedColor, int focusedColor) {
        int[] colors = new int[] { focusedColor,checkedColor,normalColor};
        int[][] states = new int[3][];
        states[0] = new int[] { android.R.attr.state_focused };
        states[1] = new int[] { android.R.attr.state_checked};
        states[2] = new int[] { };
        return new ColorStateList(states, colors);
    }
}

