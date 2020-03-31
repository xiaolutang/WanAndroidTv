package com.txl.tvlib.config

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/31
 * description：
 */
class TvLibConfig private constructor() {

    companion object{
        var defaultConfig:TvLibConfig = Builder().setBorderColor(Color.RED).setBorderWidth(30).setHasSelectBorder(true).build()

    }

    /**
     * 边框线的宽度
     * */
    var borderWidth = 0
    private set
    /**
     * 边框线的颜色
     * */
    var borderColor = 0
        private set
    /**
     * 是否开启上焦点的时候显示边框
     * */
    var hasSelectBorder = true
        private set

    class Builder {
        private val tvLibConfig = TvLibConfig()

        /**
         * 边框宽度
         * */
        fun setBorderWidth(borderWidth: Int):Builder{
            tvLibConfig.borderWidth = borderWidth
            return this
        }

        /**
         * 边框颜色
         * */
        fun setBorderColor(@ColorInt borderColor: Int):Builder{
            tvLibConfig.borderColor = borderColor
            return this
        }

        /**
         * 是否开启 选中的边框
         * */
        fun setHasSelectBorder(has: Boolean):Builder{
            tvLibConfig.hasSelectBorder = has
            return this
        }

        fun build():TvLibConfig{
            return tvLibConfig
        }
    }
}