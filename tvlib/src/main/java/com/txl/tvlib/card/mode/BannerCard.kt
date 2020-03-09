package com.txl.tvlib.card.mode

/**
 * 轮播图
 * */
const val TYPE_BANNER = 2
class BannerCard(var titles:ArrayList<String>,var images:ArrayList<String>,date: Any) : BaseCard(TYPE_BANNER, date) {
    override fun getCardType(): Int {
        return TYPE_BANNER
    }

    override fun getCardName(): String {
        return BannerCard::class.java.simpleName
    }
}