package com.txl.wan_android_data_provider.viewModel.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.txl.wan_android_data_provider.bean.user.UserInfo
import com.txl.wan_android_data_provider.data.DataDriven
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/10/16
 * description：可以借助ViewModel管理用户信息,如何共享viewModel
 */
class UserViewModel :ViewModel(){

    val userInfo: MutableLiveData<UserInfo?> = MutableLiveData()

    fun login(userName:String, password:String){
        GlobalScope.launch {
            val response = DataDriven.login(userName,password)
            if(response.netSuccess()){
                userInfo.postValue(response.data)
            }else{
                userInfo.postValue(null)
            }
        }
    }
}

class UserViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    companion object{
        var userViewModel:UserViewModel? = null
            private set
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(userViewModel == null){
            userViewModel = UserViewModel()
        }
        return userViewModel as T
    }
}