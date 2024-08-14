package com.example.application.services;


import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.HorizontalAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;

@RequiredArgsConstructor
@Service
public class ReportsPdfService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";

    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    public InputStream getStream(String fileString) {
        File file = new File(fileString);
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

    public String generateReportByCode(VotingCouncelEntity entity, String fileTitle, ScriptEnum value) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        //set margins
        float topMargin = 25f;
        float leftMargin=42f;
        float rightMargin=42f;
        document.setTopMargin(topMargin);
        document.setLeftMargin(leftMargin);
        document.setRightMargin(rightMargin);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);

        generateContentByCode(entity, document, value);

        document.close();

        String fileName = ROOT_PATH + File.separator + entity.getCode() + "_" + System.currentTimeMillis() + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
    }

    private void generateContentByCode(VotingCouncelEntity entity, Document document, ScriptEnum value) {
        
    }
}
