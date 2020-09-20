package com.txl.wanandroidtv;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.txl.ui_basic.BaseActivity;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/18
 * description：
 */
public class WanAndroidBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
        return super.getResources();
    }
}
