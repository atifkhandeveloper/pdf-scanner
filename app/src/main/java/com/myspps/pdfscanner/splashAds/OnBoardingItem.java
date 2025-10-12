package com.myspps.pdfscanner.splashAds;


public class OnBoardingItem {
    private int image;
    private String title;
    private String description;

    public OnBoardingItem(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getImage() { return image; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

