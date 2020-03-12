package com.txl.txllog;

import android.util.Log;

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/12
 * description：  对Android的Log进行装饰添加仿便自己通用处理
 * */
public class AndroidLogWrapper {
    public static void e(String tag, String msg){
        Log.e( tag, msg );
    }
    public static void v(String tag, String msg){
        Log.v( tag, msg );
    }
    public static void i(String tag, String msg){
        Log.i( tag, msg );
    }
    public static void w(String tag, String msg){
        Log.w( tag, msg );
    }
    public static void d(String tag, String msg){
        Log.d( tag, msg );
    }
}
