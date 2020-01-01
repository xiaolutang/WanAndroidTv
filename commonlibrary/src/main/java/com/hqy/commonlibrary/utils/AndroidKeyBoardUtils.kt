package com.hqy.commonlibrary.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidKeyBoardUtils {
    fun hideKeyBoard(currentFocus: View){
        val imm: InputMethodManager = currentFocus.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(imm.isActive ){
            //拿到view的token 不为空
            if (currentFocus.windowToken !=null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
        }
    }

    fun showKeyBoard(currentFocus: View){
        val imm: InputMethodManager = currentFocus.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(imm.isActive ){
            imm.showSoftInput(currentFocus, 0)
        }
    }
}