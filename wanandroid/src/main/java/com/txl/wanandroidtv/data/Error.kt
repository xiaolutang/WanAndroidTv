package com.txl.wanandroidtv.data

import androidx.annotation.Keep

/**
 *
 * 服务器返回的错误信息
 *
 */
@Keep
data class Error(
    /**
     * 错误代码
     */
    val code:String,
    /**
     * 系统外部的错误码
     */
    val hd_code:Int,
    /**
     * 错误信息的描述
     */
    val description:String){
    companion object{
        fun newNetError(code: String):Error{
            return Error(code,ErrorCode.ERROR_NETWORK_EXCEPTION,"Network exception")
        }
    }
}


