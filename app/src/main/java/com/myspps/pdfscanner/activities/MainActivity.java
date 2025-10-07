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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.myspps.pdfscanner.R;

import com.myspps.pdfscanner.fragments.DocumentsFragment;
import com.myspps.pdfscanner.fragments.HomeFragment;
import com.myspps.pdfscanner.utils.Permissions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class MainActivity extends DrawerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
    private static final String INTERSTITIAL_ID = "Interstitial_Android";


    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);





        getPermission();
        loadAd();


        this.packageName = getApplicationContext().getPackageName();
        BottomNavigationView bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        this.bottomNavigationView = bottomNavigationView2;
        bottomNavigationView2.setSelectedItemId(R.id.home);
        contextOfApplication = this;
        final String packageName2 = getPackageName();
        loadFragment(new HomeFragment());
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setDrawerTheme(new DrawerTheme((Context) this));
        addProfile(new DrawerProfile().setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.logo_png)).setBackground(getResources().getDrawable(R.drawable.drawer_bg)).setName(getString(R.string.profile_name)).setDescription(getString(R.string.profile_description)).setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
            public void onClick(DrawerProfile drawerProfile, long j) {
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_docs)).setTextPrimary("Docs").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.type = drawerItem.getTextPrimary().toString();
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.CAMERA") == 0) {
                    MainActivity.this.goToCameraActivity();
                    showAd();
                } else {
                    Permissions.requestCameraPermission(MainActivity.this.getApplicationContext());
                }
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_id_card)).setTextPrimary("ID Card").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.type = drawerItem.getTextPrimary().toString();
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.CAMERA") == 0) {

                } else {
                    Permissions.requestCameraPermission(MainActivity.this.getApplicationContext());
                }
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_book)).setTextPrimary("Book").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.type = drawerItem.getTextPrimary().toString();
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.CAMERA") == 0) {
                    showAd();
                } else {
                    Permissions.requestCameraPermission(MainActivity.this.getApplicationContext());
                }
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_id_photo)).setTextPrimary("ID Photo").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.type = drawerItem.getTextPrimary().toString();
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.CAMERA") == 0) {
                    showAd();
                } else {
                    Permissions.requestCameraPermission(MainActivity.this.getApplicationContext());
                }
            }
        }));
        addDivider();
        addItem(new DrawerItem().setTextPrimary("Import from").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_menu_gallery)).setTextPrimary("Gallery").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), ImagePickerActivity.class);
                    intent.putExtra("imageType", "Docs");
                    startActivity(intent);
                    return;
                }
                Permissions.requestReadStoragePermission(MainActivity.this.getApplicationContext());
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_menu_camera)).setTextPrimary("Camera").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.type = "Docs";
                if (ActivityCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), "android.permission.CAMERA") == 0) {
                  showAd();
                } else {
                    Permissions.requestCameraPermission(MainActivity.this.getApplicationContext());
                }
            }
        }));
        addDivider();
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_homeeeeeeeeeeeeeeee)).setTextPrimary("Home").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                MainActivity.this.closeDrawer();
            }
        }));
        addItem(new DrawerItem().setImage(getResources().getDrawable(R.drawable.ic_rateee)).setTextPrimary("Rate us").setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            public void onClick(DrawerItem drawerItem, long j, int i) {
                try {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName2)));
                } catch (ActivityNotFoundException unused) {
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName2)));
                }
            }
        }));
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

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment;
        int itemId = menuItem.getItemId();
        if (itemId == R.id.documents) {
            fragment = new DocumentsFragment();
            loadFragment(fragment);
        } else if (itemId != R.id.home) {
            fragment = null;
        } else {
            fragment = new HomeFragment();
            loadFragment(fragment);
        }
        return loadFragment(fragment);
    }

    
    public void goToCameraActivity() {
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        intent.putExtra("textFromButton", this.type);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getPermission() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, 0);
            }
        }
    }

    public void loadAd() {
        UnityAds.load(INTERSTITIAL_ID, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("UnityAds", "Ad loaded: " + placementId);
//                showAd(); // You can show right away or later
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                Log.e("UnityAds", "Failed to load: " + placementId + " - " + error + " - " + message);
            }
        });
    }

    public void showAd() {
        UnityAds.show(this, INTERSTITIAL_ID, new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowStart(String placementId) {
                Log.d("UnityAds", "Ad started: " + placementId);
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                Log.d("UnityAds", "Ad clicked: " + placementId);
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                Log.d("UnityAds", "Ad completed: " + placementId + " - " + state);
                loadAd();
                MainActivity.this.goToCameraActivity();


            }

            @Override
            public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                Log.e("UnityAds", "Ad failed: " + placementId + " - " + error + " - " + message);
                loadAd();
                MainActivity.this.goToCameraActivity();

            }
        });
    }

}
