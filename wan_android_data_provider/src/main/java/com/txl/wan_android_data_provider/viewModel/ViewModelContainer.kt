package com.txl.wan_android_data_provider.viewModel

import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/26
 * description：
 */

object ViewModelContainer {

    /**
     * 保存每种导航类型的 ViewModel 的class
     * */
    private val viewModels = HashMap<String,Class<out AbsNavItemListViewModel>>()
    private val viewModelFactories = HashMap<String, ViewModelProvider.Factory>()

    /**
     * 获取Model class,当没有category匹配的时候默认返回  NavItemListViewModel::class.java
     * */
    fun  getViewModelClass(category:String):Class<out AbsNavItemListViewModel>{
        return viewModels[category] ?: throw RuntimeException("viewModel class is not register in ")
    }

    fun putViewModelClass(category: String, modelClass :Class<out AbsNavItemListViewModel>){
        if(viewModels.containsKey(category)){
            throw IllegalArgumentException("category  has been put in viewModels please check category is $category")
        }
        viewModels[category] = modelClass
    }

    /**
     * 有没有必要在没获取到对应的 对象的情况下自己初始化？
     * */
    fun getViewModelFactory(category: String): ViewModelProvider.Factory{
        return viewModelFactories[category] ?: throw RuntimeException("has not register category $category in")
    }

    fun putViewModelFactory(category: String, factory : ViewModelProvider.Factory){
        if(viewModelFactories.containsKey(category)){
            throw IllegalArgumentException("category  has been put in viewModelFactorys please check category is $category")
        }
        viewModelFactories[category] = factory
    }
}