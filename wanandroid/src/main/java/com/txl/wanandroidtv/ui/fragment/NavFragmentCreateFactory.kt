package com.txl.wanandroidtv.ui.fragment

import com.txl.ui_basic.fragment.BaseFragment

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/16
 * description：创建导航Fragment
 */
/**
 * 首页
 * */
const val CATEGORY_HOME = "1"
/**
 * 广场
 * */
const val CATEGORY_SQUARE = "2"
/**
 * 导航
 * */
const val CATEGORY_NAVIGATE = "3"

class NavFragmentCreateFactory {
    companion object {
        @JvmStatic
        fun createFragmentByCategory(category: String, vararg args: Any): BaseFragment {
            when (category) {
                CATEGORY_HOME-> {
                    return HomeNavFragment::class.java.newInstance()
                }
                CATEGORY_SQUARE->{
                    return SquareNavFragment::class.java.newInstance()
                }
                CATEGORY_NAVIGATE->{
                    return NavigateNavFragment::class.java.newInstance()
                }
                else -> {
                    return HomeNavFragment::class.java.newInstance()
                }
            }
        }
    }
}