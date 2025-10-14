package com.myspps.pdfscanner.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.utils.CustomToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OpenPDFFiles extends AppCompatActivity {

    private Context context = this;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;

    private ImageView pdfImageView;
    private ImageView btnPrev, btnNext;
    private int currentPageIndex = 0;
    private int totalPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_p_d_f_files);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        pdfImageView = findViewById(R.id.pdfImageView);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        String pdfPath = getIntent().getStringExtra("pdfPath");
        String isNew = getIntent().getStringExtra("newPdf");

        Log.e("OpenPDF", "Received Path: " + pdfPath);

        if (pdfPath == null || pdfPath.isEmpty()) {
            new CustomToast(this, "No PDF path provided");
            finish();
            return;
        }

        try {
            File file;

            // Handle both file:// and content:// URIs properly
            if (pdfPath.startsWith("content://")) {
                Uri uri = Uri.parse(pdfPath);
                parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            } else {
                if (isNew == null)
                    file = new File(Uri.parse(pdfPath).getPath());
                else
                    file = new File(pdfPath);

                if (!file.exists()) throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
                parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            }

            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
            totalPages = pdfRenderer.getPageCount();

            if (totalPages == 0) {
                new CustomToast(this, "Empty PDF file");
                finish();
                return;
            }

            showPage(currentPageIndex);

        } catch (Exception e) {
            e.printStackTrace();
            new CustomToast(this, "Failed to open PDF");
        }

        btnPrev.setOnClickListener(v -> {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                showPage(currentPageIndex);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPageIndex < totalPages - 1) {
                currentPageIndex++;
                showPage(currentPageIndex);
            }
        });
    }

    private void showPage(int index) {
        try {
            if (pdfRenderer == null || index < 0 || index >= totalPages) return;

            if (currentPage != null) currentPage.close();
            currentPage = pdfRenderer.openPage(index);

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (width * 1.414); // maintain A4 aspect ratio

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pdfImageView.setImageBitmap(bitmap);

            btnPrev.setEnabled(index > 0);
            btnNext.setEnabled(index < totalPages - 1);

            setTitle("Page " + (index + 1) + " of " + totalPages);
        } catch (Exception e) {
            e.printStackTrace();
            new CustomToast(this, "Failed to render PDF page");
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (currentPage != null) currentPage.close();
            if (pdfRenderer != null) pdfRenderer.close();
            if (parcelFileDescriptor != null) parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
