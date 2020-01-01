package com.huaqiyun.tvlib.card.mode

class ImageTextCard(var imageUrl:String?,var title:String?, date: Any?) : BaseCard(TYPE_IMAGE_TEXT, date) {
    override fun getCardName(): String {
        return ImageTextCard::class.java.simpleName
    }

    override fun getCardType(): Int {
        return TYPE_IMAGE_TEXT
    }

}