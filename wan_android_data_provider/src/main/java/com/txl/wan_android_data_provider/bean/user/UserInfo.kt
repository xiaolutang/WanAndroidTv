package com.txl.wan_android_data_provider.bean.user

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/10/31
 * description：
 */
data class UserInfo (var admin:Boolean?=false,var coinCount:Int?,var email:String?,var icon:String?,
                     val id:Int,var nickname:String?,var password:String?,var publishName:String?,
                     var token:String?,var type:Int?,var username:String,var collectIds:List<Int>?):Cloneable{



    override fun clone(): UserInfo {
       return UserInfo(this.admin,this.coinCount,this.email,this.icon,this.id,this.nickname,this.password, this.publishName,this.token,this.type,this.username,ArrayList<Int>(collectIds))
    }

}