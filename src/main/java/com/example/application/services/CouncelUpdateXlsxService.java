package com.example.application.services;

import com.example.application.entities.*;
import com.example.application.repositories.PresidentRepository;
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
    private final PresidentRepository presidentRepository;
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

    public List<PresidentEntity> getModifiedPresidents(Workbook workbook, boolean deleteEmptyRows) {
        List<PresidentEntity> result = new ArrayList<>();

        VotingCouncelEntity activeVotingCouncel = null;
        boolean readingPresidents = false;

        Sheet sheet = workbook.getSheetAt(1);
        if(sheet == null)
            return result;
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
            else if(latinToCyrillicConverter.convert(value.toUpperCase()).equals("ГИК")) {
                Optional<PresidentEntity> optionalPresidentEntity = presidentRepository.findByVotingCouncel_CodeAndIsPresident(activeVotingCouncel.getCode(), readingPresidents);
                if(optionalPresidentEntity.isEmpty())
                    continue;
                PresidentEntity presidentToUpdate = optionalPresidentEntity.get();

                boolean isEmpty = isPresidentEmpty(row);
                if(isEmpty == false || (isEmpty && deleteEmptyRows && presidentToUpdate != null)) {
                    MemberEntity memberEntity = null;
                    if(presidentToUpdate == null) {
                        System.out.println("Loggg: " + cell.getRow() + ", " + cell.getColumnIndex());
                    }
                    convertRowStringToPresident(row, presidentToUpdate, deleteEmptyRows);
                    result.add(presidentToUpdate);
                }
            } else {
                readingPresidents = !readingPresidents;
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
        String acknowledged = getCellValue(row.getCell(1));
        if(acknowledged != null && acknowledged.trim().length() > 0) {
            if("ДА".equalsIgnoreCase(latinToCyrillicConverter.convert(acknowledged.trim())))
                memberEntity.setIsAcknowledged(true);
            else if("НЕ".equalsIgnoreCase(latinToCyrillicConverter.convert(acknowledged.trim())))
                memberEntity.setIsAcknowledged(false);
        }

        if(memberEntity.getIsAcknowledged() != null && memberEntity.getIsAcknowledged()) {
            String priceString = getCellValue(row.getCell(2));
            if(priceString != null) {
                String cleanedPriceString = priceString.trim().replace(",", ".");
                try {
                    double price = Double.parseDouble(cleanedPriceString);
                    memberEntity.setPrice((int)price);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price at row: " + row.getRowNum());
                }
            } else {
                String votingCouncelCode = memberEntity.getConstraint().getVotingCouncel().getCode();
                if(memberEntity.getConstraint().getTitle().getId() == 1) {
                    if(votingCouncelCode.contains("MT") || votingCouncelCode.contains("МТ"))
                        memberEntity.setPrice(100);
                    else if(votingCouncelCode.endsWith("501"))
                        memberEntity.setPrice(100);
                    else if(votingCouncelCode.endsWith("NNN") || votingCouncelCode.endsWith("ННН"))
                        memberEntity.setPrice(100);
                    else
                        memberEntity.setPrice(110);
                }
                else if(memberEntity.getConstraint().getTitle().getId() == 2) {
                    if(votingCouncelCode.contains("MT") || votingCouncelCode.contains("МТ"))
                        memberEntity.setPrice(50);
                    else if(votingCouncelCode.endsWith("501"))
                        memberEntity.setPrice(50);
                    else if(votingCouncelCode.endsWith("NNN") || votingCouncelCode.endsWith("ННН"))
                        memberEntity.setPrice(50);
                    else
                        memberEntity.setPrice(60);
                }
            }
        } else {
            memberEntity.setPrice(0);
        }

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

    private void convertRowStringToPresident(Row row, PresidentEntity presidentEntity, boolean deleteEmptyRows) {
        String acknowledged = getCellValue(row.getCell(1));
        if(acknowledged != null && acknowledged.trim().length() > 0) {
            if("ДА".equalsIgnoreCase(latinToCyrillicConverter.convert(acknowledged.trim())))
                presidentEntity.setIsAcknowledged(true);
            else if("НЕ".equalsIgnoreCase(latinToCyrillicConverter.convert(acknowledged.trim())))
                presidentEntity.setIsAcknowledged(false);
        }

        if(presidentEntity.getIsAcknowledged() != null && presidentEntity.getIsAcknowledged()) {
            String priceString = getCellValue(row.getCell(2));
            if(priceString != null) {
                String cleanedPriceString = priceString.trim().replace(",", ".");
                try {
                    double price = Double.parseDouble(cleanedPriceString);
                    presidentEntity.setPrice((int)price);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price at row: " + row.getRowNum());
                }
            } else {
                String votingCouncelCode = presidentEntity.getVotingCouncel().getCode();
                if(presidentEntity.getIsPresident()) {
                    if(votingCouncelCode.contains("MT") || votingCouncelCode.contains("МТ"))
                        presidentEntity.setPrice(200);
                    else if(votingCouncelCode.endsWith("501"))
                        presidentEntity.setPrice(200);
                    else if(votingCouncelCode.endsWith("NNN") || votingCouncelCode.endsWith("ННН"))
                        presidentEntity.setPrice(200);
                    else
                        presidentEntity.setPrice(400);
                }
                else  {
                    if(votingCouncelCode.contains("MT") || votingCouncelCode.contains("МТ"))
                        presidentEntity.setPrice(100);
                    else if(votingCouncelCode.endsWith("501"))
                        presidentEntity.setPrice(100);
                    else if(votingCouncelCode.endsWith("NNN") || votingCouncelCode.endsWith("ННН"))
                        presidentEntity.setPrice(100);
                    else
                        presidentEntity.setPrice(200);
                }
            }
        } else {
            presidentEntity.setPrice(0);
        }


        String bankNumber = getCellValue(row.getCell(8));
        readBankNumber(bankNumber, presidentEntity, deleteEmptyRows);
        //readBankNumberTemp(bankNumber, memberEntity, deleteEmptyRows);

        String bankName = getCellValue(row.getCell(9));
        readBankName(bankName, presidentEntity, deleteEmptyRows);
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

//    private void readBankNumber(String bankNumber, MemberEntity memberEntity, boolean deleteEmptyRows) {
//        if(bankNumber != null)
//            bankNumber = bankNumber.replaceAll("\\D", "");
//        if (memberEntity.isEmpty() || (deleteEmptyRows && (bankNumber == null || bankNumber.trim().isEmpty()))) {
//            memberEntity.setBankNumber(null);
//        } else if (bankNumber != null && bankNumber.trim().length() > 1) {
//            memberEntity.setBankNumber(bankNumber);
//        } else if (memberEntity.getBankNumber() == null || memberEntity.getBankNumber().trim().isEmpty()) {
//            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
//            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
//                memberEntity.setBankNumber(optional.get().getBankNumber());
//            } else {
//                memberEntity.setBankNumber(null);
//            }
//        }
//    }

    private void readBankNumber(String bankNumber, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if(bankNumber != null)
            bankNumber = bankNumber.replaceAll("\\D", "");
        if (memberEntity.isEmpty()) {
            memberEntity.setBankNumber(null);
        } else if (bankNumber != null && bankNumber.trim().length() > 1)
            memberEntity.setBankNumber(bankNumber);
    }

//    private void readBankNumber(String bankNumber, PresidentEntity presidentEntity, boolean deleteEmptyRows) {
//        if(bankNumber != null)
//            bankNumber = bankNumber.replaceAll("\\D", "");
//        if (presidentEntity.isEmpty() || (deleteEmptyRows && (bankNumber == null || bankNumber.trim().isEmpty()))) {
//            presidentEntity.setBankNumber(null);
//        } else if (bankNumber != null && bankNumber.trim().length() > 1) {
//            presidentEntity.setBankNumber(bankNumber);
//        } else if (presidentEntity.getBankNumber() == null || presidentEntity.getBankNumber().trim().isEmpty()) {
//            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(presidentEntity.getJmbg());
//            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
//                presidentEntity.setBankNumber(optional.get().getBankNumber());
//            } else {
//                presidentEntity.setBankNumber(null);
//            }
//        }
//    }

    private void readBankNumber(String bankNumber, PresidentEntity presidentEntity, boolean deleteEmptyRows) {
        if(bankNumber != null)
            bankNumber = bankNumber.replaceAll("\\D", "");
        if (presidentEntity.isEmpty()) {
            presidentEntity.setBankNumber(null);
        } else if (bankNumber != null && bankNumber.trim().length() > 1)
            presidentEntity.setBankNumber(bankNumber);
    }

//    private void readBankName(String bankName, MemberEntity memberEntity, boolean deleteEmptyRows) {
//        if (memberEntity.isEmpty() || (deleteEmptyRows && (bankName == null || bankName.trim().isEmpty()))) {
//            memberEntity.setBankName(null);
//        } else if (bankName != null && bankName.trim().length() > 1) {
//            memberEntity.setBankName(bankName);
//        } else if (memberEntity.getBankName() == null || memberEntity.getBankName().trim().isEmpty()) {
//            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(memberEntity.getJmbg());
//            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
//                memberEntity.setBankName(optional.get().getBankName());
//            } else {
//                memberEntity.setBankName(null);
//            }
//        }
//    }

    private void readBankName(String bankName, MemberEntity memberEntity, boolean deleteEmptyRows) {
        if (memberEntity.isEmpty()) {
            memberEntity.setBankName(null);
        }
        if (bankName != null && bankName.trim().length() > 1)
            memberEntity.setBankName(bankName);
    }

//    private void readBankName(String bankName, PresidentEntity presidentEntity, boolean deleteEmptyRows) {
//        if (presidentEntity.isEmpty() || (deleteEmptyRows && (bankName == null || bankName.trim().isEmpty()))) {
//            presidentEntity.setBankName(null);
//        } else if (bankName != null && bankName.trim().length() > 1) {
//            presidentEntity.setBankName(bankName);
//        } else if (presidentEntity.getBankName() == null || presidentEntity.getBankName().trim().isEmpty()) {
//            Optional<SubstituteEntity> optional = substituteRepository.findFirstByJmbg(presidentEntity.getJmbg());
//            if (optional.isPresent() && optional.get().getJmbg() != null && !optional.get().getJmbg().isEmpty()) {
//                presidentEntity.setBankName(optional.get().getBankName());
//            } else {
//                presidentEntity.setBankName(null);
//            }
//        }
//    }

    private void readBankName(String bankName, PresidentEntity presidentEntity, boolean deleteEmptyRows) {
        if (presidentEntity.isEmpty()) {
            presidentEntity.setBankName(null);
        } else if (bankName != null && bankName.trim().length() > 1)
            presidentEntity.setBankName(bankName);
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

    private boolean isPresidentEmpty(Row row) {
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
