package com.example.application.initializers;

import com.example.application.entities.PresidentEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.VotingCouncelRepository;
import com.example.application.services.CyrillicToLatinConverter;
import com.example.application.services.LatinToCyrillicConverter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PresidentInitializer /*implements ApplicationRunner*/ {
    private final PresidentRepository presidentRepository;
    private final VotingCouncelRepository votingCouncelRepository;
    private final LatinToCyrillicConverter latinToCyrillicConverter;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        String absolutePath = "src/main/resources/documents/predsjednici2025.xlsx";
        try (InputStream inputStream = new FileInputStream(absolutePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<PresidentEntity> presidentEntityList = new ArrayList<>();
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if(row == null)
                    continue;
                PresidentEntity entity = createPresidentFromRow(row);
                presidentEntityList.add(entity);
                presidentRepository.save(entity);
            }
            //for(SubstituteEntity entity: substituteEntityList)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PresidentEntity createPresidentFromRow(Row row) {
        PresidentEntity entity = new PresidentEntity();

        String value = getCellValue(row.getCell(0));
        if(value == null|| value.trim().length() == 0)
            entity.setLastname(null);
        else {
            entity.setLastname(value);
        }

        value = getCellValue(row.getCell(1));
        if(value == null|| value.trim().length() == 1)
            entity.setFirstname(null);
        else {
            entity.setFirstname(value);
        }

        String jmbg = getCellValue(row.getCell(2));
        readJmbg(jmbg, entity);

        String phoneNumber = getCellValue(row.getCell(3));
        readPhoneNumber(phoneNumber, entity);

        String bankNumber = getCellValue(row.getCell(5));
        readBankNumber(bankNumber, entity);

        String bankName = getCellValue(row.getCell(6));
        if(bankName == null || bankName.length() == 0)
            entity.setBankName(null);
        else
            entity.setBankName(bankName);

        String code = getCellValue(row.getCell(4));
        if(code == null|| code.trim().length() == 0)
            entity.setVotingCouncel(null);
        else {
            Optional<VotingCouncelEntity> votingCouncelEntityOptional = votingCouncelRepository.findByCode(code);
            if(votingCouncelEntityOptional.isEmpty())
                entity.setVotingCouncel(null);
            else {
                VotingCouncelEntity votingCouncel = votingCouncelEntityOptional.get();
                entity.setVotingCouncel(votingCouncel);
                if(presidentRepository.existsByVotingCouncel_Code(votingCouncel.getCode()) == false)
                    entity.setIsPresident(true);
                else
                    entity.setIsPresident(false);
            }
        }
        return entity;
    }

    private String getCellValue(Cell cell) {
        if(cell == null)
            return null;
        switch (cell.getCellType()) {
            case STRING:
                return latinToCyrillicConverter.convert(cell.getStringCellValue().replaceAll("^\\s+", "").trim());
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC:
                return Long.toString((long)cell.getNumericCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Cell Type";
        }
    }

    private void readJmbg(String jmbg, PresidentEntity entity) {
        jmbg = jmbg.replaceAll("\\D", "");
        if(jmbg == null || jmbg.trim().length() == 0)
            entity.setJmbg(null);
        else
            entity.setJmbg(jmbg);
    }

    private void readPhoneNumber(String phoneNumber, PresidentEntity entity) {
        if(phoneNumber == null) {
            entity.setPhoneNumber(null);
            return;
        }
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        if(phoneNumber == null || phoneNumber.trim().length() == 0)
            entity.setPhoneNumber(null);
        else
            entity.setPhoneNumber(phoneNumber);
    }

    private void readBankNumber(String bankNumber, PresidentEntity entity) {
        if(bankNumber == null) {
            entity.setBankNumber(null);
            return;
        }
        bankNumber = bankNumber.replaceAll("\\D", "");
        if(bankNumber.trim().length() == 0)
            entity.setBankNumber(null);
        else
            entity.setBankNumber(bankNumber);
    }
}
