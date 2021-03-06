package com.txl.wan_android_data_provider.data

import androidx.annotation.Keep

@Keep
data class Response<T>(
    /**
     * 接口调用返回数据
     */
    val data: T? = null,
    var originString:String? = "",
    /**
     * 错误码，为0正常
     * */
    var errorCode:Int?=0,
    var errorMsg:String?=""
){
    fun netSuccess():Boolean{
        return errorCode == 0
    }
}

