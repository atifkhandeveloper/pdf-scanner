package com.myspps.pdfscanner.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.CameraActivity;
import com.myspps.pdfscanner.activities.ImagePickerActivity;
import com.myspps.pdfscanner.utils.Permissions;

public class HomeFragment extends Fragment {

    Button docsButton, bookButton, idCardButton, idPhotoButton;
    FloatingActionButton ivCamera, ivGallery;
    ImageView ivNoData;
    RecyclerView recyclerview;
    CardView carddoc, cardbook, cardid, cardphoto;
    TextView tv_docs, tv_book, tv_card_id, tv_card_photo;
    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        docsButton = view.findViewById(R.id.docsButton);
        bookButton = view.findViewById(R.id.bookButton);
        idCardButton = view.findViewById(R.id.idCardButton);
        idPhotoButton = view.findViewById(R.id.idPhotoButton);
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

        // Card clicks
        carddoc.setOnClickListener(v -> openCamera(tv_docs.getText().toString()));
        cardbook.setOnClickListener(v -> openCamera(tv_book.getText().toString()));
        cardid.setOnClickListener(v -> openCamera(tv_card_id.getText().toString()));
        cardphoto.setOnClickListener(v -> openCamera(tv_card_photo.getText().toString()));

        // Floating buttons
        ivCamera.setOnClickListener(v -> openCamera("Docs"));
        ivGallery.setOnClickListener(v -> openGallery());

        // Uncomment to set up RecyclerView for PDF files
        // setUpRecyclerView();

        return view;
    }

    private void openCamera(String type) {
        this.type = type;
        if (Permissions.hasCameraPermission((Activity) requireContext())) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity()); // Fragment-compatible
        }
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("textFromButton", type);
        startActivity(intent);
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (Permissions.hasStoragePermission((Activity) requireContext())) {
                startGalleryActivity();
            } else {
                Permissions.requestMediaPermission(requireActivity());
            }
        } else {
            if (Permissions.hasStoragePermission((Activity) requireContext())) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Permissions.CAMERA_PERMISSION_CODE) {
            if (Permissions.hasCameraPermission((Activity) requireContext())) {
                goToCameraActivity();
            } else {
                showPermissionDeniedDialog();
            }
        } else if (requestCode == Permissions.STORAGE_PERMISSION_CODE) {
            if (Permissions.hasStoragePermission((Activity) requireContext())) {
                startGalleryActivity();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Camera permission is necessary")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(android.net.Uri.parse("package:" + getContext().getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
