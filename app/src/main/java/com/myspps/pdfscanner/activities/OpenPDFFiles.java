package com.myspps.pdfscanner.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.utils.CustomToast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import java.io.File;
import java.util.List;

public class OpenPDFFiles extends AppCompatActivity {

    Context context = this;
    String isNew = null;
    int pageNumber = 1;
    PDFView pdfView;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_open_p_d_f_files);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);





        String stringExtra = getIntent().getStringExtra("pdfPath");
        this.isNew = getIntent().getStringExtra("newPdf");
        Log.e("TAG", "onCreate: " + stringExtra);
        this.pdfView = (PDFView) findViewById(R.id.pdfViewer);
        String name = new File(stringExtra).getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf > 0 && lastIndexOf < name.length() - 1) {
            name = name.substring(0, lastIndexOf);
        }
        if (this.isNew == null) {
            String finalName = name;
            this.pdfView.fromUri(Uri.parse(stringExtra)).defaultPage(0).enableSwipe(true).autoSpacing(true).swipeHorizontal(false).onPageChange(new OnPageChangeListener() {
                public final  String f$1;

                {
                    this.f$1 = finalName;
                }

                public final void onPageChanged(int i, int i2) {
                    OpenPDFFiles.this.lambda$onCreate$0$OpenPDFFiles(this.f$1, i, i2);
                }
            }).onPageError(new OnPageErrorListener() {
                public final void onPageError(int i, Throwable th) {
                    OpenPDFFiles.this.lambda$onCreate$1$OpenPDFFiles(i, th);
                }
            }).enableAnnotationRendering(true).onLoad(new OnLoadCompleteListener() {
                public final void loadComplete(int i) {
                    OpenPDFFiles.this.lambda$onCreate$2$OpenPDFFiles(i);
                }
            }).scrollHandle(new DefaultScrollHandle(this)).spacing(20).enableDoubletap(true).load();
        } else {
            String finalName1 = name;
            this.pdfView.fromUri(Uri.fromFile(new File(stringExtra))).defaultPage(0).enableSwipe(true).autoSpacing(true).swipeHorizontal(false).onPageChange(new OnPageChangeListener() {
                public final  String f$1;

                {
                    this.f$1 = finalName1;
                }

                public final void onPageChanged(int i, int i2) {
                    OpenPDFFiles.this.lambda$onCreate$3$OpenPDFFiles(this.f$1, i, i2);
                }
            }).onPageError(new OnPageErrorListener() {
                public final void onPageError(int i, Throwable th) {
                    OpenPDFFiles.this.lambda$onCreate$4$OpenPDFFiles(i, th);
                }
            }).enableAnnotationRendering(true).onLoad(new OnLoadCompleteListener() {
                public final void loadComplete(int i) {
                    OpenPDFFiles.this.lambda$onCreate$5$OpenPDFFiles(i);
                }
            }).scrollHandle(new DefaultScrollHandle(this)).spacing(20).enableDoubletap(true).load();
        }
    }

    public  void lambda$onCreate$0$OpenPDFFiles(String str, int i, int i2) {
        this.pageNumber = i;
        setTitle(String.format("%s %s /%s", new Object[]{str, Integer.valueOf(i + 1), Integer.valueOf(i2)}));
    }

    public  void lambda$onCreate$1$OpenPDFFiles(int i, Throwable th) {
        new CustomToast(this.context, th.getMessage());
    }

    public  void lambda$onCreate$2$OpenPDFFiles(int i) {
        this.pdfView.getDocumentMeta();
        printBookmarksTree(this.pdfView.getTableOfContents(), "-");
    }

    public  void lambda$onCreate$3$OpenPDFFiles(String str, int i, int i2) {
        this.pageNumber = i;
        setTitle(String.format("%s %s /%s", new Object[]{str, Integer.valueOf(i + 1), Integer.valueOf(i2)}));
    }

    public  void lambda$onCreate$4$OpenPDFFiles(int i, Throwable th) {
        new CustomToast(this.context, th.getMessage());
    }

    public  void lambda$onCreate$5$OpenPDFFiles(int i) {
        this.pdfView.getDocumentMeta();
        printBookmarksTree(this.pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> list, String str) {
        for (PdfDocument.Bookmark next : list) {
            if (next.hasChildren()) {
                List<PdfDocument.Bookmark> children = next.getChildren();
                printBookmarksTree(children, str + "-");
            }
        }
    }
}
