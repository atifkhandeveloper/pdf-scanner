package com.myspps.pdfscanner.splashAds;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.myspps.pdfscanner.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_page_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primarymain));
        }




        checkMultiplePermissions(this);


        ((RelativeLayout) findViewById(R.id.btnPermission)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PermissionPageActivity.this, FirstPageMainActivity.class);
                startActivity(intent);
            }
        });

    }

    public static boolean checkMultiplePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (!addPermission(activity, arrayList2, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                arrayList.add("Read Storage");
            }
            if (!addPermission(activity, arrayList2, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                arrayList.add("Write Storage");
            }
            if (arrayList2.size() > 0) {
                activity.requestPermissions((String[]) arrayList2.toArray(new String[arrayList2.size()]), 124);
                return false;
            }
        }
        return true;
    }

    public static boolean addPermission(Activity activity, List<String> list, String str) {
        if (Build.VERSION.SDK_INT >= 23 && activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
            list.add(str);
            if (!activity.shouldShowRequestPermissionRationale(str)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
