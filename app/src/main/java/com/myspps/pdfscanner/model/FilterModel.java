package com.myspps.pdfscanner.model;

import android.graphics.Bitmap;

public class FilterModel {
    Bitmap bitmapPath;
    String filterName;

    public FilterModel(Bitmap bitmap, String str) {
        this.bitmapPath = bitmap;
        this.filterName = str;
    }

    public Bitmap getBitmapPath() {
        return this.bitmapPath;
    }

    public void setBitmapPath(Bitmap bitmap) {
        this.bitmapPath = bitmap;
    }

    public String getFilterName() {
        return this.filterName;
    }

    public void setFilterName(String str) {
        this.filterName = str;
    }
}
