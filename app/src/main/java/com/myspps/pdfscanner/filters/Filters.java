package com.myspps.pdfscanner.filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Array;
import java.util.Random;

public class Filters {
    public static int A = 0;
    public static int B = 0;
    public static final double FULL_CIRCLE_DEGREE = 360.0d;
    public static int G = 0;
    public static final double HALF_CIRCLE_DEGREE = 180.0d;
    public static final double PI = 3.14159d;
    public static int R = 0;
    public static final double RANGE = 256.0d;
    public static Bitmap bitmap;
    public static int pixel;
    PhotoFilters currentEffect;

    public static Bitmap GreyScaleFilter(Bitmap bitmap2) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        bitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel2 = bitmap2.getPixel(i, i2);
                pixel = pixel2;
                A = Color.alpha(pixel2);
                R = Color.red(pixel);
                G = Color.green(pixel);
                int blue = Color.blue(pixel);
                B = blue;
                int i3 = (int) ((((double) R) * 0.299d) + (((double) G) * 0.587d) + (((double) blue) * 0.114d));
                B = i3;
                G = i3;
                R = i3;
                bitmap.setPixel(i, i2, Color.argb(A, i3, i3, i3));
            }
        }
        return bitmap;
    }

    public static Bitmap ContrastFilter(Bitmap bitmap2, double d) {
        Bitmap bitmap3 = bitmap2;
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        canvas.drawBitmap(bitmap3, 0.0f, 0.0f, new Paint(ViewCompat.MEASURED_STATE_MASK));
        double pow = Math.pow((d + 100.0d) / 100.0d, 2.0d);
        int i = 0;
        while (i < width) {
            int i2 = 0;
            while (i2 < height) {
                int pixel2 = bitmap3.getPixel(i, i2);
                int alpha = Color.alpha(pixel2);
                int red = (int) (((((((double) Color.red(pixel2)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                int i3 = 255;
                if (red < 0) {
                    red = 0;
                } else if (red > 255) {
                    red = 255;
                }
                int i4 = width;
                int green = (int) (((((((double) Color.green(pixel2)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (green < 0) {
                    green = 0;
                } else if (green > 255) {
                    green = 255;
                }
                int i5 = height;
                int blue = (int) (((((((double) Color.blue(pixel2)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (blue < 0) {
                    i3 = 0;
                } else if (blue <= 255) {
                    i3 = blue;
                }
                createBitmap.setPixel(i, i2, Color.argb(alpha, red, green, i3));
                i2++;
                bitmap3 = bitmap2;
                height = i5;
                width = i4;
            }
            int i6 = width;
            int i7 = height;
            i++;
            bitmap3 = bitmap2;
        }
        return createBitmap;
    }

    public static Bitmap BrightnessFilter(Bitmap bitmap2, double d) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        bitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel2 = bitmap2.getPixel(i, i2);
                pixel = pixel2;
                A = Color.alpha(pixel2);
                R = Color.red(pixel);
                G = Color.green(pixel);
                int blue = Color.blue(pixel);
                B = blue;
                int i3 = (int) (((double) R) + d);
                R = i3;
                if (i3 < 0) {
                    R = 0;
                } else if (i3 > 255) {
                    R = 255;
                }
                int i4 = (int) (((double) G) + d);
                G = i4;
                if (i4 < 0) {
                    G = 0;
                } else if (i4 > 255) {
                    G = 255;
                }
                int i5 = (int) (((double) blue) + d);
                B = i5;
                if (i5 < 0) {
                    B = 0;
                } else if (i5 > 255) {
                    B = 255;
                }
                bitmap.setPixel(i, i2, Color.argb(A, R, G, B));
            }
        }
        return bitmap;
    }

    public static Bitmap ColorFilter(Bitmap bitmap2, double d, double d2, double d3) {
        double d4 = d * 100.0d;
        double d5 = d2 * 100.0d;
        double d6 = 100.0d * d3;
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        bitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel2 = bitmap2.getPixel(i, i2);
                pixel = pixel2;
                A = Color.alpha(pixel2);
                R = (int) (((double) Color.red(pixel)) * d4);
                G = (int) (((double) Color.green(pixel)) * d5);
                int blue = (int) (((double) Color.blue(pixel)) * d6);
                B = blue;
                bitmap.setPixel(i, i2, Color.argb(A, R, G, blue));
            }
            Bitmap bitmap3 = bitmap2;
        }
        return bitmap;
    }

    public static Bitmap SharpenFilter(Bitmap bitmap2) {
        ConvolutionMatrix convolutionMatrix = new ConvolutionMatrix(3);
        convolutionMatrix.applyConfig(new double[][]{new double[]{0.0d, -2.0d, 0.0d}, new double[]{-2.0d, 11.0d, -2.0d}, new double[]{0.0d, -2.0d, 0.0d}});
        convolutionMatrix.Factor = 3.0d;
        return ConvolutionMatrix.computeConvolution3x3(bitmap2, convolutionMatrix);
    }

    public static Bitmap NoiseFilter(Bitmap bitmap2) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int i3 = (i * width) + i2;
                iArr[i3] = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)) | iArr[i3];
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        bitmap2.recycle();
        return createBitmap;
    }

    public static Bitmap SepiaFilter(Bitmap bitmap2) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel2 = bitmap2.getPixel(i, i2);
                int alpha = Color.alpha(pixel2);
                int red = (int) ((((double) Color.red(pixel2)) * 0.3d) + (((double) Color.green(pixel2)) * 0.59d) + (((double) Color.blue(pixel2)) * 0.11d));
                int i3 = red + 110;
                int i4 = 255;
                if (i3 > 255) {
                    i3 = 255;
                }
                int i5 = red + 65;
                if (i5 > 255) {
                    i5 = 255;
                }
                int i6 = red + 20;
                if (i6 <= 255) {
                    i4 = i6;
                }
                createBitmap.setPixel(i, i2, Color.argb(alpha, i3, i5, i4));
            }
        }
        return createBitmap;
    }

    public static Bitmap SaturationFilter(Bitmap bitmap2, int i) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation((float) (((double) i) / 100.0d));
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap GammaFilter(Bitmap bitmap2, double d, double d2, double d3) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int[] iArr = new int[256];
        int[] iArr2 = new int[256];
        int[] iArr3 = new int[256];
        int i = 0;
        for (int i2 = 256; i < i2; i2 = 256) {
            double d4 = ((double) i) / 255.0d;
            int[] iArr4 = iArr;
            iArr4[i] = Math.min(255, (int) ((Math.pow(d4, 1.0d / d) * 255.0d) + 0.5d));
            int i3 = i;
            iArr2[i3] = Math.min(255, (int) ((Math.pow(d4, 1.0d / d2) * 255.0d) + 0.5d));
            iArr3[i3] = Math.min(255, (int) ((Math.pow(d4, 1.0d / d3) * 255.0d) + 0.5d));
            i = i3 + 1;
            iArr = iArr4;
        }
        int[] iArr5 = iArr;
        for (int i4 = 0; i4 < width; i4++) {
            for (int i5 = 0; i5 < height; i5++) {
                int pixel2 = bitmap2.getPixel(i4, i5);
                createBitmap.setPixel(i4, i5, Color.argb(Color.alpha(pixel2), iArr5[Color.red(pixel2)], iArr2[Color.green(pixel2)], iArr3[Color.blue(pixel2)]));
            }
            Bitmap bitmap3 = bitmap2;
        }
        return createBitmap;
    }

    public static Bitmap VignetteFilter(Bitmap bitmap2) {
        int width = bitmap2.getWidth();
        RadialGradient radialGradient = new RadialGradient((float) (width / 2), (float) (bitmap2.getHeight() / 2), (float) (((double) width) / 1.2d), new int[]{0, 1426063360, ViewCompat.MEASURED_STATE_MASK}, new float[]{0.0f, 0.5f, 1.0f}, Shader.TileMode.REPEAT);
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawARGB(1, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setShader(radialGradient);
        Rect rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        canvas.drawRect(new RectF(rect), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, paint);
        return bitmap2;
    }

    public static Bitmap HueFilter(Bitmap bitmap2, float f) {
        Bitmap copy = bitmap2.copy(bitmap2.getConfig(), true);
        int width = copy.getWidth();
        int height = copy.getHeight();
        float[] fArr = new float[3];
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int pixel2 = copy.getPixel(i2, i);
                Color.colorToHSV(pixel2, fArr);
                fArr[0] = f;
                copy.setPixel(i2, i, Color.HSVToColor(Color.alpha(pixel2), fArr));
            }
        }
        bitmap2.recycle();
        return copy;
    }

    public static Bitmap TintFilter(Bitmap bitmap2, int i) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        double d = (((double) i) * 3.14159d) / 180.0d;
        int sin = (int) (Math.sin(d) * 256.0d);
        int cos = (int) (Math.cos(d) * 256.0d);
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (i2 * width) + i3;
                int i5 = 255;
                int i6 = (iArr[i4] >> 16) & 255;
                int i7 = (iArr[i4] >> 8) & 255;
                int i8 = iArr[i4] & 255;
                int i9 = i7 * 59;
                int i10 = i8 * 11;
                int i11 = (((i6 * 70) - i9) - i10) / 100;
                int i12 = i6 * -30;
                int i13 = ((i12 + (i7 * 41)) - i10) / 100;
                int i14 = ((i12 - i9) + (i8 * 89)) / 100;
                int i15 = (((i6 * 30) + i9) + i10) / 100;
                int i16 = ((sin * i14) + (cos * i11)) / 256;
                int i17 = ((i14 * cos) - (i11 * sin)) / 256;
                int i18 = ((i16 * -51) - (i17 * 19)) / 100;
                int i19 = i16 + i15;
                if (i19 < 0) {
                    i19 = 0;
                } else if (i19 > 255) {
                    i19 = 255;
                }
                int i20 = i18 + i15;
                if (i20 < 0) {
                    i20 = 0;
                } else if (i20 > 255) {
                    i20 = 255;
                }
                int i21 = i15 + i17;
                if (i21 < 0) {
                    i5 = 0;
                } else if (i21 <= 255) {
                    i5 = i21;
                }
                iArr[i4] = -16777216 | (i19 << 16) | (i20 << 8) | i5;
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap InvertFilter(Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
        int height = bitmap2.getHeight();
        int width = bitmap2.getWidth();
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int pixel2 = bitmap2.getPixel(i2, i);
                createBitmap.setPixel(i2, i, Color.argb(Color.alpha(pixel2), 255 - Color.red(pixel2), 255 - Color.green(pixel2), 255 - Color.blue(pixel2)));
            }
        }
        return createBitmap;
    }

    public static Bitmap SketchFilter(Bitmap bitmap2) {
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap2.getConfig());
        int[][] iArr = (int[][]) Array.newInstance(int.class, new int[]{3, 3});
        for (int i = 0; i < height - 2; i++) {
            int i2 = 0;
            while (i2 < width - 2) {
                for (int i3 = 0; i3 < 3; i3++) {
                    for (int i4 = 0; i4 < 3; i4++) {
                        iArr[i3][i4] = bitmap2.getPixel(i2 + i3, i + i4);
                    }
                }
                int alpha = Color.alpha(iArr[1][1]);
                int red = ((((Color.red(iArr[1][1]) * 6) - Color.red(iArr[0][0])) - Color.red(iArr[0][2])) - Color.red(iArr[2][0])) - Color.red(iArr[2][2]);
                int green = ((((Color.green(iArr[1][1]) * 6) - Color.green(iArr[0][0])) - Color.green(iArr[0][2])) - Color.green(iArr[2][0])) - Color.green(iArr[2][2]);
                int blue = ((((6 * Color.blue(iArr[1][1])) - Color.blue(iArr[0][0])) - Color.blue(iArr[0][2])) - Color.blue(iArr[2][0])) - Color.blue(iArr[2][2]);
                int i5 = red + 130;
                int i6 = 255;
                if (i5 < 0) {
                    i5 = 0;
                } else if (i5 > 255) {
                    i5 = 255;
                }
                int i7 = green + 130;
                if (i7 < 0) {
                    i7 = 0;
                } else if (i7 > 255) {
                    i7 = 255;
                }
                int i8 = blue + 130;
                if (i8 < 0) {
                    i6 = 0;
                } else if (i8 <= 255) {
                    i6 = i8;
                }
                i2++;
                createBitmap.setPixel(i2, i + 1, Color.argb(alpha, i5, i7, i6));
            }
        }
        return createBitmap;
    }
}
