package com.txl.commonlibrary.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

public class WebBrowse extends WebView {
    public WebBrowse(Context context) {
        super(context);
        init();
    }

    public WebBrowse(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebBrowse(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebBrowse(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init(){
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        settings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        setBackgroundColor(0);
    }
}
