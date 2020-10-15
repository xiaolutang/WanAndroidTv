package com.txl.ui_basic.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.txl.ui_basic.R


object PopWindowUtils {
    /**
     * 只有一个确认按钮的
     */
    fun showPositivePop(context: Context, title: String, content: String, listener: NoticeActionListener? = null) {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_bgs, null)
        val pop = buildPopupWindow(context, view)
        view.findViewById<TextView>(R.id.tx_title).text = title
        view.findViewById<TextView>(R.id.tx_content).text = content
        view.findViewById<View>(R.id.tx_make_sure).setOnClickListener { listener?.onPositive(pop) }
        pop.setOnDismissListener {
            listener?.onPositive(pop)
        }
    }

    /**
     * 确认和取消
     */
    fun showChoicePop(context: Context, title: String?, content: String?, alpha: Float, listener: NoticeActionListener
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_choice_bgs, null)
        val pop = buildPopupWindow(context, view, alpha)
        view.findViewById<TextView>(R.id.tx_title).let {
            if (title == null) {
                it.visibility = View.GONE
            } else {
                it.text = title
            }
        }
        view.findViewById<TextView>(R.id.tx_content).let {
            if (content == null) {
                it.visibility = View.GONE
            } else {
                it.text = content
            }
        }
        view.findViewById<View>(R.id.tx_make_sure).setOnClickListener { listener.onPositive(pop) }
        view.findViewById<View>(R.id.tx_cancel).setOnClickListener { listener.onNegative(pop) }
    }

    fun showWaitPop(context: Context, message: String): PopupWindow {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_wait_notice, null)
        val pop = buildPopupWindow(context, view)
        view.findViewById<TextView>(R.id.tx_title).text = message
        return pop
    }

    /**
     * 用于文稿加载的转圈圈
     */
    fun showLoadPop(context: Context): PopupWindow {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_loading_l, null)
        return buildPopupWindow(context, view)
    }

    /**
     * 快速构建一个屏幕中心显示的PopWindow
     */
    fun buildPopupWindow(context: Context, view: View, alpha: Float = 1f,onDismissListener: PopupWindow.OnDismissListener?=null): PopupWindow {
        val pop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)
        pop.showAtLocation(view, Gravity.CENTER, 0, 0)
        if (alpha != 1f) {
            alpha(context, alpha)
        }
        pop.isOutsideTouchable = false
        pop.isFocusable = false
        pop.setBackgroundDrawable(ColorDrawable())
        pop.setOnDismissListener {
            if (alpha != 1f) {
                alpha(context, 1f)
                onDismissListener?.onDismiss()
            }
        }
        return pop
    }


    private fun alpha(context: Context, alpha: Float) {
        if (context is Activity) {
            val lp = context.window.attributes
            lp.alpha = alpha
            context.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            context.window.attributes = lp
        }
    }

    interface NoticeActionListener {
        fun onPositive(pop: PopupWindow)
        fun onNegative(pop: PopupWindow) {
            pop.dismiss()
        }
    }
}