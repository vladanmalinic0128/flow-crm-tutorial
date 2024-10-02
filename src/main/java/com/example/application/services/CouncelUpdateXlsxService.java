package com.example.application.services;

import com.example.application.entities.ConstraintEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.SubstituteEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.SubstituteRepository;
import com.example.application.repositories.VotingCouncelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouncelUpdateXlsxService {
    private final String MUNICIPALITY_CODE = "034Б";
    private final String MUNICIPALITY_CODE_FOR_MOBILE_TEAMS = "034МТ";
    private final VotingCouncelRepository votingCouncelRepository;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private final SubstituteRepository substituteRepository;
    public List<ConstraintEntity> getModifiedConstraints(Workbook workbook, boolean deleteEmptyRows) {
        List<ConstraintEntity> result = new ArrayList<>();

        VotingCouncelEntity activeVotingCouncel = null;
        boolean readingMembers = false;

        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            if(row == null)
                continue;
            Cell cell = row.getCell(0);
            if(cell == null)
                continue;
            String value = getCellValue(cell);
            if(latinToCyrillicConverter.convert(value).startsWith(MUNICIPALITY_CODE) || latinToCyrillicConverter.convert(value).startsWith(MUNICIPALITY_CODE_FOR_MOBILE_TEAMS)) {
                activeVotingCouncel = tryReadingVotingCouncel(cell);
            }
            else if(value.trim().length() == 5) {
                ConstraintEntity activeConstraint = tryReadingConstraint(value, activeVotingCouncel, readingMembers);


                boolean isEmpty = isEmpty(row);
                if(isEmpty == false || (isEmpty && deleteEmptyRows && activeConstraint.getMember() != null)) {
                    MemberEntity memberEntity = null;
                    if(activeConstraint == null)
                        System.out.println("Loggg: " + cell.getRow() + ", " + cell.getColumnIndex());
                    memberEntity = activeConstraint.getMember() != null ? activeConstraint.getMember() : new MemberEntity();
                    memberEntity.setConstraint(activeConstraint);
                    activeConstraint.setMember(memberEntity);

                    convertRowStringToMember(row, memberEntity, deleteEmptyRows);

                    activeConstraint.setMember(memberEntity);
                    result.add(activeConstraint);
                }
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

    private void convertRowStringToMember(Row row, MemberEntity memberEntity, boolean deleteEmptyRows) {
        String name = getCellValue(row.getCell(3));
        if(name == null || name.trim().length() == 0) {
            memberEntity.setFirstname(null);
            memberEntity.setLastname(null);
            memberEntity.setIsGik(null);
        }
        else
            readNameCell(name, memberEntity);

        String qualifications = getCellValue(row.getCell(4));
        if(qualifications == null || qualifications.trim().length() == 0)
            memberEntity.setQualifications(null);
        else
            memberEntity.setQualifications(qualifications);

        String gender = getCellValue(row.getCell(5));
        readGenderCell(gender, memberEntity);

        String jmbg = getCellValue(row.getCell(6));
        readJmbg(jmbg, memberEntity);

        String phoneNumber = getCellValue(row.getCell(7));
        readPhoneNumber(phoneNumber, memberEntity);

        String bankNumber = getCellValue(row.getCell(8));
        readBankNumber(bankNumber, memberEntity, deleteEmptyRows);
        //readBankNumberTemp(bankNumber, memberEntity, deleteEmptyRows);

        String bankName = getCellValue(row.getCell(9));
        readBankName(bankName, memberEntity, deleteEmptyRows);
        //readBankNameTemp(bankName, memberEntity, deleteEmptyRows);
    }

    public void readNameCell(String value, MemberEntity memberEntity) {
        if(value.startsWith("*")) {
            memberEntity.setIsGik(true);
            value = value.substring(1);
        } else
            memberEntity.setIsGik(false);

        String[] splittingResult = value.split(" ", 2);

        if (splittingResult.length >= 2) {
            memberEntity.setFirstname(splittingResult[0]);
            memberEntity.setLastname( splittingResult[1]);
        }
    }

    private void readGenderCell(String gender, MemberEntity memberEntity) {
        if(gender.equalsIgnoreCase("М"))
            memberEntity.setIsMale(true);
        else if( gender.equalsIgnoreCase("Ж"))
            memberEntity.setIsMale(false);
        else
            memberEntity.setIsMale(null);
    }

    private void readJmbg(String jmbg, MemberEntity memberEntity) {
        jmbg = jmbg.replaceAll("\\D", "");
        if(jmbg == null || jmbg.trim().length() == 0)
            memberEntity.setJmbg(null);
        else
            memberEntity.setJmbg(jmbg);
    }

    private void readPhoneNumber(String phoneNumber, MemberEntity memberEntity) {
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        if(phoneNumber == null || phoneNumber.trim().length() == 0)
            memberEntity.setPhoneNumber(null);
        else
            memberEntity.setPhoneNumber(phoneNumber);
    }

    private void readBankNumber(String bankNumber, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if(bankNumber != null)
            bankNumber = bankNumber.replaceAll("\\D", "");
        if (memberEntity.isEmpty() || (deleteEmptyRows && (bankNumber == null || bankNumber.trim().isEmpty()))) {
            memberEntity.setBankNumber(null);
        } else if (bankNumber != null && bankNumber.trim().length() > 1) {
            memberEntity.setBankNumber(bankNumber);
        } else if (memberEntity.getBankNumber() == null || memberEntity.getBankNumber().trim().isEmpty()) {
            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
                memberEntity.setBankNumber(optional.get().getBankNumber());
            } else {
                memberEntity.setBankNumber(null);
            }
        }
    }

    private void readBankName(String bankName, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if (memberEntity.isEmpty() || (deleteEmptyRows && (bankName == null || bankName.trim().isEmpty()))) {
            memberEntity.setBankName(null);
        } else if (bankName != null && bankName.trim().length() > 1) {
            memberEntity.setBankName(bankName);
        } else if (memberEntity.getBankName() == null || memberEntity.getBankName().trim().isEmpty()) {
            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
                memberEntity.setBankName(optional.get().getBankName());
            } else {
                memberEntity.setBankName(null);
            }
        }
    }

    private void readBankNumberTemp(String bankNumber, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if(bankNumber != null)
            bankNumber = bankNumber.replaceAll("\\D", "");
        if(memberEntity.isEmpty())
            memberEntity.setBankNumber(null);
        else if(memberEntity.getIsGik()){
            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
                memberEntity.setBankNumber(optional.get().getBankNumber());
            } else {
                memberEntity.setBankNumber(null);
            }
        }
    }


    private void readBankNameTemp(String bankName, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if(memberEntity.isEmpty())
            memberEntity.setBankName(null);
        else if(memberEntity.getIsGik()){
            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
                memberEntity.setBankNumber(optional.get().getBankNumber());
            } else {
                memberEntity.setBankNumber(null);
            }
        }
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
                return Double.toString(cell.getNumericCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Cell Type";
        }
    }

    private boolean isEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 3; i <= 9; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = getCellValue(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
