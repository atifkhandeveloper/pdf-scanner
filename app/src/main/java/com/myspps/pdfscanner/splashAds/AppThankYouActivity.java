package com.myspps.pdfscanner.splashAds;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.myspps.pdfscanner.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class AppThankYouActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_app);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        loadnative();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }



        RelativeLayout Exit = (RelativeLayout) findViewById(R.id.exitapp);
        RelativeLayout Rate = (RelativeLayout) findViewById(R.id.rate);

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void loadnative(){
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, getResources().getString(R.string.nativead))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                        template.setVisibility(VISIBLE);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }
}