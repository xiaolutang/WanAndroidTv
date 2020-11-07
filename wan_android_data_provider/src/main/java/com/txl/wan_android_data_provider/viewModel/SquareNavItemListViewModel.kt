package com.txl.wan_android_data_provider.viewModel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tencent.mmkv.MMKV
import com.txl.commonlibrary.utils.Md5Utils
import com.txl.commonlibrary.utils.StringUtils.isNetUrl
import com.txl.commonlibrary.utils.exector.AppExecutors
import com.txl.wan_android_data_provider.data.DataDriven
import com.txl.weblinkparse.WebLinkParse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/3/26
 * description：
 */
class SquareNavItemListViewModel: AbsNavItemListViewModel() {

    override fun getPageData() {
        getSquareNavItemListData()
    }

    private fun getSquareNavItemListData() {
        GlobalScope.launch {
            val response = DataDriven.getSquareArticleList(currentPage)
            if(response.netSuccess()){//fixme 在子线程加载
                for (item in  response.data!!.datas){
                    if (!isNetUrl(item.link)) {
                        continue
                    }
                    item.imagePath = getMaxImageAddress(item.link)
                }
                val resourceBoundary = ResourceBoundary<Any>(STATE_LOADED, 0, "success", response, currentPage)
                data.postValue(resourceBoundary)
            }else{
                val resourceBoundary = ResourceBoundary<Any>(STATE_ERROR, -1, "failed", response, currentPage)
                data.postValue(resourceBoundary)
            }
        }
    }

    private fun getMaxImageAddress(linkUrl: String): String? {
        val md5 = Md5Utils.MD5(linkUrl)
        var imgpath = MMKV.defaultMMKV().decodeString(md5)
        if (!TextUtils.isEmpty(imgpath)) {
            return imgpath
        }
        imgpath = WebLinkParse.getMaxImgAddress(linkUrl)
        if (!TextUtils.isEmpty(imgpath)) {
            MMKV.defaultMMKV().encode(md5, imgpath)
        }
        return imgpath
    }
}

class SquareViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SquareNavItemListViewModel() as T
    }
}
