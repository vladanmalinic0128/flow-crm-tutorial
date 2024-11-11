package com.example.application.services;

import com.example.application.entities.*;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.AssociateRepository;
import com.example.application.repositories.AssociateStatusRepository;
import com.example.application.repositories.BankRepository;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.sql.Date;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CollaboratorsXlsxService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;

    private final BankRepository bankRepository;
    private final AssociateRepository associateRepository;
    private final AssociateStatusRepository associateStatusRepository;

    private final ReportsXlsxService reportsXlsxService;

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

    public String generateReportForBanks(String fileTitle, ScriptEnum scriptEnum, boolean isExtern) throws IOException {
        return reportsXlsxService.generateReportForBanksByAssociates(fileTitle, scriptEnum, isExtern);
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

    public void updateCollaboratorsThroughXlsxFile(InputStream inputStream, boolean isExtern) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            List<AssociateEntity> associates = this.getCollaboratorsFromXlsxFile(workbook.getSheetAt(0), isExtern);

            for(AssociateEntity a: associates)
                associateRepository.save(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<AssociateEntity> getCollaboratorsFromXlsxFile(XSSFSheet sheet, boolean isExtern) {
        int rows = sheet.getLastRowNum();
        List<AssociateEntity> associates = new ArrayList<>();

        for(int r=1; r<=rows; r++) {
            System.out.println(r);
            XSSFRow row = sheet.getRow(r);
            if(row == null)
                continue;
            int cols = row.getLastCellNum();
            if(isExtern && row.getLastCellNum() < 12)
                continue;
            else if(isExtern == false && row.getLastCellNum() < 10)
                continue;
            AssociateEntity associate = new AssociateEntity();
            associate.setIsExtern(isExtern);

            XSSFCell cell = row.getCell(0);
            int number = cell != null ? (int)cell.getNumericCellValue() : -1;
            associate.setOrderNumber(number);

            String jmbg = getCellValue(row.getCell(1));
            readJmbg(jmbg, associate);

            String fullName = getCellValue(row.getCell(2));
            readNameCell(fullName, associate);

            String residence = getCellValue(row.getCell(3));
            associate.setResidence(residence);

            cell = row.getCell(4);
            double price = cell != null ? cell.getNumericCellValue() : -1;
            associate.setPrice(price);

            if(isExtern) {
                String bankNumber = getCellValue(row.getCell(5));
                readBankNumber(bankNumber, associate);

                String bankName = getCellValue(row.getCell(6));
                readBankName(bankName, associate);
            }

            String contractDate = getCellValue(row.getCell(calculatePosition(7, isExtern)));
            associate.setContractDate(parseDateCell(contractDate));

            String startDate = getCellValue(row.getCell(calculatePosition(8, isExtern)));
            associate.setStartDate(parseDateCell(startDate));

            String endDate = getCellValue(row.getCell(calculatePosition(9, isExtern)));
            associate.setEndDate(parseDateCell(endDate));

            String reportDate = getCellValue(row.getCell(calculatePosition(10, isExtern)));
            associate.setReportDate(parseDateCell(reportDate));

            String status = getCellValue(row.getCell(calculatePosition(11, isExtern)));
            assignStatus(status, associate);

            associates.add(associate);
        }

        return associates;
    }

    private int calculatePosition(int i, boolean isExtern) {
        return isExtern ? i : (i-2);
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

    private void readJmbg(String jmbg, AssociateEntity associateEntity) {
        jmbg = jmbg.replaceAll("\\D", "");
        if(jmbg == null || jmbg.trim().length() == 0)
            associateEntity.setJmbg(null);
        else
            associateEntity.setJmbg(jmbg);
    }

    public void readNameCell(String value, AssociateEntity associateEntity) {
        String[] splittingResult = value.split(" ");

        if (splittingResult.length > 0) {
            associateEntity.setFirstname(splittingResult[splittingResult.length - 1]);

            if (splittingResult.length > 1) {
                String lastname = String.join(" ", Arrays.copyOf(splittingResult, splittingResult.length - 1));
                associateEntity.setLastname(lastname);
            } else
                associateEntity.setLastname(splittingResult[0]);
        }
    }

    private void readBankNumber(String bankNumber, AssociateEntity associateEntity) {
        if(bankNumber != null)
            bankNumber = bankNumber.replaceAll("\\D", "");
        if (bankNumber != null && bankNumber.trim().length() > 1)
            associateEntity.setBankNumber(bankNumber);
        else
            associateEntity.setBankNumber(null);
    }

    private void readBankName(String bankName, AssociateEntity associateEntity) {
        if (bankName != null && bankName.trim().length() > 1)
            associateEntity.setBankName(bankName);
        else
            associateEntity.setBankName(null);
    }

    public Date parseDateCell(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        try {
            java.util.Date parsedDate = dateFormat.parse(dateString);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void assignStatus(String cellContent, AssociateEntity associate) {
        try {
            int orderNumber = Integer.parseInt(cellContent.split("\\.")[0].trim());

            associateStatusRepository.findByOrderNumber(orderNumber)
                    .ifPresentOrElse(associate::setStatus,
                            () -> System.out.println("Status with order number " + orderNumber + " not found.")
                    );

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in cell content: " + cellContent);
        }
    }

    public String generateContracts(String fileTitle, ScriptEnum value, boolean isExtern) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        //set margins
        float topMargin = 50f;
        float leftMargin=52f;
        float rightMargin=52f;
        document.setTopMargin(topMargin);
        document.setLeftMargin(leftMargin);
        document.setRightMargin(rightMargin);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);

        List<AssociateEntity> associates = associateRepository.findAllByIsExtern(isExtern);

        int decisionNumber = 1;
        if(isExtern)
            decisionNumber += associateRepository.findAllByIsExtern(false).stream().count();

        for(AssociateEntity associate: associates) {
            generateContractByAssociate(document, associate, value, decisionNumber);
            document.add(new AreaBreak());
            generateReportByAssociate(document, associate, value, decisionNumber);
            document.add(new AreaBreak());

            decisionNumber++;
        }

        document.close();

        String fileName;
        fileName = ROOT_PATH + File.separator + fileTitle + "_" + System.currentTimeMillis() + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
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

    private void generateContractByAssociate(Document document, AssociateEntity associate, ScriptEnum scriptEnum, int decisionNumber) throws IOException {
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy.");

        String plainMainText = "UGOVOR O DJELU";
        String titleMainText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(plainMainText) : cyrillicToLatinConverter.convert(plainMainText);
        Paragraph mainTitle = new Paragraph(titleMainText)
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(12f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
        document.add(mainTitle);

        document.add(new Paragraph("\n"));

        String firstSentencePlain = "Zaključen u Banjaluci dana " + dateFormatter.format(associate.getContractDate()) + " godine između:";
        Paragraph firstSentence = new Paragraph(writeWithAppropriateScript(firstSentencePlain, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(firstSentence);

        addSmallEmptyRow(document);

        String secondSentencePlain = "1. Gradske izborne komisije Banjaluka, koju zastupa predsjednik Dubravko Malinić (u daljem tekstu: Naručilac posla) i ";
        Paragraph secondSentence = new Paragraph(writeWithAppropriateScript(secondSentencePlain, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(secondSentence);

        String thirdSentencePlain = "2. " + associate.getLastname() + " " + associate.getFirstname() + ",  iz opštine " + associate.getResidence() + ", JMB " + associate.getJmbg() + " (u daljem tekstu: Izvršilac posla).";
        Paragraph thirdSentence = new Paragraph(writeWithAppropriateScript(thirdSentencePlain, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(thirdSentence);

        addSmallEmptyRow(document);

        String firstMainSentencePlain = "Naručilac posla i Izvršilac posla sporazumijeli su se o sljedećem:";
        Paragraph firstMainSentence = new Paragraph(writeWithAppropriateScript(firstMainSentencePlain, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(firstMainSentence);

        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 1.");
        addSmallEmptyRow(document);

        String description = "Izvršilac posla se obavezuje da po nalogu Naručioca posla radi " + associate.getStatus().getAkkName() + " u pripremi i provođenju Lokalnih  izbora 2024. godine.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 2.");
        addSmallEmptyRow(document);
        description = "Poslove iz člana 1. ovog ugovora Izvršilac posla će obaviti u periodu od " + dateFormatter.format(associate.getStartDate()) + " do " + dateFormatter.format(associate.getEndDate()) + " godine, a sve po potrebi i rasporedu koji utvrdi Naručilac.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        description = "Izvršilac posla je dužan da preuzete poslove obavi kvalitetno i u predviđenom roku.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 3.");
        addSmallEmptyRow(document);
        description = "Za izvršeni posao iz člana 1. ovog ugovora, Naručilac posla isplatiće iznos obračunat na osnovu evidencija rada, tj. za Izvršioca " + associate.getLastname() + " " + associate.getFirstname() + ", u iznosu od " + String.format(Locale.getDefault(), "%.2f", associate.getPrice()).replace('.', ',') + " KM.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 4.");
        addSmallEmptyRow(document);
        if(associate.getIsExtern())
            description = "Naručilac posla se obavezuje da će ugovoreni iznos uplatiti na tekući račun Izvršioca posla broj " + associate.getBankNumber() + ", otvoren kod " + findBankNameByBankNumber(associate) + ", a tereteći potrošačku jedinicu 2002111 – GIK Banja Luka, stavku 412900 (0160) – tehnička priprema i provođenje izbora.";
        else
            description = "Naručilac posla se obavezuje da će ugovoreni iznos uplatiti na tekući račun Izvršioca posla  na koji mu se isplaćuju redovna mjesečna primanja, kao zaposleniku Gradske uprave Banja Luka, tereteći potrošačku jedinicu 2002111 – GIK Banja Luka, stavku 4129000 (0160) – tehnička priprema i provođenje izbora.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 5.");
        addSmallEmptyRow(document);
        description = "U slučaju spora po ovom ugovoru, nadležan je Osnovni sud u Banjaluci.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addLawArticleTitle(document, arialFont, scriptEnum, "Član 6.");
        addSmallEmptyRow(document);
        description = "Ovaj ugovor sačinjen je u 3 (tri) istovjetna primjerka, od kojih svaka ugovorna strana zadržava po 1 (jedan) primjerak, a jedan primjerak se dostavlja Odjeljenju za finansije na realizaciju.";
        addLawArticleDescription(document, arialFont, scriptEnum, description);
        addSmallEmptyRow(document);

        addSignatureForContract(document, arialFont, scriptEnum);

        addSmallEmptyRow(document);
        if(associate.getIsExtern())
            description = "Broj: 01-03-1/22-203-" + decisionNumber;
        else
            description = "Broj: 01-03-1/22-204-" + decisionNumber;
        addLawArticleDescription(document, arialFont, scriptEnum, description);
    }

    private void addLawArticleTitle(Document document, PdfFont arialFont, ScriptEnum scriptEnum, String title) {
        Paragraph mainTitle = new Paragraph(writeWithAppropriateScript(title, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
        document.add(mainTitle);
    }

    private void addLawArticleDescription(Document document, PdfFont arialFont, ScriptEnum scriptEnum, String description) {
        Paragraph paragraph = new Paragraph(writeWithAppropriateScript(description, scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(paragraph);
    }
    private void generateReportByAssociate(Document document, AssociateEntity associate, ScriptEnum scriptEnum, int decisionNumber) throws IOException {
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy.");

        Paragraph mainTitle = new Paragraph(writeWithAppropriateScript("IZVJEŠTAJ", scriptEnum))
                .setFont(arialFont)
                .setFontSize(12)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(12f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
        document.add(mainTitle);

        Paragraph paragraph = new Paragraph(writeWithAppropriateScript("o obavljenom poslu po zaključenom ugovoru o djelu", scriptEnum))
                .setFont(arialFont)
                .setMargin(0)
                .setPadding(0)
                .setFontSize(10.5f)
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        addSmallEmptyRow(document);
        addSmallEmptyRow(document);

        String text;
        if(associate.getIsExtern())
            text = "01-03-1/24-203-" + decisionNumber;
        else
            text = "01-03-1/24-204-" + decisionNumber;

        String firstParagraph = "Ugovorom o djelu broj " + text + "   od dana  " + dateFormatter.format(associate.getContractDate()) + " godine, koji su u Banjaluci zaključili Naručilac posla Gradska izborna komisija Banja Luka, koju zastupa predsjednik Dubravko Malinić i  " + associate.getLastname() + " " + associate.getFirstname() + ", iz opštine " + associate.getResidence() + ", na period od " + dateFormatter.format(associate.getStartDate()) + " do " + dateFormatter.format(associate.getEndDate()) + " godine, " + associate.getLastname() + " " + associate.getFirstname() + " se, kao ugovorna strana-angažovano lice, obavezao da će za potrebe naručioca posla vršiti " + associate.getStatus().getAkkName() + ".";
        addLawArticleDescription(document, arialFont, scriptEnum, firstParagraph);
        addSmallEmptyRow(document);

        String secondParagraph = "Shodno navedenom ugovoru Naručilac posla obavezao se angažovanom licu isplatiti iznos " + String.format(Locale.getDefault(), "%.2f", associate.getPrice()).replace('.', ',') + " KM za obavljeni posao, a po prethodno pregledanom i odobrenom izvještaju od strane Gradske izborne komisije Banja Luka, u ime Naručioca posla.";
        addLawArticleDescription(document, arialFont, scriptEnum, secondParagraph);
        addSmallEmptyRow(document);

        String thirdParagraph = "S tim u vezi angažovano lice podnosi izvještaj da je preuzete poslove obavilo u predviđenom roku od " + dateFormatter.format(associate.getStartDate()) + " do " + dateFormatter.format(associate.getEndDate()) + " godine. U navedenom izvještajnom periodu angažovani je obavljao " + associate.getStatus().getAkkName() + ".";
        addLawArticleDescription(document, arialFont, scriptEnum, thirdParagraph);
        addSmallEmptyRow(document);

        String endParagraph = "Banja Luka, " + dateFormatter.format(associate.getReportDate());
        addLawArticleDescription(document, arialFont, scriptEnum, endParagraph);
        addSmallEmptyRow(document);

        addSignatureForReport(document, arialFont, scriptEnum);
    }

    private String writeWithAppropriateScript(String plainText, ScriptEnum scriptEnum) {
        return scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(plainText) : cyrillicToLatinConverter.convert(plainText);
    }

    private void addSmallEmptyRow(Document document) {
        document.add(new Paragraph("\n").setMargin(0).setPadding(0).setFontSize(8f));
    }

    private void addSignatureForContract(Document document, PdfFont font, ScriptEnum scriptEnum) {
        // Create a table with 2 columns and make it full width
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();


        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(writeWithAppropriateScript("Naručilac posla:\nGradska izborna komisija", scriptEnum))
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(writeWithAppropriateScript("Izvršilac posla:", scriptEnum))
                        .setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Left signature line
        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("________________________________")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Right signature line
        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("________________________________")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Left cell for title
        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(writeWithAppropriateScript("Predsjednik Dubravko Malinić", scriptEnum))
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("")) // Empty cell for spacing
                .setBorder(Border.NO_BORDER));

        // Add the table to the document
        document.add(table);
    }

    private void addSignatureForReport(Document document, PdfFont font, ScriptEnum scriptEnum) {
        // Create a table with 2 columns and make it full width
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();


        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(writeWithAppropriateScript("ANGAŽOVANO LICE", scriptEnum))
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(writeWithAppropriateScript("PREDSJEDNIK\nDubravko Malinić", scriptEnum))
                        .setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Left signature line
        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("________________________________")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Right signature line
        table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("________________________________")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setFontSize(10))
                .setBorder(Border.NO_BORDER));

        // Add the table to the document
        document.add(table);
    }

    private String findBankNameByBankNumber(AssociateEntity associate) {
        Optional<BankEntity> bank = bankRepository.findAll().stream().filter(b -> associate.getBankNumber() != null && associate.getBankNumber().startsWith(b.getPrefix())).findFirst();
        if(bank.isPresent())
            return bank.get().getName();
        else
            return associate.getBankName();
    }

    public String generateDecision(String fileTitle, ScriptEnum scriptEnum, boolean isExtern) throws IOException, InvalidFormatException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XWPFDocument document = new XWPFDocument();

        createHeader(document, scriptEnum);


        addDecisionNumber(document, scriptEnum, isExtern);
        addDate(document, "08.11.2024.", scriptEnum);
        addEmptyLine(document);

        setFirstParagraphForIntroduction(document, scriptEnum);
        setCenteredTitle(document, scriptEnum, "О Д Л У К У");
        setSecondParagraphForIntroduction(document, scriptEnum, isExtern);
        setCenteredTitle(document, scriptEnum, "1.");
        setThirdParagraphForIntroduction(document, scriptEnum);
        setFourthParagraphForIntroduction(document, scriptEnum);

        Map<Integer, String> arabianToLatinNumbers = new HashMap<>();
        arabianToLatinNumbers.put(1, "I");
        arabianToLatinNumbers.put(2, "II");
        arabianToLatinNumbers.put(3, "III");
        arabianToLatinNumbers.put(4, "IV");
        arabianToLatinNumbers.put(5, "V");
        arabianToLatinNumbers.put(6, "VI");
        arabianToLatinNumbers.put(7, "VII");
        arabianToLatinNumbers.put(8, "VIII");
        arabianToLatinNumbers.put(9, "IX");
        arabianToLatinNumbers.put(10, "X");

        List<AssociateStatusEntity> statuses = associateStatusRepository.findAll().stream().filter(status -> associateRepository.findAllByIsExternAndStatus_Id(isExtern, status.getId()).stream().count() > 0).toList();
        int orderNumber = 1;
        for (AssociateStatusEntity status : statuses) {
            setStatusLabel(document, scriptEnum, arabianToLatinNumbers.get(orderNumber), status.getName());
            addMainTable(document, scriptEnum, status, isExtern);
            orderNumber++;
        }

        setCenteredTitle(document, scriptEnum, "2.");
        setFifthParagraphForIntroduction(document, scriptEnum);
        setCenteredTitle(document, scriptEnum, "3.");
        setSixthParagraphForIntroduction(document, scriptEnum);

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

    private void addDecisionNumber(XWPFDocument document, ScriptEnum scriptEnum, boolean isExtern) {
        String label = "Број: ";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(label) : cyrillicToLatinConverter.convert(label);
        if(isExtern)
            text += "01-03-1/24 - 203.";
        else
            text += "01-03-1/24 - 204.";
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

    public void addEmptyLine(XWPFDocument document) {
        document.createParagraph();
    }

    public void setFirstParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum) {
        String resultLabel = "Na osnovu ovlaštenja iz Izbornog zakona BiH („Službeni glasnik BiH br. br.23/01, 7/02, 9/02, 20/02, 25/02, 4/04, 20/04, 25/05, 52/05, 65/05, 77/05, 11/06, 24/06, 32/07, 33/08, 37/08, 32/10, 18/13, 17/14, 31/16,41/20,38 /22, 51/22, 67/22 i 24/24), podzakonskih akata Centralne izborne komisije Bosne i Hercegovine i člana 19. Poslovnika o radu Gradske izborne komisije Banja Luka (broj: 07-03-1/16-64. od 08.07.2016. godine), Gradska izborna komisija Banja Luka je, na sjednici održanoj 08.11. 2022. godine, zaključila je i donijela";
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

    public void setStatusLabel(XWPFDocument document, ScriptEnum scriptEnum, String latinNumber, String text) {
        String translatedText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(text) : cyrillicToLatinConverter.convert(text);
        String resultText = latinNumber + " - " + translatedText;

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    private void setCenteredTitle(XWPFDocument document, ScriptEnum scriptEnum, String label) {
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(true);
        run.setFontSize(12);
    }

    public void setSecondParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum, boolean isExtern) {
        String resultLabel;
        if(isExtern)
            resultLabel = "o angažovanju potrebnog broja pojedinaca – vanjskih saradnika za potrebe stručne,operativne, administrativne i tehničke podrške u pripremi i provođenju Lokalnih izbora 06. oktobra 2024. godine na području OIJ 034 B - Banja Luka";
        else
            resultLabel = "o angažovanju potrebnog broja pojedinca – službenika i namještenika Gradske uprave Grada Banja Luka za potrebe stručne, operativne, administrativne i tehničke podrške u pripremi i provođenju Lokalnih izbora 06. oktobra 2024. godine na području OIJ 034 B - Banja Luka";
        String resultText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(resultLabel) : cyrillicToLatinConverter.convert(resultLabel);

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(720);
        removeSpacing(paragraph);
        XWPFRun run = paragraph.createRun();
        run.setText(resultText);
        run.setBold(true);
        run.setFontSize(10);

        addEmptyLine(document);
    }

    public void setThirdParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum) {
        String resultLabel = "Gradska izborna komisija Banja Luka, u skladu sa ovlaštenjima, odgovornostima i rokovima za pripremu i provođenje svih nivoa izbora ovom odlukom – u izbornom periodu, uključujući okončanje svih neophodnih aktivnosti po provođenju Lokalnih izbora, angažovala je potreban broj službenika i namještenika – uposlenika Gradske uprave Banja Luka. Angažovanje je vršeno u skladu sa izbornim rokovima, i to za: obezbjeđivanje uslova za glasanje 193 295 birača na 262 lokacija redovnih biračkih mjesta; za rad 262 biračkih odbora i 25 mobilnih timova; imenovanje, obuku i testiranje sveukupno 2674 pojedinca (pozicije) u b/o i m/t, od čega su 287 predsjednika i 287 zamjenika predsjednika u preko 150 termina obuka i testiranja na lokacijama obuka; za provjeru podataka i izradu akrekditacija za preko 7000 posmatrača; provjere podataka kroz bazu birača i za biračke odbore i za posmatrače, unos podataka u eksel tabele i JIS sistem CIK BiH; provjere podataka kroz baze za prijedloge za biračke odbore i za posmatrače; obavještavanje za obuke; priprema, izrada, selekcija, izdavanje, distribucija i prijem – sveukupnog izbornog matrijala (osjetljivi i neosjetljivi); obezbjeđivanje uslova za obuku; izrada rješenja i izmjena rješenja o imenovanju b/o i m/t; rad na info-linijama; obrada izbornih podataka i rezultata; rad u timovima za izdavanje i prijem izbornog materijala; 2 tima za ponovno otvaranje vreća i pravilno utvrđivanje i objedinjavanje rezultata; domaćini objekata - biračkih mjesta; fizički poslovi; vozači, vozila i drugo neophodno.";
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

    public void setFourthParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum) {
        String resultLabel = "Prema izbornim i postizbornim aktivnostima, Komisija je angažovanje izvršila u periodu od 01.06. do 10.11.2022.godine, kako slijedi:";
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

    public void setFifthParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum) {
        String resultLabel = "Odluka Gradske izborne komisije je podloga za nastale obaveze prema angažovanim vanjskim saradnicima, po aktivnostima i satnici angažovanja za sačinjavanje ugovora o djelu.";
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

    public void setSixthParagraphForIntroduction(XWPFDocument document, ScriptEnum scriptEnum) {
        String resultLabel = "Odluka će biti dostavljena Odjeljenju za finansije Gradske uprave Banja Luka, Poreskoj upravi RS unosom u JS PU RS, u popis akata i evidenciju Gradske izborne komisije.";
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

    private void addMainTable(XWPFDocument document, ScriptEnum scriptEnum, AssociateStatusEntity status, boolean isExtern) {
        XWPFTable table = document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);
        table.setWidthType(TableWidthType.PCT);
        table.setWidth("70%");


        XWPFTableRow row = table.getRow(0);
        row.setHeight(200);
        String orderNumberHeaderLabel = "R.B.".toUpperCase();
        String orderNumberHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(orderNumberHeaderLabel) : orderNumberHeaderLabel;
        row.getCell(0).setText(orderNumberHeaderText);

        String lastnameHeaderLabel = "Prezime i ime".toUpperCase();
        String lastnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(lastnameHeaderLabel) : lastnameHeaderLabel;
        row.addNewTableCell().setText(lastnameHeaderText);

        String firstnameHeaderLabel = "JMBG".toUpperCase();
        String firstnameHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(firstnameHeaderLabel) : firstnameHeaderLabel;
        row.addNewTableCell().setText(firstnameHeaderText);
        styleHeaderRow(table.getRow(0));

        Collator collator = getCollatorForScript(scriptEnum);

        List<AssociateEntity> associates = associateRepository.findAllByIsExternAndStatus_Id(isExtern, status.getId());

        if(scriptEnum == ScriptEnum.CYRILLIC)
            associates.forEach(a -> {
                a.setLastname(latinToCyrillicConverter.convert(a.getLastname()).toUpperCase());
                a.setFirstname(latinToCyrillicConverter.convert(a.getFirstname()).toUpperCase());
            });
        List<AssociateEntity> sortedAssociates = associates.stream()
                .sorted(Comparator.comparing(AssociateEntity::getLastname, collator)
                        .thenComparing(AssociateEntity::getFirstname, collator))
                .collect(Collectors.toList());

        int i = 0;
        for(AssociateEntity associate: sortedAssociates) {
            XWPFTableRow newRow = table.createRow();

            newRow.getCell(0).setText((i+1) + ".");
            newRow.getCell(1).setText(associate.getLastname() + " " + associate.getFirstname());
            newRow.getCell(2).setText(associate.getJmbg());
            styleDataRow(newRow, i);
            i++;
        }

        addEmptyLine(document);
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

    private Collator getCollatorForScript(ScriptEnum scriptEnum) {
        Locale.Builder localeBuilder = new Locale.Builder().setLanguage("sr").setRegion("RS");
        if (scriptEnum == ScriptEnum.CYRILLIC) {
            localeBuilder.setScript("Cyrl");
        }
        Locale locale = localeBuilder.build();
        Collator collator = Collator.getInstance(locale);
        return collator;
    }
}
