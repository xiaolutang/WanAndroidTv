package com.txl.testtvlib.testborder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.txl.testtvlib.R
import kotlinx.android.synthetic.main.activity_test_border_view.*

class TestBorderViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_border_view)
        border_02.setBorderDrawable(ContextCompat.getDrawable(this,R.drawable.border))
    }
}