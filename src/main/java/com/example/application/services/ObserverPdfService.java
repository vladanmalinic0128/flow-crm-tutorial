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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class ObserverPdfService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final LatinToCyrillicConverter latinToCyrillicConverter;

    public ObserverPdfService(LatinToCyrillicConverter latinToCyrillicConverter) {
        this.latinToCyrillicConverter = latinToCyrillicConverter;
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

        String electionLogoPath = "src/main/resources/logo/election24.png";
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


}

