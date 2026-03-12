package com.aijobplatform.ai.common;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

public class ResumeParserUtil {

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

    private static String extractFromPDF(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
    private static String extractFromDOCX(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {

            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        }
    }
}