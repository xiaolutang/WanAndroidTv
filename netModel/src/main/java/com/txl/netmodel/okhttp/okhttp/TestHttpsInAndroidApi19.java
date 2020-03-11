package com.txl.netmodel.okhttp.okhttp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class TestHttpsInAndroidApi19 {
    private static final String TAG = TestHttpsInAndroidApi19.class.getSimpleName();
    public static void test(){

//       AppExecutors.execNetIo(new Runnable() {
//           @Override
//           public void run() {
//               try {
//
//                   try {
//                       BufferedReader input = null;
//                       StringBuilder sb = null;
////                       URL url = new URL("https://gank.io/api/today");
//                       URL url = new URL("https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png");
//                       final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                           public X509Certificate[] getAcceptedIssuers() {
//                               return new X509Certificate[0];
//                           }
//                           public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                           }
//                           public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                           }
//                       }};
//                       HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                           @Override
//                           public boolean verify(String hostname, SSLSession session) {
//                               return true;
//                           }
//                       });
//                       SSLContext sc = SSLContext.getInstance("TLS");
//                       sc.init(null, trustAllCerts, new SecureRandom());
//                       Tls12SocketFactory sf = new Tls12SocketFactory(sc.getSocketFactory());
////                       HttpsURLConnection.setDefaultSSLSocketFactory(sf);
//                       HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                       connection.setSSLSocketFactory(new SSLSocketFactoryCompat(new X509TrustManager(){
//                           @Override
//                           public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                           }
//
//                           @Override
//                           public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                           }
//
//                           @Override
//                           public X509Certificate[] getAcceptedIssuers() {
//                               return new X509Certificate[0];
//                           }
//                       }));
//                       InputStream inputStream = connection.getInputStream();
//                       input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                       sb = new StringBuilder();
//                       String s;
//                       while ((s = input.readLine()) != null) {
//                           sb.append(s).append("\n");
//                       }
//                       AndroidLogWrapperUtil.d(TAG,"success :: "+sb.toString());
//                       inputStream.close();
//                   } catch (MalformedURLException e) {
//                       e.printStackTrace();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   } catch (NoSuchAlgorithmException e) {
//                       e.printStackTrace();
//                   } catch (KeyManagementException e) {
//                       e.printStackTrace();
//                   }
//
//
////                   httpGet("https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png");
//               }catch (Exception e){
//                   e.printStackTrace();
//               }
//           }
//       });

        if(false){
            return;
        }
        OkHttpClient cli;
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null, null);

            ArrayList<ConnectionSpec> arrayList = new ArrayList<>();
            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();
            arrayList.add(cs);
            cli = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .callTimeout(30, TimeUnit.SECONDS)
                    .sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()),x509TrustManager)
                    .connectionSpecs(arrayList)
                    .hostnameVerifier((hostname, session) -> true)
                    .build();




            cli = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory())
                    .build();
            cli = OkHttpUtils.getOkHttpClient();

            String url = "https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png";
//            知乎
//            String url = "https://www.zhihu.com/signin?next=%2F";
//            开源中国
//            String url = "https://zb.oschina.net/";
//            腾讯
//            String url = "https://www.tencent.com/zh-cn/index.html";
//            爱奇艺
//            String url = "https://www.iqiyi.com/";
//            新浪
//            String url = "https://www.sina.com.cn/";

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            cli.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"onFailure  url is :: "+url+"   " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG,"onResponse  url is :: "+url+"   " );
                }
            });


//            AppExecutors.execNetIo(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        httpGet("https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png");
//                        HttpsURLConnection httpURLConnection = (HttpsURLConnection) new URL("https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png").openConnection();
////                        HttpsURLConnection httpURLConnection = (HttpsURLConnection) new URL("https://gank.io/api/today").openConnection();
//                        /**
//                         * 设置HttpURLConnection对象的参数
//                         */
//                        httpURLConnection.setSSLSocketFactory(new SSLSocketFactoryCompat());
//                        // 设置请求方法为 GET 请求
//                        httpURLConnection.setRequestMethod("GET");
//                        //使用输入流
//                        httpURLConnection.setDoInput(true);
//                        //GET 方式，不需要使用输出流
//                        httpURLConnection.setDoOutput(false);
//                        //设置超时
//                        httpURLConnection.setConnectTimeout(10000);
//                        httpURLConnection.setReadTimeout(1000);
//                        //连接
//                        httpURLConnection.connect();
//                        //还有很多参数设置 请自行查阅
//                        //连接后，创建一个输入流来读取response
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
//                        String line = "";
//                        StringBuilder stringBuilder = new StringBuilder();
//                        String response = "";
//                        //每次读取一行，若非空则添加至 stringBuilder
//                        while((line = bufferedReader.readLine()) != null){
//                            stringBuilder.append(line);
//                        }
//                        //读取所有的数据后，赋值给 response
//                        response = stringBuilder.toString().trim();
//                        AndroidLogWrapperUtil.d(TAG," 打印响应 " + response);
//                        final String finalResponse = response;
//
//                        bufferedReader.close();
//                        httpURLConnection.disconnect();
//
//                    } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    public static String httpGet(String httpUrl) {
        BufferedReader input = null;
        StringBuilder sb = null;
        URL url = null;
        HttpURLConnection con = null;
//        String cre = Credentials.basic(username,password);//此处为伪代码
//        Log.d("request","Credentials:"+cre);
//        return response.request().newBuilder().header("Authorization",cre).build();
        try {
            url = new URL(httpUrl);
            try {
//                trustAllHosts();
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
//                https.addRequestProperty("Authorization",cre);
                Tls12SocketFactory sf = new Tls12SocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultSSLSocketFactory(sf);
                HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
//                https.setSSLSocketFactory(sf);
                con = (HttpURLConnection) url.openConnection();

                input = new BufferedReader(new InputStreamReader(con.getInputStream()));
                sb = new StringBuilder();
                String s;
                while ((s = input.readLine()) != null) {
                    sb.append(s).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } finally {
            // close buffered
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // disconnecting releases the resources held by a connection so they may be closed or reused
            if (con != null) {
                con.disconnect();
            }
        }

        return sb == null ? null : sb.toString();
    }

    public static void testAll(){
        List<String> urls = new ArrayList<>();
        urls.add("https://mserver.chinamcloud.cn/cms/mrzd/upload/Image/XGMRMZ/2019/10/24/1_d13a5d05c63245feb42c1e482f71dc3a.png");
        urls.add("https://mserver.chinamcloud.cn/");
//        知乎
        urls.add("https://www.zhihu.com/signin?next=%2F");
//        开源中国
        urls.add("https://zb.oschina.net/");
//        腾讯
        urls.add("https://www.tencent.com/zh-cn/index.html");
//        爱奇艺
        urls.add("https://www.iqiyi.com/");
//        新浪
        urls.add("https://www.sina.com.cn/");
        for (String url:urls){
            test(url);
        }
    }

    private static void test(String url){
        OkHttpClient cli = OkHttpUtils.getOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        cli.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG,"onFailure  url is :: "+url+"   " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"onResponse  url is :: "+url+"   " );
            }
        });
    }
}
