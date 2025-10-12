package com.myspps.pdfscanner.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.splashAds.OnBoardingActivity;
import com.myspps.pdfscanner.splashAds.PrivacyTermsActivity;
import com.myspps.pdfscanner.splashAds.SplashActivity;


public class SettingsFragment extends Fragment {

    private LinearLayout rateUs, shareApp, privacyPolicy, howToUse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        rateUs = view.findViewById(R.id.layout_rate_us);
        shareApp = view.findViewById(R.id.layout_share);
        privacyPolicy = view.findViewById(R.id.layout_privacy_policy);
        howToUse = view.findViewById(R.id.layout_how_to_use);

        // 🌟 Rate Us
        rateUs.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("market://details?id=" + requireContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to open Play Store", Toast.LENGTH_SHORT).show();
            }
        });

        // 🌟 Share App
        shareApp.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this awesome app:\nhttps://play.google.com/store/apps/details?id=" + requireContext().getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // 🌟 Privacy Policy
        privacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PrivacyTermsActivity.class));
        });

        // 🌟 How to Use
        howToUse.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OnBoardingActivity.class));
        });

        return view;
    }
}
