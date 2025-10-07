package com.myspps.pdfscanner.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.MainActivity;
import com.myspps.pdfscanner.activities.OpenPDFFiles;
import com.myspps.pdfscanner.adapters.ViewFilesAdapter;
import com.myspps.pdfscanner.interfaces.FilesClickInterface;
import com.myspps.pdfscanner.model.ViewFilesModel;
import com.myspps.pdfscanner.utils.CustomToast;
import com.myspps.pdfscanner.utils.Permissions;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DocumentsFragment extends Fragment implements FilesClickInterface {
    Context applicationContext = MainActivity.getContextOfApplication();
    Context context = getContext();
    File fDelete;
    File fRename = null;
    File fShare;
    ViewFilesAdapter filesAdapter;
    ImageView ivNodata;
    RecyclerView recyclerview;
    ArrayList<ViewFilesModel> viewFilesArray = new ArrayList<>();

    public static String getDate(long j, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_documents, viewGroup, false);
        this.recyclerview = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        this.ivNodata = (ImageView) inflate.findViewById(R.id.nodata);
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            getPDF();
        } else {
            Permissions.requestReadStoragePermission(getActivity());
        }
        setUpRecyclerView();
        return inflate;
    }

    
    public ArrayList<ViewFilesModel> getPDF() {
        File file;
        ArrayList<ViewFilesModel> arrayList = this.viewFilesArray;
        if (arrayList != null) {
            arrayList.clear();
        }
        if (Build.VERSION.SDK_INT <= 27) {
            file = Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner");
        } else {
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner");
        }
        if (file.exists()) {
            this.ivNodata.setVisibility(8);
            File[] listFiles = file.listFiles();
            for (int length = listFiles.length - 1; length >= 0; length += -1) {
                File file2 = listFiles[length];
                long lastModified = new File(file2.getPath()).lastModified();
                this.viewFilesArray.add(new ViewFilesModel(String.valueOf(Uri.fromFile(file2)), file2.getName(), String.format("%.2f", new Object[]{Float.valueOf(Float.parseFloat(String.valueOf(file2.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 1024.0f)}) + " MB", getDate(lastModified, "dd/MM/yyyy") + ""));
            }
        }
        return this.viewFilesArray;
    }

    
    public void setUpRecyclerView() {
        this.filesAdapter = new ViewFilesAdapter(getContext(), getPDF(), this);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerview.setAdapter(this.filesAdapter);
    }

    public void deleteFileFromMediaStore(ContentResolver contentResolver, File file) {
        String str;
        Uri uri;
        try {
            str = file.getCanonicalPath();
        } catch (IOException unused) {
            str = file.getAbsolutePath();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Files.getContentUri("external_primary");
        } else {
            uri = MediaStore.Files.getContentUri("external");
        }
        if (contentResolver.delete(uri, "_data=?", new String[]{str}) == 0) {
            String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(str)) {
                contentResolver.delete(uri, "_data=?", new String[]{absolutePath});
            }
        }
    }

    public String formatSize(long j) {
        String str;
        if (j >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            j /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            if (j >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
                j /= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
                str = " MB";
            } else {
                str = " KB";
            }
        } else {
            str = null;
        }
        StringBuilder sb = new StringBuilder(Long.toString(j));
        for (int length = sb.length() - 3; length > 0; length -= 3) {
            sb.insert(length, ',');
        }
        if (str != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    public void onItemClick(int i) {
        File file;
        if (Build.VERSION.SDK_INT <= 27) {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        Log.d("nameee", this.viewFilesArray.get(i).getName());
        Context context2 = getContext();
        openPDFViewer(String.valueOf(FileProvider.getUriForFile(context2, getActivity().getPackageName() + ".provider", file)));
    }

    public void onDeleteClick(int i) {
        deleteItem(i, this.viewFilesArray.get(i).getPath());
        deleteFileFromMediaStore(getActivity().getContentResolver(), new File(this.viewFilesArray.get(i).getPath()));
    }

    public void onRenameClick(int i) {
        renameFile(i);
    }

    private void renameFile(int i) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_rename);
        dialog.getWindow().setLayout(-1, -2);
        EditText editText = (EditText) dialog.findViewById(R.id.rename);
        editText.setText(this.viewFilesArray.get(i).getName());
        editText.setSelectAllOnFocus(true);
        ((AppCompatButton) dialog.findViewById(R.id.textViewSave)).setOnClickListener(new View.OnClickListener() {
            public final  EditText f$1;
            public final  int f$2;
            public final  Dialog f$3;

            {
                this.f$1 = editText;
                this.f$2 = i;
                this.f$3 = dialog;
            }

            public final void onClick(View view) {
                DocumentsFragment.this.lambda$renameFile$0$DocumentsFragment(this.f$1, this.f$2, this.f$3, view);
            }
        });
        ((AppCompatButton) dialog.findViewById(R.id.textViewNo)).setOnClickListener(new View.OnClickListener() {
            public final  Dialog f$0;

            {
                this.f$0 = dialog;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.show();
    }

    public  void lambda$renameFile$0$DocumentsFragment(EditText editText, int i, Dialog dialog, View view) {
        if (editText.getText().toString().isEmpty()) {
            new CustomToast(getContext(), "Please enter valid file name");
            return;
        }
        rename(i, editText.getText().toString());
        dialog.dismiss();
    }

    private void rename(int i, String str) {
        File file;
        String str2;
        Uri uri;
        if (Build.VERSION.SDK_INT <= 27) {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        new File(this.viewFilesArray.get(i).getPath());
        String absolutePath = file.getParentFile().getAbsolutePath();
        String absolutePath2 = file.getAbsolutePath();
        String substring = absolutePath2.substring(absolutePath2.lastIndexOf("."));
        File file2 = new File(absolutePath + "/" + str + substring);
        if (file.renameTo(file2)) {
            try {
                str2 = file.getCanonicalPath();
            } catch (IOException unused) {
                str2 = file.getAbsolutePath();
            }
            if (Build.VERSION.SDK_INT >= 29) {
                uri = MediaStore.Files.getContentUri("external_primary");
            } else {
                uri = MediaStore.Files.getContentUri("external");
            }
            ContentResolver contentResolver = getActivity().getContentResolver();
            if (contentResolver.delete(uri, "_data=?", new String[]{str2}) == 0) {
                String absolutePath3 = file.getAbsolutePath();
                if (!absolutePath3.equals(str2)) {
                    contentResolver.delete(uri, "_data=?", new String[]{absolutePath3});
                }
            }
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file2));
            getActivity().sendBroadcast(intent);
            new CustomToast(getContext(), "File Renamed");
            this.viewFilesArray.clear();
            refreshGallery(file);
            this.filesAdapter.notifyDataSetChanged();
            getPDF();
            return;
        }
        new CustomToast(getContext(), "Can't rename file");
    }

    public void onShareClick(int i) {
        shareFile(i);
    }

    private void deleteItem(int i, String str) {
        try {
            this.fDelete = new File(new URI(String.valueOf(this.viewFilesArray.get(i).getPath())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false).setMessage((CharSequence) "Really want to delete?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (DocumentsFragment.this.fDelete.exists()) {
                    if (DocumentsFragment.this.fDelete.delete()) {
                        Toast.makeText(DocumentsFragment.this.getContext(), "Deleted", 0).show();
                        DocumentsFragment documentsFragment = DocumentsFragment.this;
                        documentsFragment.refreshGallery(documentsFragment.fDelete);
                        ArrayList unused = DocumentsFragment.this.getPDF();
                        DocumentsFragment.this.setUpRecyclerView();
                    } else {
                        Toast.makeText(DocumentsFragment.this.getContext(), "Not deleted", 0).show();
                    }
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    
    public void refreshGallery(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        getActivity().sendBroadcast(intent);
    }

    private void shareFile(int i) {
        File file;
        if (Build.VERSION.SDK_INT <= 27) {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        Log.d("nameee", this.viewFilesArray.get(i).getName());
        Context context2 = getContext();
        Uri uriForFile = FileProvider.getUriForFile(context2, getActivity().getPackageName() + ".provider", file);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(URLConnection.guessContentTypeFromName(this.viewFilesArray.get(i).getName()));
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        getContext().startActivity(Intent.createChooser(intent, "Share File"));
    }

    private void openPDFViewer(String str) {
        Intent intent = new Intent(getActivity(), OpenPDFFiles.class);
        intent.putExtra("pdfPath", str);
        startActivity(intent);
    }
}
