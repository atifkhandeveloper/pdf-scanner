package com.myspps.pdfscanner.model;

public class ViewFilesModel {
    String date;
    String name;
    String path;
    String size;

    public ViewFilesModel(String str, String str2, String str3, String str4) {
        this.path = str;
        this.name = str2;
        this.size = str3;
        this.date = str4;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }
}
