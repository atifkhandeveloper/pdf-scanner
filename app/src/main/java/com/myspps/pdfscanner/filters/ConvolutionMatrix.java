package com.myspps.pdfscanner.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.lang.reflect.Array;

public class ConvolutionMatrix {
    public static final int SIZE = 3;
    public double Factor = 1.0d;
    public double[][] Matrix;
    public double Offset = 1.0d;

    public ConvolutionMatrix(int i) {
        int[] iArr = new int[2];
        iArr[1] = i;
        iArr[0] = i;
        this.Matrix = (double[][]) Array.newInstance(double.class, iArr);
    }

    public void setAll(double d) {
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                this.Matrix[i][i2] = d;
            }
        }
    }

    public void applyConfig(double[][] dArr) {
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                this.Matrix[i][i2] = dArr[i][i2];
            }
        }
    }

    public static Bitmap computeConvolution3x3(Bitmap bitmap, ConvolutionMatrix convolutionMatrix) {
        ConvolutionMatrix convolutionMatrix2 = convolutionMatrix;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int[][] iArr = (int[][]) Array.newInstance(int.class, new int[]{3, 3});
        int i = 0;
        while (i < height - 2) {
            int i2 = 0;
            while (i2 < width - 2) {
                for (int i3 = 0; i3 < 3; i3++) {
                    for (int i4 = 0; i4 < 3; i4++) {
                        iArr[i3][i4] = bitmap.getPixel(i2 + i3, i + i4);
                    }
                    Bitmap bitmap2 = bitmap;
                }
                Bitmap bitmap3 = bitmap;
                int alpha = Color.alpha(iArr[1][1]);
                int i5 = 0;
                int i6 = 0;
                int i7 = 0;
                for (int i8 = 0; i8 < 3; i8++) {
                    int i9 = 0;
                    while (i9 < 3) {
                        i5 = (int) (((double) i5) + (((double) Color.red(iArr[i8][i9])) * convolutionMatrix2.Matrix[i8][i9]));
                        i6 = (int) (((double) i6) + (((double) Color.green(iArr[i8][i9])) * convolutionMatrix2.Matrix[i8][i9]));
                        i7 = (int) (((double) i7) + (((double) Color.blue(iArr[i8][i9])) * convolutionMatrix2.Matrix[i8][i9]));
                        i9++;
                        i = i;
                        width = width;
                        height = height;
                    }
                    int i10 = width;
                    int i11 = height;
                    int i12 = i;
                }
                int i13 = width;
                int i14 = height;
                int i15 = i;
                double d = convolutionMatrix2.Factor;
                double d2 = convolutionMatrix2.Offset;
                int i16 = (int) ((((double) i5) / d) + d2);
                int i17 = 255;
                if (i16 < 0) {
                    i16 = 0;
                } else if (i16 > 255) {
                    i16 = 255;
                }
                int i18 = (int) ((((double) i6) / d) + d2);
                if (i18 < 0) {
                    i18 = 0;
                } else if (i18 > 255) {
                    i18 = 255;
                }
                int i19 = (int) ((((double) i7) / d) + d2);
                if (i19 < 0) {
                    i17 = 0;
                } else if (i19 <= 255) {
                    i17 = i19;
                }
                i2++;
                createBitmap.setPixel(i2, i15 + 1, Color.argb(alpha, i16, i18, i17));
                i = i15;
                width = i13;
                height = i14;
            }
            Bitmap bitmap4 = bitmap;
            int i20 = width;
            int i21 = height;
            i++;
        }
        Bitmap bitmap5 = bitmap;
        bitmap.recycle();
        return createBitmap;
    }
}
