package com.myspps.pdfscanner.splashAds;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.MainActivity;
import com.myspps.pdfscanner.ads.MyApplication;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primarymain));
        }




        ((LinearLayout) findViewById(R.id.btnstart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPageMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ((LinearLayout) findViewById(R.id.btnrate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }
        });
        ((LinearLayout) findViewById(R.id.btnshare)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appName = getResources().getString(R.string.app_name);
                final String appPackageName = getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, appName + " : \nhttps://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        ((LinearLayout) findViewById(R.id.btnmore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + MyApplication.MoreApps));
                startActivity(intent);
            }
        });
        ((LinearLayout) findViewById(R.id.btnprivacy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPrivacy = new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.PrivacyPolicy));
                intentPrivacy.setPackage("com.android.chrome");
                startActivity(intentPrivacy);
            }
        });


    }

    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    private void ExitDialog() {

        final Dialog dialog = new Dialog(FirstPageMainActivity.this, R.style.DialogTheme);
        dialog.setContentView(R.layout.popup_exit_dialog);
        dialog.setCancelable(false);

        RelativeLayout no = (RelativeLayout) dialog.findViewById(R.id.no);
        RelativeLayout rate = (RelativeLayout) dialog.findViewById(R.id.rate);
        RelativeLayout yes = (RelativeLayout) dialog.findViewById(R.id.yes);



        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AppThankYouActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        dialog.show();

    }


}
