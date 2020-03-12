package com.txl.wanandroidtv.data

import androidx.annotation.Keep

@Keep
data class Response<T>(

    /**
     * 接口调用状态
     */
    val state: Boolean = false,
    /**
     * 接口调用返回数据
     */
    val data: T? = null,
    /**
     * 错误信息：只有在接口调用状态state为false时
     */
    val error: Error?= null,
    /**
     * 接口调用消息
     */
    val message: String = "",
    var originString:String? = ""
)

