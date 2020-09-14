package com.txl.tvlib.border.config;

import android.graphics.drawable.Drawable;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/13
 * description：
 */
public class BorderConfig {
    private Drawable drawable;

    public class BorderConfigBuilder{
        private Drawable borderDrawable;

        public BorderConfigBuilder setViewDefaultDrawable(Drawable defaultDrawable){
            borderDrawable = defaultDrawable;
            return this;
        }
    }
}
