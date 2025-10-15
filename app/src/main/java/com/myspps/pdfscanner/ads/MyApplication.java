package com.myspps.pdfscanner.ads;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;


public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {


    public static SharedPreferences sharedPreferencesInApp;
    public static SharedPreferences.Editor editorInApp;

    private Activity currentActivity;

    public static Context context1;

    public static String MoreApps = "";
    public static String PrivacyPolicy = "";

    private AppOpenAdManager appOpenAdManager;
    private Activity activity;


    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Register lifecycle observer to detect app foreground
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        appOpenAdManager = new AppOpenAdManager();



        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();



        sharedPreferencesInApp = getSharedPreferences("my", MODE_PRIVATE);
        editorInApp = sharedPreferencesInApp.edit();


        context1 = getApplicationContext();

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        // Show the ad (if available) when the app moves to foreground.
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}
    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }
    @Override
    public void onActivityResumed(@NonNull Activity activity) { currentActivity = activity; }
    @Override
    public void onActivityPaused(@NonNull Activity activity) {}
    @Override
    public void onActivityStopped(@NonNull Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}

    // Helper methods to control from SplashActivity
    public void showAdAfterSplash(Activity activity, OnShowAdCompleteListener listener) {
        appOpenAdManager.showAdIfAvailable(activity, listener);
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    // ------------------ INNER CLASS ------------------
    private class AppOpenAdManager {
        private static final String AD_UNIT_ID = "ca-app-pub-5969006643846426/7296431541";
        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        private long loadTime = 0;

        private boolean isAdAvailable() {
            return appOpenAd != null && (new Date().getTime() - loadTime < 4 * 3600000);
        }

        public void loadAd(Context context) {
            if (isLoadingAd || isAdAvailable()) return;
            isLoadingAd = true;

            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, AD_UNIT_ID, request, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    Log.d(TAG, "App open ad loaded");
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = new Date().getTime();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.d(TAG, "App open ad failed to load: " + loadAdError.getMessage());
                    isLoadingAd = false;
                }
            });
        }

        public void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, () -> {});
        }

        public void showAdIfAvailable(@NonNull final Activity activity, @NonNull final OnShowAdCompleteListener listener) {
            if (isShowingAd) {
                Log.d(TAG, "Ad is already showing.");
                return;
            }

            if (!isAdAvailable()) {
                Log.d(TAG, "Ad not ready. Loading a new one.");
                loadAd(activity);
                listener.onShowAdComplete();
                return;
            }

            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    appOpenAd = null;
                    isShowingAd = false;
                    listener.onShowAdComplete();
                    loadAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    appOpenAd = null;
                    isShowingAd = false;
                    listener.onShowAdComplete();
                    loadAd(activity);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    isShowingAd = true;
                }
            });

            isShowingAd = true;
            appOpenAd.show(activity);
        }
    }
}