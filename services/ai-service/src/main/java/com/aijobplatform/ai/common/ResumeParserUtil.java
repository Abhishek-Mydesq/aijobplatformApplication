package com.aijobplatform.ai.common;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ResumeParserUtil {

    // OLD
    public static String extractText(MultipartFile file) {

        try {

            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                throw new RuntimeException("Invalid file name");
            }

            if (fileName.endsWith(".pdf")) {
                return extractFromPDF(file);
            }

            if (fileName.endsWith(".docx")) {
                return extractFromDOCX(file);
            }

            throw new RuntimeException("Unsupported file type");

        } catch (Exception e) {
            throw new RuntimeException("Error parsing resume", e);
        }
    }


    // ✅ NEW (for production)
    public static String extractText(File file) {

        try {

            String fileName = file.getName();

            if (fileName.endsWith(".pdf")) {
                return extractFromPDF(file);
            }

            if (fileName.endsWith(".docx")) {
                return extractFromDOCX(file);
            }

            throw new RuntimeException("Unsupported file type");

        } catch (Exception e) {
            throw new RuntimeException("Error parsing resume", e);
        }
    }


    // ---------- PDF Multipart ----------
    private static String extractFromPDF(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }


    // ---------- DOCX Multipart ----------
    private static String extractFromDOCX(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        }
    }


    // ---------- PDF File ----------
    private static String extractFromPDF(File file) throws Exception {

        try (InputStream inputStream = new FileInputStream(file);
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }


    // ---------- DOCX File ----------
    private static String extractFromDOCX(File file) throws Exception {

        try (InputStream inputStream = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(inputStream)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        }
    }
}