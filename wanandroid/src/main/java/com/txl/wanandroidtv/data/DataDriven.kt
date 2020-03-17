package com.txl.wanandroidtv.data
import com.txl.netmodel.NetInvokerUtils
import com.txl.txllog.AndroidLogWrapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/12
 * description：数据驱动，从网络或者本地取数据
 */
object DataDriven {
    private const val TAG = "DataDriven"
    private const val BASE_URL = "https://www.wanandroid.com"


    /**
     * 获取玩Android首页文章列表数据
     * @param page 第几页数据
     * @param useCache 是否对数据进行缓存
     * @return Response 不对数据进行解析，
     * #Notice:这个方法以同步的方法进行，需要在子线程中进行
     * */
    fun getHomeArticleList(page:Int,useCache:Boolean = page == 0):Response<String>{
        val url = "$BASE_URL/article/list/$page/json"
        var response: okhttp3.Response? = null
        var originString = ""
        try {
            response = NetInvokerUtils.get(url)
            if(response?.code == 200){
                response.body?.let {
                    val content = it.string()
//                    val inputStream = it.byteStream()
//                    val inputReader = InputStreamReader(inputStream,"UTF-8");
//                    //InputStreamReader inputReader = new InputStreamReader(ssq_is,"UTF-8");
//                    val bufReader = BufferedReader(inputReader);
//                    var line = "";
//                    val stringBuilder = StringBuilder()
//                    while (line != null){
//                        stringBuilder.append(line)
//                        line = bufReader.readLine()
//
//                    }
//                    originString = String(stringBuilder)
                    AndroidLogWrapper.d(TAG,"url is $url Api response:${content}")
                    return Response(true,content, null,"",content)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        if(useCache){
            //缓存中读取数据
        }
        if(response == null){
            return Response(false,null, Error.newNetError("-1"),"response is null")
        }else{
            return Response(false,originString, null, response.message,originString)
        }
    }
}