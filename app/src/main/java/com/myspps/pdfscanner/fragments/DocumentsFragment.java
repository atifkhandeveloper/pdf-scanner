package com.myspps.pdfscanner.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.OpenPDFFiles;
import com.myspps.pdfscanner.adapters.ViewFilesAdapter;
import com.myspps.pdfscanner.interfaces.FilesClickInterface;
import com.myspps.pdfscanner.model.ViewFilesModel;
import com.myspps.pdfscanner.utils.CustomToast;
import com.myspps.pdfscanner.utils.Permissions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DocumentsFragment extends Fragment implements FilesClickInterface {

    private RecyclerView recyclerView;
    private ImageView ivNodata;
    private ViewFilesAdapter filesAdapter;
    private ArrayList<ViewFilesModel> viewFilesArray = new ArrayList<>();
    private File fDelete;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documents, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        ivNodata = view.findViewById(R.id.nodata);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading Documents...");
        progressDialog.setCancelable(false);

        // ✅ Request proper permissions for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permissions.requestMediaPermission(requireActivity());
        } else {
            Permissions.requestReadStoragePermission(requireActivity());
        }

        loadDocuments();
        return view;
    }

    // 🧭 Load Documents with Progress
    private void loadDocuments() {
        progressDialog.show();
        new Thread(() -> {
            ArrayList<ViewFilesModel> docsList = getDocuments();

            requireActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                if (docsList == null || docsList.isEmpty()) {
                    ivNodata.setVisibility(View.VISIBLE);
                } else {
                    ivNodata.setVisibility(View.GONE);
                }
                setUpRecyclerView(docsList);
            });
        }).start();
    }

    // 🗂️ Get PDF and DOC/DOCX Files
    private ArrayList<ViewFilesModel> getDocuments() {
        viewFilesArray.clear();

        File folder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFScanner");
        } else {
            folder = new File(Environment.getExternalStorageDirectory(), "Documents/PDFScanner");
        }

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (int i = files.length - 1; i >= 0; i--) {
                    File file = files[i];
                    String name = file.getName().toLowerCase();

                    // ✅ Support PDF, DOC, DOCX
                    if (name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx")) {
                        long lastModified = file.lastModified();
                        viewFilesArray.add(new ViewFilesModel(
                                String.valueOf(Uri.fromFile(file)),
                                file.getName(),
                                String.format("%.2f MB", (file.length() / 1024f / 1024f)),
                                getDate(lastModified, "dd/MM/yyyy")
                        ));
                    }
                }
            }
        }

        return viewFilesArray;
    }

    private static String getDate(long millis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return sdf.format(cal.getTime());
    }

    private void setUpRecyclerView(ArrayList<ViewFilesModel> docsList) {
        filesAdapter = new ViewFilesAdapter(requireContext(), docsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(filesAdapter);
    }

    // 📄 Open PDF or DOC/DOCX
    @Override
    public void onItemClick(int position) {
        if (viewFilesArray == null || position >= viewFilesArray.size()) return;

        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/PDFScanner/" + viewFilesArray.get(position).getName());
        } else {
            file = new File(Environment.getExternalStorageDirectory()
                    + "/Documents/PDFScanner/" + viewFilesArray.get(position).getName());
        }

        if (!file.exists()) {
            Toast.makeText(requireContext(), "File not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(requireContext(),
                requireActivity().getPackageName() + ".provider", file);

        String name = file.getName().toLowerCase();

        if (name.endsWith(".pdf")) {
            // Open in internal PDF viewer
            Intent intent = new Intent(requireActivity(), OpenPDFFiles.class);
            intent.putExtra("pdfPath", uri.toString());
            startActivity(intent);
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            // Open DOC/DOCX in external app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/msword");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "No app found to open this file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 🗑️ Delete File
    @Override
    public void onDeleteClick(int position) {
        deleteItem(position);
    }

    private void deleteItem(int position) {
        if (viewFilesArray == null || position >= viewFilesArray.size()) return;

        try {
            fDelete = new File(new URI(viewFilesArray.get(position).getPath()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setMessage("Really want to delete?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (fDelete.exists() && fDelete.delete()) {
                        deleteFileFromMediaStore(requireActivity().getContentResolver(), fDelete);
                        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        loadDocuments();
                    } else {
                        Toast.makeText(requireContext(), "Not deleted", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteFileFromMediaStore(ContentResolver contentResolver, File file) {
        if (file == null) return;
        Uri uri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(uri, MediaStore.MediaColumns.DATA + "=?", new String[]{file.getAbsolutePath()});
    }

    // ✏️ Rename File
    @Override
    public void onRenameClick(int position) {
        renameFile(position);
    }

    private void renameFile(int position) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_rename);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editText = dialog.findViewById(R.id.rename);
        editText.setText(viewFilesArray.get(position).getName());
        editText.setSelectAllOnFocus(true);

        AppCompatButton btnSave = dialog.findViewById(R.id.textViewSave);
        AppCompatButton btnCancel = dialog.findViewById(R.id.textViewNo);

        btnSave.setOnClickListener(v -> {
            String newName = editText.getText().toString().trim();
            if (newName.isEmpty()) {
                new CustomToast(requireContext(), "Please enter valid file name");
                return;
            }
            rename(position, newName);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void rename(int position, String newName) {
        File oldFile;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            oldFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/PDFScanner/" + viewFilesArray.get(position).getName());
        } else {
            oldFile = new File(Environment.getExternalStorageDirectory()
                    + "/Documents/PDFScanner/" + viewFilesArray.get(position).getName());
        }

        if (!oldFile.exists()) {
            new CustomToast(requireContext(), "File not found");
            return;
        }

        String ext = oldFile.getName().substring(oldFile.getName().lastIndexOf("."));
        File newFile = new File(oldFile.getParent(), newName + ext);

        if (oldFile.renameTo(newFile)) {
            new CustomToast(requireContext(), "File renamed");
            loadDocuments();
        } else {
            new CustomToast(requireContext(), "Rename failed");
        }
    }

    // 📤 Share File
    @Override
    public void onShareClick(int position) {
        if (viewFilesArray == null || position >= viewFilesArray.size()) return;

        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/PDFScanner/" + viewFilesArray.get(position).getName());
        } else {
            file = new File(Environment.getExternalStorageDirectory()
                    + "/Documents/PDFScanner/" + viewFilesArray.get(position).getName());
        }

        if (!file.exists()) {
            Toast.makeText(requireContext(), "File not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(requireContext(),
                requireActivity().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Share File"));
    }
}
