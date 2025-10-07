package com.myspps.pdfscanner.interfaces;

import android.net.Uri;

public interface IScanner {
    void onBitmapSelect(Uri uri);

    void onScanFinish(Uri uri);
}
