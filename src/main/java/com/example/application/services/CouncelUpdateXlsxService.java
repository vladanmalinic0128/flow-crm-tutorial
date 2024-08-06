package com.example.application.services;

import com.example.application.entities.ConstraintEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.VotingCouncelRepository;
import jakarta.validation.Constraint;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouncelUpdateXlsxService {
    private final String MUNICIPALITY_CODE = "034Б";
    private final VotingCouncelRepository votingCouncelRepository;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    public List<ConstraintEntity> getModifiedConstraints(Workbook workbook) {
        List<ConstraintEntity> result = new ArrayList<>();

        VotingCouncelEntity activeVotingCouncel = null;
        boolean readingMembers = false;

        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            if(cell == null)
                continue;
            String value = getCellValue(cell);
            if(activeVotingCouncel == null) {
                System.out.println("Nullic: " + row.getRowNum());
            }
            if(value.startsWith(MUNICIPALITY_CODE)) {
                activeVotingCouncel = tryReadingVotingCouncel(cell);
            }
            else if(value.trim().length() == 5) {
                    ConstraintEntity activeConstraint = tryReadingConstraint(value, activeVotingCouncel, readingMembers);


                    MemberEntity memberEntity = null;
                    memberEntity = activeConstraint.getMember() != null ? activeConstraint.getMember() : new MemberEntity();
                    memberEntity.setConstraint(activeConstraint);
                    activeConstraint.setMember(memberEntity);

                    convertRowStringToMember(row, memberEntity);

                    activeConstraint.setMember(memberEntity);
                    result.add(activeConstraint);
            } else {
                readingMembers = !readingMembers;
            }
        }

        return result;
    }

    private ConstraintEntity tryReadingConstraint(String value, VotingCouncelEntity activeVotingCouncel, boolean readingMembers) {
        if(activeVotingCouncel == null)
            return null;

        Optional<ConstraintEntity> optionalConstraintEntity = activeVotingCouncel.getConstraints().stream()
                .filter(c -> c.getPoliticalOrganization().getCode().equals(value))
                .filter(c -> readingMembers ? c.getTitle().getId() == 1 : c.getTitle().getId() == 2)
                .findFirst();

        if(optionalConstraintEntity.isPresent())
            return optionalConstraintEntity.get();
        else
            return null;

    }

    private VotingCouncelEntity tryReadingVotingCouncel(Cell cell) {
        String code = getCellValue(cell);
        Optional<VotingCouncelEntity> optionalVotingCouncel = votingCouncelRepository.findByCode(code);
        if(optionalVotingCouncel.isEmpty())
            return null;
        else
            return optionalVotingCouncel.get();
    }

    private void convertRowStringToMember(Row row, MemberEntity memberEntity) {
        String name = getCellValue(row.getCell(3));
        readNameCell(name, memberEntity);

        String qualifications = getCellValue(row.getCell(4));
        if(qualifications == null || qualifications.length() == 0)
            memberEntity.setQualifications(null);
        else
            memberEntity.setQualifications(qualifications);

        String gender = getCellValue(row.getCell(5));
        readGenderCell(gender, memberEntity);

        String jmbg = getCellValue(row.getCell(6));
        if(jmbg == null || jmbg.length() == 0)
            memberEntity.setJmbg(null);
        else
            memberEntity.setJmbg(jmbg);

        String phoneNumber = getCellValue(row.getCell(7));
        readPhoneNumber(phoneNumber, memberEntity);

        String bankNumber = getCellValue(row.getCell(8));
        readBankNumber(bankNumber, memberEntity);

        String bankName = getCellValue(row.getCell(9));
        if(bankName == null || bankName.length() == 0)
            memberEntity.setBankName(null);
        else
            memberEntity.setBankName(bankName);
    }

    public void readNameCell(String value, MemberEntity memberEntity) {
        if(value.startsWith("*")) {
            memberEntity.setIsGik(true);
            value = value.substring(1);
        }

        String[] splittingResult = value.split(" ", 2);

        if (splittingResult.length >= 2) {
            memberEntity.setFirstname(splittingResult[0]);
            memberEntity.setLastname( splittingResult[1]);
        }
    }

    private void readGenderCell(String gender, MemberEntity memberEntity) {
        if(gender.equals("М"))
            memberEntity.setIsMale(true);
        else if( gender.equals("Ж"))
            memberEntity.setIsMale(false);
    }

    private void readPhoneNumber(String phoneNumber, MemberEntity memberEntity) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        if(phoneNumber == null || phoneNumber.length() == 0)
            memberEntity.setPhoneNumber(null);
        else
            memberEntity.setPhoneNumber(phoneNumber);
    }

    private void readBankNumber(String bankNumber, MemberEntity memberEntity) {
        bankNumber = bankNumber.replaceAll("\\D", "");
        if(bankNumber == null || bankNumber.length() == 0)
            memberEntity.setBankNumber(null);
        else
            memberEntity.setBankNumber(bankNumber);
    }


    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return latinToCyrillicConverter.convert(cell.getStringCellValue());
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Cell Type";
        }
    }
}
