package com.myspps.pdfscanner.splashAds;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.ads.MyApplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    String var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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


    }

    public void OpenAppAds() {
                goNext();

    }

    private void goNext() {
        loadOpenApp();
    }

    private void loadOpenApp() {

            Intent i = new Intent(SplashActivity.this, FirstPageMainActivity.class);
            startActivity(i);

    }

}
