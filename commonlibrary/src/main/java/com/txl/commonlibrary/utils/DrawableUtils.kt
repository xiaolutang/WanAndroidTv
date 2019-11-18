package com.txl.commonlibrary.utils
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.core.graphics.drawable.DrawableCompat
import org.jetbrains.anko.withAlpha


/**
 * citsin
 * 2019-09-02
 */
object DrawableUtils{


    /**
     * 无边框矩形Drawable
     * @param color 填充颜色
     * @param radius 圆角半径
     */
    fun makeFramelessRectangleDrawable(color:Int, radius:Float):Drawable{
        val drawable = GradientDrawable()
        drawable.setShape(GradientDrawable.RECTANGLE)
        drawable.setColor(color)
        drawable.setCornerRadius(radius)
        return drawable
    }

    /**
     * 边框矩形Drawable
     * @param width 边框宽度
     * @param borderColor 边框颜色
     * @param radius 圆角半径
     * @param solidColor 填充颜色
     *
     */
    fun makeBorderRectangleDrawable(width:Int,radius:Float,borderColor: Int,solidColor:Int = Color.TRANSPARENT):Drawable{
        val drawable = GradientDrawable()
        drawable.setShape(GradientDrawable.RECTANGLE)
        drawable.setColor(solidColor)
        drawable.setStroke(width,borderColor)
        drawable.setCornerRadius(radius)
        return drawable
    }

    /**
     * 输入框Drawable
     * @param width 边框宽度
     * @param borderColor 边框颜色
     * @param radius 圆角半径
     * @param solidColor 填充颜色
     *
     */
    fun makeInputDrawable(width:Int,radius:Float,borderColor: Int,solidColor:Int = Color.WHITE):Drawable{
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_focused), makeBorderRectangleDrawable(width,radius,borderColor,solidColor))
        drawable.addState(intArrayOf(android.R.attr.state_selected),  makeBorderRectangleDrawable(width,radius,borderColor,solidColor))
        drawable.addState(intArrayOf(), makeFramelessRectangleDrawable(solidColor,radius))
        return drawable
    }




//    /**
//     * 边框矩形Drawable
//     * @param color 边框颜色
//     * @param radius 圆角半径
//     */
//    fun makeBorderStateListDrawable(normal:Int,focuse:Int,width:Int,radius:Float):Drawable{
//        val drawable = StateListDrawable()
//        drawable.addState(intArrayOf(android.R.attr.state_focused), makeBorderRectangleDrawable(focuse,width,radius))
//        drawable.addState(intArrayOf(android.R.attr.state_selected), makeBorderRectangleDrawable(focuse,width,radius))
//        drawable.addState(intArrayOf(), makeBorderRectangleDrawable(normal,width,radius))
//        return drawable
//    }


    /**
     * @param colorNormal 默认状态下的填充色
     * @param colorFocus  获取焦点状态下的填充色
     * @param radius      圆角半径
     */
    fun makeFramelessStateListDrawable(colorNormal:Int,colorFocus:Int,radius:Float):Drawable{
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_focused), makeFramelessRectangleDrawable(colorFocus,radius))
        drawable.addState(intArrayOf(), makeFramelessRectangleDrawable(colorNormal,radius))
        return drawable
    }

    /**
     * @param colorNormal 默认状态下的填充色
     * @param colorFocus  获取焦点状态下的填充色
     * @param colorSelect 被选中的颜色
     * @param radius      圆角半径
     */
    fun makeFramelessStateListDrawable(colorNormal:Int,colorFocus:Int,colorSelect:Int,radius:Float):Drawable{
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_focused), makeFramelessRectangleDrawable(colorFocus,radius))
        drawable.addState(intArrayOf(android.R.attr.state_selected), makeFramelessRectangleDrawable(colorSelect,radius))
        drawable.addState(intArrayOf(), makeFramelessRectangleDrawable(colorNormal,radius))
        return drawable
    }

    /**
     * @param drawableNormal 默认状态下的drawable
     * @param drawableFocus  获取焦点状态下的drawable
     * @param drawableSelect 被选中的drawable
     */
    fun makeFramelessStateListDrawable(drawableNormal:Drawable,drawableFocus:Drawable,drawableSelect:Drawable):Drawable{
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_focused), drawableFocus)
        drawable.addState(intArrayOf(android.R.attr.state_selected),drawableSelect)
        drawable.addState(intArrayOf(), drawableNormal)
        return drawable
    }

    fun addState(stateListDrawable: StateListDrawable,state:IntArray,drawable: Drawable){
        stateListDrawable.addState(state,drawable)
    }

    /**
     * @param colorFocus 获取焦点状态时的颜色
     * @param colorNormal 默认状态的颜色
     */
    fun makeStateListColor(colorFocus:Int,colorNormal:Int = colorFocus.withAlpha(128)):ColorStateList{
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_focused)
        states[1] = intArrayOf()
        val colors = intArrayOf(colorFocus,colorNormal)
        return  ColorStateList(states, colors)
    }

    /**
     * @param colorFocus 获取焦点状态时的颜色
     * @param colorNormal 默认状态的颜色
     * @param colorSelect 选中时的颜色
     */
    fun makeStateListColor(colorFocus:Int,colorNormal:Int = colorFocus.withAlpha(128),colorSelect:Int=colorFocus):ColorStateList{
        val states = arrayOfNulls<IntArray>(3)
        states[0] = intArrayOf(android.R.attr.state_focused)
        states[1] = intArrayOf(android.R.attr.state_selected)
        states[2] = intArrayOf()
        val colors = intArrayOf(colorFocus,colorSelect,colorNormal)
        return  ColorStateList(states, colors)
    }


    /**
     * 给图片着色
     * */
    fun <T : Drawable> tintDrawable(color: Int, drawable: Drawable): T {
        var newDrawable = DrawableCompat.wrap(drawable)
        setBitmapDrawableTintColor(color, newDrawable)
        newDrawable = newDrawable.mutate()
        newDrawable.invalidateSelf()
        return newDrawable as T
    }

    /**
     * 给按状态图片着色
     * */
    fun <T : Drawable> tintDrawable(colorStateList: ColorStateList, drawable: Drawable): T {
        var newDrawable = DrawableCompat.wrap(drawable)
        setBitmapDrawableTintColor(colorStateList, newDrawable)
        newDrawable = newDrawable.mutate()
        newDrawable.invalidateSelf()
        return newDrawable as T
    }

    private fun setBitmapDrawableTintColor(color: Int, vararg drawables: Drawable) {
        setBitmapDrawableTintColor(ColorStateList.valueOf(color), *drawables)
    }

    private fun setBitmapDrawableTintColor(colorStateList: ColorStateList, vararg drawables: Drawable) {
        for (item in drawables) {
            DrawableCompat.setTintList(item, colorStateList)
        }
    }


}
