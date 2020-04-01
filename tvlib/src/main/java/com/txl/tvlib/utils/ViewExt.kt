package com.txl.tvlib.utils

import android.view.View

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/4/1
 * description：
 */

fun View.viewCanFocus():Boolean{
    return isFocusable && visibility == View.VISIBLE
}