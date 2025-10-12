package com.myspps.pdfscanner.splashAds;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.MainActivity;
import com.myspps.pdfscanner.ads.MyApplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class SplashActivity extends AppCompatActivity {

    String var;
    SharedPreferences prefs;
    boolean isFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                OpenAppAds();
            }
        }, 5000);

        prefs = getSharedPreferences("onboarding", MODE_PRIVATE);
        isFinished = prefs.getBoolean("isFinished", false);

    }

    public void OpenAppAds() {
                goNext();

    }

    private void goNext() {
        loadOpenApp();
    }

    private void loadOpenApp() {

        if (isFinished) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
            finish();
        }


    }

}
