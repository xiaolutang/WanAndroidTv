package com.txl.commonlibrary.utils.file;

import android.util.Log;

import com.txl.commonlibrary.utils.CloseableUtils;
import com.txl.commonlibrary.utils.Md5Utils;
import com.txl.commonlibrary.utils.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadUtils {
    private final static String TAG = "DownLoadUtils";
    /**
     * @param fileUrl 文件下载地址
     * @param destFileDir 文件目录
     * @param callback 文件下载回调
     * @param suffix 文件后缀
     * */
    public static void downLoadFile(String fileUrl, final String destFileDir,String suffix, IFileDownloadCallback callback){
        final String fileName = Md5Utils.MD5(fileUrl)+suffix;

        final File file = new File(destFileDir,fileName);
        Log.d(TAG,"downLoadFile file path  "+file.getPath() +"file url is "+fileUrl);
        if (file.exists()) {
            callback.onDownloadSuccess(file);
            return;
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        OkHttpUtils.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG,"downLoadFile onFailure "+e.getMessage());
                callback.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"downLoadFile onResponse ");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    Log.e(TAG,"response code is "+response.code());
                    if(response.code() != 200){
                        callback.onDownloadFailed();
                        return;
                    }
                    long total = response.body().contentLength();
                    Log.d(TAG,"downLoadFile onResponse total is "+total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        updateProgressCallback(callback,current*1.0f/total);
                    }
                    fos.flush();
                    successCallback(callback,file);
                } catch (IOException e) {
                    e.printStackTrace();
                    failedCallback(callback);
                } finally {
                    CloseableUtils.close(is);
                    CloseableUtils.close(fos);
                }
            }
        });
    }

    private static void successCallback(IFileDownloadCallback callback, File file){
        if(callback != null){
            callback.onDownloadSuccess(file);
        }
    }

    private static void failedCallback(IFileDownloadCallback callback){
        if(callback != null){
            callback.onDownloadFailed();
        }
    }

    private static void updateProgressCallback(IFileDownloadCallback callback, float percent){
        if(callback != null){
            callback.onProgressUpdate(percent);
        }
    }

    public interface IFileDownloadCallback {
        /**
         * @param percent  下载进度更新 0-1
         */
        void onProgressUpdate(float percent);

        void onDownloadSuccess(File downloadFile);

        void onDownloadFailed();
    }
}
