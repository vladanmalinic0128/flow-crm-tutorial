package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.ObserverRepository;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OverallObserversService {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final ObserverRepository observerRepository;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final LatinToCyrillicConverter latinToCyrillicConverter;

    public String generateOverallObserversReport(String fileTitle, ScriptEnum scriptEnum) throws IOException {
        PdfFont arialFont = PdfFontFactory.createFont("src/main/resources/font/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
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

        float[] columnWidths = {10, 30, 30, 30}; // Custom column widths
        Table observersTable = new Table(UnitValue.createPercentArray(columnWidths));
        observersTable.setWidth(UnitValue.createPercentValue(100)); // Set table width

        for (TranslatedObserver observer : getEligibleObserversSortedByName(scriptEnum)) {
            createObserverRowInTable(observer, observersTable, arialFont, scriptEnum);
        }
        document.add(observersTable);
        document.close();

        String fileName = ROOT_PATH + File.separator + "_zbirni_spisak_posmatraca_" + System.currentTimeMillis() + ".pdf";

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            out.writeTo(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;

    }

    /**
     * Fetches all observers, keeps only the ones eligible for the report, and translates their
     * name into the target script - without mutating the JPA-managed {@link ObserverEntity}
     * instances returned by the repository (Spring Boot's default open-in-view behaviour keeps
     * the persistence context open for the whole request, so writing translated names onto a
     * managed entity risks Hibernate silently flushing them back to the database).
     */
    List<TranslatedObserver> getEligibleObserversSortedByName(ScriptEnum scriptEnum) {
        Collator collator = getCollatorForScript(scriptEnum);

        return observerRepository.findAll().stream()
                .filter(OverallObserversService::isEligibleForReport)
                .map(observer -> translateObserverName(observer, scriptEnum))
                .sorted(Comparator.comparing(TranslatedObserver::lastname, collator)
                        .thenComparing(TranslatedObserver::firstname, collator))
                .collect(Collectors.toList());
    }

    static boolean isEligibleForReport(ObserverEntity observer) {
        return Boolean.TRUE.equals(observer.getStatus().getSuccess()) || Boolean.TRUE.equals(observer.getForce());
    }

    private TranslatedObserver translateObserverName(ObserverEntity observer, ScriptEnum scriptEnum) {
        return new TranslatedObserver(
                observer,
                translateName(observer.getFirstname(), scriptEnum),
                translateName(observer.getLastname(), scriptEnum));
    }

    private String translateName(String name, ScriptEnum scriptEnum) {
        if (name == null) {
            return null;
        }
        return scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(name) : cyrillicToLatinConverter.convert(name);
    }

    /**
     * An observer paired with its name already translated into the report's target script.
     * {@code source} stays around only so the row renderer can still reach stack/status data.
     */
    record TranslatedObserver(ObserverEntity source, String firstname, String lastname) {}

    private void createObserverRowInTable(TranslatedObserver observer, Table table, PdfFont arialFont, ScriptEnum scriptEnum) {
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

        if(observer == null) {
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

        ObserverEntity source = observer.source();

        String text = source.getStack().getPoliticalOrganization().getCode();
        Paragraph paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        cell.add(paragraph);
        table.addCell(cell);

        text = observer.lastname() == null ? null : observer.lastname().toUpperCase(Locale.ROOT);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell lastnameCell = new Cell();
        lastnameCell.setBorder(border);
        lastnameCell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        lastnameCell.setTextAlignment(TextAlignment.CENTER);
        lastnameCell.add(paragraph);
        table.addCell(lastnameCell);

        text = observer.firstname() == null ? null : observer.firstname().toUpperCase(Locale.ROOT);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell firstnameCell = new Cell();
        firstnameCell.setBorder(border);
        firstnameCell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        firstnameCell.setTextAlignment(TextAlignment.CENTER);
        firstnameCell.add(paragraph);
        table.addCell(firstnameCell);

        String label = observer.lastname() == null ? null : source.getStack().getDecisionNumber() + "-1";
        text = scriptEnum == ScriptEnum.CYRILLIC ? label : cyrillicToLatinConverter.convert(label);
        paragraph = new Paragraph(text);
        paragraph.addStyle(style).setFixedLeading(12);

        Cell decisionNumberCell = new Cell();
        decisionNumberCell.setBorder(border);
        decisionNumberCell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        decisionNumberCell.setTextAlignment(TextAlignment.CENTER);
        decisionNumberCell.add(paragraph);
        table.addCell(decisionNumberCell);
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
    private Collator getCollatorForScript(ScriptEnum scriptEnum) {
        Locale.Builder localeBuilder = new Locale.Builder().setLanguage("sr").setRegion("RS");
        if (scriptEnum == ScriptEnum.CYRILLIC) {
            localeBuilder.setScript("Cyrl");
        }
        Locale locale = localeBuilder.build();
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.PRIMARY);
        return collator;
    }

}
