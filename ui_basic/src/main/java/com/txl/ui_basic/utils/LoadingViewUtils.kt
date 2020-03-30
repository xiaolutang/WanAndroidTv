package com.txl.ui_basic.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.txl.ui_basic.R

/**
 * 封装统一的页面加载效果
 * fixme 抽取接口，供其他项目使用
 * */
class LoadingViewUtils {
    lateinit var loadingRootView:View
        private set
    lateinit var loadingImage:ImageView
        private set
    lateinit var loadingText:TextView
        private set
    lateinit var loadingProgress:ProgressBar
        private set

    constructor(context: Context){
        loadingRootView = LayoutInflater.from(context).inflate(R.layout.lib_loading,null,false)
        initView(loadingRootView)
    }

    private fun initView(parent:View){
        loadingRootView  = parent.findViewById(R.id.loading_root)
        loadingImage = loadingRootView.findViewById(R.id.image_message)
        loadingText = loadingRootView.findViewById(R.id.tv_message)
        loadingProgress = loadingRootView.findViewById(R.id.progressView)
    }

    constructor(parent:View){
        initView(parent)
    }

    /**
     * 关闭整个浮层
     * */
    fun showLoadingView(visible: Boolean){
        if(visible){
            loadingRootView.visibility = View.VISIBLE
        }else{
            loadingRootView.visibility = View.GONE
        }
    }

    fun showLoadingtextView(visible: Boolean){
        if(visible){
            loadingText.visibility = View.VISIBLE
        }else{
            loadingText.visibility = View.GONE
        }
    }

    fun showLoadingImageView(visible: Boolean){
        if(visible){
            loadingImage.visibility = View.VISIBLE
        }else{
            loadingImage.visibility = View.GONE
        }
    }

    fun showLoadingProgressBar(visible: Boolean){
        if(visible){
            loadingProgress.visibility = View.VISIBLE
        }else{
            loadingProgress.visibility = View.GONE
        }
    }

}