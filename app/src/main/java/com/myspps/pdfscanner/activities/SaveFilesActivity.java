package com.myspps.pdfscanner.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.adapters.SaveFilesAdapter;
import com.myspps.pdfscanner.model.FiltersAppliedModel;
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
    
    public static ArrayList<FiltersAppliedModel> filteredImagesArray;
    String File_Folder = "PDFScanner";
    Context context = this;
    ProgressDialog dialog;
    PdfDocument document;
    String exception;
    String filePath;
    boolean fileSaved = false;
    boolean filenotSaved = false;
    String nameOfFile;
    OutputStream outputStream;
    String pdfFilePath;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    String relativePath;
    Button saveButton;
    SaveFilesAdapter saveFilesAdapter;
    Uri uri;

    public static void startResultActivity(Context context2, ArrayList<FiltersAppliedModel> arrayList) {
        filteredImagesArray = arrayList;
        context2.startActivity(new Intent(context2, SaveFilesActivity.class));
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_save_files);




        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.saveButton = (Button) findViewById(R.id.save_file);
        setupRecyclerView();
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                SaveFilesActivity.this.lambda$onCreate$0$SaveFilesActivity(view);
            }
        });
    }

    public  void lambda$onCreate$0$SaveFilesActivity(View view) {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Choose Quality").setSingleChoiceItems((int) R.array.choices, 0, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                int checkedItemPosition = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();
                if (checkedItemPosition == 0) {
                    if (ContextCompat.checkSelfPermission(SaveFilesActivity.this.context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        new CreatingPDF().execute(new String[0]);
                    } else {
                        Permissions.writeStoragePermission(SaveFilesActivity.this.context);
                    }
                } else if (checkedItemPosition != 1) {
                } else {
                     if (ContextCompat.checkSelfPermission(SaveFilesActivity.this.context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        new CreatingPDF().execute(new String[0]);
                    } else {
                        Permissions.writeStoragePermission(SaveFilesActivity.this.context);
                    }
                }
            }
        }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    private void setupRecyclerView() {
        this.saveFilesAdapter = new SaveFilesAdapter(this, filteredImagesArray);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.saveFilesAdapter);
    }

    
    public void moveTasktoBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "Do you want to quit without saving").setPositiveButton((CharSequence) "I'm sure", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                SaveFilesActivity.this.lambda$onBackPressed$1$SaveFilesActivity(dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) $$Lambda$SaveFilesActivity$6Qhw90oZ9vcW2fZ6sIBkVY0tfFU.INSTANCE).show();
    }

    public  void lambda$onBackPressed$1$SaveFilesActivity(DialogInterface dialogInterface, int i) {
        moveTasktoBack();
        dialogInterface.dismiss();
        super.onBackPressed();
    }

    
    public void moveTasktoNext() {
        Intent intent = new Intent(this, PDFActionsActivity.class);
        if (Build.VERSION.SDK_INT >= 29) {
            String realPathFromURI = getRealPathFromURI(Uri.parse(this.filePath));
            this.filePath = realPathFromURI;
            intent.putExtra("filePath", realPathFromURI);
            Log.e("TAG", "Q PATH: " + this.filePath);
        } else {
            intent.putExtra("filePath", this.pdfFilePath);
            Log.e("TAG", "O PATH: " + this.pdfFilePath);
        }
        startActivity(intent);
    }

    
    public void saveFileInDirectory() throws IOException {
        PdfDocument pdfDocument = new PdfDocument();
        int i = 0;
        while (i < filteredImagesArray.size()) {
            int i2 = i + 1;
            PdfDocument.Page startPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(filteredImagesArray.get(i).getBitmap().getWidth(), filteredImagesArray.get(i).getBitmap().getHeight(), i2).create());
            Canvas canvas = startPage.getCanvas();
            Paint paint = new Paint();
            paint.setColor(-1);
            canvas.drawPaint(paint);
            canvas.drawBitmap(filteredImagesArray.get(i).getBitmap(), 0.0f, 0.0f, (Paint) null);
            pdfDocument.finishPage(startPage);
            i = i2;
        }
        this.nameOfFile = fileName();
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", this.nameOfFile);
            contentValues.put("mime_type", "application/pdf");
            contentValues.put("relative_path", Environment.DIRECTORY_DOWNLOADS + File.separator + this.File_Folder);
            Uri insert = contentResolver.insert(MediaStore.Files.getContentUri("external_primary"), contentValues);
            Objects.requireNonNull(insert);
            OutputStream openOutputStream = contentResolver.openOutputStream(insert);
            this.outputStream = openOutputStream;
            pdfDocument.writeTo(openOutputStream);
            this.filePath = String.valueOf(insert);
        } else {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), this.File_Folder);
            if (!file.exists()) {
                file.mkdirs();
            } else {
                this.pdfFilePath = file + "/" + fileName();
                pdfDocument.writeTo(new FileOutputStream(this.pdfFilePath));
            }
        }
        pdfDocument.close();
    }

    private String getRealPathFromURI(Uri uri2) {
        Cursor loadInBackground = new CursorLoader(getApplicationContext(), uri2, new String[]{"_data"}, (String) null, (String[]) null, (String) null).loadInBackground();
        int columnIndexOrThrow = loadInBackground.getColumnIndexOrThrow("_data");
        loadInBackground.moveToFirst();
        String string = loadInBackground.getString(columnIndexOrThrow);
        loadInBackground.close();
        return string;
    }

    private String fileName() {
        return "PDfScanner" + new SimpleDateFormat("-dd-MM-yy-HH-mm-ss-a", Locale.ENGLISH).format(Long.valueOf(System.currentTimeMillis())) + ".pdf";
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 2) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            new CreatingPDF().execute(new String[0]);
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (!shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
                new AlertDialog.Builder(this.context).setMessage("Permission Necessary").setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        SaveFilesActivity.this.lambda$onRequestPermissionsResult$3$SaveFilesActivity(dialogInterface, i);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        SaveFilesActivity.this.lambda$onRequestPermissionsResult$4$SaveFilesActivity(dialogInterface, i);
                    }
                }).show();
            } else {
                finish();
            }
        }
    }

    public  void lambda$onRequestPermissionsResult$3$SaveFilesActivity(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0);
    }

    public  void lambda$onRequestPermissionsResult$4$SaveFilesActivity(DialogInterface dialogInterface, int i) {
        finish();
    }

    private class CreatingPDF extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        String exception;
        boolean fileSaved;
        boolean filenotSaved;

        private CreatingPDF() {
            this.fileSaved = false;
            this.filenotSaved = false;
        }

        
        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog = new ProgressDialog(SaveFilesActivity.this.context);
            this.dialog = progressDialog;
            progressDialog.setTitle("Please Wait");
            this.dialog.setMessage("Creation of PDF. this may take a while");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        
        public String doInBackground(String... strArr) {
            try {
                SaveFilesActivity.this.saveFileInDirectory();
                this.fileSaved = true;
            } catch (IOException e) {
                this.exception = e.getMessage();
                this.filenotSaved = true;
            }
            SaveFilesActivity.filteredImagesArray.clear();
            return null;
        }

        
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            this.dialog.dismiss();
            if (this.fileSaved) {
                SaveFilesActivity.this.moveTasktoNext();
            } else if (this.filenotSaved) {
                new AlertDialog.Builder(SaveFilesActivity.this.context).setTitle((CharSequence) "Error Message").setMessage((CharSequence) this.exception).setNegativeButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        CreatingPDF.this.lambda$onPostExecute$0$SaveFilesActivity$CreatingPDF(dialogInterface, i);
                    }
                }).show();
                Log.e("TAG", "onPostExecute: " + this.exception);
            }
        }

        public  void lambda$onPostExecute$0$SaveFilesActivity$CreatingPDF(DialogInterface dialogInterface, int i) {
            SaveFilesActivity.this.moveTasktoBack();
            dialogInterface.dismiss();
        }
    }
}
