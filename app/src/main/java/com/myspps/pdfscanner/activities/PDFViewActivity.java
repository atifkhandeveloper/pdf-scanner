package com.myspps.pdfscanner.activities;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myspps.pdfscanner.R;

import java.io.File;
import java.io.IOException;

public class PDFViewActivity extends AppCompatActivity {

    private ImageView pdfImageView;
    private Button btnPrev, btnNext;

    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    private int totalPages = 0;
    private int currentPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfImageView = findViewById(R.id.pdfImageView);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        String pdfPath = getIntent().getStringExtra("pdfPath");
        if (pdfPath == null) {
            Toast.makeText(this, "No PDF file path provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            openRenderer(pdfFile);
            showPage(currentPageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void openRenderer(File file) throws IOException {
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        totalPages = pdfRenderer.getPageCount();
    }

    private void showPage(int index) {
        if (pdfRenderer == null || pdfRenderer.getPageCount() <= index) return;

        if (currentPage != null) currentPage.close();

        currentPage = pdfRenderer.openPage(index);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (width * 1.5); // maintain aspect ratio

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        pdfImageView.setImageBitmap(bitmap);

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < totalPages - 1);
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
