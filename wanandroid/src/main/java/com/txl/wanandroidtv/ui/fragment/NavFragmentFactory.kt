package com.txl.wanandroidtv.ui.fragment

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/16
 * description：创建导航Fragment
 */
const val FRAGMENT_HOME = "1"

class NavFragmentFactory {
    companion object {
        @JvmStatic
        fun createFragmentByCategory(category: String, vararg args: Any): BaseNavFragment {
            when (category) {
                FRAGMENT_HOME -> {
                    return HomeNavFragment::class.java.newInstance()
                }
                else -> {
                    return HomeNavFragment::class.java.newInstance()
                }
            }
        }
    }
}