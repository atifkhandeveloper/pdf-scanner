package com.myspps.pdfscanner.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.activities.CameraActivity;
import com.myspps.pdfscanner.activities.ImagePickerActivity;
import com.myspps.pdfscanner.activities.MainActivity;
import com.myspps.pdfscanner.activities.OpenPDFFiles;
import com.myspps.pdfscanner.adapters.ViewFilesAdapter;
import com.myspps.pdfscanner.interfaces.FilesClickInterface;
import com.myspps.pdfscanner.model.ViewFilesModel;
import com.myspps.pdfscanner.utils.CustomToast;
import com.myspps.pdfscanner.utils.Permissions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements FilesClickInterface {
    public static final String pdfExtension = "pdf";
    Context applicationContext = MainActivity.getContextOfApplication();
    Button bookButton;
    Context context = getContext();
    Button docsButton;
    File fDelete = null;
    File file;
    ViewFilesAdapter filesAdapter;
    Button idCardButton;
    Button idPhotoButton;
    List<File> items = null;
    FloatingActionButton ivCamera;
    FloatingActionButton ivGallery;
    ImageView ivNoData;
    RecyclerView recyclerview;
    String type;
    ArrayList<ViewFilesModel> viewFilesArray = new ArrayList<>();

    public static String getDate(long j, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.docsButton = (Button) inflate.findViewById(R.id.docsButton);
        this.idCardButton = (Button) inflate.findViewById(R.id.idCardButton);
        this.bookButton = (Button) inflate.findViewById(R.id.bookButton);
        this.idPhotoButton = (Button) inflate.findViewById(R.id.idPhotoButton);
        this.recyclerview = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        this.ivNoData = (ImageView) inflate.findViewById(R.id.nodata);
        this.ivCamera = (FloatingActionButton) inflate.findViewById(R.id.camera_button);
        this.ivGallery = (FloatingActionButton) inflate.findViewById(R.id.gallery_button);
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            getPDF();
        } else {
            Permissions.requestReadStoragePermission(getActivity());
        }
        setUpRecyclerView();
        this.docsButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$0$HomeFragment(view);
            }
        });
        this.idCardButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$1$HomeFragment(view);
            }
        });
        this.bookButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$2$HomeFragment(view);
            }
        });
        this.idPhotoButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$3$HomeFragment(view);
            }
        });
        this.ivGallery.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$4$HomeFragment(view);
            }
        });
        this.ivCamera.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                HomeFragment.this.lambda$onCreateView$5$HomeFragment(view);
            }
        });
        return inflate;
    }

    public  void lambda$onCreateView$0$HomeFragment(View view) {
        this.type = this.docsButton.getText().toString();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity());
        }
    }

    public  void lambda$onCreateView$1$HomeFragment(View view) {
        this.type = this.idCardButton.getText().toString();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity());
        }
    }

    public  void lambda$onCreateView$2$HomeFragment(View view) {
        this.type = this.bookButton.getText().toString();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity());
        }
    }

    public  void lambda$onCreateView$3$HomeFragment(View view) {
        this.type = this.idPhotoButton.getText().toString();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity());
        }
    }

    public  void lambda$onCreateView$4$HomeFragment(View view) {
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Intent intent = new Intent(getContext(), ImagePickerActivity.class);
            intent.putExtra("imageType", "Docs");
            startActivity(intent);
            return;
        }
        Permissions.requestReadStoragePermission(getActivity());
    }

    public  void lambda$onCreateView$5$HomeFragment(View view) {
        this.type = "Docs";
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0) {
            goToCameraActivity();
        } else {
            Permissions.requestCameraPermission(getActivity());
        }
    }

    
    public void setUpRecyclerView() {
        this.filesAdapter = new ViewFilesAdapter(getContext(), getPDF(), this);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerview.setAdapter(this.filesAdapter);
    }

    private void goToCameraActivity() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("textFromButton", this.type);
        startActivity(intent);
    }

    
    public ArrayList<ViewFilesModel> getPDF() {
        File file2;
        ArrayList<ViewFilesModel> arrayList = this.viewFilesArray;
        if (arrayList != null) {
            arrayList.clear();
        }
        if (Build.VERSION.SDK_INT <= 27) {
            file2 = Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner");
        } else {
            file2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner");
        }
        if (file2.exists()) {
            this.ivNoData.setVisibility(8);
            File[] listFiles = file2.listFiles();
            for (int length = listFiles.length - 1; length >= 0; length += -1) {
                File file3 = listFiles[length];
                long lastModified = new File(file3.getPath()).lastModified();
                this.viewFilesArray.add(new ViewFilesModel(String.valueOf(Uri.fromFile(file3)), file3.getName(), String.format("%.2f", new Object[]{Float.valueOf(Float.parseFloat(String.valueOf(file3.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 1024.0f)}) + " MB", getDate(lastModified, "dd/MM/yyyy") + ""));
            }
        }
        return this.viewFilesArray;
    }

    private void getPDFFilesFromDirecotry() {
        String str;
        Uri uri;
        String[] strArr = {Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "PDFScanner"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Files.getContentUri("external");
            str = "relative_path LIKE?";
        } else {
            uri = MediaStore.Files.getContentUri("external");
            str = "_data LIKE?";
        }
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor query = contentResolver.query(uri, new String[]{"_id", "_display_name"}, str, strArr, (String) null);
        FragmentActivity activity = getActivity();
        Toast.makeText(activity, query.getCount() + "", 0).show();
        if (query == null || query.getCount() <= 0 || !query.moveToFirst()) {
            Log.e("tag", "No data");
            return;
        }
        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(pdfExtension);
        MediaScannerConnection.scanFile(getActivity(), new String[]{this.file.getPath()}, new String[]{mimeTypeFromExtension}, (MediaScannerConnection.OnScanCompletedListener) null);
        while (query.moveToNext()) {
            Log.e("TAG", "Message: ");
            String string = query.getString(query.getColumnIndex("bucket_display_name"));
            Log.e("TAG", "getPDFFilesFromDirectory Path: " + string);
            if (string != null) {
                this.ivNoData.setVisibility(8);
                File file2 = new File(string);
                this.file = file2;
                String name = file2.getName();
                int lastIndexOf = name.lastIndexOf(".");
                if (lastIndexOf > 0 && lastIndexOf < name.length() - 1) {
                    name = name.substring(0, lastIndexOf);
                }
                String format = new SimpleDateFormat("dd-MMM-yy hh.mm a").format(new Date(this.file.lastModified()));
                this.viewFilesArray.add(new ViewFilesModel(string, name, formatSize(this.file.length()), format));
            }
        }
        query.close();
        Log.e("TAG", "getPDFFilesFromDirectory: " + this.viewFilesArray.size());
        this.filesAdapter.notifyDataSetChanged();
    }

    public void deleteFileFromMediaStore(ContentResolver contentResolver, File file2) {
        String str;
        Uri uri;
        try {
            str = file2.getCanonicalPath();
        } catch (IOException unused) {
            str = file2.getAbsolutePath();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Files.getContentUri("external_primary");
        } else {
            uri = MediaStore.Files.getContentUri("external");
        }
        if (contentResolver.delete(uri, "_data=?", new String[]{str}) == 0) {
            String absolutePath = file2.getAbsolutePath();
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
        File file2;
        if (Build.VERSION.SDK_INT <= 27) {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        Log.d("nameee", this.viewFilesArray.get(i).getName());
        Context context2 = getContext();
        openPDFViewer(String.valueOf(FileProvider.getUriForFile(context2, getActivity().getPackageName() + ".provider", file2)));
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
                HomeFragment.this.lambda$renameFile$6$HomeFragment(this.f$1, this.f$2, this.f$3, view);
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

    public  void lambda$renameFile$6$HomeFragment(EditText editText, int i, Dialog dialog, View view) {
        if (editText.getText().toString().isEmpty()) {
            new CustomToast(getContext(), "Please enter valid file name");
            return;
        }
        rename(i, editText.getText().toString());
        dialog.dismiss();
    }

    private void rename(int i, String str) {
        File file2;
        String str2;
        Uri uri;
        if (Build.VERSION.SDK_INT <= 27) {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        new File(this.viewFilesArray.get(i).getPath());
        String absolutePath = file2.getParentFile().getAbsolutePath();
        String absolutePath2 = file2.getAbsolutePath();
        String substring = absolutePath2.substring(absolutePath2.lastIndexOf("."));
        File file3 = new File(absolutePath + "/" + str + substring);
        if (file2.renameTo(file3)) {
            try {
                str2 = file2.getCanonicalPath();
            } catch (IOException unused) {
                str2 = file2.getAbsolutePath();
            }
            if (Build.VERSION.SDK_INT >= 29) {
                uri = MediaStore.Files.getContentUri("external_primary");
            } else {
                uri = MediaStore.Files.getContentUri("external");
            }
            ContentResolver contentResolver = getActivity().getContentResolver();
            if (contentResolver.delete(uri, "_data=?", new String[]{str2}) == 0) {
                String absolutePath3 = file2.getAbsolutePath();
                if (!absolutePath3.equals(str2)) {
                    contentResolver.delete(uri, "_data=?", new String[]{absolutePath3});
                }
            }
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file3));
            getActivity().sendBroadcast(intent);
            new CustomToast(getContext(), "File Renamed");
            this.viewFilesArray.clear();
            refreshGallery(file2);
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
                if (HomeFragment.this.fDelete.exists()) {
                    if (HomeFragment.this.fDelete.delete()) {
                        Toast.makeText(HomeFragment.this.getContext(), "Deleted", 0).show();
                        HomeFragment homeFragment = HomeFragment.this;
                        homeFragment.refreshGallery(homeFragment.fDelete);
                        ArrayList unused = HomeFragment.this.getPDF();
                        HomeFragment.this.setUpRecyclerView();
                    } else {
                        Toast.makeText(HomeFragment.this.getContext(), "Not deleted", 0).show();
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

    
    public void refreshGallery(File file2) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file2));
        getActivity().sendBroadcast(intent);
    }

    private void shareFile(int i) {
        File file2;
        if (Build.VERSION.SDK_INT <= 27) {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("/Documents/PDFScanner/" + this.viewFilesArray.get(i).getName())));
        } else {
            file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + "PDFScanner/" + this.viewFilesArray.get(i).getName())));
        }
        Log.d("nameee", this.viewFilesArray.get(i).getName());
        Context context2 = getContext();
        Uri uriForFile = FileProvider.getUriForFile(context2, getActivity().getPackageName() + ".provider", file2);
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

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            goToCameraActivity();
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (!shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                new AlertDialog.Builder(getActivity()).setMessage("Permission Necessary").setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + HomeFragment.this.getContext().getPackageName()));
                        HomeFragment.this.startActivityForResult(intent, 0);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HomeFragment.this.getActivity().finish();
                    }
                }).show();
            } else {
                getActivity().finish();
            }
        }
    }
}
