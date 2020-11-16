package com.txl.wanandroidtv;

import androidx.multidex.MultiDexApplication;

import com.txl.netmodel.okhttp.okhttp.OkHttpUtils;

import io.github.prototypez.appjoint.core.AppSpec;
import me.jessyan.autosize.AutoSize;
import skin.support.SkinCompatManager;

@AppSpec
public class WanAndroidTvApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        AutoSize.initCompatMultiProcess(this);
        OkHttpUtils.initCache(getExternalCacheDir().toString(),( 20 * 1024 * 1024));
    }
}
