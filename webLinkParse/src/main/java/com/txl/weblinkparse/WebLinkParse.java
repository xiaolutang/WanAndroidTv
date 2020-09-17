package com.txl.weblinkparse;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/17
 * description：
 */
public class WebLinkParse {
    private static final String TAG = "WebLinkParse";

    public static String getMaxImgAddress(String linkUrl){
        String suitImg = "";

        try {
            Connection connect = Jsoup.connect(linkUrl);
            // 得到Document对象
            Document document = connect.get();
            // 查找所有img标签
            Elements imgs = document.getElementsByTag("img");
            BitmapFactory.Options maxOptions = null;
            // 遍历img标签并获得src的属性
            for (Element element : imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                Log.d(TAG,"link url :"+linkUrl+"  imgSrc :: "+imgSrc);
                if(!imgSrc.endsWith(".gif") && !imgSrc.endsWith(".png") && !imgSrc.endsWith(".jpg")){
                    continue;
                }
                URL url = new URL(imgSrc);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(30 * 1000);
                conn.setRequestMethod("GET");
                InputStream inStream = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inStream,null,options);
                if(maxOptions != null){
                    if(options.outWidth+options.outHeight > maxOptions.outWidth+maxOptions.outHeight){
                        maxOptions = options;
                        suitImg =  imgSrc;
                    }
                }else {
                    maxOptions = options;
                    suitImg =  imgSrc;
                }
            }
            URL url = new URL(linkUrl);
            //B站
            boolean bz = url.getHost().contains("www.bilibili.com");
            if(bz){
                Elements meta = document.getElementsByTag("meta");
                for (Element element:meta){
                    String itemprop = element.attr("itemprop");
                    if("image".equals(itemprop)){
                        String img = element.attr("content");
                        Log.d(TAG,"link url :"+linkUrl+" BZ  img :: "+img);
                        URL imgUrl = new URL(img);
                        HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                        conn.setConnectTimeout(30 * 1000);
                        conn.setRequestMethod("GET");
                        InputStream inStream = conn.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(inStream,null,options);
                        if(maxOptions != null){
                            if(options.outWidth+options.outHeight > maxOptions.outWidth+maxOptions.outHeight){
                                maxOptions = options;
                                suitImg = img;
                            }
                        }else {
                            maxOptions = options;
                            suitImg = img;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return suitImg;
    }
}
