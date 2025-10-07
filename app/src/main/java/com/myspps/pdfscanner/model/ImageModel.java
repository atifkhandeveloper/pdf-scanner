package com.myspps.pdfscanner.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        public ImageModel createFromParcel(Parcel parcel) {
            return new ImageModel(parcel);
        }

        public ImageModel[] newArray(int i) {
            return new ImageModel[i];
        }
    };
    Uri uri;

    public int describeContents() {
        return 0;
    }

    public ImageModel(Uri uri2) {
        this.uri = uri2;
    }

    protected ImageModel(Parcel parcel) {
        this.uri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
    }

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri2) {
        this.uri = uri2;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.uri, i);
    }
}
