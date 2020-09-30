package com.txl.web.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.txl.web.R;

import java.util.List;

/**
 * Copyright (c) 2020 唐小陆 All rights reserved.
 * author：txl
 * date：2020/9/30
 * description：
 */
public class WebViewThirdAppJumpUtils {
    public static String Msg_Title = "来自网页的消息 ";
    private Context context;

    public WebViewThirdAppJumpUtils(Context context) {
        this.context = context;
    }

    public boolean matchDefaultUrl(String url) {
        final Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(url));
        if (queryIntentActivities(context, intent)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String msg = context.getResources().getString(R.string.web_webview_opennative, context.getResources().getString(R.string.app_name), queryIntentActivityName(context, intent));
            builder.setTitle(Msg_Title);
            builder.setMessage(msg);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.web_cancel, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(R.string.web_sure, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(intent);
                }
            });
            builder.show();
            return true;
        }
        return false;
    }

    public static boolean queryIntentActivities(Context context, Intent intent) {
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfoList != null && resolveInfoList.size() > 0;
    }


    public static String queryIntentActivityName(Context context, Intent intent) {
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            return resolveInfoList.get(0).loadLabel(context.getPackageManager()).toString();
        }
        return null;
    }
}
