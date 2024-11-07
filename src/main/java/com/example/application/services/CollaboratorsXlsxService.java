package com.example.application.services;

import com.example.application.entities.AssociateEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.PresidentEntity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class CollaboratorsXlsxService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;

    private final BankRepository bankRepository;
    private final AssociateRepository associateRepository;
    private final AssociateStatusRepository associateStatusRepository;

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

    public String generateReportForBanks(String fileTitle, ScriptEnum scriptEnum) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();


        return saveDocument(fileTitle, workbook);
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

    public String generateDecision(String fileTitle, ScriptEnum scriptEnum, boolean isExtern) throws IOException, InvalidFormatException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XWPFDocument document = new XWPFDocument();
        createHeader(document, scriptEnum);

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

        for(AssociateEntity associate: associates) {
            generateContractByAssociate(document, associate, value);
            document.add(new AreaBreak());
            generateReportByAssociate(document, associate, value);
            document.add(new AreaBreak());
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

    private void generateContractByAssociate(Document document, AssociateEntity associate, ScriptEnum scriptEnum) throws IOException {
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

        String description = "Izvršilac posla se obavezuje da po nalogu Naručioca posla radi administrativno tehničke poslove u pripremi i provođenju Lokalnih  izbora 2024. godine.";
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
            description = "Naručilac posla se obavezuje da će ugovoreni iznos uplatiti na tekući račun Izvršioca posla broj " + associate.getBankNumber() + ", otvoren kod " + associate.getBankName() + ", a tereteći potrošačku jedinicu 2002111 – GIK Banja Luka, stavku 412900 (0160) – tehnička priprema i provođenje izbora.";
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
        description = "Broj: 01-03-1/22-541-1";
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
    private void generateReportByAssociate(Document document, AssociateEntity associate, ScriptEnum scriptEnum) throws IOException {
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

        String firstParagraph = "Ugovorom o djelu broj 01-03-1/22-541-" + associate.getId() + "   od dana  " + dateFormatter.format(associate.getContractDate()) + " godine, koji su u Banjaluci zaključili Naručilac posla Gradska izborna komisija Banja Luka, koju zastupa predsjednik Dubravko Malinić i  " + associate.getLastname() + " " + associate.getFirstname() + ", iz opštine " + associate.getResidence() + ", na period od " + dateFormatter.format(associate.getStartDate()) + " do " + dateFormatter.format(associate.getEndDate()) + " godine, " + associate.getLastname() + " " + associate.getFirstname() + " se, kao ugovorna strana-angažovano lice, obavezao da će za potrebe naručioca posla vršiti administrativno tehničke poslove.";
        addLawArticleDescription(document, arialFont, scriptEnum, firstParagraph);
        addSmallEmptyRow(document);

        String secondParagraph = "Shodno navedenom ugovoru Naručilac posla obavezao se angažovanom licu isplatiti iznos " + String.format(Locale.getDefault(), "%.2f", associate.getPrice()).replace('.', ',') + " KM za obavljeni posao, a po prethodno pregledanom i odobrenom izvještaju od strane Gradske izborne komisije Banja Luka, u ime Naručioca posla.";
        addLawArticleDescription(document, arialFont, scriptEnum, secondParagraph);
        addSmallEmptyRow(document);

        String thirdParagraph = "S tim u vezi angažovano lice podnosi izvještaj da je preuzete poslove obavilo u predviđenom roku od " + dateFormatter.format(associate.getStartDate()) + " do " + dateFormatter.format(associate.getEndDate()) + " godine. U navedenom izvještajnom periodu angažovani je obavljao administrativno tehničke poslove.";
        addLawArticleDescription(document, arialFont, scriptEnum, thirdParagraph);
        addSmallEmptyRow(document);

        String endParagraph = "Banja Luka, " + dateFormatter.format(associate.getReportDate());
        addLawArticleDescription(document, arialFont, scriptEnum, endParagraph);
        addSmallEmptyRow(document);
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
}
