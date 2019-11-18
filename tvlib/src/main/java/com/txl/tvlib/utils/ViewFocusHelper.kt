package com.txl.tvlib.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.txl.commonlibrary.utils.DrawableUtils

/**
 * 现阶段不要使用
 * 统一对元素获取焦点进行处理,待开发
 * */
class ViewFocusHelper : ViewTreeObserver.OnGlobalFocusChangeListener {
    private var _activity: Activity? = null
    private var _frameLayout: FrameLayout? = null

    private var _focusBdColor: Int = 0
    /**
     * 上一个焦点元素的背景
     */
    private var _oldFocusDrawable: Drawable? = null

    private val _currentFocusDrawable: Drawable? = null

    /**
     * call after activity setContentView
     */
    fun init(activity: Activity, focusBdColor: Int) {
        this._activity = activity
        this._focusBdColor = focusBdColor
        _frameLayout = FrameLayout(_activity!!)
        _frameLayout!!.isFocusable = false
        _frameLayout!!.setBackgroundColor(Color.TRANSPARENT)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        _frameLayout!!.viewTreeObserver.addOnGlobalFocusChangeListener(this)
        _activity!!.addContentView(_frameLayout, params)
    }

    fun destory() {
        if (_frameLayout != null) {
            _frameLayout!!.viewTreeObserver.removeOnGlobalFocusChangeListener(this)
        }
        _activity = null
    }

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        lostFocus(oldFocus)
        focusView(newFocus)
    }

    private fun focusView(newFocus: View?) {
        _oldFocusDrawable = newFocus?.background
        newFocus?.background = buildFocusBgDrawable()
    }

    private fun lostFocus(oldFocus: View?) {
        if (_oldFocusDrawable != null) {
            oldFocus?.background = _oldFocusDrawable
            _oldFocusDrawable = null
        }
    }

    /**
     * 这个东西需要灵活的定制
     */
    private fun buildFocusBgDrawable(): Drawable {
        return DrawableUtils.makeFramelessRectangleDrawable(_focusBdColor, 0f)
    }
}