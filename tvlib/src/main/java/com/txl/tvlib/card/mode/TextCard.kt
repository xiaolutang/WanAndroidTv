package com.txl.tvlib.card.mode

/**
 * 纯文本
 * */
class TextCard(var title:String?, date: Any?) : BaseCard(TYPE_TEXT, date) {
    override fun getCardType(): Int {
        return TYPE_TEXT
    }

    override fun getCardName(): String {
        return TextCard::class.java.simpleName
    }
}