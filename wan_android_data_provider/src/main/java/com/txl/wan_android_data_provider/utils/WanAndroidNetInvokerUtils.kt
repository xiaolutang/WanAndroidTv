package com.txl.wan_android_data_provider.utils

import com.txl.netmodel.okhttp.okhttp.OkHttpUtils
import okhttp3.*
import okhttp3.FormBody
import java.util.*


/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/11
 * description：网络数据请求工具封装
 */
object WanAndroidNetInvokerUtils {
    const val TAG = "WanAndroidNetInvokerUtils"

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpUtils.getOkHttpClient()
    }

    /**
     * 使用GET方法发送一个请求
     *
     * @param url     请求的url地址
     * @param params  请求的参数,默认为空,除登录外均需要加入tenantid：租户id和group_id：cmc租户id
     */
    fun get(url: String, params: SortedMap<String, String> = TreeMap()): Response? {
        val urlParams = buildParamsText(params)
        val sb = StringBuilder(url).append('?').append(urlParams)
        return get(String(sb))
    }

    fun get(url:String): Response? {
        val request = Request.Builder().url(url)
                .get()
                .build()
        try {
            return getOkHttpClient().newCall(request).execute()
        } catch (e: Exception) {//上面的代码中网络部分可能会抛出异常
            e.printStackTrace()
        }
        return null
    }

    fun get(url: String, params: SortedMap<String, String> = TreeMap(),callback: Callback) {
        val urlParams = buildParamsText(params)
        val sb = StringBuilder(url).append('?').append(urlParams)
        get(String(sb), callback)
    }

    fun get(url: String,callback: Callback){
        val request = Request.Builder().url(url)
                .get()
                .build()
        try {
            getOkHttpClient().newCall(request).enqueue(callback)
        } catch (e: Exception) {//上面的代码中网络部分可能会抛出异常
            e.printStackTrace()
        }
    }

    fun post(url: String, params: SortedMap<String, String> = TreeMap()):Response?{
        val builder = FormBody.Builder()
        for (param in params){
            builder.add(param.key, param.value)
        }
        val requestBody: RequestBody =  builder.build()
        val request = Request.Builder().url(url)
                .post(requestBody)
                .build()
        try {
            return getOkHttpClient().newCall(request).execute()
        } catch (e: Exception) {//上面的代码中网络部分可能会抛出异常
            e.printStackTrace()
        }
        return null
    }

    /**
     * 构造参数文本串
     * key1=value1&key2=value2
     */
    private fun buildParamsText(params: Map<String, String>): String {
        if (params.isEmpty()) return ""
        val sb = StringBuilder()
        params.map {
            sb.append(it.key).append('=').append(it.value).append('&')
        }
        sb.deleteCharAt(sb.length - 1)
        return String(sb)
    }
}