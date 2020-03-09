package com.txl.screenadaptation;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.txl.screenadaptation.utils.ScerenAdapterUtils;

import me.jessyan.autosize.AutoSizeCompat;

/**
 * 采用今日头条适配方案
 * 屏幕适配基础Activity
 * */
public abstract class ScreenAdaptionBaseActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName();

    private boolean openLog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenAdaption();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(openLog)
        Log.d(TAG,"ScreenAdaptionBaseActivity onRestart");
    }

    /**
     * 进行屏幕适配，如果不需要直接继承这个方法空实现即可
     * */
    protected void screenAdaption(){
//        ScerenAdapterUtils.setCustomDensity(this,getApplication());
    }

    @Override
    public Resources getResources() {
        autoSizeFix();
        return super.getResources();
    }

    protected void autoSizeFix(){
        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));
    }
}
