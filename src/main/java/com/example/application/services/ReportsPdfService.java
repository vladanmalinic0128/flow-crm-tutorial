package com.example.application.services;


import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        return "";
    }
}
