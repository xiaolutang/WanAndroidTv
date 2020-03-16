package com.txl.wanandroidtv.viewModel

import androidx.annotation.Keep

/**
 * txl
 * 封装接口返回数据
 */

/**
 * 加载完成
 */
const val STATE_LOADED = 0
/**
 * 加载状态
 */
const val STATE_LOADING = 1
/**
 * 错误状态
 */
const val STATE_ERROR = 2

@Keep
data class ResourceBoundary<T>(
        /**
         * 响应状态： STATE_LOADED； STATE_LOADING； STATE_ERROR
         * */
    val state: Int,
    val extraCode: Int,
    val message:String = "",
    val data: T? = null,
    var currentPage:Int=0
)
