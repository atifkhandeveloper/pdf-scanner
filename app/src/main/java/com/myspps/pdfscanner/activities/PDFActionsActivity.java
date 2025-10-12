package com.myspps.pdfscanner.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;

import com.myspps.pdfscanner.R;

import com.myspps.pdfscanner.utils.CustomToast;
import java.io.File;

public class PDFActionsActivity extends AppCompatActivity {
    Activity activity = this;
    Context context = this;
    EditText editTextFileName;
    EditText etRename;
    boolean fileNameChanged = false;
    String filename;
    File from;
    String newPath;
    Button openFile;
    String pdfPath;
    Button shareFile;
    File to;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_p_d_f_actions);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);






        this.editTextFileName = (EditText) findViewById(R.id.fileName);
        this.openFile = (Button) findViewById(R.id.open_file);
        this.shareFile = (Button) findViewById(R.id.share_file);
        this.pdfPath = getIntent().getStringExtra("filePath");
        File file = new File(this.pdfPath);
        String name = file.getName();
        this.filename = name;
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf > 0 && lastIndexOf < name.length() - 1) {
            name = name.substring(0, lastIndexOf);
        }
        this.editTextFileName.setText(name);
        this.editTextFileName.setFocusable(false);

        this.editTextFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextFileName.setFocusable(false);
                Dialog dialog = new Dialog(PDFActionsActivity.this);
                dialog.setContentView(R.layout.dialog_rename);
                dialog.getWindow().setLayout(-1, -2);
                etRename = (EditText) dialog.findViewById(R.id.rename);
                etRename.setText(editTextFileName.getText().toString());
                ((AppCompatButton) dialog.findViewById(R.id.textViewSave)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etRename.getText().toString().isEmpty()) {
                            new CustomToast(PDFActionsActivity.this, "Please enter valid file name");
                            return;
                        }
                        rename(etRename.getText().toString());
                        dialog.dismiss();
                    }
                });
                ((AppCompatButton) dialog.findViewById(R.id.textViewNo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        this.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PDFActionsActivity.this, OpenPDFFiles.class);
                if (!fileNameChanged) {
                    intent.putExtra("pdfPath", pdfPath);
                    intent.putExtra("newPdf", "new");
                } else {
                    intent.putExtra("pdfPath", to.getPath());
                    intent.putExtra("newPdf", "new");
                }
                startActivity(intent);
            }
        });

        this.shareFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                Uri uriForFile = FileProvider.getUriForFile(PDFActionsActivity.this, getPackageName() + ".provider", file);
                intent.setType("*/*");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

    }

    private void rename(String str) {
        File file = new File(this.pdfPath);
        this.from = file;
        String absolutePath = file.getParentFile().getAbsolutePath();
        String absolutePath2 = this.from.getAbsolutePath();
        String substring = absolutePath2.substring(absolutePath2.lastIndexOf("."));
        if (str.isEmpty()) {
            new CustomToast(this.context, "Please enter valid name");
        } else {
            this.newPath = absolutePath + "/" + str + substring;
        }
        File file2 = new File(this.newPath);
        this.to = file2;
        if (this.from.renameTo(file2)) {
            this.context.getContentResolver().delete(MediaStore.Files.getContentUri("external"), "_data=?", new String[]{this.from.getAbsolutePath()});
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(this.to));
            this.context.sendBroadcast(intent);
            new CustomToast(this.context, "File Renamed");
            this.editTextFileName.setText(this.etRename.getText().toString());
            this.fileNameChanged = true;
            return;
        }
        new CustomToast(this.context, "Can't rename file");
    }

    private void moveTasktoBack() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    public void onBackPressed() {
        moveTasktoBack();
    }

}
