package com.txl.wanandroidtv.ui.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.txl.wanandroidtv.R

object ThemeUtils {
    fun getThemeColor(context: Context):Int{
        return ContextCompat.getColor(context, R.color.theme)
    }
}