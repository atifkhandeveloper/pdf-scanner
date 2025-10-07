package com.myspps.pdfscanner.model;

public class ImagePickerModel {
    String imagePath;
    boolean isSelected;
    String name;

    public ImagePickerModel(String str, boolean z) {
        this.imagePath = str;
        this.isSelected = z;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String str) {
        this.imagePath = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
