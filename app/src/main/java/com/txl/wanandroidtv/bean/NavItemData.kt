package com.txl.wanandroidtv.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 导航数据结构
 * */
data class NavItemData(val type:String?,
                       val title:String?,
                       var imageResId:String?,
                       var imageUrl:String?):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(imageResId)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavItemData> {
        override fun createFromParcel(parcel: Parcel): NavItemData {
            return NavItemData(parcel)
        }

        override fun newArray(size: Int): Array<NavItemData?> {
            return arrayOfNulls(size)
        }
    }
}