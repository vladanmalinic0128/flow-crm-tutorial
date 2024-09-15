package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.StackEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.enums.SideEnum;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class ObserverPdfService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;

    public ObserverPdfService(LatinToCyrillicConverter latinToCyrillicConverter, CyrillicToLatinConverter cyrillicToLatinConverter) {
        this.latinToCyrillicConverter = latinToCyrillicConverter;
        this.cyrillicToLatinConverter = cyrillicToLatinConverter;
    }

    public String downloadOverallPdf(PoliticalOrganizationEntity entity) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add a blank page
        pdfDoc.addNewPage();

        document.close();

        String fileName = ROOT_PATH + File.separator + entity.getCode() + "_" + System.currentTimeMillis() + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
    }

    public String downloadOverallXlsx(PoliticalOrganizationEntity entity) {
        return null;
    }

    private static final String GREEN_HEADER = "#006a4e";
    private static final String GREEN_DATA = "#8abaae";
    private static final String RED_HEADER = "#a00b24";
    private static final String RED_DATA = "#f9a4b3";

    public String downloadAccreditatationsPdf(StackEntity entity, LocalDate datePicker, ScriptEnum script, SideEnum sideNumber, String fileTitle) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Add a blank page
        //pdfDoc.addNewPage();

        //set margins
        float topMargin = 25f;
        float leftMargin=42f;
        float rightMargin=42f;
        document.setTopMargin(topMargin);
        document.setLeftMargin(leftMargin);
        document.setRightMargin(rightMargin);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Create a 2x5 table
        float[] columnWidths = {340f, 340f}; // Custom column widths
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Set table width

        if(sideNumber == SideEnum.ONE_SIDED) {
            List<ObserverEntity> filteredObservers = entity.getObservers().stream().filter(o -> o.getStatus().getSuccess() == true || o.getForce() == true).sorted(Comparator.comparing(ObserverEntity::getDocumentNumber)).collect(Collectors.toList());
            for(ObserverEntity observer: filteredObservers) {
                Cell front = new Cell();
                front.add(generateFrontPageForAccreditation(observer, datePicker, script));

                Cell back = new Cell();
                back.setVerticalAlignment(VerticalAlignment.MIDDLE);
                back.setTextAlignment(TextAlignment.CENTER);
                back.setHorizontalAlignment(HorizontalAlignment.CENTER);
                back.add(generateBackPageForAccreditation(script));

                front.setHeight(150f);
                table.addCell(front);
                back.setHeight(150f);
                table.addCell(back);
            }
        }
        else if(sideNumber == SideEnum.TWO_SIDED) {
            List<ObserverEntity> filteredObservers = entity.getObservers().stream().filter(o -> o.getStatus().getSuccess() == true || o.getForce() == true).sorted(Comparator.comparing(ObserverEntity::getDocumentNumber)).collect(Collectors.toList());
            int chunkSize = 10;
            int numberOfChunks = (filteredObservers.size() + chunkSize - 1) / chunkSize;

            IntStream.range(0, numberOfChunks)
                    .mapToObj(i -> filteredObservers.subList(i * chunkSize, Math.min((i + 1) * chunkSize, filteredObservers.size())))
                    .forEach(chunk -> {
                        for(ObserverEntity observer: chunk) {
                            Cell front = new Cell();
                            front.add(generateFrontPageForAccreditation(observer, datePicker, script));
                            front.setHeight(150f);
                            table.addCell(front);
                        }
                        if(chunk.size() < 10) {
                            for(int i = chunk.size(); i < 10; i++) {
                                Cell front = new Cell();
                                front.setHeight(150f);
                                table.addCell(front);
                            }
                        }
                        for(int i = 0; i < 10; i++) {
                            Cell back = new Cell();
                            back.setVerticalAlignment(VerticalAlignment.MIDDLE);
                            back.setTextAlignment(TextAlignment.CENTER);
                            back.setHorizontalAlignment(HorizontalAlignment.CENTER);
                            back.add(generateBackPageForAccreditation(script));
                            back.setHeight(150f);
                            table.addCell(back);
                        }
                    });
        }

        // Add table to document
        document.add(table);


        document.close();

        String filePath = ROOT_PATH + File.separator + fileTitle;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filePath;
    }

    private Paragraph generateBackPageForAccreditation(ScriptEnum script) {
        PdfFont arialFont = null;
        try {
            arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String paragraphString = "Posmatrač, u toku posmatranja izbornog procesa, neće ometati izborne aktivnosti i poštivaće tajnost glasanja. Za vrijeme posmatranja izbornih aktivnosti, posmatrač će nositi službenu akreditaciju i neće nositi bilo kakva obilježja ili oznake koje ga povezuju s određenom političkom partijom, koalicijom, listom nezavisnih kandidata, nezavisnim kandidatom i kandidatom sa posebne liste kandidata pripadnika nacionalnih manjina.";
        return new Paragraph(script == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(paragraphString) : paragraphString)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFontSize(8f)
                .setFont(arialFont);
    }

    private Table generateFrontPageForAccreditation(ObserverEntity observer, LocalDate localDate, ScriptEnum script) {
        // Create a main table with 1 column
        float[] mainColumnWidths = {100f}; // Full width
        Table mainTable = new Table(UnitValue.createPercentArray(mainColumnWidths));
        mainTable.setWidth(UnitValue.createPercentValue(100));

        Table headerTable = null;
        try {
            headerTable = generateAccreditationHeader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Cell headerCell = new Cell();
        headerCell.add(headerTable);
        headerCell.setBorder(Border.NO_BORDER);
        headerCell.setBorderBottom(new SolidBorder(1f));
        headerCell.setMargin(3f);


        Cell bodyCell = new Cell();
        try {
            bodyCell.add(generateAccreditationBody(observer, localDate, script));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bodyCell.setBorder(Border.NO_BORDER);

        mainTable.addCell(headerCell);
        mainTable.addCell(bodyCell);
        return mainTable;
    }

    private Table generateAccreditationBody(ObserverEntity observer, LocalDate localDate, ScriptEnum scriptEnum) throws IOException {
        PdfFont cyrillicFont = PdfFontFactory.createFont("src/main/resources/font/cyrillic-font.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);


        float[] columnWidths = {200f, 120f}; // Custom column widths
        Table bodyTable = new Table(UnitValue.createPercentArray(columnWidths));
        bodyTable.setWidth(UnitValue.createPercentValue(100)); // Set table width

        Style keyStyle = new Style();
        keyStyle.setFontSize(10f)
                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFont(arialFont);

        Style valueStyle = new Style();
        valueStyle.setFontSize(11f)
                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBold()
                .setFont(arialFont);

        Text firstnameKey = new Text(doConvert("Ime: ", scriptEnum)).addStyle(keyStyle);
        Text firstnameValue = new Text(doConvert(observer.getFirstname().toUpperCase(), scriptEnum)).addStyle(valueStyle);
        Paragraph firstname = new Paragraph().add(firstnameKey).add(firstnameValue);

        Text lastnameKey = new Text(doConvert("Prezime: ", scriptEnum)).addStyle(keyStyle);
        Text lastnameValue = new Text(doConvert(observer.getLastname().toUpperCase(), scriptEnum)).addStyle(valueStyle);
        Paragraph lastname = new Paragraph().add(lastnameKey).add(lastnameValue);

        Text decisionNumberKey = new Text(doConvert("Pravo posmatranja prema odluci OIK/GIK br. ", scriptEnum) + "\n").addStyle(keyStyle);
        Text decisionNumberValue = new Text(observer.getStack().getDecisionNumber()).addStyle(valueStyle);
        Paragraph decisionNumber = new Paragraph().add(decisionNumberKey).add(decisionNumberValue);

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String formattedDate = localDate.format(customFormatter);

        Text dateKey = new Text(doConvert("Datum: ", scriptEnum)).addStyle(keyStyle);
        Text dateValue = new Text(formattedDate).addStyle(valueStyle);
        Paragraph date = new Paragraph().add(dateKey).add(dateValue);

        Text politicalOrganizationKey = new Text(doConvert(" PS-", scriptEnum)).addStyle(keyStyle).setFontSize(14f).setBold();
        Text politicalOrganizationValue = new Text(observer.getStack().getPoliticalOrganization().getCode()).addStyle(valueStyle).setFontSize(14f);
        Paragraph politicalOrganization = new Paragraph().add(politicalOrganizationKey).add(politicalOrganizationValue);
        politicalOrganization.setTextAlignment(TextAlignment.CENTER);

        Text signatureKey = new Text(doConvert("Ovjerava: ", scriptEnum)).addStyle(keyStyle);
        Text signatureValue = new Text(doConvert("D.M.", scriptEnum)).addStyle(valueStyle);
        Paragraph signature = new Paragraph().add(signatureKey).add(signatureValue);

        Paragraph sealPlace = new Paragraph("_______________").addStyle(keyStyle);
        Paragraph sealText = new Paragraph(doConvert("MP", scriptEnum)).addStyle(keyStyle);

        String electionLogoPath = "src/main/resources/logo/election24-rotated.png";
        ImageData imageData = null;
        try {
            imageData = ImageDataFactory.create(electionLogoPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Image electionLogo = new Image(imageData);
        electionLogo.setHeight(50f);
        electionLogo.setWidth(50f);
        electionLogo.setTextAlignment(TextAlignment.CENTER);
        electionLogo.setHorizontalAlignment(HorizontalAlignment.CENTER);

        Cell firstnameCell = new Cell().add(firstname);
        firstnameCell.setBorder(Border.NO_BORDER);
        firstnameCell.setPadding(0f);
        firstnameCell.setMargin(0f);
        firstnameCell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        bodyTable.addCell(firstnameCell);

        Cell politicalOrganizationCell = new Cell().add(politicalOrganization);
        politicalOrganizationCell.setBorder(Border.NO_BORDER);
        politicalOrganizationCell.setPadding(0f);
        politicalOrganizationCell.setMargin(0f);
        bodyTable.addCell(politicalOrganizationCell);

        Cell lastnameCell = new Cell().add(lastname);
        lastnameCell.setBorder(Border.NO_BORDER);
        lastnameCell.setPadding(0f);
        lastnameCell.setMargin(0f);
        bodyTable.addCell(lastnameCell);

        Cell electionLogoCell = new Cell(2, 0).add(electionLogo);
        electionLogoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        electionLogoCell.setTextAlignment(TextAlignment.CENTER);
        electionLogoCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        electionLogoCell.setBorder(Border.NO_BORDER);
        electionLogoCell.setPadding(0f);
        electionLogoCell.setMargin(0f);
        bodyTable.addCell(electionLogoCell);

        Cell decisionNumberCell = new Cell().add(decisionNumber);
        decisionNumberCell.setBorder(Border.NO_BORDER);
        decisionNumberCell.setPadding(0f);
        decisionNumberCell.setMargin(0f);
        bodyTable.addCell(decisionNumberCell);

        Cell dateCell = new Cell().add(date);
        dateCell.setBorder(Border.NO_BORDER);
        dateCell.setPadding(0f);
        dateCell.setMargin(0f);
        bodyTable.addCell(dateCell);

        Cell sealPlaceCell = new Cell().add(sealPlace);
        sealPlaceCell.setBorder(Border.NO_BORDER);
        sealPlaceCell.setPadding(0f);
        sealPlaceCell.setMargin(0f);
        bodyTable.addCell(sealPlaceCell);

        Cell signatureCell = new Cell().add(signature);
        signatureCell.setBorder(Border.NO_BORDER);
        signatureCell.setPadding(0f);
        signatureCell.setMargin(0f);
        bodyTable.addCell(signatureCell);

        Cell sealTextCell = new Cell().add(sealText);
        sealTextCell.setBorder(Border.NO_BORDER);
        sealTextCell.setPadding(0f);
        sealTextCell.setMargin(0f);
        bodyTable.addCell(sealTextCell);

        return bodyTable;
    }

    private Table generateAccreditationHeader() throws IOException {
        float[] columnWidths = {145f, 30f, 145f}; // Custom column widths
        Table headerTable = new Table(UnitValue.createPercentArray(columnWidths));
        headerTable.setWidth(UnitValue.createPercentValue(100)); // Set table width

        Style countryNameStyle = new Style();
        countryNameStyle.setFontSize(4.5f)
                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

        Style companyNameStyle = new Style();
        companyNameStyle.setFontSize(5.5f)
                .setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

        PdfFont cyrillicFont = PdfFontFactory.createFont("src/main/resources/font/cyrillic-font.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        Paragraph countryNameLatin = new Paragraph("Bosna i Hercegovina".toUpperCase());
        countryNameLatin.addStyle(countryNameStyle);

        Paragraph countryNameCyrillic = new Paragraph("Босна и Херцеговина".toUpperCase());
        countryNameCyrillic.setFont(cyrillicFont);
        countryNameCyrillic.addStyle(countryNameStyle);

        Paragraph companyNameBosnian = new Paragraph("Centralna izborna komisija".toUpperCase());
        companyNameBosnian.addStyle(companyNameStyle);
        Paragraph companyNameCroatian = new Paragraph("Središnje izborno povjerenstvo".toUpperCase());
        companyNameCroatian.addStyle(companyNameStyle);
        Paragraph companyNameSerbian = new Paragraph("Централна изборна комсиија".toUpperCase());
        companyNameSerbian.setFont(cyrillicFont);
        companyNameSerbian.addStyle(companyNameStyle);

        String countryLogoPath = "src/main/resources/logo/logo_bih.png";
        ImageData imageData = ImageDataFactory.create(countryLogoPath);
        Image countryLogo = new Image(imageData);
        countryLogo.setHeight(23f);
        countryLogo.setWidth(20f);

        //Celije se dodaju po redovima, pa se odmah dodaje span - itext7 plejlista, video 19
        Cell countryNameLatinCell = new Cell().add(countryNameLatin);
        countryNameLatinCell.setBorder(Border.NO_BORDER);
        countryNameLatinCell.setPadding(0f);
        headerTable.addCell(countryNameLatinCell);

        Cell countryLogoCell = new Cell(3, 0).add(countryLogo);
        countryLogoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        countryLogoCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        countryLogoCell.setBorder(Border.NO_BORDER);
        countryLogoCell.setPadding(0f);
        headerTable.addCell(countryLogoCell);

        Cell countryNameCyrillicCell = new Cell().add(countryNameCyrillic);
        countryNameCyrillicCell.setBorder(Border.NO_BORDER);
        countryNameCyrillicCell.setPadding(0f);
        headerTable.addCell(countryNameCyrillicCell);

        Cell companyNameBosnianCell = new Cell().add(companyNameBosnian);
        companyNameBosnianCell.setBorder(Border.NO_BORDER);
        companyNameBosnianCell.setPadding(0f);
        headerTable.addCell(companyNameBosnianCell);

        Cell companyNameSerbianCell = new Cell(2, 0).add(companyNameSerbian);
        companyNameSerbianCell.setBorder(Border.NO_BORDER);
        companyNameSerbianCell.setPadding(0f);
        companyNameSerbianCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        headerTable.addCell(companyNameSerbianCell);

        Cell companyNameCroatianCell = new Cell().add(companyNameCroatian);
        companyNameCroatianCell.setBorder(Border.NO_BORDER);
        companyNameCroatianCell.setPadding(0f);
        headerTable.addCell(companyNameCroatianCell);


        return headerTable;
    }

    private String doConvert(String value, ScriptEnum scriptEnum) {
        return scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(value) : value;
    }

    public String downloadAcceptedObserversXslx(StackEntity entity, ScriptEnum scriptEnum, String fileTitle) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet acceptedObservers = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Prihvaćeni") : "Prihvaćeni");

        addHeaderForAcceptedObservers(acceptedObservers, scriptEnum);

        int counter = 1; //Jer je header 0
        XSSFCellStyle successStyle = createCellStyleWithColor(acceptedObservers.getWorkbook(), GREEN_DATA, false, false);
        XSSFCellStyle errorStyle = createCellStyleWithColor(acceptedObservers.getWorkbook(), RED_DATA, false, false);
        XSSFCellStyle npcStyle = createCellStyleWithColor(acceptedObservers.getWorkbook(), "#ffffff", false, false);

        for(ObserverEntity observer: entity.getObservers().stream().filter(o -> o.getStatus().getSuccess() == true || o.getForce() == true).sorted(Comparator.comparing(ObserverEntity::getDocumentNumber)).collect(Collectors.toList())) {
            writeObserver(acceptedObservers, observer, counter, counter % 2 != 0 ? npcStyle : successStyle, scriptEnum, false);
            counter++;
        }

        XSSFSheet rejectedObservers = workbook.createSheet(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("Odbijeni") : "Odbijeni");
        addHeaderForRejectedObservers(rejectedObservers, scriptEnum);

        counter = 1;
        for(ObserverEntity observer: entity.getObservers().stream().filter(o -> o.getStatus().getSuccess() == false && o.getForce() == false).sorted(Comparator.comparing(ObserverEntity::getDocumentNumber)).collect(Collectors.toList())) {
            writeObserver(rejectedObservers, observer, counter, counter % 2 != 0 ? npcStyle : errorStyle, scriptEnum, true);
            counter++;
        }

        String filePath = ROOT_PATH + File.separator + fileTitle;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
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

        return filePath;
    }

    private void addHeaderForAcceptedObservers(XSSFSheet sheet, ScriptEnum scriptEnum) {
        XSSFRow row = sheet.createRow(0);
        List<String> headerElements = new ArrayList<>();
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  R.B.  ") : "  R.B.  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  JMBG  ") : "  JMBG  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Broj dokumenta  ") : "  Broj dokumenta  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Prezime  ") : "  Prezime  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Ime  ") : "  Ime  ");

        XSSFCellStyle style = createCellStyleWithColor(sheet.getWorkbook(), GREEN_HEADER, true, true);

        int columnIndex = 0;
        for(String headerElement: headerElements) {
            XSSFCell cell = row.createCell(columnIndex);
            cell.setCellStyle(style);
            cell.setCellValue(headerElement);
            sheet.autoSizeColumn(columnIndex);
            columnIndex++;
        }
    }

    private void addHeaderForRejectedObservers(XSSFSheet sheet, ScriptEnum scriptEnum) {
        XSSFRow row = sheet.createRow(0);
        List<String> headerElements = new ArrayList<>();
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  R.B.  ") : "  R.B.  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  JMBG  ") : "  JMBG  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Broj dokumenta  ") : "  Broj dokumenta  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Prezime  ") : "  Prezime  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Ime  ") : "  Ime  ");
        headerElements.add(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert("  Razlog  ") : "  Razlog  ");

        XSSFCellStyle style = createCellStyleWithColor(sheet.getWorkbook(), RED_HEADER, true, true);

        int columnIndex = 0;
        for(String headerElement: headerElements) {
            XSSFCell cell = row.createCell(columnIndex);
            cell.setCellStyle(style);
            cell.setCellValue(headerElement);
            sheet.autoSizeColumn(columnIndex);
            columnIndex++;
        }
    }

    private void writeObserver(XSSFSheet sheet, ObserverEntity observer, int counter, XSSFCellStyle style, ScriptEnum scriptEnum, boolean showReason) {
        XSSFRow row = sheet.createRow(counter);

        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(observer.getDocumentNumber());
        sheet.autoSizeColumn(0);

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(observer.getJmbg() + "  ");
        sheet.autoSizeColumn(1);

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue(observer.getCardId() + "  ");
        sheet.autoSizeColumn(2);

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(observer.getLastname().toUpperCase() + "  ") : observer.getLastname().toUpperCase()  + "  ");
        sheet.autoSizeColumn(3);

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(observer.getFirstname().toUpperCase() + "  ") : observer.getFirstname().toUpperCase() + "  ");
        sheet.autoSizeColumn(4);

        if(showReason) {
            cell = row.createCell(5);
            cell.setCellStyle(style);
            cell.setCellValue(scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(observer.getStatus().getName() + "     ") : observer.getStatus().getName() + "     ");
            sheet.autoSizeColumn(5);
        }
    }

    private static XSSFCellStyle createCellStyleWithColor(Workbook workbook, String hexColor, boolean useWhiteText, boolean isBold) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // Set background color if provided
        if (hexColor != null) {
            XSSFColor backgroundColor = new XSSFColor(java.awt.Color.decode(hexColor), null);
            cellStyle.setFillForegroundColor(backgroundColor);
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create and configure the font
        Font font = workbook.createFont();
        if (useWhiteText) {
            font.setColor(IndexedColors.WHITE.getIndex());
        } else {
            font.setColor(IndexedColors.BLACK.getIndex()); // Default text color
        }
        if(isBold)
            font.setBold(true);

        // Apply the font to the cell style
        cellStyle.setFont(font);



        // Set solid black borders
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

        return cellStyle;
    }


    public String generateAcceptedObserversForDecision(StackEntity entity, String fileTitle, ScriptEnum scriptEnum) throws IOException, InvalidFormatException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XWPFDocument document = new XWPFDocument();

        //page break
        //run1.addBreak(org.apache.poi.xwpf.usermodel.BreakType.PAGE);
        createHeader(document, scriptEnum);


        addDecisionNumber(document, entity.getDecisionNumber(), scriptEnum, "-1");
        addDate(document, entity.convertDecisionDate(), scriptEnum);
        addEmptyLine(document);

        setFirstParagraphForIntroduction(document, entity.getPoliticalOrganization(), entity.convertDecisionDate(), entity.convertRequestDate(), scriptEnum);
        setCenteredTitle(document, scriptEnum);
        setFirstParagraphForDecision(document, entity.getPoliticalOrganization(), scriptEnum);
        addMainTable(document, entity.getObservers(), scriptEnum);
        setSecondParagraphForDecision(document, entity.getExpirationDecisionDate(), scriptEnum);
        setThirdParagraphForDecision(document, scriptEnum);

        setCenteredTitleForExplanation(document, scriptEnum);
        setFirstParagraphForExplanation(document, entity.getPoliticalOrganization(), entity.convertRequestDate(), scriptEnum);
        setSecondParagraphForExplanation(document, scriptEnum);
        setThirdParagraphForExplanation(document, scriptEnum);


        setSignature(document, scriptEnum);
        createFooter(document, scriptEnum);

        String filePath = ROOT_PATH + File.separator + fileTitle;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            document.write(out);
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private void setCenteredTitleForExplanation(XWPFDocument document, ScriptEnum scriptEnum) {
        String label = "О б р а з л о ж е њ е";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(12);
    }

    private void setCenteredTitle(XWPFDocument document, ScriptEnum scriptEnum) {
        String label = "О Д Л У К У";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(12);
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

    private void createHeader(XWPFDocument document, ScriptEnum scriptEnum) throws IOException, InvalidFormatException {
        // Create a table with one row and two columns
        XWPFTable table = document.createTable(1, 2);
        XWPFTableRow row = table.getRow(0);

        // Set column widths using CTTblGrid for accurate control
        int imageWidth = 2000;
        int textWidth = 9000;
        table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(imageWidth));
        table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(textWidth));

        row.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(imageWidth));
        row.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(textWidth));

        // Column for Image
        XWPFTableCell cellImage = row.getCell(0);

        CTTcPr tcPr = cellImage.getCTTc().addNewTcPr();
        removeBorder(cellImage);

        cellImage.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph pImage = cellImage.addParagraph();
        pImage.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runImage = pImage.createRun();
        String imgFile = "src/main/resources/logo/logo-bl-without-signature.png";
        try (InputStream is = new FileInputStream(imgFile)) {
            runImage.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(67), Units.toEMU(80));
        }

        // Column for Text
        XWPFTableCell cellText = row.getCell(1);
        removeBorder(cellText);
        cellText.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.TOP);

        String entityLabel = "Република Српска";
        String cityLabel = "Град Бања Лука";
        String gikLabel = "Градска изборна комисија";
        String streetLabel = "Трг српских владара 1, Бања Лука";

        String entityText = scriptEnum == ScriptEnum.CYRILLIC ? entityLabel : cyrillicToLatinConverter.convert(entityLabel);
        String cityText = scriptEnum == ScriptEnum.CYRILLIC ? cityLabel : cyrillicToLatinConverter.convert(cityLabel);
        String gikText = scriptEnum == ScriptEnum.CYRILLIC ? gikLabel : cyrillicToLatinConverter.convert(gikLabel);
        String streetText = scriptEnum == ScriptEnum.CYRILLIC ? streetLabel : cyrillicToLatinConverter.convert(streetLabel);

        String[] lines = {
                entityText,
                cityText,
                gikText,
                streetText
        };

        // Create a paragraph for each line
        for (String line : lines) {
            XWPFParagraph pText = cellText.addParagraph();
            XWPFRun runText = pText.createRun();
            runText.setText(line);
            runText.setFontSize(10);
            if (line.equals(entityText))
                runText.setBold(true);
            if(line.equals(streetText))
                runText.setItalic(true);
            pText.setSpacingAfter(0);
        }

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setBorderTop(Borders.SINGLE);
        paragraph.setBorderTop(Borders.THICK);
    }
    private void removeBorder(XWPFTableCell cell) {
        //An empty paragraph is added into cell by default, so it should be removed
        cell.removeParagraph(0); // Remove the default empty paragraph

        CTTcPr tcPr = cell.getCTTc().addNewTcPr();

        CTTcBorders borders = tcPr.addNewTcBorders();
        borders.addNewTop().setVal(STBorder.NIL);
        borders.addNewBottom().setVal(STBorder.NIL);
        borders.addNewLeft().setVal(STBorder.NIL);
        borders.addNewRight().setVal(STBorder.NIL);
    }

    private void createFooter(XWPFDocument document, ScriptEnum scriptEnum) {
        XWPFFooter footer = document.createFooter(HeaderFooterType.FIRST);
        XWPFParagraph paragraph = footer.createParagraph();
        paragraph.setBorderBottom(Borders.SINGLE);
        paragraph.setBorderBottom(Borders.THICK);

        // Create a table with 3 columns for the footer text
        XWPFTable footerTable = footer.createTable(1, 3);
        XWPFTableRow footerRow = footerTable.getRow(0);
        CTTblWidth tableWidth = footerTable.getCTTbl().getTblPr().addNewTblW();
        tableWidth.setType(STTblWidth.DXA);
        tableWidth.setW(BigInteger.valueOf(10000));

        // Remove table borders (optional)
        removeBorder(footerRow.getCell(0));
        removeBorder(footerRow.getCell(1));
        removeBorder(footerRow.getCell(2));

        // First cell for "tel" and "faks"
        String label = "тел: +387 51 244 532, факс: +387 51 244 532";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : latinToCyrillicConverter.convert(label);
        XWPFTableCell cell1 = footerRow.getCell(0);
        XWPFParagraph p1 = cell1.addParagraph();
        p1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = p1.createRun();
        run1.setItalic(true);
        run1.setText(text);
        run1.setFontSize(10);

        // Second cell for website
        XWPFTableCell cell2 = footerRow.getCell(1);
        XWPFParagraph p2 = cell2.addParagraph();
        p2.setAlignment(ParagraphAlignment.CENTER);
        p2.setIndentationRight(150);
        XWPFRun run2 = p2.createRun();
        run2.setItalic(true);
        run2.setText("www.banjaluka.rs.ba");
        run2.setFontSize(10);

        // Third cell for email
        XWPFTableCell cell3 = footerRow.getCell(2);
        XWPFParagraph p3 = cell3.addParagraph();
        p3.setAlignment(ParagraphAlignment.CENTER);
        p3.setIndentationRight(300);
        XWPFRun run3 = p3.createRun();
        run3.setItalic(true);
        run3.setText("gikbl034@banjaluka.rs.ba");
        run3.setFontSize(10);

        // Adjust the column widths (optional, depending on your layout needs)
        int telFaksWidth = 3000;
        int websiteWidth = 3000;
        int emailWidth = 3000;
        footerTable.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(telFaksWidth));
        footerTable.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(websiteWidth));
        footerTable.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(emailWidth));

        XWPFFooter defaultFooter = document.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph footerParagraph = defaultFooter.createParagraph();
        footerParagraph.setAlignment(ParagraphAlignment.CENTER);
        footerParagraph.getCTP().addNewFldSimple().setInstr("PAGE");
    }

    private void addDecisionNumber(XWPFDocument document, String decisionNumber, ScriptEnum scriptEnum, String decisionNumberSuffix) {
        String label = "Број: ";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(label) : cyrillicToLatinConverter.convert(label);
        text += decisionNumber + decisionNumberSuffix + ".";
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(11);
        run.setText(text);
    }

    private void addDate(XWPFDocument document, String date, ScriptEnum scriptEnum) {
        String label = "Дана, ";
        String labelEnd = " године";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(label + date + labelEnd) : cyrillicToLatinConverter.convert(label + date + labelEnd);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(11);
        run.setText(text);
    }

    private void removeSpacing(XWPFParagraph paragraph) {
        paragraph.setSpacingBefore(0);
        paragraph.setSpacingAfter(0);
    }

    public void setFirstParagraphForIntroduction(XWPFDocument document, PoliticalOrganizationEntity politicalOrganization, String decisionDate, String requestDate, ScriptEnum scriptEnum) {
        String firstPartLabel = "На основу чл. 17.5 Изборног закона БиХ („Службени гласник БиХ“, бр. 23/1, 7/02, 9/02, 20/02, 25/02, 4/04, 20/04, 25/05, 52/05, 65/05, 77/05, 11/06, 24/06, 32/07, 33/08, 37/08, 32/10, 18/13 7/14, 31/16, 41/20, 38/22 , 51/22, 67/22 i 24/24) и чл. 3., 8., 9. и 15. Упутства о условима и процедурама за акредитовање изборних посматрача у БиХ („Службени гласник, БиХ“, број 31/24), Градска изборна комисија Бања Лука је, на сједници одржаној " + decisionDate + " године разматрала захтјев политичког субјекта ";
        String politicalOrganizationPart = politicalOrganization.getName();
        String thirdPart = " (шифра ";
        String code = politicalOrganization.getCode();
        String fourthPart = ") од " + requestDate + ", за акредитовање посматрача за посматрање изборних активности изборне комисије, центра за бирачки списак и бирачких мјеста на подручју основне изборне јединице 034 Б – Бања Лука и донијела сљедећу";

        String resultLabel = firstPartLabel + politicalOrganizationPart + thirdPart + code + fourthPart;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setFirstParagraphForDecision(XWPFDocument document, PoliticalOrganizationEntity politicalOrganization, ScriptEnum scriptEnum) {
        String firstPartLabel = "1. Акредитују се посматрачи политичког субјекта ";
        String politicalOrganizationLabel = politicalOrganization.getName();
        String secondPartLabel = ", шифра ";
        String politicalOrganizationCode = politicalOrganization.getCode();
        String thirdPartLabel = ", за посматрање изборних активности Градске изборне комисије Бања Лука, Центра за бирачки списак Бања Лука и активности на бирачким мјестима на подручју основне изборне јединице 034 Б – Бања Лука";

        String resultLabel = firstPartLabel + politicalOrganizationLabel + secondPartLabel + politicalOrganizationCode + thirdPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setSecondParagraphForDecision(XWPFDocument document, String date, ScriptEnum scriptEnum) {
        String firstPartLabel = "2. Ова одлука ступа на снагу дана ";
        String secondPartLabel = " године.";

        String resultLabel = firstPartLabel + date + secondPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setThirdParagraphForDecision(XWPFDocument document, ScriptEnum scriptEnum) {
        String firstPartLabel = "3. Одлука ће бити достављена подносиоцу захтјева, Централној изборној комисији БиХ, у попис аката и евиденцију Градске изборне комисије.";

        String resultLabel = firstPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setFirstParagraphForExplanation(XWPFDocument document, PoliticalOrganizationEntity politicalOrganization, String date, ScriptEnum scriptEnum) {
        String firstPartLabel = "Политички субјект ";
        String politicalOrganizationLabel = politicalOrganization.getName();
        String secondPartLabel = " je ";
        String thirdPartLabel = " године, поднио Градској изборној комисији Бања Лука захтјев за актедитовање посматрача за посматрање изборних активности Градске изборне комисије, Центра за бирачки списак и бирачких мјеста (одбора) на подручју основне изборне јединице 034 Б – Бања Лука.";

        String resultLabel = firstPartLabel + politicalOrganizationLabel + secondPartLabel + date + thirdPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setSecondParagraphForExplanation(XWPFDocument document, ScriptEnum scriptEnum) {
        String firstPartLabel = "Градска изборна комисија Бања Лука утврдила је да су испуњени неопходни услови за акредитовање изборних посматрача и да нема сметњи за акредитовање, те је поступила према Поглављу 17. Изборног закона БиХ и Упутства о условима и процедурама за акредитовање изборних посматрача у БиХ, и одлучила као у диспозитиву одлуке.";

        String resultLabel = firstPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setThirdParagraphForExplanation(XWPFDocument document, ScriptEnum scriptEnum) {
        String firstPartLabel = "ПРАВНА ПОУКА: Против ове одлуке, може се изјавити жалба у року од 24 сата од пријема. Жалба се подноси Централној изборној комисији путем Градске изборне комисије Бања Лука. Достављање жалбе врши се путем факс апарата или лично у сједиште Градске изборне комисије на прописаном обрасцу.";

        String resultLabel = firstPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }
    public void setSignature(XWPFDocument document, ScriptEnum scriptEnum) {
        String label = "ПРЕДСЈЕДНИК";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        paragraph.setIndentationRight(920);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(11);
        run.setBold(true);


        label = "Дубравко Малинић";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        paragraph.setIndentationRight(700);
        run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(11);
        run.setBold(true);
    }

    public void addEmptyLine(XWPFDocument document) {
        document.createParagraph();
    }

    private void addMainTable(XWPFDocument document, List<ObserverEntity> observers, ScriptEnum scriptEnum) {
        XWPFTable table = document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);
        table.setWidthType(TableWidthType.PCT);
        table.setWidth("70%");


        XWPFTableRow row = table.getRow(0);
        row.setHeight(200);
        String orderNumberHeaderLabel = "R.B.".toUpperCase();
        String orderNumberHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(orderNumberHeaderLabel) : orderNumberHeaderLabel;
        row.getCell(0).setText(orderNumberHeaderText);

        String lastnameHeaderLabel = "Prezime".toUpperCase();
        String lastnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastnameHeaderLabel) : lastnameHeaderLabel;
        row.addNewTableCell().setText(lastnameHeaderText);

        String firstnameHeaderLabel = "Ime".toUpperCase();
        String firstnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstnameHeaderLabel) : firstnameHeaderLabel;
        row.addNewTableCell().setText(firstnameHeaderText);
        styleHeaderRow(table.getRow(0));

        Collator collator = getCollatorForScript(scriptEnum);

        if(scriptEnum == ScriptEnum.CYRILLIC)
            observers.forEach(o -> {
                o.setLastname(latinToCyrillicConverter.convert(o.getLastname()).toUpperCase());
                o.setFirstname(latinToCyrillicConverter.convert(o.getFirstname()).toUpperCase());
            });
        List<ObserverEntity> sortedObservers = observers.stream()
                .filter(o -> o.getStatus().getSuccess())
                .sorted(Comparator.comparing(ObserverEntity::getLastname, collator)
                        .thenComparing(ObserverEntity::getFirstname, collator))
                .collect(Collectors.toList());

        int i = 0;
        for(ObserverEntity observer: sortedObservers) {
            XWPFTableRow newRow = table.createRow();

            newRow.getCell(0).setText((i+1) + ".");
            newRow.getCell(1).setText(observer.getLastname());
            newRow.getCell(2).setText(observer.getFirstname());
            styleDataRow(newRow, i);
            i++;
        }

        addEmptyLine(document);
    }

    private Collator getCollatorForScript(ScriptEnum scriptEnum) {
        Locale.Builder localeBuilder = new Locale.Builder().setLanguage("sr").setRegion("RS");
        if (scriptEnum == ScriptEnum.CYRILLIC) {
            localeBuilder.setScript("Cyrl");
        }
        Locale locale = localeBuilder.build();
        Collator collator = Collator.getInstance(locale);
        return collator;
    }

    private static void styleHeaderRow(XWPFTableRow row) {
        row.setHeight(100);
        for (XWPFTableCell cell : row.getTableCells()) {
            cell.setColor("A9A9A9");

            XWPFParagraph existingParagraph = cell.getParagraphs().get(0);
            String text = existingParagraph.getText();
            XWPFParagraph paragraph = cell.addParagraph();
            cell.removeParagraph(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            paragraph.setVerticalAlignment(org.apache.poi.xwpf.usermodel.TextAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setFontSize(11);
            run.setText(text);
            run.setColor("FFFFFF");
            run.setVerticalAlignment("baseline");
            run.setBold(true);

            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // Center vertically
        }
    }

    private static void styleDataRow(XWPFTableRow row, int rowNum) {
        row.setHeight(100);
        for (XWPFTableCell cell : row.getTableCells()) {
            if(rowNum % 2 != 0)
                cell.setColor("EEEEEE");
            else
                cell.setColor("FFFFFF");

            XWPFParagraph existingParagraph = cell.getParagraphs().get(0);
            String text = existingParagraph.getText();
            XWPFParagraph paragraph = cell.addParagraph();
            cell.removeParagraph(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            paragraph.setVerticalAlignment(org.apache.poi.xwpf.usermodel.TextAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setFontSize(11);
            run.setText(text);
            run.setColor("000000");
            run.setVerticalAlignment("baseline");

            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // Center vertically
        }
    }

    public String generateRejectedObserversForDecision(StackEntity entity, String fileTitle, ScriptEnum scriptEnum) throws IOException, InvalidFormatException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XWPFDocument document = new XWPFDocument();
        createHeader(document, scriptEnum);


        addDecisionNumber(document, entity.getDecisionNumber(), scriptEnum, "-2");
        addDate(document, entity.convertDecisionDate(), scriptEnum);
        addEmptyLine(document);

        setFirstParagraphForIntroduction(document, entity.getPoliticalOrganization(), entity.convertDecisionDate(), entity.convertRequestDate(), scriptEnum);
        setCenteredTitle(document, scriptEnum);
        setFirstParagraphForRejectingDecision(document, entity.getPoliticalOrganization(), scriptEnum);
        addMainTableForRejection(document, entity.getObservers(), scriptEnum);
        setSecondParagraphForDecision(document, entity.getExpirationDecisionDate(), scriptEnum);
        setThirdParagraphForDecision(document, scriptEnum);


        setCenteredTitleForExplanation(document, scriptEnum);
        setFirstParagraphForExplanation(document, entity.getPoliticalOrganization(), entity.convertRequestDate(), scriptEnum);
        setSecondParagraphForRejectionExplanation(document, scriptEnum);
        setThirdParagraphForExplanation(document, scriptEnum);


        setSignature(document, scriptEnum);
        createFooter(document, scriptEnum);

        String filePath = ROOT_PATH + File.separator + fileTitle;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            document.write(out);
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private void setFirstParagraphForRejectingDecision(XWPFDocument document, PoliticalOrganizationEntity politicalOrganization,  ScriptEnum scriptEnum) {
        String firstPartLabel = "1. Одбија се захтјев за акредитовање посматрача политичког субјекта ";
        String politicalOrganizationLabel = politicalOrganization.getName();
        String secondPartLabel = ", шифра ";
        String politicalOrganizationCode = politicalOrganization.getCode();
        String thirdPartLabel = ", за посматрање изборних активности Градске изборне комисије Бања Лука, Центра за бирачки списак Бања Лука и активности на бирачким мјестима на подручју основне изборне јединице 034 Б – Бања Лука, за лица која не испуњавају услове из чл. 9. став (6) тачка б) Упутства о условима и процедурама за акредитовање изборних посматрача у БиХ, и то:";

        String resultLabel = firstPartLabel + politicalOrganizationLabel + secondPartLabel + politicalOrganizationCode + thirdPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setSecondParagraphForRejectionExplanation(XWPFDocument document, ScriptEnum scriptEnum) {
        String firstPartLabel = "Градска изборна комисија Бања Лука утврдила је да нису испуњени неопходни услови за акредитовање изборних посматрача, те је поступила према Поглављу 17. Изборног закона БиХ и Упутства о условима и процедурама за акредитовање изборних посматрача у БиХ, и одлучила као у диспозитиву одлуке.";

        String resultLabel = firstPartLabel;
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    private void addMainTableForRejection(XWPFDocument document, List<ObserverEntity> observers, ScriptEnum scriptEnum) {
        XWPFTable table = document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);
        table.setWidthType(TableWidthType.PCT);
        table.setWidth("90%");


        XWPFTableRow row = table.getRow(0);
        row.setHeight(200);
        String orderNumberHeaderLabel = "R.B.".toUpperCase();
        String orderNumberHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(orderNumberHeaderLabel) : orderNumberHeaderLabel;
        row.getCell(0).setText(orderNumberHeaderText);

        String lastnameHeaderLabel = "Prezime".toUpperCase();
        String lastnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastnameHeaderLabel) : lastnameHeaderLabel;
        row.addNewTableCell().setText(lastnameHeaderText);

        String firstnameHeaderLabel = "Ime".toUpperCase();
        String firstnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstnameHeaderLabel) : firstnameHeaderLabel;
        row.addNewTableCell().setText(firstnameHeaderText);
        styleHeaderRow(table.getRow(0));

        String reasonHeaderLabel = "Razlog".toUpperCase();
        String reasonHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(reasonHeaderLabel) : reasonHeaderLabel;
        row.addNewTableCell().setText(reasonHeaderText);
        styleHeaderRow(table.getRow(0));

        Collator collator = getCollatorForScript(scriptEnum);

        if(scriptEnum == ScriptEnum.CYRILLIC)
            observers.forEach(o -> {
                o.setLastname(latinToCyrillicConverter.convert(o.getLastname()).toUpperCase());
                o.setFirstname(latinToCyrillicConverter.convert(o.getFirstname()).toUpperCase());
            });
        List<ObserverEntity> sortedObservers = observers.stream()
                .filter(o -> o.getStatus().getSuccess() == false)
                .sorted(Comparator.comparing(ObserverEntity::getLastname, collator)
                        .thenComparing(ObserverEntity::getFirstname, collator))
                .collect(Collectors.toList());

        int i = 0;
        for(ObserverEntity observer: sortedObservers) {
            XWPFTableRow newRow = table.createRow();

            newRow.getCell(0).setText((i+1) + ".");
            newRow.getCell(1).setText(observer.getLastname());
            newRow.getCell(2).setText(observer.getFirstname());
            String reasonLabel = observer.getStatus().getName();
            String reasonText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(reasonLabel) : cyrillicToLatinConverter.convert(reasonLabel);
            newRow.getCell(3).setText(reasonText);
            styleDataRow(newRow, i);
            i++;
        }

        addEmptyLine(document);
    }
}

