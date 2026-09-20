package com.example.application.initializers;

import com.example.application.entities.PresidentEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.VotingCouncelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class President2026Initializer /*implements ApplicationRunner*/ {
    private final PresidentRepository presidentRepository;
    private final VotingCouncelRepository votingCouncelRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        String absolutePath = "src/main/resources/documents/2026/predsjednici kontakt.xlsx";
        try (InputStream inputStream = new FileInputStream(absolutePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // No header row - data starts at the first row
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                PresidentEntity entity = createPresidentFromRow(row);
                if (entity != null)
                    presidentRepository.save(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PresidentEntity createPresidentFromRow(Row row) {
        String code = getCellValue(row.getCell(0));
        if (code == null || code.isEmpty())
            return null;

        Optional<VotingCouncelEntity> votingCouncelOptional = votingCouncelRepository.findByCode(code);
        if (votingCouncelOptional.isEmpty()) {
            System.out.println("President2026Initializer: voting councel not found for code " + code);
            return null;
        }

        String firstname = textOrNull(getCellValue(row.getCell(3)));
        String lastname = textOrNull(getCellValue(row.getCell(4)));
        String jmbg = digitsOrNull(getCellValue(row.getCell(2)));
        // Some deputies are not filled in yet - don't create empty records for them
        if (firstname == null && lastname == null && jmbg == null)
            return null;

        PresidentEntity entity = new PresidentEntity();
        entity.setVotingCouncel(votingCouncelOptional.get());

        // "Предсједник" / "Замјеник предсједника" - a deputy is anything that starts with "Зам"
        String role = getCellValue(row.getCell(1));
        entity.setIsPresident(role == null || !role.toLowerCase().startsWith("зам"));

        entity.setJmbg(jmbg);
        entity.setFirstname(firstname);
        entity.setLastname(lastname);
        entity.setPhoneNumber(digitsOrNull(getCellValue(row.getCell(5))));
        return entity;
    }

    private String textOrNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private String digitsOrNull(String value) {
        if (value == null)
            return null;
        value = value.replaceAll("\\D", "");
        return value.isEmpty() ? null : value;
    }

    private String getCellValue(Cell cell) {
        if (cell == null)
            return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return Long.toString((long) cell.getNumericCellValue());
            default:
                return null;
        }
    }
}
