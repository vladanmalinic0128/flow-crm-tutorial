package com.example.application;

import com.example.application.entities.ConstraintEntity;
import com.example.application.services.CouncelUpdateXlsxService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
class ImportSkipDebugTest {

    @Autowired
    CouncelUpdateXlsxService councelUpdateXlsxService;

    @Test
    void runImportAgainstTestFileAndCaptureSkips() throws Exception {
        String path = "src/main/resources/documents/2026/prijedlozi BO/test/ZBIRNI_SPISAK_1787816157603.xlsx";

        PrintStream originalOut = System.out;
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capture, true, StandardCharsets.UTF_8));

        List<ConstraintEntity> result;
        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {
            result = councelUpdateXlsxService.getModifiedConstraints(workbook, false);
        } finally {
            System.setOut(originalOut);
        }

        String log = capture.toString(StandardCharsets.UTF_8);
        System.out.println("Total constraints successfully matched/returned: " + result.size());

        long skippedNoConstraint = log.lines().filter(l -> l.contains("preskačem red")).count();
        long skippedNoColumnA = log.lines().filter(l -> l.contains("kolona A")).count();
        long warnings = log.lines().filter(l -> l.contains("UPOZORENJE")).count();

        System.out.println("skippedNoConstraint(count)=" + skippedNoConstraint);
        System.out.println("skippedNoColumnA(count)=" + skippedNoColumnA);
        System.out.println("warnings(count)=" + warnings);

        System.out.println("---- FULL LOG START ----");
        System.out.println(log);
        System.out.println("---- FULL LOG END ----");
    }
}
