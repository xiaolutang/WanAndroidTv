package com.txl.web.router

import android.content.Context
import com.txl.router.web.IWebRouter
import com.txl.web.ui.WebActivity
import io.github.prototypez.appjoint.core.ServiceProvider

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/25
 * description：
 */
@ServiceProvider
class WebRouterImpl  :IWebRouter {
    override fun openWebPage(context: Context, title: String, url: String) {
        WebActivity.openWebPage(context,title,url)
    }
}