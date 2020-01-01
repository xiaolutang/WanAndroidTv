package com.hqy.commonlibrary.utils;

import android.util.Log;

/**
 * 对需要调试的打印 进行全局的配置
 * */
public class AndroidLogWrapperUtil {
    /**
     * 开启调试打印
     * */
    public static boolean DEBUG = true;

    public static void i(String tag,String message){
        if(DEBUG){
            Log.i(tag,message);
        }
    }

    public static void d(String tag,String message){
        if(DEBUG){
            Log.d(tag,message);
        }
    }

    /**
     * 错误信息直接打印
     * */
    public static void e(String tag,String message){
        Log.e(tag,message);
    }

    /**
     * 警告信息直接打印
     * */
    public static void w(String tag,String message){
        Log.w(tag,message);
    }
}
