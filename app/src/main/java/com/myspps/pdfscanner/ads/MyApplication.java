package com.myspps.pdfscanner.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;



import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private static final String ONESIGNAL_APP_ID = "";  //add your one signal key

    public static SharedPreferences sharedPreferencesInApp;
    public static SharedPreferences.Editor editorInApp;

    private Activity currentActivity;


    /*public static String AdMob_Banner = "";
    public static String AdMob_Int = "";
    public static String AdMob_Int2 = "";
    public static String AdMob_NativeAdvance = "";
    public static String AdMob_NativeAdvance2 = "";
    public static String App_Open = "";*/




    public static Context context1;


    public static String FbBanner = "";
    public static String Fbnative = "";
    public static String FbInter = "";
    public static String FbNativeB = "";


    //test ads id fb
   /* public static String FbBanner = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String Fbnative = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String FbInter = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String FbNativeB = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";*/


    public static String MAX_Banner = "";
    public static String MAX_Int = "";
    public static String MAX_Native = "";

    /*public static String MAX_Banner = "ffad804ad2a0368e";
    public static String MAX_Int = "096bd87a62a84493";
    public static String MAX_Native = "a3bc1621ddcc28b4";*/


    public static String Type = "admob";   //admob | fb | max | mix


    public static int click = 2;
    public static int backclick = 2;
    public static int click1 = 0;

    public static String MoreApps = "";
    public static String PrivacyPolicy = "";

    private static final String UNITY_GAME_ID = "5937712"; // from Unity Dashboard
    private static final boolean TEST_MODE = false;


    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);


        sharedPreferencesInApp = getSharedPreferences("my", MODE_PRIVATE);
        editorInApp = sharedPreferencesInApp.edit();


        context1 = getApplicationContext();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);


        UnityAds.initialize(this, UNITY_GAME_ID, TEST_MODE, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.d("UnityAds", "Initialization complete.");
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.e("UnityAds", "Initialization failed: " + error + " - " + message);
            }
        });

    }

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void showAdIfAvailable(
            @NonNull Activity activity,
            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    private class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager";

        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        private long loadTime = 0;

        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                        }
                    });
        }

        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }


            Log.d(LOG_TAG, "Will show ad.");

        }

    }


}