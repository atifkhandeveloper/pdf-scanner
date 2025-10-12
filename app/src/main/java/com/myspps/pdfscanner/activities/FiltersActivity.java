package com.myspps.pdfscanner.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.adapters.FiltersAdapter;
import com.myspps.pdfscanner.filters.Filters;
import com.myspps.pdfscanner.filters.PhotoFilters;
import com.myspps.pdfscanner.interfaces.FilterClickInterface;
import com.myspps.pdfscanner.model.FilterModel;
import com.myspps.pdfscanner.model.FiltersAppliedModel;
import com.myspps.pdfscanner.model.ImageModel;
import com.myspps.pdfscanner.utils.ImageConverter;
import java.io.IOException;
import java.util.ArrayList;

public class FiltersActivity extends AppCompatActivity implements FilterClickInterface {
    ContentResolver contentResolver;
    Context context = this;
    ArrayList<ImageModel> croppedImagesArray;
    ImageView filterImageView;
    ArrayList<FilterModel> filterImagesList = new ArrayList<>();
    ArrayList<FiltersAppliedModel> filteredArray = new ArrayList<>();
    FiltersAdapter filtersAdapter;
    RecyclerView filtersRecyclerView;
    ImageView nextIcon;
    TextView okButton;
    PhotoFilters photoFilters = new PhotoFilters();
    int position = 0;
    ImageView previousIcon;
    ProgressBar progressBar;
    ArrayList<ImageModel> savedArrayList = new ArrayList<>();
    TextView tvImageSize;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_filters);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);




        this.filterImageView = (ImageView) findViewById(R.id.filterImageView);
        this.nextIcon = (ImageView) findViewById(R.id.nextImage);
        this.previousIcon = (ImageView) findViewById(R.id.previousImage);
        this.tvImageSize = (TextView) findViewById(R.id.tv_image_size);
        this.filtersRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclerView);
        this.contentResolver = this.context.getContentResolver();
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.okButton = (TextView) findViewById(R.id.okButton);
        this.croppedImagesArray = getIntent().getParcelableArrayListExtra("cameraCroppedImages");
        setUpRecyclerView();
        for (int i = 0; i < this.croppedImagesArray.size(); i++) {
            this.filteredArray.add(new FiltersAppliedModel(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 0));
        }
        ((RequestBuilder) Glide.with(this.context).load(this.croppedImagesArray.get(0).getUri()).fitCenter()).into(this.filterImageView);
        TextView textView = this.tvImageSize;
        textView.setText((this.position + 1) + "/" + this.croppedImagesArray.size());
        new AddFilterAsync().execute(new Void[0]);
        this.nextIcon.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FiltersActivity.this.lambda$onCreate$0$FiltersActivity(view);
            }
        });
        this.previousIcon.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FiltersActivity.this.lambda$onCreate$1$FiltersActivity(view);
            }
        });
        this.okButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                FiltersActivity.this.lambda$onCreate$2$FiltersActivity(view);
            }
        });
    }

    public  void lambda$onCreate$0$FiltersActivity(View view) {
        if (this.position < this.croppedImagesArray.size() - 1) {
            int i = this.position + 1;
            this.position = i;
            this.croppedImagesArray.get(i);
            new AddFilterAsync().execute(new Void[0]);
            ((RequestBuilder) Glide.with(this.context).load(this.filteredArray.get(this.position).getBitmap()).fitCenter()).into(this.filterImageView);
            TextView textView = this.tvImageSize;
            textView.setText((this.position + 1) + "/" + this.croppedImagesArray.size());
            return;
        }
        Log.d("TAG", "Reached Last Record");
    }

    public  void lambda$onCreate$1$FiltersActivity(View view) {
        int i = this.position;
        if (i > 0) {
            int i2 = i - 1;
            this.position = i2;
            this.croppedImagesArray.get(i2);
            new AddFilterAsync().execute(new Void[0]);
            ((RequestBuilder) Glide.with(this.context).load(this.filteredArray.get(this.position).getBitmap()).fitCenter()).into(this.filterImageView);
            this.tvImageSize.setText((this.position + 1) + "/" + this.croppedImagesArray.size());
            return;
        }
        Log.d("TAG", "Reach First Record");
    }

    public  void lambda$onCreate$2$FiltersActivity(View view) {
        moveToNext();
    }

    private void moveTasktoBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "Do you want to quit without saving").setPositiveButton((CharSequence) "I'm sure", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                FiltersActivity.this.lambda$onBackPressed$3$FiltersActivity(dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) $$Lambda$FiltersActivity$nvqq6A7_OuM4Jjz8bGyPVu6m7g.INSTANCE).show();
    }

    public  void lambda$onBackPressed$3$FiltersActivity(DialogInterface dialogInterface, int i) {
        moveTasktoBack();
        dialogInterface.dismiss();
        super.onBackPressed();
    }

    private void moveToNext() {
        SaveFilesActivity.startResultActivity(this, this.filteredArray);
    }

    private void setUpRecyclerView() {
        this.filtersRecyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
        FiltersAdapter filtersAdapter2 = new FiltersAdapter(this, this.filterImagesList, this);
        this.filtersAdapter = filtersAdapter2;
        this.filtersRecyclerView.setAdapter(filtersAdapter2);
    }

    
    public void addingFilters(int i) {
        this.filterImagesList.clear();
        this.filterImagesList.add(new FilterModel(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), "Original"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.one(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Brightness"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.two(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), ExifInterface.TAG_CONTRAST));
        this.filterImagesList.add(new FilterModel(this.photoFilters.three(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Color Filter"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.four(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), ExifInterface.TAG_SATURATION));
        this.filterImagesList.add(new FilterModel(this.photoFilters.five(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), ExifInterface.TAG_GAMMA));
        this.filterImagesList.add(new FilterModel(this.photoFilters.six(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Grey"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.seven(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Noise"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.eight(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Sepia"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.nine(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Vignette"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.ten(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Hue"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.eleven(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Tint"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.twelve(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Invert"));
        this.filterImagesList.add(new FilterModel(this.photoFilters.thirteen(this.context, ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Sketch"));
    }

    private void addFilters(int i) throws IOException {
        this.filterImagesList.clear();
        this.filterImagesList.add(0, new FilterModel(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), "Original"));
        this.filterImagesList.add(1, new FilterModel(Filters.BrightnessFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 30.0d), "Brightness"));
        this.filterImagesList.add(2, new FilterModel(Filters.ContrastFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 20.0d), ExifInterface.TAG_CONTRAST));
        this.filterImagesList.add(3, new FilterModel(Filters.ColorFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 1.8d, 1.8d, 1.8d), "Color Filter"));
        this.filterImagesList.add(4, new FilterModel(Filters.SaturationFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 120), ExifInterface.TAG_SATURATION));
        this.filterImagesList.add(5, new FilterModel(Filters.GammaFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 1.8d, 1.8d, 1.8d), ExifInterface.TAG_GAMMA));
        this.filterImagesList.add(6, new FilterModel(Filters.GreyScaleFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Grey"));
        this.filterImagesList.add(7, new FilterModel(Filters.NoiseFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Noise"));
        this.filterImagesList.add(8, new FilterModel(Filters.SepiaFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Sepia"));
        this.filterImagesList.add(9, new FilterModel(Filters.VignetteFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Vignette"));
        this.filterImagesList.add(10, new FilterModel(Filters.HueFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 200.0f), "Hue"));
        this.filterImagesList.add(11, new FilterModel(Filters.TintFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri()), 50), "Tint"));
        this.filterImagesList.add(12, new FilterModel(Filters.InvertFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Invert"));
        this.filterImagesList.add(13, new FilterModel(Filters.SketchFilter(ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(i).getUri())), "Sketch"));
    }

    public void onFilterClick(int i, Bitmap bitmap) {
        Log.e("TAG", "onFilterClick: " + i);
        Glide.with(this.context).load(updateFilter(i, bitmap)).into(this.filterImageView);
        this.filteredArray.get(this.position).setBitmap(bitmap);
        this.filteredArray.get(this.position).setPosition(i);
        Log.e("TAG", "FilteredArray: " + this.filteredArray.get(this.position).getBitmap() + ".." + this.filteredArray.get(this.position).getPosition());
        StringBuilder sb = new StringBuilder();
        sb.append("onFilterSize: ");
        sb.append(this.filteredArray.size());
        Log.e("TAG", sb.toString());
    }

    private Bitmap updateFilter(int i, Bitmap bitmap) {
        switch (i) {
            case 0:
                return ImageConverter.getBitmapFromUri(this.contentResolver, this.croppedImagesArray.get(this.position).getUri());
            case 1:
                return this.photoFilters.one(this.context, bitmap);
            case 2:
                return this.photoFilters.two(this.context, bitmap);
            case 3:
                return this.photoFilters.three(this.context, bitmap);
            case 4:
                return this.photoFilters.four(this.context, bitmap);
            case 5:
                return this.photoFilters.five(this.context, bitmap);
            case 6:
                return this.photoFilters.six(this.context, bitmap);
            case 7:
                return this.photoFilters.seven(this.context, bitmap);
            case 8:
                return this.photoFilters.eight(this.context, bitmap);
            case 9:
                return this.photoFilters.nine(this.context, bitmap);
            case 10:
                return this.photoFilters.ten(this.context, bitmap);
            case 11:
                return this.photoFilters.eleven(this.context, bitmap);
            case 12:
                return this.photoFilters.twelve(this.context, bitmap);
            case 13:
                return this.photoFilters.thirteen(this.context, bitmap);
            case 14:
                return this.photoFilters.fourteen(this.context, bitmap);
            case 15:
                return this.photoFilters.fifteen(this.context, bitmap);
            default:
                return bitmap;
        }
    }

    class AddFilterAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        AddFilterAsync() {
        }

        
        public Void doInBackground(Void... voidArr) {
            FiltersActivity filtersActivity = FiltersActivity.this;
            filtersActivity.addingFilters(filtersActivity.position);
            return null;
        }

        
        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog2 = new ProgressDialog(FiltersActivity.this.context);
            this.progressDialog = progressDialog2;
            progressDialog2.setCancelable(false);
            this.progressDialog.show();
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            FiltersActivity.this.filtersAdapter.notifyDataSetChanged();
            this.progressDialog.dismiss();
        }
    }
}
