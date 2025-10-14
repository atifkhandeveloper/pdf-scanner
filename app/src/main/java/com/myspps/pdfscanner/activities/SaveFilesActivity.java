package com.myspps.pdfscanner.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.adapters.SaveFilesAdapter;
import com.myspps.pdfscanner.model.FiltersAppliedModel;
import com.myspps.pdfscanner.splashAds.PrivacyTermsActivity;
import com.myspps.pdfscanner.utils.Permissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SaveFilesActivity extends AppCompatActivity {

    public static ArrayList<FiltersAppliedModel> filteredImagesArray = new ArrayList<>();

    private final String FILE_FOLDER = "PDFScanner";
    private Context context = this;
    private RecyclerView recyclerView;
    private Button saveButton;
    private SaveFilesAdapter saveFilesAdapter;

    private String filePath;
    private String pdfFilePath;
    private OutputStream outputStream;

    private InterstitialAd interstitialAd;
    private boolean adIsLoading;
    Activity activity;

    public static void startResultActivity(Context context2, ArrayList<FiltersAppliedModel> arrayList) {
        filteredImagesArray = arrayList;
        context2.startActivity(new Intent(context2, SaveFilesActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_files);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        getStoragePermission(); // ✅ Request storage access

        loadAd();
        // ✅ Prevent crash if no images were passed
        if (filteredImagesArray == null || filteredImagesArray.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Images Found")
                    .setMessage("No filtered images were provided to create the PDF.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
            return;
        }

        recyclerView = findViewById(R.id.recycler_view);
        saveButton = findViewById(R.id.save_file);

        setupRecyclerView();

        saveButton.setOnClickListener(view -> showQualityDialog());
    }

    private void setupRecyclerView() {
        saveFilesAdapter = new SaveFilesAdapter(this, filteredImagesArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(saveFilesAdapter);
    }

    private void showQualityDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Choose Quality")
                .setSingleChoiceItems(R.array.choices, 0, (dialog, which) -> {
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        new CreatingPDF().execute();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Permissions.requestMediaPermission(this);
                        } else {
                            Permissions.requestReadStoragePermission(this);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void moveTaskToNext() {
        Intent intent = new Intent(this, PDFActionsActivity.class);
        if (Build.VERSION.SDK_INT >= 29) {
            String realPathFromURI = getRealPathFromURI(Uri.parse(filePath));
            filePath = realPathFromURI;
            intent.putExtra("filePath", realPathFromURI);
            Log.e("TAG", "Q PATH: " + filePath);
        } else {
            intent.putExtra("filePath", pdfFilePath);
            Log.e("TAG", "O PATH: " + pdfFilePath);
        }
        startActivity(intent);
    }

    private void saveFileInDirectory() throws IOException {
        PdfDocument pdfDocument = new PdfDocument();

        for (int i = 0; i < filteredImagesArray.size(); i++) {
            PdfDocument.Page page = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(
                    filteredImagesArray.get(i).getBitmap().getWidth(),
                    filteredImagesArray.get(i).getBitmap().getHeight(),
                    i + 1).create());

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(-1);
            canvas.drawPaint(paint);
            canvas.drawBitmap(filteredImagesArray.get(i).getBitmap(), 0, 0, null);
            pdfDocument.finishPage(page);
        }

        String nameOfFile = fileName();

        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, nameOfFile);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + File.separator + FILE_FOLDER);

            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external_primary"), values);
            if (uri != null) {
                outputStream = resolver.openOutputStream(uri);
                pdfDocument.writeTo(outputStream);
                filePath = uri.toString();
            }
        } else {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            pdfFilePath = folder + "/" + nameOfFile;
            pdfDocument.writeTo(new FileOutputStream(pdfFilePath));
        }

        pdfDocument.close();
    }

    private String fileName() {
        return "PDFScanner" + new SimpleDateFormat("-dd-MM-yy-HH-mm-ss-a", Locale.ENGLISH)
                .format(System.currentTimeMillis()) + ".pdf";
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = new CursorLoader(getApplicationContext(), uri, new String[]{"_data"}, null, null, null).loadInBackground();
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    // ✅ Updated for Android 6–15
    private void getStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                }, 200);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 201);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean granted = true;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (!granted) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("Storage access is required to save PDF files.")
                        .setPositiveButton("Try Again", (dialog, which) -> getStoragePermission())
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
    }

    private class CreatingPDF extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        private String exception;
        private boolean fileSaved;
        private boolean filenotSaved;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SaveFilesActivity.this.context);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Creating PDF, this may take a while...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                saveFileInDirectory();
                fileSaved = true;
            } catch (IOException e) {
                exception = e.getMessage();
                filenotSaved = true;
            }
            filteredImagesArray.clear();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (fileSaved) {
                showInterstitial();
            } else if (filenotSaved) {
                new AlertDialog.Builder(SaveFilesActivity.this.context)
                        .setTitle("Error")
                        .setMessage(exception != null ? exception : "Failed to create PDF.")
                        .setNegativeButton("OK", (dialog, which) -> finish())
                        .show();
            }
        }
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
                        SaveFilesActivity.this.interstitialAd = interstitialAd;
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
                                        SaveFilesActivity.this.interstitialAd = null;
                                        moveTaskToNext();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d(TAG, "The ad failed to show.");
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        SaveFilesActivity.this.interstitialAd = null;
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
                        moveTaskToNext();

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
            moveTaskToNext();
            loadAd();
            // [END_EXCLUDE]
        }
        // [END show_ad]
    }
}
