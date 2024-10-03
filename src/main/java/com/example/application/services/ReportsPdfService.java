package com.example.application.services;


import com.example.application.entities.ConstraintEntity;
import com.example.application.entities.MemberEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.VotingCouncelRepository;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportsPdfService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";

    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private final VotingCouncelRepository votingCouncelRepository;
    private final PresidentRepository presidentRepository;
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

    public String generateReportByCode(VotingCouncelEntity entity, String fileTitle, ScriptEnum value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        //set margins
        float topMargin = 45f;
        float leftMargin=52f;
        float rightMargin=52f;
        document.setTopMargin(topMargin);
        document.setLeftMargin(leftMargin);
        document.setRightMargin(rightMargin);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);

        if("034".equals(entity.getCode()))
        {
            for(VotingCouncelEntity votingCouncel: votingCouncelRepository.findAll().stream().sorted(Comparator.comparing(VotingCouncelEntity::getId)).collect(Collectors.toList())){
                generateContentByCode(votingCouncel, document, value);
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        else
            generateContentByCode(entity, document, value);

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

    private void generateContentByCode(VotingCouncelEntity entity, Document document, ScriptEnum scriptEnum) throws IOException {
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        Style headerBoldStyle = new Style();
        headerBoldStyle.setFont(arialFont)
                .setFontSize(10.5f)
                .setMargin(0)
                .setPadding(0)
                .setCharacterSpacing(0.25f)
                .setBold()
                .setTextAlignment(TextAlignment.JUSTIFIED);

        String entityLabel = "Р Е П У Б Л И К А   С Р П С К А";
        String entityText = scriptEnum == ScriptEnum.CYRILLIC ? entityLabel : cyrillicToLatinConverter.convert(entityLabel);
        Paragraph entityParagraph = new Paragraph(entityText)
                .addStyle(headerBoldStyle).setFixedLeading(13);

        String cityLabel = "ГРАД БАЊА ЛУКА";
        String cityText = scriptEnum == ScriptEnum.CYRILLIC ? cityLabel : cyrillicToLatinConverter.convert(cityLabel);
        Paragraph cityParagraph = new Paragraph(cityText)
                .addStyle(headerBoldStyle)
                .setFixedLeading(13)
                .setMarginLeft(30f);

        String councelLabel = "ГРАДСКА ИЗБОРНА КОМИСИЈА";
        String councelText = scriptEnum == ScriptEnum.CYRILLIC ? councelLabel : cyrillicToLatinConverter.convert(councelLabel);
        Paragraph councelParagraph = new Paragraph(councelText)
                .addStyle(headerBoldStyle)
                .setFixedLeading(13);

        document.add(entityParagraph);
        document.add(cityParagraph);
        document.add(councelParagraph);

        Style headerStyle = new Style();
        headerStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        String decisionNumberLabel = "Број: 01-03-1/24-165-" + entity.getId() + ".";
        String decisionNumberText = scriptEnum == ScriptEnum.CYRILLIC ? decisionNumberLabel : cyrillicToLatinConverter.convert(decisionNumberLabel);
        Paragraph decisionNumberParagraph = new Paragraph(decisionNumberText)
                .addStyle(headerStyle)
                .setFixedLeading(13);

        String dateHeaderLabel = "Дана, 06.10.2024. године. ";
        String dateHeaderText = scriptEnum == ScriptEnum.CYRILLIC ? dateHeaderLabel : cyrillicToLatinConverter.convert(dateHeaderLabel);
        Paragraph dateHeaderParagraph = new Paragraph(dateHeaderText)
                .addStyle(headerStyle)
                .setFixedLeading(13);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(10));
        document.add(decisionNumberParagraph);
        document.add(dateHeaderParagraph);

        Style introduceTextStyle = new Style();
        introduceTextStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        String introduceLabel = "На основу члана 2.13 тачке 2. и 3., члана 2.19 и члана 5.3 став 1. Изборног закона БиХ („Службени гласник БиХ бр. 23/01, 7/02, 9/02, 20/02, 25/02, 4/04, 20/04, 25/05, 52/05, 65/05, 77/05, 11/06, 24/06, 32/07, 33/08, 37/08, 32/10, 18/13, 7/14, 31/16, 41/20, 38/22, 51/22, 67/22, 24/24 и 31/24), Градска изборна комисија Бања Лука, на сједници одржаној дана 02.10.2024. године, донијела је";
        String introduceText = scriptEnum == ScriptEnum.CYRILLIC ? introduceLabel : cyrillicToLatinConverter.convert(introduceLabel);
        Paragraph introduceParagraph = new Paragraph(introduceText).addStyle(introduceTextStyle).setFirstLineIndent(30).setFixedLeading(13);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(13));
        document.add(introduceParagraph);

        Style titleTextStyle = new Style();
        titleTextStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setMarginLeft(30)
                .setMarginRight(30)
                .setCharacterSpacing(0.25f)
                .setPadding(0)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        String solutionTitleLabel = "Р Ј Е Ш Е Њ Е";
        String solutionTitleText = scriptEnum == ScriptEnum.CYRILLIC ? solutionTitleLabel : cyrillicToLatinConverter.convert(solutionTitleLabel);
        Paragraph solutionTitleParagraph = new Paragraph(solutionTitleText).addStyle(titleTextStyle).setFixedLeading(13);

        String solutionDetailsLabel = "о именовању чланова бирачких одбора и њихових замјеника за Локалне изборе – 06. октобра, 2024. године";
        String solutionDetailsText = scriptEnum == ScriptEnum.CYRILLIC ? solutionDetailsLabel : cyrillicToLatinConverter.convert(solutionDetailsLabel);
        Paragraph solutionDetailsParagraph = new Paragraph(solutionDetailsText).addStyle(titleTextStyle).setFixedLeading(13);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(13));
        document.add(solutionTitleParagraph);
        document.add(solutionDetailsParagraph);

        Style votingCouncelFirstPartDetailsStyle = new Style();
        votingCouncelFirstPartDetailsStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        Style votingCouncelSecondPartDetailsStyle = new Style();
        votingCouncelSecondPartDetailsStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setBold()
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        String votingCouncelFirstPartDetailsLabel = createCouncelDetailsFirstPartForSolutionReport(entity);
        String votingCouncelFirstPartDetailsText = scriptEnum == ScriptEnum.CYRILLIC ? votingCouncelFirstPartDetailsLabel : cyrillicToLatinConverter.convert(votingCouncelFirstPartDetailsLabel);
        Text firstPartText = new Text(votingCouncelFirstPartDetailsText);
        firstPartText.addStyle(votingCouncelFirstPartDetailsStyle);

        String votingCouncelSecondPartDetailsLabel = createCouncelDetailsSecondPartForSolutionReport(entity);
        String votingCouncelSecondPartDetailsText = scriptEnum == ScriptEnum.CYRILLIC ? votingCouncelSecondPartDetailsLabel : cyrillicToLatinConverter.convert(votingCouncelSecondPartDetailsLabel);
        Text secondPartText = new Text(votingCouncelSecondPartDetailsText);
        secondPartText.addStyle(votingCouncelSecondPartDetailsStyle);

        Paragraph votingCouncelDetails = new Paragraph();
        votingCouncelDetails.add(firstPartText);
        votingCouncelDetails.add(secondPartText);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(13));
        document.add(votingCouncelDetails);

        Style membersLabelStyle = new Style();
        membersLabelStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setMarginLeft(30)
                .setPadding(0)
                .setBold()
                .setCharacterSpacing(0.25f)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        String membersLabel = "Чланови бирачког одбора:";
        String membersText = scriptEnum == ScriptEnum.CYRILLIC ? membersLabel : cyrillicToLatinConverter.convert(membersLabel);
        Paragraph members = new Paragraph(membersText).addStyle(membersLabelStyle).setFixedLeading(13);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(11));
        document.add(members);


        float[] columnWidths = {11, 46, 6, 20, 17}; // Custom column widths
        Table membersTable = new Table(UnitValue.createPercentArray(columnWidths));
        membersTable.setWidth(UnitValue.createPercentValue(100)); // Set table width

        for(MemberEntity member: entity.getConstraints().stream().filter(c -> c.getTitle().getId() == 1).sorted(Comparator.comparing(ConstraintEntity::getPosition)).map(ConstraintEntity::getMember).collect(Collectors.toList()))
            createMemberRowInTable(member, membersTable, arialFont, scriptEnum);
        document.add(membersTable);

        String membersSubstitutesLabel = "Замјеници чланова бирачког одбора:";
        String membersSubstitutesText = scriptEnum == ScriptEnum.CYRILLIC ? membersSubstitutesLabel : cyrillicToLatinConverter.convert(membersSubstitutesLabel);
        Paragraph membersSubstitutes = new Paragraph(membersSubstitutesText).addStyle(membersLabelStyle).setFixedLeading(13);

        Table membersSubtitutesTable = new Table(UnitValue.createPercentArray(columnWidths));
        membersSubtitutesTable.setWidth(UnitValue.createPercentValue(100)); // Set table width

        for(MemberEntity member: entity.getConstraints().stream().filter(c -> c.getTitle().getId() == 2).sorted(Comparator.comparing(ConstraintEntity::getPosition)).map(ConstraintEntity::getMember).collect(Collectors.toList()))
            createMemberRowInTable(member, membersSubtitutesTable, arialFont, scriptEnum);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(11));
        document.add(membersSubstitutes);
        document.add(membersSubtitutesTable);

        String secondParagraphTitleLabel = "II – Дужности чланова и замјеника чланова бирачких одбора су:";
        String secondParagraphTitleText = scriptEnum == ScriptEnum.CYRILLIC ? secondParagraphTitleLabel : cyrillicToLatinConverter.convert(secondParagraphTitleLabel);

        Style secondParagraphTitleStyle = new Style();
        secondParagraphTitleStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        Paragraph secondParagraphTitle = new Paragraph(secondParagraphTitleText);
        secondParagraphTitle.addStyle(secondParagraphTitleStyle).setFixedLeading(11);
        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(14));
        document.add(secondParagraphTitle);

        List<String> secondParagraphItemsLabels = new ArrayList<>();
        if(this.isRegularTeam(entity)) {
            secondParagraphItemsLabels.add("да су обучени за рад у бирачким одборима;");
            secondParagraphItemsLabels.add("да дана 05. октобра, 2024. године преузму изборни материјал на локацији дистрибуције и припреме бирачко мјесто (предсједник и чланови б/о) на локацији бирачког мјеста;");
            secondParagraphItemsLabels.add("да се бирачки материјал у ноћи прије отварања бирачког мјеста чува у закључаним просторијама, на локацији бирачког мјеста. Полиција осигурава бирачки материјал у ноћи прије отварања бирачких мјеста на локацијама бирачких мјеста;");
            secondParagraphItemsLabels.add("да на Дан избора – 06. октобра, 2024. године, предсједник и чланови б/о дођу на бирачко мјесто најкасније у 06.00 сати ујутро;");
            secondParagraphItemsLabels.add("да Градској изборној комисији достављају тражене извјештаје на телефоне и у сатницама наведеним у посебном подсјетнику (07.00, 11.00, 15.00 и 19.00 часова);");
            secondParagraphItemsLabels.add("да тачно, прецизно и са пуном одговорношћу попуне све пратеће обрасце и изборни материјал спакују према шеми паковања;");
            secondParagraphItemsLabels.add("да, у комплетном процесу провођења гласања и пребројавања гласова, поступају непристрасно, професионално и стручно - према Изборном закону БиХ и да, након завршетка изборног процеса и пребројавања гласова, комплетан изборни материјал са бирачких мјеста доставе овој Комисији у објекат Спортске дворане \"Центар.\"");
        } else {
            secondParagraphItemsLabels.add("да су обучени за рад у мобилним тимовима;");
            secondParagraphItemsLabels.add("да дана 06. октобра, 2024. године преузму изборни материјал на локацији дистрибуције – објекат Градске управе Бања Лука;");
            secondParagraphItemsLabels.add("да на Дан избора – 06. октобра, 2024. године, предсједник и чланови мобилних тимова преузму правце обиласка и обиђу све бираче са допунског списка;");
            secondParagraphItemsLabels.add("да предсједници попуне Евиденцију рада на Дан избора са свим траженим подацима;");
            secondParagraphItemsLabels.add("да Градској изборној комисији достављају тражене извјештаје на телефоне и у сатницама наведеним у посебном подсјетнику (07.00, 11.00, 15.00 и 19.00 часова);");
            secondParagraphItemsLabels.add("да, по завршетку обиласка бирача, сав изборни материјал спакују према шеми паковања објекту Градске управе и, по том, доставе Градској изборној комисији;");
            secondParagraphItemsLabels.add("да тачно, прецизно и са пуном одговорношћу попуне све пратеће обрасце;");
            secondParagraphItemsLabels.add("да, у комплетном процесу провођења гласања, поступају непристрасно, професионално и стручно - према Изборном закону БиХ.");
        }
        List<String> secondParagraphItemsTexts = new ArrayList<>();
        for(String label: secondParagraphItemsLabels)
            secondParagraphItemsTexts.add(scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label));

        Style secondParagraphItemsStyle = new Style();
        secondParagraphItemsStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setMarginLeft(30)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        com.itextpdf.layout.element.List itext7List = new com.itextpdf.layout.element.List();
        itext7List.addStyle(secondParagraphItemsStyle);
        for(String text: secondParagraphItemsTexts)
            itext7List.add(text);

        document.add(itext7List);

        Style footerBoldStyle = new Style();
        footerBoldStyle.setFont(arialFont)
                .setFontSize(10.5f)
                .setMargin(0)
                .setPadding(0)
                .setCharacterSpacing(0.25f)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);

        String footerTitleLabel = "ПРЕДСЈЕДНИК";
        String footerTitleText = scriptEnum == ScriptEnum.CYRILLIC ? footerTitleLabel : cyrillicToLatinConverter.convert(footerTitleLabel);
        Paragraph footerTitle = new Paragraph(footerTitleText);
        footerTitle.addStyle(footerBoldStyle);
        if(scriptEnum == ScriptEnum.CYRILLIC)
            footerTitle.setMarginRight(47);
        else
            footerTitle.setMarginRight(38);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(10));
        document.add(footerTitle);

        String footerValueLabel = "Дубравко Малинић";
        String footerValueText = scriptEnum == ScriptEnum.CYRILLIC ? footerValueLabel : cyrillicToLatinConverter.convert(footerValueLabel);
        Paragraph footerValue = new Paragraph(footerValueText);
        footerValue.addStyle(footerBoldStyle);
        footerValue.setMarginRight(30);

        document.add(new Paragraph("\n").addStyle(headerStyle).setFixedLeading(10));
        document.add(footerValue);
    }

    private void createMemberRowInTable(MemberEntity member, Table table, PdfFont arialFont, ScriptEnum scriptEnum) {
        Border border = new GrooveBorder(new DeviceRgb(0,0,0), 1);
        Cell cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);


        Style style = new Style();
        style.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0);

        if(member == null) {
            for(int i = 0; i<5; i++) {
                cell = new Cell();
                cell.setBorder(border);
                cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
                cell.setTextAlignment(TextAlignment.CENTER);
                cell.setHeight(13);
                table.addCell(cell);
            }
            return;
        }

        String label = member.getConstraint().getPoliticalOrganization().getCode();
        String text = label;
        Paragraph paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        cell.add(paragraph);
        table.addCell(cell);

        if(member.getIsGik() != null)
            label = (member.getIsGik() ? "*" : "") + member.getFullname().toUpperCase();
        else
            label = member.getFullname().toUpperCase();
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell fullNameCell = new Cell();
        fullNameCell.setBorder(border);
        fullNameCell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        fullNameCell.add(paragraph);
        table.addCell(fullNameCell);

        if(member.getIsMale() != null)
            label = member.getIsMale() ? "М" : "Ж";
        else
            label = "";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell genderCell = new Cell();
        genderCell.setBorder(border);
        genderCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        genderCell.setTextAlignment(TextAlignment.CENTER);
        genderCell.add(paragraph);
        table.addCell(genderCell);

        label = member.getJmbg();
        if(member.getJmbg() != null)
            paragraph = new Paragraph(label);
        else
            paragraph = new Paragraph("");
        paragraph.addStyle(style).setFixedLeading(12);

        Cell jmbgCell = new Cell();
        jmbgCell.setBorder(border);
        jmbgCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        jmbgCell.setTextAlignment(TextAlignment.CENTER);
        jmbgCell.add(paragraph);
        table.addCell(jmbgCell);

        if(member.getPhoneNumber() != null)
            label = member.getPhoneNumber();
        else
            label="";
        text = formatPhoneNumber(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell phoneNumberCell = new Cell();
        phoneNumberCell.setBorder(border);
        phoneNumberCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        phoneNumberCell.setTextAlignment(TextAlignment.CENTER);
        phoneNumberCell.add(paragraph);
        table.addCell(phoneNumberCell);
    }

    private String createCouncelDetailsFirstPartForSolutionReport(VotingCouncelEntity entity) {
        return "I – На бирачко мјесто " +
                entity.getCode() +
                ", ";
    }

    private String createCouncelDetailsSecondPartForSolutionReport(VotingCouncelEntity entity) {
        return entity.getName() +
                ", " +
                entity.getLocation() +
                ", " +
                "број бирача " +
                entity.getNumberOfVoters() +
                ", именује се бирачки одбор у саставу:";
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

    public String generateShortReportByCode(VotingCouncelEntity entity, String fileTitle, ScriptEnum value) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        //set margins
        float topMargin = 25f;
        float leftMargin=52f;
        float rightMargin=52f;
        document.setTopMargin(topMargin);
        document.setLeftMargin(leftMargin);
        document.setRightMargin(rightMargin);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);

        if("034".equals(entity.getCode()))
        {
            for(VotingCouncelEntity votingCouncel: votingCouncelRepository.findAll().stream().sorted(Comparator.comparing(VotingCouncelEntity::getId)).collect(Collectors.toList())){
                generateShortContentByCode(votingCouncel, document, value);
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        else if("034MT".equals(entity.getCode()) || "034МТ".equals(entity.getCode()))
        {
            for(VotingCouncelEntity votingCouncel: votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("MT") || vc.getCode().contains("МТ")).sorted(Comparator.comparing(VotingCouncelEntity::getId)).collect(Collectors.toList())){
                generateShortContentByCode(votingCouncel, document, value);
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        else
            generateShortContentByCode(entity, document, value);

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

    private void generateShortContentByCode(VotingCouncelEntity entity, Document document, ScriptEnum scriptEnum) throws IOException {
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        Style titleBoldStyle = new Style();
        titleBoldStyle.setFont(arialFont)
                .setFontSize(16)
                .setMargin(0)
                .setPadding(0)
                .setCharacterSpacing(0.25f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        String titleLabel = "Списак чланова бирачког одбора";
        String titleText = scriptEnum == ScriptEnum.CYRILLIC ? titleLabel : cyrillicToLatinConverter.convert(titleLabel);
        Paragraph titleParagraph = new Paragraph(titleText)
                .addStyle(titleBoldStyle).setFixedLeading(17);

        document.add(titleParagraph);

        Style votingCouncelStyle = new Style();
        votingCouncelStyle.setFont(arialFont)
                .setFontSize(16)
                .setMargin(0)
                .setPadding(0)
                .setCharacterSpacing(0.25f)
                .setUnderline()
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        String votingCouncelCodeLabel = entity.getCode();
        String votingCouncelCodeText = scriptEnum == ScriptEnum.CYRILLIC ? votingCouncelCodeLabel : cyrillicToLatinConverter.convert(votingCouncelCodeLabel);
        Paragraph votingCouncelCodeParagraph = new Paragraph(votingCouncelCodeText)
                .addStyle(votingCouncelStyle).setFixedLeading(17);

        Style emptyRowStyle = new Style();
        emptyRowStyle.setFont(arialFont)
                .setFontSize(10)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.JUSTIFIED);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(votingCouncelCodeParagraph);

        Style labelStyle = new Style();
        labelStyle.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        Style descriptionStyle = new Style();
        descriptionStyle.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.CENTER);

        String codeDescriptionLabel = "(Број БМ)";
        String codeDescriptionText = scriptEnum == ScriptEnum.CYRILLIC ? codeDescriptionLabel : cyrillicToLatinConverter.convert(codeDescriptionLabel);
        Paragraph codeDescriptionParagraph = new Paragraph(codeDescriptionText)
                .addStyle(descriptionStyle).setFixedLeading(12);

        document.add(codeDescriptionParagraph);

        String presidentLabel = "ПРЕДСЈЕДНИК";
        String presidentText = scriptEnum == ScriptEnum.CYRILLIC ? presidentLabel : cyrillicToLatinConverter.convert(presidentLabel);
        Paragraph presidentParagraph = new Paragraph(presidentText)
                .addStyle(labelStyle).setFixedLeading(12);

        String presidentDescriptionLabel = "(Именован од Централне изборне комисије БиХ)";
        String presidentDescriptionText = scriptEnum == ScriptEnum.CYRILLIC ? presidentDescriptionLabel : cyrillicToLatinConverter.convert(presidentDescriptionLabel);
        Paragraph presidentDescriptionParagraph = new Paragraph(presidentDescriptionText)
                .addStyle(descriptionStyle).setFixedLeading(12);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(presidentParagraph);
        document.add(presidentDescriptionParagraph);
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));


        float[] columnWidthsForPresidentTable = {11, 89}; // Custom column widths
        Table presidentTable = new Table(UnitValue.createPercentArray(columnWidthsForPresidentTable));
        presidentTable.setWidth(UnitValue.createPercentValue(100)); // Set table width
        createHeaderInTableForShortReportForPresident(presidentTable, arialFont, scriptEnum);


        createPresidentRowInTableForShortReport(entity, presidentTable, arialFont, scriptEnum, true);
        document.add(presidentTable);

        String memberLabel = "ЧЛАНОВИ";
        String memberText = scriptEnum == ScriptEnum.CYRILLIC ? memberLabel : cyrillicToLatinConverter.convert(memberLabel);
        Paragraph memberParagraph = new Paragraph(memberText)
                .addStyle(labelStyle).setFixedLeading(12);

        String memberDescriptionLabel = "(Именовани од стране ИК ОИЈ)";
        String memberDescriptionText = scriptEnum == ScriptEnum.CYRILLIC ? memberDescriptionLabel : cyrillicToLatinConverter.convert(memberDescriptionLabel);
        Paragraph memberDescriptionParagraph = new Paragraph(memberDescriptionText)
                .addStyle(descriptionStyle).setFixedLeading(12);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(memberParagraph);
        document.add(memberDescriptionParagraph);
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));

        float[] columnWidths = {11, 30, 59}; // Custom column widths
        Table membersTable = new Table(UnitValue.createPercentArray(columnWidths));
        membersTable.setWidth(UnitValue.createPercentValue(100)); // Set table width
        createHeaderInTableForShortReport(membersTable, arialFont, scriptEnum);

        for(MemberEntity member: entity.getConstraints().stream().filter(c -> c.getTitle().getId() == 1).sorted(Comparator.comparing(ConstraintEntity::getPosition)).map(ConstraintEntity::getMember).collect(Collectors.toList()))
            createMemberRowInTableForShortReport(member, membersTable, arialFont, scriptEnum);
        document.add(membersTable);

        String presidentSubstituteLabel = "ЗАМЈЕНИК ПРЕДСЈЕДНИКА";
        String presidentSubstituteText = scriptEnum == ScriptEnum.CYRILLIC ? presidentSubstituteLabel : cyrillicToLatinConverter.convert(presidentSubstituteLabel);
        Paragraph presidentSubstituteParagraph = new Paragraph(presidentSubstituteText)
                .addStyle(labelStyle).setFixedLeading(12);

        String presidentSubstituteDescriptionLabel = "(Именован од Централне изборне комисије БиХ)";
        String presidentSubstituteDescriptionText = scriptEnum == ScriptEnum.CYRILLIC ? presidentSubstituteDescriptionLabel : cyrillicToLatinConverter.convert(presidentSubstituteDescriptionLabel);
        Paragraph presidentSubstituteDescriptionParagraph = new Paragraph(presidentSubstituteDescriptionText)
                .addStyle(descriptionStyle).setFixedLeading(12);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(presidentSubstituteParagraph);
        document.add(presidentSubstituteDescriptionParagraph);
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));

        columnWidthsForPresidentTable = new float[]{11, 89}; // Custom column widths
        presidentTable = new Table(UnitValue.createPercentArray(columnWidthsForPresidentTable));
        presidentTable.setWidth(UnitValue.createPercentValue(100)); // Set table width
        createHeaderInTableForShortReportForPresident(presidentTable, arialFont, scriptEnum);

        createPresidentRowInTableForShortReport(entity, presidentTable, arialFont, scriptEnum, false);
        document.add(presidentTable);

        String memberSubstituteLabel = "ЗАМЈЕНИЦИ ЧЛАНОВА";
        String memberSubstituteText = scriptEnum == ScriptEnum.CYRILLIC ? memberSubstituteLabel : cyrillicToLatinConverter.convert(memberSubstituteLabel);
        Paragraph memberSubstituteParagraph = new Paragraph(memberSubstituteText)
                .addStyle(labelStyle).setFixedLeading(12);

        String memberSubstituteDescriptionLabel = "(Именовани од стране ИК ОИЈ)";
        String memberSubstituteDescriptionText = scriptEnum == ScriptEnum.CYRILLIC ? memberSubstituteDescriptionLabel : cyrillicToLatinConverter.convert(memberSubstituteDescriptionLabel);
        Paragraph memberSubstituteDescriptionParagraph = new Paragraph(memberSubstituteDescriptionText)
                .addStyle(descriptionStyle).setFixedLeading(12);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(memberSubstituteParagraph);
        document.add(memberSubstituteDescriptionParagraph);
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));

        columnWidths = new float[]{11, 30, 59}; // Custom column widths
        membersTable = new Table(UnitValue.createPercentArray(columnWidths));
        membersTable.setWidth(UnitValue.createPercentValue(100)); // Set table width
        createHeaderInTableForShortReport(membersTable, arialFont, scriptEnum);

        for(MemberEntity member: entity.getConstraints().stream().filter(c -> c.getTitle().getId() == 2).sorted(Comparator.comparing(ConstraintEntity::getPosition)).map(ConstraintEntity::getMember).collect(Collectors.toList()))
            createMemberRowInTableForShortReport(member, membersTable, arialFont, scriptEnum);
        document.add(membersTable);

        Style signatureStyle = new Style();
        descriptionStyle.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.RIGHT);

        String presidentSignatureLabel = "Предсједник ГИК";
        String presidentSignatureText = scriptEnum == ScriptEnum.CYRILLIC ? presidentSignatureLabel : cyrillicToLatinConverter.convert(presidentSignatureLabel);
        Paragraph presidentSignatureParagraph = new Paragraph(presidentSignatureText)
                .addStyle(descriptionStyle).setFixedLeading(12).setMarginRight(44).setBold();

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));
        document.add(presidentSignatureParagraph);

        document.add(new Paragraph("\n").addStyle(emptyRowStyle).setFixedLeading(10));

        presidentSignatureLabel = "___________________";
        presidentSignatureText = scriptEnum == ScriptEnum.CYRILLIC ? presidentSignatureLabel : cyrillicToLatinConverter.convert(presidentSignatureLabel);
        presidentSignatureParagraph = new Paragraph(presidentSignatureText)
                .addStyle(descriptionStyle).setFixedLeading(12).setMarginRight(30);
        document.add(presidentSignatureParagraph);

        presidentSignatureLabel = "(потпис)";
        presidentSignatureText = scriptEnum == ScriptEnum.CYRILLIC ? presidentSignatureLabel : cyrillicToLatinConverter.convert(presidentSignatureLabel);
        presidentSignatureParagraph = new Paragraph(presidentSignatureText)
                .addStyle(descriptionStyle).setFixedLeading(12).setMarginRight(60);
        document.add(presidentSignatureParagraph);

    }

    private void createPresidentRowInTableForShortReport(VotingCouncelEntity entity, Table table, PdfFont arialFont, ScriptEnum scriptEnum, boolean isPresident) {
        Border border = new GrooveBorder(new DeviceRgb(0,0,0), 1);
        Cell cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        Style style = new Style();
        style.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setTextAlignment(TextAlignment.CENTER);

        String label = "1.";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        Paragraph paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        Optional<PresidentEntity> presidentEntityOptional = presidentRepository.findByVotingCouncel_CodeAndIsPresident(entity.getCode(), isPresident);
        if(presidentEntityOptional.isEmpty() == false) {
            label = presidentEntityOptional.get().getFullname();
            text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
            paragraph = new Paragraph(text);
            paragraph.addStyle(style).setFixedLeading(12);
            cell.add(paragraph);
        }
        table.addCell(cell);
    }

    private void createHeaderInTableForShortReportForPresident(Table table, PdfFont arialFont, ScriptEnum scriptEnum) {
        Border border = new GrooveBorder(new DeviceRgb(0,0,0), 1);
        Cell cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        Style style = new Style();
        style.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        String label = "Р.Б.";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        Paragraph paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        label = "ИМЕ И ПРЕЗИМЕ";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);
    }

    private void createHeaderInTableForShortReport(Table table, PdfFont arialFont, ScriptEnum scriptEnum) {
        Border border = new GrooveBorder(new DeviceRgb(0,0,0), 1);
        Cell cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        Style style = new Style();
        style.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0)
                .setBold();

        String label = "Р.Б.";
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        Paragraph paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        label = "ИМЕ И ПРЕЗИМЕ";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);

        label = "ИМЕНОВАН НА ПРИЈЕДЛОГ";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);
        cell.add(paragraph);
        table.addCell(cell);
    }

    private void createMemberRowInTableForShortReport(MemberEntity member, Table table, PdfFont arialFont, ScriptEnum scriptEnum) {
        Border border = new GrooveBorder(new DeviceRgb(0,0,0), 1);
        Cell cell = new Cell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setTextAlignment(TextAlignment.CENTER);



        Style style = new Style();
        style.setFont(arialFont)
                .setFontSize(11)
                .setMargin(0)
                .setPadding(0);

        if(member == null) {
            for(int i = 0; i<3; i++) {
                cell = new Cell();
                cell.setBorder(border);
                cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
                cell.setTextAlignment(TextAlignment.CENTER);
                cell.setHeight(13);
                table.addCell(cell);
            }
            return;
        }

        Paragraph paragraph = new Paragraph(member.getConstraint().getPosition() + ".");
        paragraph.addStyle(style).setFixedLeading(12);

        Cell orderCell = new Cell();
        orderCell.setBorder(border);
        orderCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        orderCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        orderCell.add(paragraph);
        table.addCell(orderCell);


        String label = member.getFullname().toUpperCase();
        String text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell fullNameCell = new Cell();
        fullNameCell.setBorder(border);
        fullNameCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        fullNameCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        fullNameCell.add(paragraph);
        table.addCell(fullNameCell);

        if(member.getIsGik() != null)
            label = member.getIsGik() ? "Градска изборна комисија".toUpperCase() : member.getConstraint().getPoliticalOrganization().getCode() + " - " + member.getConstraint().getPoliticalOrganization().getName();
        else
            label = member.getConstraint().getPoliticalOrganization().getCode() + " - " + member.getConstraint().getPoliticalOrganization().getName();
        text = scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(label) : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell politicalOrganizationCell = new Cell();
        politicalOrganizationCell.setBorder(border);
        politicalOrganizationCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        politicalOrganizationCell.setTextAlignment(TextAlignment.CENTER);
        politicalOrganizationCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        politicalOrganizationCell.add(paragraph);
        table.addCell(politicalOrganizationCell);
    }

    private boolean isRegularTeam(VotingCouncelEntity entity) {
        return entity.getCode().toUpperCase().contains("Б") || entity.getCode().toUpperCase().contains("B");
    }
}
