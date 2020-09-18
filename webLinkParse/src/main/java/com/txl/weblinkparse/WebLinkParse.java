package com.txl.weblinkparse;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/17
 * description：
 */
public class WebLinkParse {
    private static final String TAG = "WebLinkParse";

    public static String getMaxImgAddress(String linkUrl){
        MaxImageInfo maxImageInfo = new MaxImageInfo("");
        try {
            Log.d(TAG,"getMaxImgAddress link url :"+linkUrl);
            Connection connect = Jsoup.connect(linkUrl);
            // 得到Document对象
            Document document = connect.get();
            // 查找所有img标签
            Elements imgs = document.getElementsByTag("img");

            // 遍历img标签并获得src的属性
            for (Element element : imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                if (!isSuitImagePath(linkUrl, imgSrc)) continue;
                Log.d(TAG,"link url :"+linkUrl+"  imgSrc :: "+imgSrc);
                maxImageInfo = compareMaxImageInfo(maxImageInfo,new MaxImageInfo(imgSrc));
            }
            maxImageInfo = parseSpecialWebMaxImg(document,linkUrl,maxImageInfo);
            return maxImageInfo.imgPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否是一个合适的图片地址
     * */
    private static boolean isSuitImagePath(String linkUrl, String imgSrc) {
        if(TextUtils.isEmpty(imgSrc)){
            return false;
        }
        if(imgSrc.equals(linkUrl)){
            return false;
        }
        //判断是不是网络地址
        String regUrl = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
        Pattern p = Pattern.compile(regUrl);
        Matcher m = p.matcher(imgSrc);
        return m.matches();
    }

    /**
     * 获取一个图片最大的图片地址
     * @param old 一已知图片大小的
     * */
    private static MaxImageInfo compareMaxImageInfo(MaxImageInfo old, MaxImageInfo maxImageInfo){
        if(maxImageInfo == null || TextUtils.isEmpty(maxImageInfo.imgPath)){
            throw new IllegalArgumentException("maxImageInfo not be null and maxImageInfo.imgPath not be null");
        }
        if(old == null || TextUtils.isEmpty(old.imgPath)){
            return maxImageInfo;
        }
        try {
            URL url = new URL(maxImageInfo.imgPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            BitmapFactory.decodeStream(inStream,null,maxImageInfo.options);
            if(old.options != null){
                if(old.options.outWidth+old.options.outHeight > maxImageInfo.options.outWidth+maxImageInfo.options.outHeight){
                    return old;
                }else {
                    return maxImageInfo;
                }
            }else {
               return maxImageInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return old;//出现异常，说明新传入的不可用
        }
    }

    private static MaxImageInfo parseSpecialWebMaxImg(Document document,String linkUrl,MaxImageInfo maxImageInfo){
        MaxImageInfo temp = maxImageInfo;
        try {
            URL url = new URL(linkUrl);
            if(url.getHost().contains("www.bilibili.com")){//B站
                Elements meta = document.getElementsByTag("meta");
                for (Element element:meta){
                    String itemprop = element.attr("itemprop");
                    if("image".equals(itemprop)){
                        String img = element.attr("content");
                        if (!isSuitImagePath(linkUrl, img)) continue;
                        Log.d(TAG,"link url :"+linkUrl+" BZ  img :: "+img);
                        temp = compareMaxImageInfo(temp,new MaxImageInfo(img));
                    }
                }
            }else if(url.getHost().contains("mp.weixin.qq.com")){//微信
                Elements meta = document.getElementsByTag("img");
                for (Element element:meta){
                    String img = element.attr("data-src");
                    if (!isSuitImagePath(linkUrl, img)) continue;
                    Log.d(TAG,"link url :"+linkUrl+" weixin  img :: "+img);
                    temp = compareMaxImageInfo(temp,new MaxImageInfo(img));
                }
            }else if(url.getHost().contains("juejin.im")){//掘金
                Elements meta = document.getElementsByTag("img");
                for (Element element:meta){
                    String img = element.attr("data-src");
                    Log.d(TAG,"link url :"+linkUrl+" juejin  img :: "+img);
                    if(!TextUtils.isEmpty(img) && img.startsWith("//")){
                        img = "https:"+img;
                    }
                    if (!isSuitImagePath(linkUrl, img)) continue;
                    Log.d(TAG,"link url :"+linkUrl+" juejin  img :: "+img);
                    temp = compareMaxImageInfo(temp,new MaxImageInfo(img));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private static class MaxImageInfo{
        private String imgPath;
        private BitmapFactory.Options options;

        public MaxImageInfo(String imgPath) {
            this.imgPath = imgPath;
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
        }
    }
}
