package com.myspps.pdfscanner.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.CameraActivity;
import com.myspps.pdfscanner.activities.ImagePickerActivity;
import com.myspps.pdfscanner.activities.PdfToDocxConverter;
import com.myspps.pdfscanner.utils.Permissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HomeFragment extends Fragment {

    Button docsButton, bookButton, idCardButton, idPhotoButton;
    FloatingActionButton ivCamera, ivGallery;
    ImageView ivNoData;
    RecyclerView recyclerview;
    CardView carddoc, cardbook, cardid, cardphoto, pdftodocs, imagetopdf;
    TextView tv_docs, tv_book, tv_card_id, tv_card_photo, tv_pdftodocs, tv_imagetopdf;
    String type;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        docsButton = view.findViewById(R.id.docsButton);
        bookButton = view.findViewById(R.id.bookButton);
        idCardButton = view.findViewById(R.id.idCardButton);
        idPhotoButton = view.findViewById(R.id.idPhotoButton);
        pdftodocs = view.findViewById(R.id.pdftodpcx);
        imagetopdf = view.findViewById(R.id.imagetopdf);
        recyclerview = view.findViewById(R.id.recycler_view);
        ivNoData = view.findViewById(R.id.nodata);
        ivCamera = view.findViewById(R.id.camera_button);
        ivGallery = view.findViewById(R.id.gallery_button);
        carddoc = view.findViewById(R.id.card_document);
        cardbook = view.findViewById(R.id.card_book);
        cardid = view.findViewById(R.id.card_id);
        cardphoto = view.findViewById(R.id.card_id_photo);
        tv_docs = view.findViewById(R.id.tv_doc);
        tv_book = view.findViewById(R.id.tv_book);
        tv_card_id = view.findViewById(R.id.tv_id_card);
        tv_card_photo = view.findViewById(R.id.tv_id_photo);
        tv_pdftodocs = view.findViewById(R.id.tv_pdf_to_docs);
        tv_imagetopdf = view.findViewById(R.id.tv_image_to_pdf);

        // File picker result
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri pdfUri = result.getData().getData();
                        handlePdfFile(pdfUri);
                    }
                }
        );

        // PDF → DOCX button click
        pdftodocs.setOnClickListener(v -> {
            if (Permissions.hasStoragePermission(requireActivity())) {
                openFilePicker();
            } else {
                Permissions.requestReadStoragePermission(requireActivity());
            }
        });

        // Card click listeners
        carddoc.setOnClickListener(v -> openCamera(tv_docs.getText().toString()));
        cardbook.setOnClickListener(v -> openCamera(tv_book.getText().toString()));
        cardid.setOnClickListener(v -> openCamera(tv_card_id.getText().toString()));
        cardphoto.setOnClickListener(v -> openCamera(tv_card_photo.getText().toString()));

        // Floating buttons
        ivCamera.setOnClickListener(v -> openCamera("Docs"));
        ivGallery.setOnClickListener(v -> openGallery());
        imagetopdf.setOnClickListener(v -> openGallery());

        return view;
    }

    private void openCamera(String type) {
        this.type = type;
        if (Permissions.hasCameraPermission(requireActivity())) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(requireActivity());
        }
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("textFromButton", type);
        startActivity(intent);
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (Permissions.hasStoragePermission(requireActivity())) {
                startGalleryActivity();
            } else {
                Permissions.requestMediaPermission(requireActivity());
            }
        } else {
            if (Permissions.hasStoragePermission(requireActivity())) {
                startGalleryActivity();
            } else {
                Permissions.requestReadStoragePermission(requireActivity());
            }
        }
    }

    private void startGalleryActivity() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra("imageType", "Docs");
        startActivity(intent);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select PDF file"));
    }

    private void handlePdfFile(Uri uri) {
        try {
            // Get file name
            String fileName = getFileName(uri);
            if (fileName == null) fileName = "converted.pdf";

            // Copy selected file to cache
            File inputFile = new File(requireActivity().getCacheDir(), fileName);
            try (InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(inputFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // Prepare output DOCX file
            File outputDir = new File(requireActivity().getExternalFilesDir(null), "ConvertedDocs");
            if (!outputDir.exists()) outputDir.mkdirs();

            String baseName = fileName.toLowerCase().endsWith(".pdf")
                    ? fileName.substring(0, fileName.length() - 4)
                    : fileName;

            File outputFile = new File(outputDir, baseName + ".docx");

            // Convert PDF → DOCX
            boolean success = PdfToDocxConverter.convertPdfToDocx(
                    requireContext(),
                    inputFile.getAbsolutePath(),
                    outputFile.getAbsolutePath()
            );

            if (success && outputFile.exists()) {
                Toast.makeText(getContext(),
                        "Converted successfully!\nSaved to:\n" + outputFile.getAbsolutePath(),
                        Toast.LENGTH_LONG).show();

                // Open DOCX file
                openConvertedDocx(outputFile);

            } else {
                Toast.makeText(getContext(), "Conversion failed!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openConvertedDocx(File docxFile) {
        try {
            Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    docxFile
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Open DOCX file"));
        } catch (Exception e) {
            Toast.makeText(getContext(), "No app found to open DOCX file", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (android.database.Cursor cursor = requireActivity().getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = new File(uri.getPath()).getName();
        }
        return result;
    }
}
