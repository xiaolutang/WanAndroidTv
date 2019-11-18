package com.txl.screenadaptation.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class ScerenAdapterUtils {
    /**
     * tv 设计稿的宽度是1920dp
     * */
    private static final float SCREEN_WIDTH = 1920.0f;
    private static float sNoncompatDensity;
    private static float sNoncompatScaleDensity;


    public static void setCustomDensity(Activity activity, final Application application){
        final DisplayMetrics appDisplayMetries = application.getResources().getDisplayMetrics();
        if(sNoncompatDensity == 0){
            sNoncompatDensity = appDisplayMetries.density;
            sNoncompatScaleDensity = appDisplayMetries.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if(newConfig.fontScale > 0){
                        sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
            final float targetDensity = appDisplayMetries.widthPixels / SCREEN_WIDTH;
            final float targetScaleDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity);
            final int targetDensityDpi = (int) (160 * targetDensity);

            appDisplayMetries.density = targetDensity;
            appDisplayMetries.scaledDensity = targetScaleDensity;
            appDisplayMetries.densityDpi = targetDensityDpi;

            final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

            activityDisplayMetrics.density = targetDensity;
            activityDisplayMetrics.scaledDensity = targetScaleDensity;
            activityDisplayMetrics.densityDpi = targetDensityDpi;
        }
    }
}
