package com.myspps.pdfscanner.splashAds;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImageView btnNext;

    private TextView btnSkip;
    private LinearLayout layoutIndicator;
    private OnBoardingAdapter adapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        loadnative();

        final View root = findViewById(android.R.id.content); // entire activity content


        prefs = getSharedPreferences("onboarding", MODE_PRIVATE);

        viewPager = findViewById(R.id.viewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        // Setup onboarding screens
        List<OnBoardingItem> items = new ArrayList<>();
        items.add(new OnBoardingItem(R.drawable.onb_one, "Welcome to Document Scanner", "Digitize reciept, cotracts, notes, and more - anytime anywhere"));
        items.add(new OnBoardingItem(R.drawable.onb_two, "Convert Image into PDF", "Transform scanned documents or photos into high quality PDF files in just one Tap. Ready to store, print and share securely."));
        items.add(new OnBoardingItem(R.drawable.onb_three, "Donwload and Share", "Share your scanned document securely and share them as PDFs or Images through email, messaging app or cloud services - All in one Tap "));

        adapter = new OnBoardingAdapter(items);
        viewPager.setAdapter(adapter);

        // Setup dots
        setupIndicators(items.size());
        setCurrentIndicator(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() + 1 < adapter.getItemCount()) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_inactive)
            );
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicatior_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_inactive)
                );
            }
        }
    }

    private void finishOnboarding() {
        prefs.edit().putBoolean("isFinished", true).apply();
        startActivity(new Intent(OnBoardingActivity.this, PrivacyTermsActivity.class));
        finish();
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


