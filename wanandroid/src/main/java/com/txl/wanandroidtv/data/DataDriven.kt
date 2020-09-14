package com.txl.wanandroidtv.data
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.txl.wanandroidtv.ui.utils.WanAndroidNetInvokerUtils
import com.txl.txllog.AndroidLogWrapper
import com.txl.wanandroidtv.bean.home.BannerItemData
import java.lang.Exception

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
     * 获取首页banner
     * */
    fun getHomeBanner():Response<List<BannerItemData>>{
        val url = "$BASE_URL/banner/json"
        return getData(url)
    }

    /**
     * 获取玩Android首页文章列表数据
     * @param page 第几页数据
     * @param useCache 是否对数据进行缓存
     * @return Response 不对数据进行解析，
     * #Notice:这个方法以同步的方法进行，需要在子线程中进行
     * */
    fun getHomeArticleList(page:Int,useCache:Boolean = page == 0):Response<String>{
        val url = "$BASE_URL/article/list/$page/json"
        return getData(url)
    }

    fun getSquareArticleList(page:Int,useCache:Boolean = page == 0):Response<String>{
        val url = "$BASE_URL/user_article/list/$page/json"
        return getData(url)
    }

    /**
     * 获取导航数据
     * */
    fun getNavigateArticleList():Response<String>{
        val url = "$BASE_URL/navi/json"
        return getData(url)
    }

    fun getQANavArticleList(page:Int,useCache:Boolean = page == 0):Response<String>{
        val url = "$BASE_URL/wenda/list/$page/json"
        return getData(url)
    }

    /**
     * 获取体系数据
     * */
    fun getSetUpNavData():Response<String>{
        val url = "$BASE_URL/tree/json"
        return getData(url)
    }

    /**
     * 知识体系下的文章
     * */
    fun getSetUpNavItemListData(page:Int,useCache:Boolean = page == 0,cid:Int):Response<String>{
        val url = "$BASE_URL/article/list/$page/json?cid=$cid"
        return getData(url)
    }

    private fun <T>getData(url:String):Response<T>{
        var response: okhttp3.Response? = null
        var originString = ""
        try {
            response = WanAndroidNetInvokerUtils.get(url)
            if(response?.code == 200){
                response.body?.let {
                    val content = it.string()
                    originString = content
                    AndroidLogWrapper.d(TAG,"url is $url Api response:${content}")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        if(!TextUtils.isEmpty(originString)){
            val type = genericType<Response<T>>()
            return Response(true,Gson().fromJson(originString, type), null, "",originString)
        }else{
            return Response(false,null, Error.newNetError("-1"),"response is null")
        }
    }

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

    private fun getGson():Gson {
        val builder = GsonBuilder()
        return builder.create()
    }
}