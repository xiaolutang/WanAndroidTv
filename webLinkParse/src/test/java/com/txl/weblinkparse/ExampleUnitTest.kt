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
//        val linkUrl = "https://www.bilibili.com/video/BV1b54y1U71P/"
        val s = WebLinkParse.getMaxImgAddress("https://juejin.im/post/6873466220885049351")
        System.out.println("$TAG img :: $s")
    }
}