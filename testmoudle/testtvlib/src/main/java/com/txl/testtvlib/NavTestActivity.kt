package com.txl.testtvlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.txl.testtvlib.testrecyclerview.item.center.TestRecyclerViewFocusCenterActivity
import kotlinx.android.synthetic.main.activity_nav_test.*

class NavTestActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_test)
        initView()
    }

    private fun initView() {
        card_frame_01.setOnClickListener(this)
        card_frame_02.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            card_frame_01->{
                startActivity(Intent(this,MainActivity::class.java))
            }
            card_frame_02->{
                startActivity(Intent(this, TestRecyclerViewFocusCenterActivity::class.java))
            }
        }
    }
}