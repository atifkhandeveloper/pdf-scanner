package com.myspps.pdfscanner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_cropper);
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        this.imageType = getIntent().getStringExtra("gallery");
        Log.e("TAG", "onCreate: " + this.imageType);
        int i = 0;
        if (this.imageType.equals("camera")) {
            this.imageUriArray = getIntent().getParcelableArrayListExtra("imageUri");
            while (i < this.imageUriArray.size()) {
                UCrop.of(this.imageUriArray.get(i).getUri(), this.imageUriArray.get(i).getUri()).withAspectRatio(9.0f, 16.0f).withOptions(options).start((Activity) this.context);
                i++;
            }
        } else if (this.imageType.equals("glry")) {
            this.stringArrayList = getIntent().getStringArrayListExtra("images");
            while (i < this.stringArrayList.size()) {
                UCrop.of(Uri.fromFile(new File(this.stringArrayList.get(i))), Uri.fromFile(new File(this.stringArrayList.get(i)))).withAspectRatio(9.0f, 16.0f).withOptions(options).start((Activity) this.context);
                i++;
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "Do you want to quit without saving").setPositiveButton((CharSequence) "I'm sure", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                CropperActivity.this.lambda$onBackPressed$0$CropperActivity(dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) $$Lambda$CropperActivity$Ut0U8_vAWB3XA0th4e5eeugR7NQ.INSTANCE).show();
    }

    public  void lambda$onBackPressed$0$CropperActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        super.onBackPressed();
    }

    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 69) {
            if (intent != null) {
                this.imageModelArray.add(new ImageModel(UCrop.getOutput(intent)));
                goToFiltersActivity();
            }
        } else if (i2 == 96) {
            Throwable error = UCrop.getError(intent);
            Log.e("TAG", "onActivityResult: " + error);
            new CustomToast(this.context, error.getMessage());
        }
    }

    private void goToFiltersActivity() {
        Intent intent = new Intent(this.context, FiltersActivity.class);
        intent.putParcelableArrayListExtra("cameraCroppedImages", this.imageModelArray);
        intent.putExtra("camera", "Camera");
        startActivity(intent);
    }
}
