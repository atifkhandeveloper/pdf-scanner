package com.myspps.pdfscanner.splashAds;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.MainActivity;
import com.myspps.pdfscanner.ads.MyApplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class PrivacyTermsActivity extends AppCompatActivity {

    Button accept_button;
    CheckBox first_check, second_check;
    Activity activity;

    private InterstitialAd interstitialAd;
    private boolean adIsLoading;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_terms);
        activity = PrivacyTermsActivity.this;

        loadAd();

        first_check = findViewById(R.id.first_check);
        second_check = findViewById(R.id.second_check);
        accept_button = findViewById(R.id.accept_button);
        accept_button.setOnClickListener(new android.view.View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(android.view.View v) {
                if (!first_check.isChecked() || !second_check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Check above options to continue", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //startActivity(new Intent(activity, PermissionPageActivity.class));
                    showInterstitial();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            accept_button.setText("Get Started");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void loadAd() {
        // Request a new ad if one isn't already loaded.
        if (adIsLoading || interstitialAd != null) {
            return;
        }
        adIsLoading = true;
        // [START load_ad]
        InterstitialAd.load(
                this,
                getResources().getString(R.string.inter),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.d(TAG, "Ad was loaded.");
                        PrivacyTermsActivity.this.interstitialAd = interstitialAd;
                        // [START_EXCLUDE silent]
                        adIsLoading = false;
                        // [START set_fullscreen_callback]
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        Log.d(TAG, "The ad was dismissed.");
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        PrivacyTermsActivity.this.interstitialAd = null;
                                        startActivity(new Intent(activity, MainActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d(TAG, "The ad failed to show.");
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        PrivacyTermsActivity.this.interstitialAd = null;
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "The ad was shown.");
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "The ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdClicked() {
                                        // Called when ad is clicked.
                                        Log.d(TAG, "The ad was clicked.");
                                    }
                                });
                        // [END set_fullscreen_callback]
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.getMessage());
                        interstitialAd = null;
                        // [START_EXCLUDE silent]
                        adIsLoading = false;
                        String error =
                                String.format(
                                        java.util.Locale.US,
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(),
                                        loadAdError.getCode(),
                                        loadAdError.getMessage());
                        startActivity(new Intent(activity, MainActivity.class));
                        finish();

                    }
                    // [END_EXCLUDE]
                });
        // [END load_ad]
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise restart the game.
        // [START show_ad]
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
            Log.d(TAG, "The interstitial ad is still loading.");
            // [START_EXCLUDE silent]
            startActivity(new Intent(activity, MainActivity.class));
            finish();
            loadAd();
            // [END_EXCLUDE]
        }
        // [END show_ad]
    }

}