package com.txl.testtvlib

import android.graphics.Color
import androidx.multidex.MultiDexApplication
import com.txl.tvlib.config.TvLibConfig

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/2
 * description：
 */
class TestTvLibApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        TvLibConfig.defaultConfig = TvLibConfig.Builder()
                .setHasSelectBorder(true)
                .setBorderWidth(resources.getDimensionPixelSize(R.dimen.dp_5))
                .setBorderColor(Color.RED)
                .build()
    }
}