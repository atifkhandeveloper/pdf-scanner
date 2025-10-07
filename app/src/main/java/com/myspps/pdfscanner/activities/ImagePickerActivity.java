package com.myspps.pdfscanner.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.adapters.ImagePickerAdapter;
import com.myspps.pdfscanner.interfaces.ItemClickInterface;
import com.myspps.pdfscanner.model.ImagePickerModel;
import com.myspps.pdfscanner.utils.CustomToast;
import java.util.ArrayList;

public class ImagePickerActivity extends AppCompatActivity implements ItemClickInterface {
    ImageView backIcon;
    Context context = this;
    Button doneButton;
    String imageType;
    ImagePickerAdapter pickerAdapter;
    ArrayList<ImagePickerModel> pickerArray = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> selectedImagesList = new ArrayList<>();
    String type = "glry";

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_image_picker);


        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.backIcon = (ImageView) findViewById(R.id.backIcon);
        this.doneButton = (Button) findViewById(R.id.done);
        this.imageType = getIntent().getStringExtra("imageType");
        Log.e("TAG", "onCreate: " + this.imageType);
        this.pickerAdapter = new ImagePickerAdapter(this, this.pickerArray, this.imageType, this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        this.recyclerView.setAdapter(this.pickerAdapter);
        String str = this.imageType;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1953280235:
                if (str.equals("ID Card")) {
                    c = 0;
                    break;
                }
                break;
            case 2076425:
                if (str.equals("Book")) {
                    c = 1;
                    break;
                }
                break;
            case 2135643:
                if (str.equals("Docs")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.doneButton.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        ImagePickerActivity.this.lambda$onCreate$0$ImagePickerActivity(view);
                    }
                });
                break;
            case 1:
            case 2:
                this.doneButton.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        ImagePickerActivity.this.lambda$onCreate$1$ImagePickerActivity(view);
                    }
                });
                break;
        }
        this.backIcon.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ImagePickerActivity.this.lambda$onCreate$2$ImagePickerActivity(view);
            }
        });
        Log.e("TAG", "on: ");
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "datetaken"}, (String) null, (String[]) null, "datetaken DESC");
        while (query.moveToNext()) {
            this.pickerArray.add(new ImagePickerModel(query.getString(query.getColumnIndex("_data")), false));
            this.pickerAdapter.notifyDataSetChanged();
        }
        query.close();
    }

    public  void lambda$onCreate$0$ImagePickerActivity(View view) {
        if (this.selectedImagesList.size() == 0) {
            new CustomToast(this.context, "Select Image");
        } else if (this.selectedImagesList.size() > 2) {
            new CustomToast(this.context, "Can't select more than 2 Images for ID Card");
        } else {
            gotoCropperActivity();
        }
    }

    public  void lambda$onCreate$1$ImagePickerActivity(View view) {
        if (this.selectedImagesList.size() == 0) {
            new CustomToast(this.context, "Select Image");
        } else {
            gotoCropperActivity();
        }
    }

    public  void lambda$onCreate$2$ImagePickerActivity(View view) {
        onBackPressed();
    }

    private void gotoCropperActivity() {
        Intent intent = new Intent(this, CropperActivity.class);
        intent.putStringArrayListExtra("images", this.selectedImagesList);
        intent.putExtra("gallery", this.type);
        startActivity(intent);
        this.selectedImagesList.clear();
    }

    private void moveTasktoBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "Do you want to quit without saving").setPositiveButton((CharSequence) "I'm sure", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                ImagePickerActivity.this.lambda$onBackPressed$3$ImagePickerActivity(dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) $$Lambda$ImagePickerActivity$Vu4WipMpi9QLKg35NNqlksxtR5Q.INSTANCE).show();
    }

    public  void lambda$onBackPressed$3$ImagePickerActivity(DialogInterface dialogInterface, int i) {
        moveTasktoBack();
        dialogInterface.dismiss();
        super.onBackPressed();
    }

    public void onItemClick(int i) {
        if (!this.pickerArray.get(i).isSelected()) {
            selectImage(i);
            return;
        }
        unSelectImage(i);
        Log.e("TAG", "onItemUnSelect: " + this.selectedImagesList.size());
    }

    private void unSelectImage(int i) {
        for (int i2 = 0; i2 < this.selectedImagesList.size(); i2++) {
            if (this.pickerArray.get(i) != null && this.selectedImagesList.get(i2).equals(this.pickerArray.get(i).getImagePath())) {
                this.pickerArray.get(i).setSelected(false);
                this.selectedImagesList.remove(i2);
                this.pickerAdapter.notifyItemChanged(i);
            }
        }
    }

    private void selectImage(int i) {
        if (!this.selectedImagesList.contains(this.pickerArray.get(i).getImagePath())) {
            this.pickerArray.get(i).setSelected(true);
            this.selectedImagesList.add(0, this.pickerArray.get(i).getImagePath());
            this.pickerAdapter.notifyItemChanged(i);
        }
    }
}
