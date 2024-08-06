package com.example.application.services;

import com.example.application.entities.*;
import com.example.application.enums.ScriptEnum;
import com.example.application.enums.TitleEnum;
import com.example.application.repositories.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CouncelXlsxService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final TitleService titleService;
    private final MentorRepository mentorRepository;

    //Globalni stilovi
    XSSFCellStyle firstRowStyle;
    Map<HorizontalAlignment, XSSFCellStyle> councelStyles;
    Map<HorizontalAlignment, XSSFCellStyle> councelMemberStyles;

    DataFormat dataFormat;



    public String generateCouncelsByPoliticalOrganization(PoliticalOrganizationEntity entity, String fileTitle, ScriptEnum scriptEnum) {

        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = scriptEnum == ScriptEnum.CYRILLIC ? "БО-ТАБЕЛА" : cyrillicToLatinConverter.convert("БО-ТАБЕЛА");
        XSSFSheet sheet = workbook.createSheet(sheetName);
        this.dataFormat = createDataFormat(sheet);

        defineColumnWidth(sheet);

        generateFrozenHeader(sheet, scriptEnum);

        Map<TitleEnum, TitleEntity> titles = titleService.getTitles();
        councelStyles = generateCellStyleForCounselsColumn(sheet);
        councelMemberStyles = generateCellStyleForCounselMembersColumn(sheet);
        Map<VotingCouncelEntity, List<ConstraintEntity>> politicalOrganizationsByCouncels = entity.getConstraints().stream()
                .collect(Collectors.groupingBy(ConstraintEntity::getVotingCouncel));


        for(Map.Entry<VotingCouncelEntity, List<ConstraintEntity>> entry : politicalOrganizationsByCouncels.entrySet().stream().sorted(Comparator.comparing(c -> c.getKey().getId())).collect(Collectors.toList())) {
            VotingCouncelEntity votingCouncel = entry.getKey();
            List<ConstraintEntity> constraints = entry.getValue();

            generateCouncelsRow(sheet, votingCouncel, scriptEnum);

            generateMemberHeaderRow(sheet, scriptEnum);
            List<ConstraintEntity> members = constraints.stream().filter(c -> c.getTitle().getId() == titles.get(TitleEnum.MEMBER).getId()).sorted(Comparator.comparing(ConstraintEntity::getPosition)).collect(Collectors.toList());
            for(ConstraintEntity member: members)
                generateMemberRow(sheet, member, scriptEnum);

            generateDeputyMemberHeaderRow(sheet, scriptEnum);
            List<ConstraintEntity> deputyMembers = constraints.stream().filter(c -> c.getTitle().getId() == titles.get(TitleEnum.MEMBER_DEPUTY).getId()).sorted(Comparator.comparing(ConstraintEntity::getPosition)).collect(Collectors.toList());
            for(ConstraintEntity deputyMember: deputyMembers)
                generateMemberRow(sheet, deputyMember, scriptEnum);
        }

        addSignature(sheet,scriptEnum);


        return saveDocument(fileTitle, workbook);

    }

    private void addSignature(XSSFSheet sheet, ScriptEnum scriptEnum) {
        CellStyle style = generateCellStyleForSignature(sheet);

        Row row = sheet.createRow(sheet.getLastRowNum() + 2);
        sheet.addMergedRegion(new CellRangeAddress(
                row.getRowNum(),  // start row
                row.getRowNum(),  // end row
                7,  // start column
                8   // end column
        ));
        String text = "ПОТПИС ОВЛАШЋЕНОГ ЛИЦА ПОЛИТИЧКОГ СУБЈЕКТА";
        String label = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        Cell cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue(label);


        row = sheet.createRow(sheet.getLastRowNum() + 2);
        sheet.addMergedRegion(new CellRangeAddress(
                row.getRowNum(),  // start row
                row.getRowNum(),  // end row
                7,  // start column
                8   // end column
        ));
        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("__________________________________________");


    }

    private void defineColumnWidth(XSSFSheet sheet) {
        //Sifra PS
        sheet.setColumnWidth(0, 14 * 256);
        //Prazna kolona
        sheet.setColumnWidth(1, 14 * 256);
        //Prazna kolona
        sheet.setColumnWidth(2, 12 * 256);
        //Clanovi
        sheet.setColumnWidth(3, 60 * 256);
        //Zanimanje
        sheet.setColumnWidth(4, 20 * 256);
        //Pol
        sheet.setColumnWidth(5, 8 * 256);
        //JMB
        sheet.setColumnWidth(6, 25 * 256);
        //Mobilni telefon
        sheet.setColumnWidth(7, 25 * 256);
        //Ziro racun
        sheet.setColumnWidth(8, 35 * 256);
        //Naziv banke racun
        sheet.setColumnWidth(9, 25 * 256);
        //Broj biraca
        sheet.setColumnWidth(10, 8 * 256);
        //Sastav
        sheet.setColumnWidth(11, 8 * 256);
    }

    private void generateFrozenHeader(XSSFSheet sheet, ScriptEnum scriptEnum) {
        firstRowStyle = generateCellStyleForFirstColumn(sheet);

        //Kreiranje prvog reda
        XSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(45);
        sheet.createFreezePane(0, 1);

        XSSFCell cell = row.createCell(0);
        String text = "Шифра БМ/ шифра ПС";
        String value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(1);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(2);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(3);
        text = "Назив бирачког мјеста / име и презиме чланова и замјеника БО";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(4);
        text = "Стр. спрема                          Занимање";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(5);
        text = "Пол";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(6);
        text = "ЈМБ";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(7);
        text = "Мобилни телефон";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(8);
        text = "Број жиро рачуна";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(9);
        text = "Назив банке";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(10);
        text = "Бр.   бир.";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        //cell.setCellValue(value);

        cell = row.createCell(11);
        text = "БО";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);
    }

    private XSSFCellStyle generateCellStyleForFirstColumn(XSSFSheet sheet) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        // Kreiranje stila
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));

        // Definisanje fonta
        Font font = workbook.createFont();
        font.setBold(true);
        cellStyle.setWrapText(true);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        // Definisanje ivica
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        // Centriranje teksta
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    private XSSFCellStyle generateCellStyleForSignature(XSSFSheet sheet) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        // Kreiranje stila
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));

        // Definisanje fonta
        Font font = workbook.createFont();
        cellStyle.setWrapText(true);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);


        // Centriranje teksta
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);

        return cellStyle;
    }

    private void generateCouncelsRow(XSSFSheet sheet, VotingCouncelEntity entity, ScriptEnum scriptEnum) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        String text = scriptEnum == ScriptEnum.CYRILLIC ? entity.getCode() : cyrillicToLatinConverter.convert(entity.getCode());
        cell.setCellValue(text);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(2);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(3);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.LEFT));
        String votingCouncelName = entity.getName() + ", " +entity.getLocation();
        text = scriptEnum == ScriptEnum.CYRILLIC ? votingCouncelName : cyrillicToLatinConverter.convert(votingCouncelName);
        cell.setCellValue(text);
        cell = row.createCell(4);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.LEFT));
        cell = row.createCell(5);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.LEFT));
        cell = row.createCell(6);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.LEFT));
        cell = row.createCell(7);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.LEFT));

        CellRangeAddress cellRangeAddress = new CellRangeAddress(
                row.getRowNum(), // start row
                row.getRowNum(), // end row
                3, // start column
                7   // end column
        );
        sheet.addMergedRegion(cellRangeAddress);

        cell = row.createCell(8);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(9);
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(10);
        //cell.setCellValue(String.valueOf(entity.getNumberOfVoters()));
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.RIGHT));

        cell = row.createCell(11);
        if(entity.getNumberOfMembers() == 4)
            cell.setCellValue("4+4");
        else if(entity.getNumberOfMembers() == 2)
            cell.setCellValue("2+2");
        cell.setCellStyle(this.councelStyles.get(HorizontalAlignment.RIGHT));
    }

    private Map<HorizontalAlignment, XSSFCellStyle> generateCellStyleForCounselsColumn(XSSFSheet sheet) {
        Map<HorizontalAlignment, XSSFCellStyle> cellStyles = new HashMap<>();

        cellStyles.put(HorizontalAlignment.LEFT, generateCellStyleForCounselColumn(sheet, HorizontalAlignment.LEFT));
        cellStyles.put(HorizontalAlignment.CENTER, generateCellStyleForCounselColumn(sheet, HorizontalAlignment.CENTER));
        cellStyles.put(HorizontalAlignment.RIGHT, generateCellStyleForCounselColumn(sheet, HorizontalAlignment.RIGHT));

        return cellStyles;
    }

    private Map<HorizontalAlignment, XSSFCellStyle> generateCellStyleForCounselMembersColumn(XSSFSheet sheet) {
        Map<HorizontalAlignment, XSSFCellStyle> cellStyles = new HashMap<>();

        cellStyles.put(HorizontalAlignment.LEFT, generateCellStyleForCounselMemberColumn(sheet, HorizontalAlignment.LEFT));
        cellStyles.put(HorizontalAlignment.CENTER, generateCellStyleForCounselMemberColumn(sheet, HorizontalAlignment.CENTER));
        cellStyles.put(HorizontalAlignment.RIGHT, generateCellStyleForCounselMemberColumn(sheet, HorizontalAlignment.RIGHT));

        return cellStyles;
    }

    private XSSFCellStyle generateCellStyleForCounselMemberColumn(XSSFSheet sheet, HorizontalAlignment horizontalAlignment) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        // Kreiranje stila
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));

        // Definisanje fonta
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        // Definisanje ivica
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        // Centriranje teksta
        cellStyle.setAlignment(horizontalAlignment);

        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    private XSSFCellStyle generateCellStyleForCounselColumn(XSSFSheet sheet, HorizontalAlignment horizontalAlignment) {
        XSSFWorkbook workbook = sheet.getWorkbook();
        // Kreiranje stila
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));

        // Definisanje fonta
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        // Definisanje ivica
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        // Centriranje teksta
        cellStyle.setAlignment(horizontalAlignment);

        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //Postavljanje pozadine
        XSSFColor backgroundColor = new XSSFColor(new java.awt.Color(204, 204, 255), new DefaultIndexedColorMap());
        cellStyle.setFillForegroundColor(backgroundColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }

    private void generateMemberHeaderRow(XSSFSheet sheet, ScriptEnum scriptEnum) {
        generateHeader(sheet, scriptEnum, "Чланови БО");
    }

    private void generateDeputyMemberHeaderRow(XSSFSheet sheet, ScriptEnum scriptEnum) {
        generateHeader(sheet, scriptEnum, "Замјеници чланова БО");
    }

    private void generateHeader(XSSFSheet sheet, ScriptEnum scriptEnum, String text) {
        //Kreiranje prvog reda
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(1);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(2);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(3);
        String value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(4);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(5);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(6);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(7);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(8);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(9);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(10);
        cell.setCellStyle(firstRowStyle);

        cell = row.createCell(11);
        cell.setCellStyle(firstRowStyle);
    }

    private void generateMemberRow(XSSFSheet sheet, ConstraintEntity constraint, ScriptEnum scriptEnum) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        cell.setCellValue(String.valueOf(constraint.getPoliticalOrganization().getCode()));
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(2);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
        String text;
        String label;
        cell = row.createCell(3);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.LEFT));
        if(constraint.getMember() != null && constraint.getMember().getIsGik() != null && constraint.getMember().getFirstname() != null && constraint.getMember().getLastname() != null) {
            text = generateNameForTable(constraint.getMember());
            label = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellValue(label);
        }

        cell = row.createCell(4);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.LEFT));
        if(constraint.getMember() != null && constraint.getMember().getQualifications() != null) {
            text = constraint.getMember().getQualifications();
            label = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellValue(label);
        }

        cell = row.createCell(5);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
        if(constraint.getMember() != null && constraint.getMember().getIsMale() != null) {
            text = constraint.getMember().getIsMale() ? "М" : "Ж";
            label = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellValue(label);
        }


        cell = row.createCell(6);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
        if(constraint.getMember() != null && constraint.getMember().getJmbg() != null) {
            label = constraint.getMember().getJmbg();
            cell.setCellValue(label);
        }

        cell = row.createCell(7);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
        if(constraint.getMember() != null && constraint.getMember().getPhoneNumber() != null) {
            label = formatPhoneNumber(constraint.getMember().getPhoneNumber());
            cell.setCellValue(label);
        }

        cell = row.createCell(8);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
        if(constraint.getMember() != null && constraint.getMember().getBankNumber() != null) {
            label = formatBankNumber(constraint.getMember().getBankNumber());
            cell.setCellValue(label);
        }

        cell = row.createCell(9);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.LEFT));
        if(constraint.getMember() != null && constraint.getMember().getBankName() != null) {
            text = constraint.getMember().getBankName();
            label = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellValue(label);
        }

        cell = row.createCell(10);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));

        cell = row.createCell(11);
        cell.setCellStyle(councelMemberStyles.get(HorizontalAlignment.CENTER));
    }

    private String generateNameForTable(MemberEntity member) {
        String result = "";

        if(member.getIsGik())
            result += "*";
        result += member.getFirstname();
        result += " ";
        result += member.getLastname();

        return result;
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Remove any non-digit characters
        String cleanedPhoneNumber = phoneNumber;

        // Check the length of the cleaned phone number
        if (cleanedPhoneNumber.length() == 9) {
            // Format as 000/000-000
            return String.format("%03d/%03d-%03d",
                    Integer.parseInt(cleanedPhoneNumber.substring(0, 3)),
                    Integer.parseInt(cleanedPhoneNumber.substring(3, 6)),
                    Integer.parseInt(cleanedPhoneNumber.substring(6, 9)));
        } else if (cleanedPhoneNumber.length() == 10) {
            // Format as 000/000-00-00
            return String.format("%03d/%03d-%02d-%02d",
                    Integer.parseInt(cleanedPhoneNumber.substring(0, 3)),
                    Integer.parseInt(cleanedPhoneNumber.substring(3, 6)),
                    Integer.parseInt(cleanedPhoneNumber.substring(6, 8)),
                    Integer.parseInt(cleanedPhoneNumber.substring(8, 10)));
        } else {
            // Return as is if it does not meet the length requirements
            return phoneNumber;
        }
    }

    private String formatBankNumber(String bankNumber) {
        String cleanedBankNumber = bankNumber;

        // Check if the cleaned bank number has exactly 16 digits
        if (cleanedBankNumber.length() == 16) {
            // Format as 000-000-00000000-00
            return String.format("%03d-%03d-%08d-%02d",
                    Integer.parseInt(cleanedBankNumber.substring(0, 3)),
                    Integer.parseInt(cleanedBankNumber.substring(3, 6)),
                    Integer.parseInt(cleanedBankNumber.substring(6, 14)),
                    Integer.parseInt(cleanedBankNumber.substring(14, 16)));
        } else {
            // Return as is if it does not have 16 digits
            return bankNumber;
        }
    }

    private String saveDocument(String fileTitle, XSSFWorkbook workbook) {
        String fileName = ROOT_PATH + File.separator + fileTitle;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fileName;
    }

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

    private DataFormat createDataFormat(XSSFSheet sheet) {
        return sheet.getWorkbook().createDataFormat();
    }

    public String generateCouncelsByMentor(MentorEntity entity, String fileTitle, ScriptEnum scriptEnum) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName = scriptEnum == ScriptEnum.CYRILLIC ? "БО-ТАБЕЛА" : cyrillicToLatinConverter.convert("БО-ТАБЕЛА");
        XSSFSheet sheet = workbook.createSheet(sheetName);
        this.dataFormat = createDataFormat(sheet);

        defineColumnWidth(sheet);

        generateFrozenHeader(sheet, scriptEnum);

        Map<TitleEnum, TitleEntity> titles = titleService.getTitles();
        councelStyles = generateCellStyleForCounselsColumn(sheet);
        councelMemberStyles = generateCellStyleForCounselMembersColumn(sheet);

        List<ConstraintEntity> entryConstraints;
        if(entity.getId() != null)
            entryConstraints = entity.getVotingCouncels().stream().flatMap(e -> e.getConstraints().stream()).collect(Collectors.toList());
        else
            entryConstraints = mentorRepository.findAll().stream().flatMap(e -> e.getVotingCouncels().stream()).flatMap(e -> e.getConstraints().stream()).collect(Collectors.toList());


        Map<VotingCouncelEntity, List<ConstraintEntity>> mentorByCounsels = entryConstraints.stream()
                .collect(Collectors.groupingBy(ConstraintEntity::getVotingCouncel));

        for(Map.Entry<VotingCouncelEntity, List<ConstraintEntity>> entry : mentorByCounsels.entrySet().stream().sorted(Comparator.comparing(c -> c.getKey().getId())).collect(Collectors.toList())) {
            VotingCouncelEntity votingCouncel = entry.getKey();
            List<ConstraintEntity> constraints = entry.getValue();

            generateCouncelsRow(sheet, votingCouncel, scriptEnum);

            generateMemberHeaderRow(sheet, scriptEnum);
            List<ConstraintEntity> members = constraints.stream().filter(c -> c.getTitle().getId() == titles.get(TitleEnum.MEMBER).getId()).sorted(Comparator.comparing(ConstraintEntity::getPosition)).collect(Collectors.toList());
            for(ConstraintEntity member: members)
                generateMemberRow(sheet, member, scriptEnum);

            generateDeputyMemberHeaderRow(sheet, scriptEnum);
            List<ConstraintEntity> deputyMembers = constraints.stream().filter(c -> c.getTitle().getId() == titles.get(TitleEnum.MEMBER_DEPUTY).getId()).sorted(Comparator.comparing(ConstraintEntity::getPosition)).collect(Collectors.toList());
            for(ConstraintEntity deputyMember: deputyMembers)
                generateMemberRow(sheet, deputyMember, scriptEnum);
        }

        return saveDocument(fileTitle, workbook);
    }
}
