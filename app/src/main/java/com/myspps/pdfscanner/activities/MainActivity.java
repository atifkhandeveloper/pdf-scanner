package com.myspps.pdfscanner.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import com.myspps.pdfscanner.R;

import com.myspps.pdfscanner.fragments.DocumentsFragment;
import com.myspps.pdfscanner.fragments.HomeFragment;
import com.myspps.pdfscanner.fragments.SettingsFragment;
import com.myspps.pdfscanner.splashAds.AppThankYouActivity;
import com.myspps.pdfscanner.splashAds.SplashActivity;
import com.myspps.pdfscanner.utils.Permissions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static boolean b = true;
    public static Context contextOfApplication;
    Activity activity = this;
    Button bookButton;
    BottomNavigationView bottomNavigationView;
    Context context = this;
    Button docsButton;
    Button idCardButton;
    Button idPhotoButton;
    String packageName;
    String type;
    private TextView tvTitle;

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView((int) R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        tvTitle = findViewById(R.id.tvTitle);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Load default fragment
        loadFragment(new HomeFragment(), "Home");

        getPermission();


        this.packageName = getApplicationContext().getPackageName();
        BottomNavigationView bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        this.bottomNavigationView = bottomNavigationView2;
        bottomNavigationView2.setSelectedItemId(R.id.home);
        contextOfApplication = this;
        final String packageName2 = getPackageName();
        loadFragment(new HomeFragment());
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
//        setDrawerTheme(new DrawerTheme((Context) this));
//        addProfile(new DrawerProfile().setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.logo_png)).setBackground(getResources().getDrawable(R.drawable.drawer_bg)).setName(getString(R.string.profile_name)).setDescription(getString(R.string.profile_description)).setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
//            public void onClick(DrawerProfile drawerProfile, long j) {
//            }
//        }));
    }
    private Boolean getBoolFromPref(Context context2, String str, String str2) {
        return Boolean.valueOf(context2.getSharedPreferences(str, 0).getBoolean(str2, false));
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";

        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            fragment = new HomeFragment();
            title = "Home";
        } else if (itemId == R.id.documents) {
            fragment = new DocumentsFragment();
            title = "Documents";
        } else if (itemId == R.id.settings) {
            fragment = new SettingsFragment();
            title = "Settings";
        }

        if (fragment != null) {
            loadFragment(fragment, title);
            return true;
        }

        return false;
    }

    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        tvTitle.setText(title);
    }

    public void goToCameraActivity() {
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        intent.putExtra("textFromButton", this.type);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, AppThankYouActivity.class));
        finish();
    }

    private void getPermission() {

        // Android 11+ (API 30+) - Check for Manage External Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    // fallback if direct intent fails
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        }

        // Android 13+ (API 33, 34, 35)
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO
                        },
                        100);
            }
        }

        // Android 6–10 (API 23–29)
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        101);
            }
        }
    }



}
