package com.txl.wanandroidtv.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 导航数据结构
 * */
data class NavItemData(/**
                        *导航的显示类型
                        */
                       val type:String?,
                       val title:String?,
                       var imageResId:String?,
                       var imageUrl:String?,
                       val isHome:Boolean = false,
                       /**
                        * 导航数据类型
                        * -1为在开发中
                        * */
                       val navType:Int = -1):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()==1
            )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(imageResId)
        parcel.writeString(imageUrl)
        parcel.writeInt(if(isHome){1}else{0} )
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