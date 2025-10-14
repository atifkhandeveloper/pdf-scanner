package com.myspps.pdfscanner.activities;

import android.content.Context;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfToDocxConverter {

    public static boolean convertPdfToDocx(Context context, String pdfPath, String docxPath) {
        PDDocument pdfDocument = null;
        try {
            // Initialize PDFBox
            PDFBoxResourceLoader.init(context);

            // Load PDF (works on Android)
            pdfDocument = PDDocument.load(new File(pdfPath));

            // Extract text
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdfDocument);

            // Write to DOCX
            XWPFDocument docx = new XWPFDocument();
            XWPFParagraph paragraph = docx.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(text);

            FileOutputStream out = new FileOutputStream(docxPath);
            docx.write(out);
            out.close();
            docx.close();
            pdfDocument.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pdfDocument != null) pdfDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
