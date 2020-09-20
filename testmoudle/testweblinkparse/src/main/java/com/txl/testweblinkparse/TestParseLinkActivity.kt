package com.txl.testweblinkparse

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.txl.weblinkparse.WebLinkParse
import kotlinx.android.synthetic.main.activity_test_parse_link.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TestParseLinkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_parse_link)
        tv_test.setOnClickListener {
            Log.d("TestParseLinkActivity","WebLinkParse click test")
            GlobalScope.launch {
                Log.d("TestParseLinkActivity","WebLinkParse suit url start")
                WebLinkParse.isSuitImagePath("https://blog.csdn.net/summerrse/article/details/108491905",
                        "https://img-blog.csdnimg.cn/20200909161743229.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3N1bW1lcnJzZQ==,size_16,color_FFFFFF,t_70#pic_center")
                Log.d("TestParseLinkActivity","WebLinkParse suit url finish")
                WebLinkParse.getMaxImgAddress("https://blog.csdn.net/summerrse/article/details/108491905")
            }

        }

    }
}
