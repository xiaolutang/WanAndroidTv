package com.hqy.commonlibrary.utils

import android.view.View
import android.view.ViewParent

object ViewUtils {


    /**
     * @param root 父容器
     * @param targetView 需要被检查的目标View
     * @return true即将获取焦点的View是当前ViewGroup的直接或间接子元素
     */
    fun checkViewIsChildToRoot(root:View?,targetView: View?): Boolean {
        if(root == null || targetView == null){
            return false
        }
        var parent: ViewParent? = null
        var temp: View? = targetView
        while (temp != null) {
            parent = temp.parent
            if (parent === root) {//理论上从RecyclerView中搜索焦点一定能够找到这个。
                return true
            }
            if (parent is View) {//这样子来找有点耗时,有条件的时候优化
                temp = parent
            } else {
                break
            }
        }
        return false
    }
}