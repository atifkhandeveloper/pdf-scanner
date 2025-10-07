package com.myspps.pdfscanner.model;

import android.net.Uri;

public class Model {
    String fPath;
    Uri img;

    public void setSelected(boolean z) {
    }

    public Model() {
    }

    public Uri getImg() {
        return this.img;
    }

    public void setImg(Uri uri) {
        this.img = uri;
    }

    public Model(String str, boolean z) {
        this.fPath = str;
    }

    public String getfPath() {
        return this.fPath;
    }

    public void setfPath(String str) {
        this.fPath = str;
    }
}
