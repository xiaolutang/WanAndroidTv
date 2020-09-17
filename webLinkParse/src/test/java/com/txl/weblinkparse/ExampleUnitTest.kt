package com.txl.weblinkparse

import android.graphics.BitmapFactory
import android.util.Log
import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private val TAG = "ParseBzTest"

    @Test
    fun parseBzImg() {
        val linkUrl = "https://www.bilibili.com/video/BV1b54y1U71P/"
        val connect = Jsoup.connect(linkUrl)
        try {
            // 得到Document对象
            val document = connect.get()
            // 查找所有img标签
            val imgs = document.getElementsByTag("img")
            val maxOptions: BitmapFactory.Options? = null
            // 遍历img标签并获得src的属性
            for (element in imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                val imgSrc = element.attr("abs:src")
                println(TAG+ "link url :$linkUrl  imgSrc :: $imgSrc")
            }
            val url = URL(linkUrl)
            //B站
            val bz = url.host.contains("www.bilibili.com")
            if (bz) {
                val meta = document.getElementsByTag("meta")
                for (element in meta) {
                    val itemprop = element.attr("itemprop")
                    if ("image" == itemprop) {
                        val imgSrc = element.attr("content")
                        println(TAG+ "link url :$linkUrl  content :: $imgSrc")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}