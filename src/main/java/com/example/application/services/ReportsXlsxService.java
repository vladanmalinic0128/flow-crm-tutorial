package com.example.application.services;


import com.example.application.entities.AssociateEntity;
import com.example.application.entities.BankEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final AssociateRepository associateRepository;

    private final JMBGValidator jmbgValidator;
    Map<Sheet, Map<HorizontalAlignment, XSSFCellStyle>> dataStyles = new HashMap<>();

    DataFormat dataFormat;

    private XSSFSheet overallSheet = null;

    public String generateReportForBanks(String fileTitle, ScriptEnum scriptEnum) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        List<BankEntity> banks = bankRepository.findAll();
        Map<String, XSSFSheet> sheetMap = new HashMap<>();

        XSSFSheet overallSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("UKUPNO") : cyrillicToLatinConverter.convert("UKUPNO"));
        this.overallSheet = overallSheet;
        createHeaderForSheet(overallSheet, scriptEnum);
        dataStyles.put(overallSheet, councelXlsxService.generateCellStyleForCounselMembersColumn(overallSheet));

        XSSFSheet membersWithoutBankNumberSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Bez računa") : cyrillicToLatinConverter.convert("Bez računa"));
        createHeaderForSheet(membersWithoutBankNumberSheet, scriptEnum);
        dataStyles.put(membersWithoutBankNumberSheet, councelXlsxService.generateCellStyleForCounselMembersColumn(membersWithoutBankNumberSheet));


        XSSFSheet membersWithoutPayment = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Nisu radili") : cyrillicToLatinConverter.convert("Nisu radili"));
        createHeaderForSheet(membersWithoutPayment, scriptEnum);
        dataStyles.put(membersWithoutPayment, councelXlsxService.generateCellStyleForCounselMembersColumn(membersWithoutPayment));


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
            if(!member.getIsAcknowledged()) {
                writeRowIntoSheet(membersWithoutPayment, member, null, scriptEnum);
                continue;
            }

            if (isBankNumberEmpty(member.getBankNumber())) {
                writeRowIntoSheet(membersWithoutBankNumberSheet, member, null, scriptEnum);
                writeRowIntoSheet(overallSheet, member, null, scriptEnum);
                continue;
            }

            Optional<BankEntity> bank = findBankByPrefix(member.getBankNumber());
            boolean isValidAccountNumber = bankAccountValidator.isValidAccountNumber(member.getBankNumber());

            if (bank.isPresent() && isValidAccountNumber) {
                writeRowIntoSheet(sheetMap.get(bank.get().getPrefix()), member, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, member, bank.get(), scriptEnum);
            } else if (bank.isPresent()) {
                writeRowIntoSheet(invalidBankNumberSheet, member, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, member, bank.get(), scriptEnum);
            } else {
                writeRowIntoSheet(invalidBankNumberSheet, member, null, scriptEnum);
                writeRowIntoSheet(overallSheet, member, null, scriptEnum);
            }

        }


        for(PresidentEntity president: presidentRepository.findAll()) {
            if(!president.getIsAcknowledged()) {
                writeRowIntoSheet(membersWithoutPayment, president, null, scriptEnum);
                continue;
            }

            if (isBankNumberEmpty(president.getBankNumber())) {
                writeRowIntoSheet(membersWithoutBankNumberSheet, president, null, scriptEnum);
                writeRowIntoSheet(overallSheet, president, null, scriptEnum);
                continue;
            }

            Optional<BankEntity> bank = findBankByPrefix(president.getBankNumber());
            boolean isValidAccountNumber = bankAccountValidator.isValidAccountNumber(president.getBankNumber());

            if (bank.isPresent() && isValidAccountNumber) {
                writeRowIntoSheet(sheetMap.get(bank.get().getPrefix()), president, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, president, bank.get(), scriptEnum);
            } else if (bank.isPresent()) {
                writeRowIntoSheet(invalidBankNumberSheet, president, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, president, bank.get(), scriptEnum);
            } else {
                writeRowIntoSheet(invalidBankNumberSheet, president, null, scriptEnum);
                writeRowIntoSheet(overallSheet, president, null, scriptEnum);
            }
        }




        writeFinalCalculation(overallSheet, scriptEnum);
        writeFinalCalculation(membersWithoutBankNumberSheet, scriptEnum);
        writeFinalCalculation(invalidBankNumberSheet, scriptEnum);
        for(XSSFSheet sheet: sheetMap.values())
            writeFinalCalculation(sheet, scriptEnum);
        

        return saveDocument(fileTitle, workbook);
    }

    public String generateReportForBanksByAssociates(String fileTitle, ScriptEnum scriptEnum, boolean isExtern) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        List<BankEntity> banks = bankRepository.findAll();
        Map<String, XSSFSheet> sheetMap = new HashMap<>();

        XSSFSheet overallSheet = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("UKUPNO") : cyrillicToLatinConverter.convert("UKUPNO"));
        this.overallSheet = overallSheet;
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

        for(AssociateEntity member: associateRepository.findAll().stream().filter(a -> a.getIsExtern() == isExtern).toList()) {
            if (isBankNumberEmpty(member.getBankNumber())) {
                if(isExtern)
                    writeRowIntoSheet(membersWithoutBankNumberSheet, member, null, scriptEnum);
                writeRowIntoSheet(overallSheet, member, null, scriptEnum);
                continue;
            }

            Optional<BankEntity> bank = findBankByPrefix(member.getBankNumber());
            boolean isValidAccountNumber = bankAccountValidator.isValidAccountNumber(member.getBankNumber());

            if (bank.isPresent() && isValidAccountNumber) {
                if(isExtern)
                    writeRowIntoSheet(sheetMap.get(bank.get().getPrefix()), member, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, member, bank.get(), scriptEnum);
            } else if (bank.isPresent()) {
                if(isExtern)
                    writeRowIntoSheet(invalidBankNumberSheet, member, bank.get(), scriptEnum);
                writeRowIntoSheet(overallSheet, member, bank.get(), scriptEnum);
            } else {
                if(isExtern)
                    writeRowIntoSheet(invalidBankNumberSheet, member, null, scriptEnum);
                writeRowIntoSheet(overallSheet, member, null, scriptEnum);
            }

        }

        writeFinalCalculation(overallSheet, scriptEnum);
        if(isExtern) {
            writeFinalCalculation(membersWithoutBankNumberSheet, scriptEnum);
            writeFinalCalculation(invalidBankNumberSheet, scriptEnum);
            for (XSSFSheet sheet : sheetMap.values())
                writeFinalCalculation(sheet, scriptEnum);
        }

        return saveDocument(fileTitle, workbook);
    }

    private boolean isBankNumberEmpty(String bankNumber) {
        return bankNumber == null || bankNumber.isEmpty();
    }

    private Optional<BankEntity> findBankByPrefix(String bankNumber) {
        return bankRepository.findAll().stream()
                .filter(bank -> bankNumber.startsWith(bank.getPrefix()))
                .findAny();
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
        else if(member.getBankName() != null) {
            String bankName = member.getBankName();
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
        Integer amount = member.getPrice();
        cell.setCellValue(amount);

        addBrutoCalculation(sheet, row);
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
        } else if(president.getBankName() != null) {
            String bankName = president.getBankName();
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
        amount = president.getPrice();
        cell.setCellValue(amount);

        addBrutoCalculation(sheet, row);
    }

    private void writeRowIntoSheet(XSSFSheet sheet, AssociateEntity associate, BankEntity bank, ScriptEnum scriptEnum) {
        XSSFCellStyle firstRowStyle = councelXlsxService.generateCellStyleForFirstColumn(sheet);

        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        XSSFCell cell = row.createCell(0);
        String text = String.valueOf(sheet.getLastRowNum());
        cell.setCellValue(text);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));

        cell = row.createCell(1);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(associate.getLastname() != null) {
            String lastname = associate.getLastname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastname) : cyrillicToLatinConverter.convert(lastname);
            cell.setCellValue(text);
        }

        cell = row.createCell(2);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.LEFT));
        if(associate.getFirstname() != null) {
            String firstname = associate.getFirstname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstname) : cyrillicToLatinConverter.convert(firstname);
            cell.setCellValue(text);
        }

        cell = row.createCell(3);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(associate.getJmbg() != null) {
            String jmbg = associate.getJmbg();
            if(jmbgValidator.isValidJMBG(jmbg) == false) {
                XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                XSSFColor redColor = new XSSFColor(new java.awt.Color(255, 0, 0), null);
                cellStyle.setFillForegroundColor(redColor);
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // Postavi stil ćelije
                cell.setCellStyle(cellStyle);
            }
            cell.setCellValue(jmbg);
        }

        cell = row.createCell(4);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.CENTER));
        if(bank != null && bank.getName() != null) {
            String bankName = bank.getName();
            text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(bankName) : cyrillicToLatinConverter.convert(bankName);
            cell.setCellValue(text);
        }
        else if(associate.getBankName() != null) {
            String bankName = associate.getBankName();
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
        if(associate.getBankNumber() != null) {
            String bankNumber = associate.getBankNumber();
            if(bankAccountValidator.isValidAccountNumber(bankNumber) == false) {
                XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                XSSFColor redColor = new XSSFColor(new java.awt.Color(255, 0, 0), null);
                cellStyle.setFillForegroundColor(redColor);
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // Postavi stil ćelije
                cell.setCellStyle(cellStyle);
            }
            cell.setCellValue(bankNumber);
        }

        cell = row.createCell(7);
        cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
        Double amount = associate.getPrice();
        cell.setCellValue(amount);

        addBrutoCalculation(sheet, row);
    }

    private void writeFinalCalculation(XSSFSheet sheet, ScriptEnum scriptEnum) {
        if(sheet.getLastRowNum() == 0)
            return;
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

        addBrutoCalculation(sheet, row);

        // Evaluate the formula (optional)
        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateFormulaCell(cell);

    }

    public void createHeaderForSheet(XSSFSheet sheet, ScriptEnum scriptEnum) {
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

        if(this.overallSheet != null && this.overallSheet.equals(sheet)) {
            cell = row.createCell(8);
            text = "Бруто доходак";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(9);
            text = "Доходак";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(10);
            text = "Доходак-ПИО";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(11);
            text = "Доходак-ЗДР";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(12);
            text = "Доходак-ДЈ. ЗАШТИТА";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(13);
            text = "Доходак-НЕЗАПОСЛЕНОСТ";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);

            cell = row.createCell(14);
            text = "ПОРЕЗ";
            value = scriptEnum == ScriptEnum.CYRILLIC ? text : cyrillicToLatinConverter.convert(text);
            cell.setCellStyle(firstRowStyle);
            cell.setCellValue(value);
        }

        int colNumber = this.overallSheet != null && this.overallSheet.equals(sheet) ? 15 : 8;
        cell = row.createCell(colNumber);
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

        int note = 8;
        if(this.overallSheet != null && this.overallSheet.equals(sheet)) {
            //Bruto
            sheet.setColumnWidth(8, 15 * 256);

            //Dohodak
            sheet.setColumnWidth(9, 15 * 256);

            //Dohodak za pio
            sheet.setColumnWidth(10, 15 * 256);

            //Dohodak za zdr
            sheet.setColumnWidth(11, 15 * 256);

            //Dohodak za dj.zas.
            sheet.setColumnWidth(12, 15 * 256);

            //Dohodak za nezap
            sheet.setColumnWidth(13, 15 * 256);

            //Porez
            sheet.setColumnWidth(14, 15 * 256);

            note=15;
        }
        //Napomena
        sheet.setColumnWidth(note, 60 * 256);
    }

    private void addBrutoCalculation(XSSFSheet sheet, XSSFRow row) {
        XSSFCell cell = null;
        if (this.overallSheet != null && this.overallSheet.equals(sheet)) {
            cell = row.createCell(8);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            String formula = String.format("ROUND(H%d/0.6003, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(9);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND(I%d*0.31, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(10);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND(I%d*0.185, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(11);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND(I%d*0.102, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(12);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND(I%d*0.017, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(13);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND(I%d*0.006, 2)", sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);

            cell = row.createCell(14);
            cell.setCellStyle(this.dataStyles.get(sheet).get(HorizontalAlignment.RIGHT));
            formula = String.format("ROUND((I%d-J%d)*0.13, 2)", sheet.getLastRowNum() + 1, sheet.getLastRowNum() + 1);
            cell.setCellFormula(formula);
        }
    }
}
