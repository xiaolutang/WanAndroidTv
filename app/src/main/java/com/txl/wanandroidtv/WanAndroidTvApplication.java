package com.txl.wanandroidtv;

import androidx.multidex.MultiDexApplication;

import skin.support.SkinCompatManager;

public class WanAndroidTvApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }
}
