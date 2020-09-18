package com.txl.wan_android_data_provider.data
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.txl.wan_android_data_provider.bean.com.besjon.pojo.Data
import com.txl.wan_android_data_provider.bean.home.Article
import com.txl.wan_android_data_provider.bean.home.BannerItemData
import com.txl.wan_android_data_provider.utils.WanAndroidNetInvokerUtils
import java.lang.Exception
import java.lang.reflect.Type

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
        val type = genericType<Response<List<BannerItemData>>>()
        return getData(url,type)
    }

    /**
     * 获取置顶文章
     * */
    fun getTopArticleList():Response<List<Article>>{
        val url = "$BASE_URL/article/top/json"
        val type = genericType<Response<List<Article>>>()
        return getData(url,type)
    }

    /**
     * 获取玩Android首页文章列表数据
     * @param page 第几页数据
     * @return Response 不对数据进行解析，
     * #Notice:这个方法以同步的方法进行，需要在子线程中进行
     * */
    fun getHomeArticleList(page:Int):Response<Data>{
        val url = "$BASE_URL/article/list/$page/json"
        val type = genericType<Response<Data>>()
        return getData(url,type)
    }

    fun getSquareArticleList(page:Int):Response<String>{
        val url = "$BASE_URL/user_article/list/$page/json"
        val type = genericType<String>()
        return getData(url,type)
    }

    /**
     * 获取导航数据
     * */
    fun getNavigateArticleList():Response<String>{
        val url = "$BASE_URL/navi/json"
        val type = genericType<String>()
        return getData(url,type)
    }

    fun getQANavArticleList(page:Int,useCache:Boolean = page == 0):Response<String>{
        val url = "$BASE_URL/wenda/list/$page/json"
        val type = genericType<String>()
        return getData(url,type)
    }

    /**
     * 获取体系数据
     * */
    fun getSetUpNavData():Response<String>{
        val url = "$BASE_URL/tree/json"
        val type = genericType<String>()
        return getData(url,type)
    }

    /**
     * 知识体系下的文章
     * */
    fun getSetUpNavItemListData(page:Int,useCache:Boolean = page == 0,cid:Int):Response<String>{
        val url = "$BASE_URL/article/list/$page/json?cid=$cid"
        val type = genericType<String>()
        return getData(url,type)
    }

    /**
     * 这个应该和 具体的业务 无关
     * */
    private fun <T>getData(url:String,type: Type):Response<T>{
        var response: okhttp3.Response? = null
        var originString = ""
        try {
            response = WanAndroidNetInvokerUtils.get(url)
            if(response?.code == 200){
                response.body?.let {
                    val content = it.string()
                    originString = content
                    Log.d(TAG,"url is $url Api response:${content}")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return if(!TextUtils.isEmpty(originString)){
            val tempResponse:Response<T> =  Gson().fromJson(originString, type)
            tempResponse.originString = originString
            tempResponse
        }else{
            Response(null,"", -1,"response is null")
        }
    }

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type!!

    private fun getGson():Gson {
        val builder = GsonBuilder()
        return builder.create()
    }
}