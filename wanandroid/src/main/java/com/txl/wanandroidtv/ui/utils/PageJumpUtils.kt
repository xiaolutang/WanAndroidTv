package com.txl.wanandroidtv.ui.utils

import android.content.Context
import com.txl.router.web.IWebRouter
import com.txl.wanandroidtv.bean.home.Article
import io.github.prototypez.appjoint.AppJoint

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/25
 * description：
 */
object PageJumpUtils {
    fun openPage(context: Context,data:Any?,vararg args:Any?){
        if(data is Article){
            val webRouter = AppJoint.service(IWebRouter::class.java)
            webRouter?.openWebPage(context,data.title,data.link)
        }
    }
}