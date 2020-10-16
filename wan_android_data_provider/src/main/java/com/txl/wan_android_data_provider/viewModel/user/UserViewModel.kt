package com.txl.wan_android_data_provider.viewModel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Copyright (c) 2020, 唐小陆 All rights reserved.
 * author：txl
 * date：2020/10/16
 * description：
 */
class UserViewModel :ViewModel(){
    fun loginOrRegister(userName:String, password:String){

    }
}

class UserViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    companion object{
        var userViewModel:UserViewModel? = null
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(userViewModel == null){
            userViewModel = UserViewModel()
        }
        return userViewModel as T
    }
}