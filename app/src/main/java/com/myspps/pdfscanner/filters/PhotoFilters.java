package com.myspps.pdfscanner.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Matrix4f;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.renderscript.ScriptIntrinsicConvolve3x3;

public class PhotoFilters {
    Allocation inputAllocation;
    Bitmap outBitmap;
    Allocation outputAllocation;
    RenderScript renderScript;

    public Bitmap one(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{-0.33f, -0.33f, -0.33f, 1.0f, -0.59f, -0.59f, -0.59f, 1.0f, -0.11f, -0.11f, -0.11f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap two(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setGreyscale();
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap three(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.78f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap four(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{0.3f, 0.0f, 0.0f, 0.0f, 0.0f, 0.65f, 0.0f, 0.0f, 0.0f, 0.0f, 0.49f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap five(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{-0.3597053f, 0.37725273f, 0.66384166f, 0.0f, 1.5668082f, 0.4566682f, 1.1261392f, 0.0f, -0.14710288f, 0.22607906f, -0.7299808f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap six(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.2f, 0.1f, 0.2f, 0.7f, 0.7f, 1.0f, 0.0f, -0.5f, -0.7f, 0.2f, 0.5f, 1.3f, 0.0f, -0.1f, 0.0f, 0.9f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap seven(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.229946f, 0.020952377f, 0.38324407f, 0.0f, 0.4501389f, 1.1873742f, -0.10693325f, -0.34008488f, 0.13167344f, 1.0636892f, 0.0f, 0.0f, 0.0f, 0.0f, 11.91f, 11.91f, 11.91f, 0.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap eight(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.44f, 0.0f, 0.0f, 0.0f, 0.0f, 1.44f, 0.0f, 0.0f, 0.0f, 0.0f, 1.44f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap nine(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{-2.0f, -1.0f, 1.0f, -2.0f, 0.0f, -2.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap ten(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.0f, 0.0f, 0.1f, -0.1f, 0.0f, 1.0f, 0.2f, 0.0f, 0.0f, 0.0f, 1.3f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap eleven(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.728147f, -0.412105f, 0.541145f, 0.0f, 0.28937826f, 1.1883553f, -1.1763717f, 0.0f, -1.0175253f, 0.22374965f, 1.6352267f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap twelve(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{0.309f, 0.409f, 0.309f, 0.0f, 0.609f, 0.309f, 0.409f, 0.0f, 0.42f, 0.42f, 0.2f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap thirteen(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicConvolve3x3 create = ScriptIntrinsicConvolve3x3.create(renderScript2, Element.U8_4(renderScript2));
        create.setInput(this.inputAllocation);
        create.setCoefficients(new float[]{-2.0f, -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.0f});
        create.forEach(this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap fourteen(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicConvolve3x3 create = ScriptIntrinsicConvolve3x3.create(renderScript2, Element.U8_4(renderScript2));
        create.setInput(this.inputAllocation);
        create.setCoefficients(new float[]{0.2f, 0.3f, 0.2f, 0.1f, 0.1f, 0.1f, 0.2f, 0.3f, 0.2f});
        create.forEach(this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap fifteen(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{2.1027913f, -0.29821262f, 0.42128146f, 0.0f, 0.22289757f, 1.687012f, -0.8834213f, 0.0f, -0.7656889f, 0.17120072f, 2.0221398f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }

    public Bitmap sixteen(Context context, Bitmap bitmap) {
        this.renderScript = RenderScript.create(context);
        this.outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Allocation createFromBitmap = Allocation.createFromBitmap(this.renderScript, bitmap);
        this.inputAllocation = createFromBitmap;
        this.outputAllocation = Allocation.createTyped(this.renderScript, createFromBitmap.getType());
        RenderScript renderScript2 = this.renderScript;
        ScriptIntrinsicColorMatrix create = ScriptIntrinsicColorMatrix.create(renderScript2, Element.U8_4(renderScript2));
        create.setColorMatrix(new Matrix4f(new float[]{1.2748853f, -0.22851132f, 0.44108868f, 0.0f, 0.32366425f, 0.9551408f, -0.7059358f, 0.0f, -0.6985495f, 0.17337048f, 1.164847f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        create.forEach(this.inputAllocation, this.outputAllocation);
        this.outputAllocation.copyTo(this.outBitmap);
        return this.outBitmap;
    }
}
