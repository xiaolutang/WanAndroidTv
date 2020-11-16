package com.txl.netmodel.okhttp.okhttp;

import android.os.Build;
import android.util.Log;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

/**
 * 全局的okHttpClient
 * */
public class OkHttpUtils {
    private static File cacheFile;
    //缓存大小为20M
    private static int cacheSize = 20 * 1024 * 1024;
    //创建缓存对象
    private static Cache cache;

    private static boolean initCache = false;

    public static void initCache(String cachePath, int cacheSize){
        if(initCache){
            return;
        }
        initCache = true;
        OkHttpUtils.cacheFile = new File(cachePath,"netCache");
        OkHttpUtils.cacheSize = cacheSize;
        cache = new Cache(cacheFile,OkHttpUtils.cacheSize);
    }

    public static OkHttpClient.Builder getOkhttpBuilder(){
        return OkHttpClientHolder.builder;
    }

    public static OkHttpClient getOkHttpClient(){
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            X509TrustManager[] list = {new X509TrustManager() {
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[]{};
//                }
//            }};
//            sslContext.init(null, list, new SecureRandom());
//            OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .callTimeout(30, TimeUnit.SECONDS)
//                    .hostnameVerifier((hostname, session) -> true)
//                    .followRedirects(true)
//                    .followSslRedirects(true)
//                    .retryOnConnectionFailure(true)
//                    .cache(null)
//
//                    .sslSocketFactory(sslContext.getSocketFactory(), list[0]);
//            return enableTls12OnPreLollipop(builder).build();
//        } catch (KeyManagementException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
        return OkHttpClientHolder.builder.build();
    }


    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder builder) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                builder.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                builder.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return builder;
    }

    private static class OkHttpClientHolder{
        private static OkHttpClient okHttpClient;
        private static OkHttpClient.Builder builder;
        static {
            try {
                System.setProperty("https.protocols", "TLSv1.2");
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                X509TrustManager[] list = {new X509TrustManager() {

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
                }};
                sslContext.init(null, list, new SecureRandom());
                builder = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .callTimeout(30, TimeUnit.SECONDS)
                        .hostnameVerifier((hostname, session) -> true)
                        .followSslRedirects(true)
                        .sslSocketFactory(new Tls12SocketFactory(sslContext.getSocketFactory()), list[0]);
                if(cache != null){
                    builder.cache( cache );
                }
                okHttpClient = builder.build();
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


    }
}
