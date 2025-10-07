package com.myspps.pdfscanner.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageConverter {
    public static Bitmap getBitmapFromUri(ContentResolver contentResolver, Uri uri) {
        InputStream inputStream;
        try {
            inputStream = contentResolver.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            inputStream = null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inMutable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        try {
            inputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return decodeStream;
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 72, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= 26) {
            return Base64.getEncoder().encodeToString(byteArray);
        }
        return null;
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int i) {
        int i2;
        float width = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
        if (width > 1.0f) {
            i2 = (int) (((float) i) / width);
        } else {
            int i3 = (int) (((float) i) * width);
            i2 = i;
            i = i3;
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public static Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", (String) null));
    }
}
