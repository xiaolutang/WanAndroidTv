package com.txl.router.web

import android.content.Context

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/25
 * description：web模块对外提供的接口
 */
interface IWebRouter {
    /**
     * @param title 网页标题
     * @param url 网页链接
     * */
    fun openWebPage(context: Context,title:String,url: String)
}