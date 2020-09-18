package com.txl.commonlibrary.utils

import android.text.TextUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/18
 * description：
 */
object StringUtils {
    fun isNetUrl(url:String?):Boolean{
        if(TextUtils.isEmpty(url)){
            return false;
        }
        val regUrl = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$"
        val p: Pattern = Pattern.compile(regUrl)
        val m: Matcher = p.matcher(url)
        if (!m.matches()) {
            return false
        }
        return true;
    }
}