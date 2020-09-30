package com.txl.web.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.txl.web.utils.WebViewThirdAppJumpUtils;

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
        // 不缩放
        setInitialScale(100);

        WebSettings settings = getSettings();
        settings.setTextZoom(100);
        setBackgroundColor(0);
        // 网页内容的宽度是否可大于WebView控件的宽度
        settings.setLoadWithOverviewMode(false);
        // 保存表单数据
        settings.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        // 启动应用缓存
        settings.setAppCacheEnabled(true);
        // 设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        settings.setUseWideViewPort(true);
        // 告诉WebView启用JavaScript执行。默认的是false。
        settings.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        settings.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        settings.setDomStorageEnabled(true);
        // 排版适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setWebViewClient(new MyWebViewClient());
    }

    public static class MyWebViewClient extends WebViewClient{
        private WebViewThirdAppJumpUtils webViewThirdAppJumpUtils;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(URLUtil.isNetworkUrl(url)){
                view.loadUrl(url);
            }else {
                if(webViewThirdAppJumpUtils == null){
                    webViewThirdAppJumpUtils = new WebViewThirdAppJumpUtils(view.getContext().getApplicationContext());
                }
                webViewThirdAppJumpUtils.matchDefaultUrl(url);
            }
            return true;
        }
    }
}
