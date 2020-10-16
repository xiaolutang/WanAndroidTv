package com.txl.wanandroidtv.ui.user

import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.txl.ui_basic.fragment.BaseFragment
import com.txl.wan_android_data_provider.viewModel.user.UserViewModel
import com.txl.wan_android_data_provider.viewModel.user.UserViewModelFactory
import com.txl.wanandroidtv.R
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/10/15
 * description：
 */
class LoginFragment:BaseFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        val userViewModel: UserViewModel = ViewModelProvider(this, UserViewModelFactory()).get(UserViewModel::class.java)
        login.setOnClickListener {
            val userName = username.text.toString()
            val password = password.text.toString()
            if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
                toast("请输入账号和密码")
                return@setOnClickListener
            }
            userViewModel.loginOrRegister(userName, password)
        }
    }

    override fun initData() {
        
    }
}