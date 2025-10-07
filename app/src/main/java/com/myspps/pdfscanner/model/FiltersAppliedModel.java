package com.myspps.pdfscanner.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class FiltersAppliedModel implements Parcelable {
    public static final Creator<FiltersAppliedModel> CREATOR = new Creator<FiltersAppliedModel>() {
        public FiltersAppliedModel createFromParcel(Parcel parcel) {
            return new FiltersAppliedModel(parcel);
        }

        public FiltersAppliedModel[] newArray(int i) {
            return new FiltersAppliedModel[i];
        }
    };
    Bitmap bitmap;
    int position;

    public int describeContents() {
        return 0;
    }

    public FiltersAppliedModel(Bitmap bitmap2, int i) {
        this.bitmap = bitmap2;
        this.position = i;
    }

    protected FiltersAppliedModel(Parcel parcel) {
        this.bitmap = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
        this.position = parcel.readInt();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.bitmap, i);
        parcel.writeInt(this.position);
    }
}
