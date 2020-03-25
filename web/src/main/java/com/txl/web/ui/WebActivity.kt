package com.txl.web.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.txl.ui_basic.BaseActivity
import com.txl.web.R
import kotlinx.android.synthetic.main.web_activity_web.*

class WebActivity : BaseActivity() {

    companion object{
        private const val TITLE = "title"
        private const val LINK = "link"

        /**
         * @param title 网页标题
         * @param url 网页链接
         * */
        fun openWebPage(context: Context,title:String, url:String){
            val intent = Intent(context,WebActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(TITLE,title)
            intent.putExtra(LINK,url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_activity_web)
        initViewAndData()
    }

    private fun initViewAndData(){
        val title = intent.getStringExtra(TITLE)
        val url = intent.getStringExtra(LINK)
        tv_title.text = title
        web_browse.loadUrl(url)
    }
}
