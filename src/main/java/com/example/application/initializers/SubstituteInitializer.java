package com.example.application.initializers;

import com.example.application.entities.MentorEntity;
import com.example.application.entities.SubstituteEntity;
import com.example.application.repositories.MentorRepository;
import com.example.application.repositories.SubstituteRepository;
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
public class SubstituteInitializer /*implements ApplicationRunner*/ {
    private final SubstituteRepository substituteRepository;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private final MentorRepository mentorRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        String absolutePath = "src/main/resources/documents/rezervni_2025_2.xlsx";
        try (InputStream inputStream = new FileInputStream(absolutePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<SubstituteEntity> substituteEntityList = new ArrayList<>();
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if(row == null)
                    continue;
                SubstituteEntity entity = createSubstituteFromRow(row);
                substituteEntityList.add(entity);
                substituteRepository.save(entity);
            }
            //for(SubstituteEntity entity: substituteEntityList)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SubstituteEntity createSubstituteFromRow(Row row) {
        SubstituteEntity substituteEntity = new SubstituteEntity();

        String orderNumber = getCellValue(row.getCell(0));
        if(orderNumber == null)
            substituteEntity.setOrderNumber(null);
        else {
            substituteEntity.setOrderNumber(Integer.parseInt(orderNumber));
        }

        String name = getCellValue(row.getCell(1));
        if(name == null || name.trim().length() == 0) {
            substituteEntity.setFirstname(null);
            substituteEntity.setLastname(null);
        }
        else
            readNameCell(name, substituteEntity);

        String qualifications = getCellValue(row.getCell(2));
        if(qualifications == null || qualifications.trim().length() == 0)
            substituteEntity.setQualifications(null);
        else
            substituteEntity.setQualifications(qualifications);

        String jmbg = getCellValue(row.getCell(3));
        readJmbg(jmbg, substituteEntity);

        String phoneNumber = getCellValue(row.getCell(4));
        readPhoneNumber(phoneNumber, substituteEntity);

        String bankNumber = getCellValue(row.getCell(5));
        readBankNumber(bankNumber, substituteEntity);

        String bankName = getCellValue(row.getCell(6));
        if(bankName == null || bankName.length() == 0)
            substituteEntity.setBankName(null);
        else
            substituteEntity.setBankName(bankName);

        substituteEntity.setIsPresident(false);

        MentorEntity dubravko = null;
        MentorEntity bojana = null;
        MentorEntity dusko = null;
        MentorEntity graba = null;
        MentorEntity leona = null;
        MentorEntity dujko = null;
        MentorEntity igor = null;


        dubravko = tryFetchMentor(1L);
        bojana = tryFetchMentor(2L);
        dusko = tryFetchMentor(3L);
        graba = tryFetchMentor(4L);
        leona = tryFetchMentor(5L);
        dujko = tryFetchMentor(6L);
        igor = tryFetchMentor(7L);

        String mentorString = getCellValue(row.getCell(7));

        if(mentorString == null) {
            substituteEntity.setMentor(dubravko);
            return substituteEntity;
        }
        switch (mentorString) {
            case "Бојана":
                substituteEntity.setMentor(bojana);
                break;
            case "Душко":
                substituteEntity.setMentor(dusko);
                break;
            case "Игор":
                substituteEntity.setMentor(igor);
                break;
            case "Граба":
                substituteEntity.setMentor(graba);
                break;
            case "Дубравко":
                substituteEntity.setMentor(dubravko);
                break;
            case "Дујко":
                substituteEntity.setMentor(dujko);
                break;
            default:
                substituteEntity.setMentor(dubravko);
                break;
        }
        return substituteEntity;
    }

    private MentorEntity tryFetchMentor(Long number) {
        Optional<MentorEntity> optional = mentorRepository.findById(1L);
        if(optional.isPresent())
            return optional.get();
        else
            return null;
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

    public void readNameCell(String value, SubstituteEntity entity) {
        String[] splittingResult = value.split(" ", 2);

        if (splittingResult.length >= 2) {
            entity.setFirstname(splittingResult[0]);
            entity.setLastname( splittingResult[1]);
        }
    }

    private void readGenderCell(String gender, SubstituteEntity entity) {
        if(gender.equalsIgnoreCase("М"))
            entity.setIsMale(true);
        else if( gender.equalsIgnoreCase("Ж"))
            entity.setIsMale(false);
        else
            entity.setIsMale(null);
    }

    private void readJmbg(String jmbg, SubstituteEntity entity) {
        jmbg = jmbg.replaceAll("\\D", "");
        if(jmbg == null || jmbg.trim().length() == 0)
            entity.setJmbg(null);
        else
            entity.setJmbg(jmbg);
    }

    private void readPhoneNumber(String phoneNumber, SubstituteEntity entity) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        if(phoneNumber == null || phoneNumber.trim().length() == 0)
            entity.setPhoneNumber(null);
        else
            entity.setPhoneNumber(phoneNumber);
    }

    private void readBankNumber(String bankNumber, SubstituteEntity entity) {
        bankNumber = bankNumber.replaceAll("\\D", "");
        if(bankNumber == null || bankNumber.trim().length() == 0)
            entity.setBankNumber(null);
        else
            entity.setBankNumber(bankNumber);
    }
}
