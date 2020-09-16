package com.txl.wanandroidtv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.txl.ui_basic.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        jumpToMainPage()
        finish()
    }

    private fun jumpToMainPage(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
