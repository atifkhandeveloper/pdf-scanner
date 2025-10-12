package com.myspps.pdfscanner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.model.ImageModel;
import com.myspps.pdfscanner.utils.CustomToast;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

public class CropperActivity extends AppCompatActivity {

    Context context = this;
    ArrayList<ImageModel> imageModelArray = new ArrayList<>();
    String imageType = "test";
    ArrayList<ImageModel> imageUriArray = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Disable Material 3 automatic edge-to-edge
        try {
            Class<?> edgeClass = Class.forName("androidx.activity.EdgeToEdge");
            edgeClass.getMethod("disable").invoke(null);
        } catch (Exception ignored) {}

        setContentView(R.layout.activity_cropper);

        // ✅ Disable edge-to-edge and prevent overlap on Android 14+
        Window window = getWindow();
        // Force content to appear below system bars
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        // Make sure system UI visibility is normal
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // Clear flags that cause layout overlap
        getWindow().clearFlags(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        // Restore normal status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        // --- UCrop setup ---
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);

        this.imageType = getIntent().getStringExtra("gallery");
        Log.e("TAG", "onCreate: " + this.imageType);

        int i = 0;
        if ("camera".equals(this.imageType)) {
            this.imageUriArray = getIntent().getParcelableArrayListExtra("imageUri");
            while (i < this.imageUriArray.size()) {
                UCrop.of(
                                this.imageUriArray.get(i).getUri(),
                                this.imageUriArray.get(i).getUri()
                        ).withAspectRatio(9f, 16f)
                        .withOptions(options)
                        .start(this);
                i++;
            }
        } else if ("glry".equals(this.imageType)) {
            this.stringArrayList = getIntent().getStringArrayListExtra("images");
            while (i < this.stringArrayList.size()) {
                UCrop.of(
                                Uri.fromFile(new File(this.stringArrayList.get(i))),
                                Uri.fromFile(new File(this.stringArrayList.get(i)))
                        ).withAspectRatio(9f, 16f)
                        .withOptions(options)
                        .start(this);
                i++;
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to quit without saving?")
                .setPositiveButton("I'm sure", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    CropperActivity.super.onBackPressed();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == RESULT_OK && i == UCrop.REQUEST_CROP) {
            if (intent != null) {
                this.imageModelArray.add(new ImageModel(UCrop.getOutput(intent)));
                goToFiltersActivity();
            }
        } else if (i2 == UCrop.RESULT_ERROR) {
            Throwable error = UCrop.getError(intent);
            Log.e("TAG", "onActivityResult: " + error);
            if (error != null) {
                new CustomToast(this.context, error.getMessage());
            }
        }
    }

    private void goToFiltersActivity() {
        Intent intent = new Intent(this.context, FiltersActivity.class);
        intent.putParcelableArrayListExtra("cameraCroppedImages", this.imageModelArray);
        intent.putExtra("camera", "Camera");
        startActivity(intent);
    }
}
