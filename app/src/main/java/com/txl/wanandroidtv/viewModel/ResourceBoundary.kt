package com.txl.wanandroidtv.viewModel

import androidx.annotation.Keep

/**
 * txl
 * 封装接口返回数据
 */

@Keep
data class ResourceBoundary<T>(
    val state: Int,
    val extraCode: Int,
    val message:String = "",
    val data: T? = null,
    var currentPage:Int=1
)
