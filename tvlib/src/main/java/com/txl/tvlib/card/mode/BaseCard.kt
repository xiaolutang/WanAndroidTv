package com.txl.tvlib.card.mode

import android.text.TextUtils
import android.util.SparseArray
import java.lang.RuntimeException

/**
 * 文字
 * */
const val TYPE_TEXT = 0
/**
 * 图片文字
 * */
const val TYPE_IMAGE_TEXT = 1

private val arrayTypes = SparseArray<String>()

/**
 * 为了防止type冲突，每次试下card就自己添加注册一次，需要保证每一个card的name是不同的
 * */
fun registerType(key:Int, name:String){
    if(TextUtils.isEmpty(name)){
        throw RuntimeException("not support empty name")
    }
    if(arrayTypes.get(key) != null && name != arrayTypes.get(key)){
        throw RuntimeException("has register the type key :: $key last type name is ${arrayTypes.get(key)}")
    }
    arrayTypes.put(key,name)
}

abstract class BaseCard(val type:Int= TYPE_TEXT,
                    var date:Any?) {
    init {
        registerType(getCardType(),getCardName())
    }

    abstract fun getCardType():Int
    abstract fun getCardName():String
}