package com.txl.tvlib.card

const val TYPE_TEXT = 0
const val TYPE_NAV_TEXT = 1

open class BaseCard(val type:Int=TYPE_TEXT,
                    var date:Any?) {
}