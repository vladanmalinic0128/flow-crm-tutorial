package com.example.application.services;


import com.example.application.entities.BankEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.BankRepository;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.VotingCouncelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportsXlsxService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";

    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private final VotingCouncelRepository votingCouncelRepository;
    private final PresidentRepository presidentRepository;
    private final MemberRepository memberRepository;
    private final BankRepository bankRepository;
    private final CouncelXlsxService councelXlsxService;
    private final BankAccountValidator bankAccountValidator;

    Map<Sheet, Map<HorizontalAlignment, XSSFCellStyle>> dataStyles = new HashMap<>();

    DataFormat dataFormat;

    public String generateReportForBanks(String fileTitle, ScriptEnum scriptEnum) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        List<BankEntity> banks = bankRepository.findAll();
        Map<String, XSSFSheet> sheetMap = new HashMap<>();

        XSSFSheet overallSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("UKUPNO") : cyrillicToLatinConverter.convert("UKUPNO"));
        createHeaderForSheet(overallSheet, scriptEnum);
        dataStyles.put(overallSheet, councelXlsxService.generateCellStyleForCounselMembersColumn(overallSheet));

        XSSFSheet membersWithoutBankNumberSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Bez računa") : cyrillicToLatinConverter.convert("Bez računa"));
        createHeaderForSheet(membersWithoutBankNumberSheet, scriptEnum);
        dataStyles.put(membersWithoutBankNumberSheet, councelXlsxService.generateCellStyleForCounselMembersColumn(membersWithoutBankNumberSheet));


        XSSFSheet invalidBankNumberSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Pogrešan broj računa") : cyrillicToLatinConverter.convert("Pogrešan broj računa"));
        createHeaderForSheet(invalidBankNumberSheet, scriptEnum);
        dataStyles.put(invalidBankNumberSheet, councelXlsxService.generateCellStyleForCounselMembersColumn(invalidBankNumberSheet));

        for(BankEntity bank: banks) {
            XSSFSheet sheet = workbook.createSheet(bank.getCode() + "-" + (scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(bank.getName()) : cyrillicToLatinConverter.convert(bank.getName())));
            createHeaderForSheet(sheet, scriptEnum);
            sheetMap.put(bank.getPrefix(), sheet);
            dataStyles.put(sheet, councelXlsxService.generateCellStyleForCounselMembersColumn(sheet));
        }

        for(MemberEntity member: memberRepository.findAll()) {
            if(member.getBankNumber() == null || member.getBankNumber().length() < 1) {
                writeRowIntoSheet(membersWithoutBankNumberSheet, member, null, scriptEnum);
                continue;
            }
            Optional<BankEntity> bank = banks.stream().filter(b -> member.getBankNumber().startsWith(b.getPrefix())).findAny();
            if(bank.isPresent() && bankAccountValidator.isValidAccountNumber(member.getBankNumber())) {
                writeRowIntoSheet(overallSheet, member, bank.get(), scriptEnum);
                writeRowIntoSheet(sheetMap.get(bank.get().getPrefix()), member, bank.get(), scriptEnum);
            } else {
                writeRowIntoSheet(invalidBankNumberSheet, member, bank.isPresent() ? bank.get() : null, scriptEnum);
            }
        }


        for(PresidentEntity president: presidentRepository.findAll()) {
            if(president.getBankNumber() == null || president.getBankNumber().length() < 1) {
                writeRowIntoSheet(membersWithoutBankNumberSheet, president, null, scriptEnum);
                continue;
            }
            Optional<BankEntity> bank = banks.stream().filter(b -> president.getBankNumber().startsWith(b.getPrefix())).findAny();
            if(bank.isPresent() && bankAccountValidator.isValidAccountNumber(president.getBankNumber())) {
                writeRowIntoSheet(overallSheet, president, bank.get(), scriptEnum);
                writeRowIntoSheet(sheetMap.get(bank.get().getPrefix()), president, bank.get(), scriptEnum);
            } else {
                writeRowIntoSheet(invalidBankNumberSheet, president, bank.isPresent() ? bank.get() : null, scriptEnum);
            }
        }




        writeFinalCalculation(overallSheet, scriptEnum);
        for(XSSFSheet sheet: sheetMap.values())
            writeFinalCalculation(sheet, scriptEnum);
        

        return saveDocument(fileTitle, workbook);
    }

    private void writeRowIntoSheet(XSSFSheet sheet, MemberEntity member, BankEntity bank, ScriptEnum scriptEnum) {
        XSSFCellStyle firstRowStyle = councelXlsxService.generateCellStyleForFirstColumn(sheet);

        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        String text = String.valueOf(sheet.getLastRowNum());
        cell.setCellValue(text);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(member.getLastname() != null) {
            String lastname = member.getLastname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastname) : cyrillicToLatinConverter.convert(lastname);
            cell.setCellValue(text);
        }

        cell = row.createCell(2);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(member.getFirstname() != null) {
            String firstname = member.getFirstname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstname) : cyrillicToLatinConverter.convert(firstname);
            cell.setCellValue(text);
        }

        cell = row.createCell(3);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(member.getJmbg() != null) {
            String jmbg = member.getJmbg();
            cell.setCellValue(jmbg);
        }

        cell = row.createCell(4);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(bank != null && bank.getName() != null) {
            String bankName = bank.getName();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(bankName) : cyrillicToLatinConverter.convert(bankName);
            cell.setCellValue(text);
        }

        cell = row.createCell(5);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(bank != null && bank.getCode() != null) {
            String bankCode = bank.getCode();
            cell.setCellValue(bankCode);
        }

        cell = row.createCell(6);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(member.getBankNumber() != null) {
            String bankNumber = member.getBankNumber();
            cell.setCellValue(bankNumber);
        }

        cell = row.createCell(7);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
        Integer amount = member.getConstraint().getTitle().getId() == 1 ? 110 : 60;
        cell.setCellValue(amount);
    }

    private void writeRowIntoSheet(XSSFSheet sheet, PresidentEntity president, BankEntity bank, ScriptEnum scriptEnum) {
        XSSFCellStyle firstRowStyle = councelXlsxService.generateCellStyleForFirstColumn(sheet);

        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        String text = String.valueOf(sheet.getLastRowNum());
        cell.setCellValue(text);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(president.getLastname() != null) {
            String lastname = president.getLastname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastname) : cyrillicToLatinConverter.convert(lastname);
            cell.setCellValue(text);
        }

        cell = row.createCell(2);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(president.getFirstname() != null) {
            String firstname = president.getFirstname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstname) : cyrillicToLatinConverter.convert(firstname);
            cell.setCellValue(text);
        }

        cell = row.createCell(3);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(president.getJmbg() != null) {
            String jmbg = president.getJmbg();
            cell.setCellValue(jmbg);
        }

        cell = row.createCell(4);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(bank != null && bank.getName() != null) {
            String bankName = bank.getName();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(bankName) : cyrillicToLatinConverter.convert(bankName);
            cell.setCellValue(text);
        }

        cell = row.createCell(5);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(bank != null && bank.getCode() != null) {
            String bankCode = bank.getCode();
            cell.setCellValue(bankCode);
        }

        cell = row.createCell(6);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(president.getBankNumber() != null) {
            String bankNumber = president.getBankNumber();
            cell.setCellValue(bankNumber);
        }

        cell = row.createCell(7);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
        Integer amount;
        if(president.getIsPresident() != null)
            amount = president.getIsPresident() ? 400 : 200;
        else
            amount = 0;
        cell.setCellValue(amount);
    }

    private void writeFinalCalculation(XSSFSheet sheet, ScriptEnum scriptEnum) {
        XSSFCellStyle firstRowStyle = councelXlsxService.generateCellStyleForFirstColumn(sheet);

        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        String overall = "UKUPNO";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(overall) : cyrillicToLatinConverter.convert(overall);
        cell.setCellValue(text);

        cell = row.createCell(2);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));


        cell = row.createCell(3);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));

        cell = row.createCell(4);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));


        cell = row.createCell(5);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));


        cell = row.createCell(6);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));


        cell = row.createCell(7);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
        String formula = String.format("SUM(H%d:H%d)", 2, sheet.getLastRowNum());
        cell.setCellFormula(formula);

        // Evaluate the formula (optional)
        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateFormulaCell(cell);

    }

    private void createHeaderForSheet(XSSFSheet sheet, ScriptEnum scriptEnum) {
        this.dataFormat = createDataFormat(sheet);

        defineColumnWidth(sheet);

        generateFrozenHeader(sheet, scriptEnum);
    }

    private void generateFrozenHeader(XSSFSheet sheet, ScriptEnum scriptEnum) {
        XSSFCellStyle firstRowStyle = councelXlsxService.generateCellStyleForFirstColumn(sheet);

        //Kreiranje prvog reda
        XSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(45);
        sheet.createFreezePane(0, 1);

        XSSFCell cell = row.createCell(0);
        String text = "РБ";
        String value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(1);
        text = "Презиме";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(2);
        text = "Име";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(3);
        text = "ЈМБ";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(4);
        text = "Назив банке";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(5);
        text = "Ознака банке";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(6);
        text = "Број жиро рачуна";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(7);
        text = "Износ";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);

        cell = row.createCell(8);
        text = "Напомена";
        value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
        cell.setCellStyle(firstRowStyle);
        cell.setCellValue(value);
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

    private void defineColumnWidth(XSSFSheet sheet) {
        //RB
        sheet.setColumnWidth(0, 7 * 256);
        //Prezime
        sheet.setColumnWidth(1, 25 * 256);
        //Ime
        sheet.setColumnWidth(2, 18 * 256);
        //JMB
        sheet.setColumnWidth(3, 25 * 256);
        //Naziv banke
        sheet.setColumnWidth(4, 40 * 256);
        //Oznaka banke
        sheet.setColumnWidth(5, 10 * 256);
        //Broj ziro racuna
        sheet.setColumnWidth(6, 30 * 256);
        //Iznos
        sheet.setColumnWidth(7, 15 * 256);
        //Napomena
        sheet.setColumnWidth(8, 60 * 256);
    }
}
