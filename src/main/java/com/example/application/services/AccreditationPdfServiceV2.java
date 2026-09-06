package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.StackEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.enums.SideEnum;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.ImageRenderer;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.BackgroundImage;
import com.itextpdf.layout.properties.BackgroundPosition;
import com.itextpdf.layout.properties.BackgroundRepeat;
import com.itextpdf.layout.properties.BackgroundSize;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.Collator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 2026 accreditation card layout: cards are laid out vertically (portrait) on a landscape A4
 * sheet, 5 across. The top row of a page holds the front of the card, the row directly below it
 * holds the (identical, year-over-year) back/description text, so a printed sheet can be cut into
 * 5 vertical strips, each with its front stacked directly above its own back.
 * <p>
 * This is a from-scratch replacement for the horizontal layout in {@link ObserverPdfService}
 * (kept there, unused, for reference/rollback) - nothing here is shared with it beyond the
 * conventions (fonts, converters, entities) common to the whole app.
 */
@Service
public class AccreditationPdfServiceV2 {
    private static final String ROOT_PATH = "src/main/resources/generated-documents";

    private static final String ARIAL_FONT_PATH = "src/main/resources/font/arial.ttf";
    private static final String CYRILLIC_FONT_PATH = "src/main/resources/font/cyrillic-font.ttf";
    private static final String WATERMARK_LOGO_PATH = "src/main/resources/logo/election26.png";
    private static final String COUNTRY_LOGO_PATH = "src/main/resources/logo/logo_bih.png";
    private static final String SIGNATURE_PATH = "src/main/resources/logo/signature.png";
    // Visual size the signature is painted at, above the "_______________" line right above "MP"
    // (see buildSignatureCell). Native signature.png is 71x65px; this keeps that aspect ratio.
    private static final float SIGNATURE_HEIGHT = 40f;
    private static final float SIGNATURE_WIDTH = SIGNATURE_HEIGHT * 71f / 65f;
    // The signature/MP row's total height is fixed by the physical card size (see CARD_HEIGHT above)
    // and has no room to reserve a SIGNATURE_HEIGHT-tall layout box on top of the line and "MP" - so
    // the image element only reserves this tiny anchor box (keeping the card layout untouched), and
    // buildSignatureCell's custom renderer paints the full-size image centered on it directly onto
    // the page instead, overlapping onto the line and the blank space above it.
    private static final float SIGNATURE_ANCHOR_HEIGHT = 1f;
    private static final float SIGNATURE_ANCHOR_WIDTH = SIGNATURE_ANCHOR_HEIGHT * 71f / 65f;

    private static final String QR_CODE_CONTENT = "https://izbori.ba";
    private static final String PRESIDENT_SHORT_NAME = "D. Malinić";

    private static final float WATERMARK_OPACITY = 0.12f;
    // Radius (px, in the source 300x277 image) of the box blur applied to the watermark's alpha
    // map before it's baked in - softens its edges/lines so they read as a faint haze rather than
    // crisp shapes competing with the card text once printed.
    private static final int WATERMARK_BLUR_RADIUS = 5;
    // Measured height (at 100% width, auto-scaled to the image's own aspect ratio) the watermark
    // rendered at before this was made explicit, times 1.2 for the ~20% vertical increase.
    private static final float WATERMARK_HEIGHT = 136.88f;
    // Pushes the (bottom-anchored) watermark up off the card's bottom edge so it centers on the
    // space below the header/PS-QR band instead - tuned empirically, see the class-level notes.
    private static final float WATERMARK_BOTTOM_SHIFT = 27.38f;

    // A4 landscape's usable height is ~10pt less than page-height-minus-margins would suggest (some
    // fixed overhead in how iText reserves space for a table), so at the old 14pt margin, two full
    // CARD_HEIGHT rows (needed for two-sided printing's all-fronts page) narrowly didn't fit on one
    // page - confirmed empirically. 10pt reclaims enough of that margin for them to fit.
    private static final float PAGE_MARGIN = 10f;
    private static final int CARDS_PER_ROW = 5;
    // Card size: front and back must both match the old horizontal card's size (per printing-stock
    // rules) - that card was 255.5x150 (width x height); rotated 90 degrees for this vertical layout,
    // width and height swap. Every other size constant below is scaled down from what it was tuned
    // for at the old, taller/wider card (~164.4 x 280) by this same ~0.9125 factor, so all the
    // font-fit/line-wrap tuning throughout the class stays proportionally correct at the new size.
    private static final float CARD_WIDTH = 150f;
    private static final float CARD_HEIGHT = 255.5f;
    private static final float LABELED_LINE_LABEL_FONT_SIZE = 9.58f;
    private static final float LABELED_LINE_VALUE_FONT_SIZE_MAX = 10.49f;
    private static final float LABELED_LINE_VALUE_FONT_SIZE_MIN = 6.39f;
    // Usable width for one "Label: VALUE" line - card column width minus the outer card cell's
    // padding/border - kept a few points under that for safety margin.
    private static final float LABELED_LINE_MAX_WIDTH = 135.05f;
    // Fixed height of everything above the signature/MP row (header through Ovjerava), so that row
    // always starts at the same Y - sized for the tallest case (both Ime and Prezime stacked, in
    // compact mode); re-check against CARD_HEIGHT if the field font sizes above change.
    private static final float TOP_CONTENT_HEIGHT = 224.48f;
    private static final float LABELED_LINE_PADDING_NORMAL = 3.65f;
    // Used instead of LABELED_LINE_PADDING_NORMAL when both Ime and Prezime stack onto two lines -
    // reclaims roughly the two extra lines' worth of height that costs, so that still fits within
    // TOP_CONTENT_HEIGHT without changing CARD_HEIGHT.
    private static final float LABELED_LINE_PADDING_COMPACT = 1.37f;
    private static final float QR_SIZE = 39.42f;
    private static final float HEADER_SIDE_COLUMN_PERCENT = 45f;
    private static final float HEADER_LOGO_COLUMN_PERCENT = 10f;
    private static final float COUNTRY_LOGO_WIDTH = 10.04f;
    private static final float COUNTRY_LOGO_HEIGHT = 11.5f;
    // Measured usable width of the header's left/right text column: card column width, minus the
    // card cell padding/border, the header cell's own padding, and this column's 45% share of what's
    // left - kept a couple points under that for safety margin.
    private static final float HEADER_TEXT_COLUMN_WIDTH = 71.18f;
    private static final float HEADER_FONT_SIZE_MAX = 5.48f;
    private static final float HEADER_FONT_SIZE_MIN = 1.83f;
    private static final float CYRILLIC_WIDTH_SAFETY_FACTOR = 1.35f;

    private static final String BACK_DESCRIPTION_TEXT = "Posmatrač, u toku posmatranja izbornog procesa, neće ometati izborne aktivnosti i poštivaće tajnost glasanja. Za vrijeme posmatranja izbornih aktivnosti, posmatrač će nositi službenu akreditaciju i neće nositi bilo kakva obilježja ili oznake koje ga povezuju s određenom političkom partijom, koalicijom, listom nezavisnih kandidata, nezavisnim kandidatom i kandidatom sa posebne liste kandidata pripadnika nacionalnih manjina.";

    private final LatinToCyrillicConverter latinToCyrillicConverter;

    public AccreditationPdfServiceV2(LatinToCyrillicConverter latinToCyrillicConverter) {
        this.latinToCyrillicConverter = latinToCyrillicConverter;
    }

    public String downloadAccreditationsPdf(StackEntity entity, LocalDate datePicker, ScriptEnum script, SideEnum sideNumber, String fileTitle) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(out));
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
        Document document = openDocument(pdfDoc);

        PdfFont arialFont = loadArialFont();
        PdfFont cyrillicFont = loadCyrillicFont();
        PdfFormXObject qrXObject = buildQrCodeXObject(pdfDoc);
        PdfImageXObject watermarkXObject = new PdfImageXObject(loadFadedWatermarkImageData());
        ImageData countryLogoImageData = loadCountryLogoImageData();
        ImageData signatureImageData = loadSignatureImageData();

        Collator collator = getCollatorForScript(script);
        // Sort on the name already converted to the target script (as the decision-document
        // generator does), not the Latin source: a Cyrillic collator doesn't correctly place Latin
        // diacritics like Č/Ć/Š/Ž/Đ, which otherwise fall out of azbuka order (e.g. Č sorting near
        // the front instead of near the end).
        Comparator<ObserverEntity> ordering = Comparator
                .comparing((ObserverEntity o) -> doConvert(o.getLastname(), script), collator)
                .thenComparing(o -> doConvert(o.getFirstname(), script), collator);

        List<ObserverEntity> filteredObservers = entity.getObservers().stream()
                .filter(AccreditationPdfServiceV2::isAcceptedForOperationalDocuments)
                .sorted(ordering)
                .collect(Collectors.toList());

        if (sideNumber == SideEnum.ONE_SIDED) {
            Table grid = buildGridTable();
            addOneSidedPages(grid, filteredObservers, datePicker, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
            document.add(grid);
        } else {
            addTwoSidedPages(document, filteredObservers, datePicker, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
        }

        document.close();

        return writeToDisk(out, fileTitle);
    }

    /**
     * A one-sided (non-duplex) printer can only print one side of a sheet, so each front is paired
     * with its back directly below it on the same page: cut the sheet in half and glue the two
     * halves back-to-back to get a two-sided badge. For that flip to leave the back reading right
     * side up once glued on, its text is pre-rotated 180 degrees here.
     */
    private void addOneSidedPages(Table grid, List<ObserverEntity> observers, LocalDate localDate, ScriptEnum script,
                                   PdfFont arialFont, PdfFont cyrillicFont, PdfFormXObject qrXObject, PdfImageXObject watermarkXObject,
                                   ImageData countryLogoImageData, ImageData signatureImageData) {
        int numberOfChunks = (observers.size() + CARDS_PER_ROW - 1) / CARDS_PER_ROW;
        for (int i = 0; i < numberOfChunks; i++) {
            List<ObserverEntity> chunk = observers.subList(i * CARDS_PER_ROW, Math.min((i + 1) * CARDS_PER_ROW, observers.size()));
            addFrontRow(grid, chunk, localDate, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
            addBackRow(grid, chunk, arialFont, script, true);
        }
    }

    /**
     * A duplex printer flips the sheet itself, so fronts and backs are laid out on separate pages
     * instead: one page of up to 10 fronts (two 5-wide rows), immediately followed by one page of
     * their backs in the same row/column positions, so the reverse of each printed sheet lines up
     * with its matching front. No pre-rotation needed here - the printer's own flip handles that.
     * <p>
     * Each page is its own table with an explicit {@link AreaBreak} in between, rather than one
     * continuous table relying on automatic overflow: two fronts rows back-to-back (560pt) are too
     * close to the ~567pt usable page height for that to reliably keep them on the same page.
     */
    private void addTwoSidedPages(Document document, List<ObserverEntity> observers, LocalDate localDate, ScriptEnum script,
                                   PdfFont arialFont, PdfFont cyrillicFont, PdfFormXObject qrXObject, PdfImageXObject watermarkXObject,
                                   ImageData countryLogoImageData, ImageData signatureImageData) {
        int chunkSize = CARDS_PER_ROW * 2;
        int numberOfChunks = (observers.size() + chunkSize - 1) / chunkSize;
        for (int i = 0; i < numberOfChunks; i++) {
            List<ObserverEntity> chunk = observers.subList(i * chunkSize, Math.min((i + 1) * chunkSize, observers.size()));
            List<ObserverEntity> firstRow = chunk.subList(0, Math.min(CARDS_PER_ROW, chunk.size()));
            List<ObserverEntity> secondRow = chunk.size() > CARDS_PER_ROW ? chunk.subList(CARDS_PER_ROW, chunk.size()) : List.of();

            if (i > 0) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

            Table frontsPage = buildGridTable();
            addFrontRow(frontsPage, firstRow, localDate, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
            addFrontRow(frontsPage, secondRow, localDate, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
            document.add(frontsPage);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            Table backsPage = buildGridTable();
            addBackRow(backsPage, firstRow, arialFont, script, false);
            addBackRow(backsPage, secondRow, arialFont, script, false);
            document.add(backsPage);
        }
    }

    /** Generates a single front/back accreditation card for one observer, regardless of status. */
    public String downloadSingleAccreditationPdf(ObserverEntity observer, LocalDate localDate, ScriptEnum script, String fileTitle) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(out));
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
        Document document = openDocument(pdfDoc);

        PdfFont arialFont = loadArialFont();
        PdfFont cyrillicFont = loadCyrillicFont();
        PdfFormXObject qrXObject = buildQrCodeXObject(pdfDoc);
        PdfImageXObject watermarkXObject = new PdfImageXObject(loadFadedWatermarkImageData());
        ImageData countryLogoImageData = loadCountryLogoImageData();
        ImageData signatureImageData = loadSignatureImageData();

        Table grid = buildGridTable();
        addFrontRow(grid, List.of(observer), localDate, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData);
        addBackRow(grid, List.of(observer), arialFont, script, false);

        document.add(grid);
        document.close();

        return writeToDisk(out, fileTitle);
    }

    private Document openDocument(PdfDocument pdfDoc) {
        Document document = new Document(pdfDoc);
        document.setMargins(PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN, PAGE_MARGIN);
        document.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return document;
    }

    private String writeToDisk(ByteArrayOutputStream out, String fileTitle) {
        String filePath = ROOT_PATH + File.separator + fileTitle;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            out.writeTo(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    /** Adds one row of up to {@link #CARDS_PER_ROW} fronts, padding out unused columns with blanks. */
    private void addFrontRow(Table grid, List<ObserverEntity> row, LocalDate localDate, ScriptEnum script,
                              PdfFont arialFont, PdfFont cyrillicFont, PdfFormXObject qrXObject, PdfImageXObject watermarkXObject,
                              ImageData countryLogoImageData, ImageData signatureImageData) {
        for (ObserverEntity observer : row) {
            grid.addCell(buildFrontCell(observer, localDate, script, arialFont, cyrillicFont, qrXObject, watermarkXObject, countryLogoImageData, signatureImageData));
        }
        for (int pad = row.size(); pad < CARDS_PER_ROW; pad++) {
            grid.addCell(blankCell(CARD_HEIGHT));
        }
    }

    /** Adds one row of up to {@link #CARDS_PER_ROW} backs, padding out unused columns with blanks. */
    private void addBackRow(Table grid, List<ObserverEntity> row, PdfFont arialFont, ScriptEnum script, boolean rotate180) {
        for (int i = 0; i < row.size(); i++) {
            grid.addCell(buildBackCell(arialFont, script, rotate180));
        }
        for (int pad = row.size(); pad < CARDS_PER_ROW; pad++) {
            grid.addCell(blankCell(CARD_HEIGHT));
        }
    }

    /**
     * Exactly {@link #CARD_WIDTH} per column (the whole point of matching the old card size) rather
     * than splitting the page width evenly, centered on the page both ways: horizontally via its own
     * alignment, vertically via a top margin sized to split the leftover space evenly above and
     * below - every page (one-sided or two-sided, fronts or backs) always renders exactly 2 rows
     * (real cards padded out with blanks to a full row), so that leftover space is the same on every
     * page and one fixed margin centers all of them.
     */
    private Table buildGridTable() {
        float[] columnWidths = {1f, 1f, 1f, 1f, 1f};
        Table grid = new Table(UnitValue.createPercentArray(columnWidths));
        grid.setWidth(UnitValue.createPointValue(CARD_WIDTH * CARDS_PER_ROW));
        grid.setHorizontalAlignment(HorizontalAlignment.CENTER);
        grid.setMarginTop(computeVerticalCenteringMargin());
        return grid;
    }

    private float computeVerticalCenteringMargin() {
        float pageHeight = PageSize.A4.rotate().getHeight();
        float availableHeight = pageHeight - 2 * PAGE_MARGIN;
        // A rendered 2-row table measures ~12pt taller than 2*CARD_HEIGHT alone would suggest (the
        // same kind of fixed table-layout overhead noted for PAGE_MARGIN above) - confirmed
        // empirically by measuring top/bottom whitespace on a rendered page and adjusting until they
        // matched, since guessing the exact iText internals responsible wasn't worth chasing further.
        float contentHeight = 2 * CARD_HEIGHT + 12f;
        return Math.max(0f, (availableHeight - contentHeight) / 2f);
    }

    private Cell buildFrontCell(ObserverEntity observer, LocalDate localDate, ScriptEnum script,
                                 PdfFont arialFont, PdfFont cyrillicFont, PdfFormXObject qrXObject, PdfImageXObject watermarkXObject,
                                 ImageData countryLogoImageData, ImageData signatureImageData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        String formattedDate = localDate.format(formatter);
        String decisionNumberText = observer.getStack().getDecisionNumber() != null ? observer.getStack().getDecisionNumber() : "";

        String firstname = observer.getFirstname().toUpperCase();
        String lastname = observer.getLastname().toUpperCase();
        // When Ime and/or Prezime need their own line for the value (see buildLabeledCell), that's
        // extra height on top of what the card height was tuned for - so every field tightens its
        // spacing to compensate and stay within TOP_CONTENT_HEIGHT (below).
        boolean compact = wouldStack(arialFont, doConvert("Ime: ", script), doConvert(firstname, script))
                || wouldStack(arialFont, doConvert("Prezime: ", script), doConvert(lastname, script));

        Table topContent = new Table(UnitValue.createPercentArray(new float[]{100f}));
        topContent.setWidth(UnitValue.createPercentValue(100));
        topContent.addCell(buildHeaderCell(arialFont, cyrillicFont, countryLogoImageData));
        topContent.addCell(buildPsAndQrCell(observer, arialFont, qrXObject, script));
        topContent.addCell(buildLabeledLineCell("Ime: ", firstname, arialFont, script, compact));
        topContent.addCell(buildLabeledLineCell("Prezime: ", lastname, arialFont, script, compact));
        topContent.addCell(buildPravoPosmatranjaCell(arialFont, script, compact));
        topContent.addCell(buildStackedLabeledCell("prema odluci br.: ", decisionNumberText, arialFont, script, compact));
        topContent.addCell(buildLabeledLineCell("Datum: ", formattedDate, arialFont, script, compact));
        topContent.addCell(buildLabeledLineCell("Ovjerava: ", PRESIDENT_SHORT_NAME, arialFont, script, compact));

        // Fixed height (sized for the tallest case: both Ime and Prezime stacked, in compact mode) so
        // the signature/MP row directly below always starts at the same Y regardless of how tall the
        // fields above actually rendered - top-aligned by default, so shorter content just leaves
        // blank space at the bottom of this cell instead of shifting the footer up.
        Cell topContentCell = new Cell();
        topContentCell.add(topContent);
        topContentCell.setBorder(Border.NO_BORDER);
        topContentCell.setPadding(0f);
        topContentCell.setHeight(TOP_CONTENT_HEIGHT);

        Table card = new Table(UnitValue.createPercentArray(new float[]{100f}));
        card.setWidth(UnitValue.createPercentValue(100));
        card.addCell(topContentCell);
        card.addCell(buildSignatureCell(arialFont, script, signatureImageData));

        return wrapAsCardCell(card, watermarkXObject, CARD_HEIGHT);
    }

    /**
     * Mirrors the trilingual BiH/CIK header from the old horizontal card ({@link ObserverPdfService
     * #generateAccreditationHeader}) - same three text lines either side of the coat of arms. The
     * old card was almost 2.5x wider, so instead of a fixed scaled-down font size (which still
     * wrapped "SREDNJE IZBORNO POVJERENSTVO" onto two lines), each line's font size is solved from
     * the font's own metrics to be the largest size that still fits on one line in this column.
     */
    private Cell buildHeaderCell(PdfFont arialFont, PdfFont cyrillicFont, ImageData countryLogoImageData) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{HEADER_SIDE_COLUMN_PERCENT, HEADER_LOGO_COLUMN_PERCENT, HEADER_SIDE_COLUMN_PERCENT}));
        header.setWidth(UnitValue.createPercentValue(100));

        String countryLatin = "BOSNA I HERCEGOVINA";
        String countryCyrillic = "БОСНА И ХЕРЦЕГОВИНА";
        String companyBosnian = "CENTRALNA IZBORNA KOMISIJA";
        String companySerbian = "ЦЕНТРАЛНА ИЗБОРНА КОМИСИЈА";
        String companyCroatian = "SREDNJE IZBORNO POVJERENSTVO";

        float countryFontSize = Math.min(
                fitFontSize(arialFont, countryLatin, HEADER_TEXT_COLUMN_WIDTH, false),
                fitFontSize(cyrillicFont, countryCyrillic, HEADER_TEXT_COLUMN_WIDTH, true));
        float companyFontSize = Math.min(
                Math.min(fitFontSize(arialFont, companyBosnian, HEADER_TEXT_COLUMN_WIDTH, false),
                        fitFontSize(arialFont, companyCroatian, HEADER_TEXT_COLUMN_WIDTH, false)),
                fitFontSize(cyrillicFont, companySerbian, HEADER_TEXT_COLUMN_WIDTH, true));

        Style countryNameStyle = new Style().setFont(arialFont).setFontSize(countryFontSize).setTextAlignment(TextAlignment.CENTER);
        Style countryNameCyrillicStyle = new Style().setFont(cyrillicFont).setFontSize(countryFontSize).setTextAlignment(TextAlignment.CENTER);
        Style companyNameStyle = new Style().setFont(arialFont).setFontSize(companyFontSize).setTextAlignment(TextAlignment.CENTER);
        Style companyNameCyrillicStyle = new Style().setFont(cyrillicFont).setFontSize(companyFontSize).setTextAlignment(TextAlignment.CENTER);

        header.addCell(plainCell(new Paragraph(countryLatin).addStyle(countryNameStyle)));

        Image countryLogo = new Image(countryLogoImageData);
        countryLogo.setWidth(COUNTRY_LOGO_WIDTH);
        countryLogo.setHeight(COUNTRY_LOGO_HEIGHT);
        Cell logoCell = new Cell(3, 1);
        logoCell.add(countryLogo);
        logoCell.setBorder(Border.NO_BORDER);
        logoCell.setPadding(0f);
        logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        logoCell.setTextAlignment(TextAlignment.CENTER);
        header.addCell(logoCell);

        header.addCell(plainCell(new Paragraph(countryCyrillic).addStyle(countryNameCyrillicStyle)));

        header.addCell(plainCell(new Paragraph(companyBosnian).addStyle(companyNameStyle)));

        Cell serbianNameCell = new Cell(2, 1);
        serbianNameCell.add(new Paragraph(companySerbian).addStyle(companyNameCyrillicStyle));
        serbianNameCell.setBorder(Border.NO_BORDER);
        serbianNameCell.setPadding(0f);
        serbianNameCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        header.addCell(serbianNameCell);

        header.addCell(plainCell(new Paragraph(companyCroatian).addStyle(companyNameStyle)));

        Cell cell = new Cell();
        cell.add(header);
        cell.setBorder(Border.NO_BORDER);
        cell.setBorderBottom(new SolidBorder(0.75f));
        cell.setPadding(1.83f);
        return cell;
    }

    private Cell buildPsAndQrCell(ObserverEntity observer, PdfFont arialFont, PdfFormXObject qrXObject, ScriptEnum script) {
        Table row = new Table(UnitValue.createPercentArray(new float[]{60f, 40f}));
        row.setWidth(UnitValue.createPercentValue(100));

        String organizationCode = padToFiveDigits(observer.getStack().getPoliticalOrganization().getCode());

        Text psLabel = new Text(doConvert("PS-", script)).setFont(arialFont).setFontSize(14.24f).simulateBold();
        Text psValue = new Text(doConvert(organizationCode, script)).setFont(arialFont).setFontSize(14.24f).simulateBold();
        Paragraph psParagraph = new Paragraph().add(psLabel).add(psValue).setMargin(0f);

        Cell psCell = new Cell();
        psCell.add(psParagraph);
        psCell.setBorder(Border.NO_BORDER);
        psCell.setPadding(0f);
        psCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        row.addCell(psCell);

        Image qrImage = new Image(qrXObject);
        qrImage.setWidth(QR_SIZE);
        qrImage.setHeight(QR_SIZE);
        qrImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        Cell qrCell = new Cell();
        qrCell.add(qrImage);
        qrCell.setBorder(Border.NO_BORDER);
        qrCell.setPadding(0f);
        qrCell.setTextAlignment(TextAlignment.RIGHT);
        row.addCell(qrCell);

        Cell outer = new Cell();
        outer.add(row);
        outer.setBorder(Border.NO_BORDER);
        outer.setPadding(1.83f);
        return outer;
    }

    /**
     * A "Label: VALUE" line used for Ime / Prezime / Datum / Ovjerava, normally with label and value
     * sharing one line at full size. A value that doesn't fit next to its label at
     * {@link #LABELED_LINE_VALUE_FONT_SIZE_MAX} (double-barrelled surnames, mainly) moves to its own
     * line below the label instead of shrinking - keeping the value legible at full size rather than
     * the label eating into its size budget. That value still shrinks (down to
     * {@link #LABELED_LINE_VALUE_FONT_SIZE_MIN}) as a last resort if it doesn't even fit alone.
     */
    private Cell buildLabeledLineCell(String label, String rawValue, PdfFont arialFont, ScriptEnum script, boolean compact) {
        float padding = compact ? LABELED_LINE_PADDING_COMPACT : LABELED_LINE_PADDING_NORMAL;
        return buildLabeledCell(label, rawValue, arialFont, script, padding, padding, compact);
    }

    /**
     * Same as {@link #buildLabeledLineCell}, but with no top padding - used for "prema odluci br.:",
     * which sits directly under "Pravo posmatranja" with no gap between them, and whose label is
     * long enough that its value always ends up on its own line in practice.
     */
    private Cell buildStackedLabeledCell(String label, String rawValue, PdfFont arialFont, ScriptEnum script, boolean compact) {
        float paddingBottom = compact ? LABELED_LINE_PADDING_COMPACT : LABELED_LINE_PADDING_NORMAL;
        return buildLabeledCell(label, rawValue, arialFont, script, 0f, paddingBottom, compact);
    }

    /** True if {@code label} plus {@code rawValue} would need {@link #buildLabeledCell} to stack them onto two lines. */
    private boolean wouldStack(PdfFont arialFont, String label, String rawValue) {
        float labelWidth = arialFont.getWidth(label, LABELED_LINE_LABEL_FONT_SIZE);
        float valueWidthAtMax = arialFont.getWidth(rawValue, LABELED_LINE_VALUE_FONT_SIZE_MAX);
        return labelWidth + valueWidthAtMax > LABELED_LINE_MAX_WIDTH;
    }

    private Cell buildLabeledCell(String label, String rawValue, PdfFont arialFont, ScriptEnum script,
                                   float paddingTop, float paddingBottom, boolean compact) {
        String convertedLabel = doConvert(label, script);
        String convertedValue = doConvert(rawValue, script);

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingBottom(paddingBottom);

        float labelWidth = arialFont.getWidth(convertedLabel, LABELED_LINE_LABEL_FONT_SIZE);
        float valueWidthAtMax = arialFont.getWidth(convertedValue, LABELED_LINE_VALUE_FONT_SIZE_MAX);
        if (labelWidth + valueWidthAtMax <= LABELED_LINE_MAX_WIDTH) {
            Style labelStyle = new Style().setFont(arialFont).setFontSize(LABELED_LINE_LABEL_FONT_SIZE);
            Style valueStyle = new Style().setFont(arialFont).setFontSize(LABELED_LINE_VALUE_FONT_SIZE_MAX).simulateBold();
            Text labelText = new Text(convertedLabel).addStyle(labelStyle);
            Text valueText = new Text(convertedValue).addStyle(valueStyle);
            cell.add(new Paragraph().add(labelText).add(valueText).setMargin(0f));
        } else {
            float valueFontSize = fitValueFontSize(arialFont, "", LABELED_LINE_LABEL_FONT_SIZE, convertedValue);
            Paragraph labelParagraph = new Paragraph(convertedLabel)
                    .setFont(arialFont).setFontSize(LABELED_LINE_LABEL_FONT_SIZE).setMargin(0f);
            Paragraph valueParagraph = new Paragraph(convertedValue)
                    .setFont(arialFont).setFontSize(valueFontSize).simulateBold().setMargin(0f)
                    .setMarginTop(compact ? 0f : 0.91f);
            cell.add(labelParagraph);
            cell.add(valueParagraph);
        }
        return cell;
    }

    private Cell buildPravoPosmatranjaCell(PdfFont arialFont, ScriptEnum script, boolean compact) {
        Style style = new Style().setFont(arialFont).setFontSize(LABELED_LINE_LABEL_FONT_SIZE).setTextAlignment(TextAlignment.LEFT);
        Paragraph paragraph = new Paragraph()
                .add(doConvert("Pravo posmatranja", script))
                .addStyle(style)
                .setMargin(0f);

        Cell cell = new Cell();
        cell.add(paragraph);
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingTop(compact ? LABELED_LINE_PADDING_COMPACT : LABELED_LINE_PADDING_NORMAL);
        cell.setPaddingBottom(0f);
        return cell;
    }

    private Cell buildSignatureCell(PdfFont arialFont, ScriptEnum script, ImageData signatureImageData) {
        Image signature = new Image(signatureImageData);
        signature.setWidth(SIGNATURE_ANCHOR_WIDTH);
        signature.setHeight(SIGNATURE_ANCHOR_HEIGHT);
        signature.setHorizontalAlignment(HorizontalAlignment.CENTER);
        signature.setNextRenderer(new ImageRenderer(signature) {
            @Override
            public void draw(DrawContext drawContext) {
                Rectangle box = getOccupiedAreaBBox();
                float x = box.getLeft() + (box.getWidth() - SIGNATURE_WIDTH) / 2f;
                float y = box.getBottom() + (box.getHeight() - SIGNATURE_HEIGHT) / 2f;
                drawContext.getCanvas().addImageFittedIntoRectangle(signatureImageData, new Rectangle(x, y, SIGNATURE_WIDTH, SIGNATURE_HEIGHT), false);
            }
        });

        Paragraph line = new Paragraph("_______________")
                .setFont(arialFont).setFontSize(6.39f).setTextAlignment(TextAlignment.CENTER).setMargin(0f);
        Paragraph mp = new Paragraph(doConvert("MP", script))
                .setFont(arialFont).setFontSize(7.3f).simulateBold()
                .setTextAlignment(TextAlignment.CENTER).setMarginTop(0.91f);

        Cell cell = new Cell();
        cell.add(signature);
        cell.add(line);
        cell.add(mp);
        cell.setBorder(Border.NO_BORDER);
        cell.setPaddingTop(5.48f);
        cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        return cell;
    }

    /**
     * @param rotate180 true for one-sided printing, where this back sits directly under its front on
     *                  the same sheet and gets cut out and glued back-to-back with it - pre-rotating
     *                  the text here is what makes it read right side up once that flip is done. Not
     *                  needed for two-sided (duplex) printing, where the printer does the flip itself.
     */
    private Cell buildBackCell(PdfFont arialFont, ScriptEnum script, boolean rotate180) {
        Paragraph paragraph = new Paragraph(doConvert(BACK_DESCRIPTION_TEXT, script))
                .setFont(arialFont)
                .setFontSize(7.3f)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        Cell cell = new Cell();
        cell.setHeight(CARD_HEIGHT);
        cell.setBorder(new SolidBorder(0.75f));
        // Must match the front card's outer padding (wrapAsCardCell) exactly: setHeight() sizes only
        // the content box, so padding is added on top of it - a different padding here would make the
        // back card's total rendered height differ from the front's, throwing off front/back
        // registration on two-sided (duplex) printing where both are laid out on separate pages.
        cell.setPadding(2.74f);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setKeepTogether(true);

        if (rotate180) {
            Div rotatedDiv = new Div();
            rotatedDiv.add(paragraph);
            rotatedDiv.setRotationAngle(Math.PI);
            cell.add(rotatedDiv);
        } else {
            cell.add(paragraph);
        }
        return cell;
    }

    /**
     * The watermark is a background on the whole card (which always has the full, unclipped
     * {@code height} to work with) rather than just the body below the header/PS-QR band: a Cell or
     * Div forced to a specific height either doesn't grow the background's clip/center area, or - if
     * it does - clips its own content to that height instead. Anchoring to the bottom edge and
     * shifting up by {@link #WATERMARK_BOTTOM_SHIFT} centers it on the leftover space below the
     * header/PS-QR band instead, without touching either.
     */
    private Cell wrapAsCardCell(Table cardContent, PdfImageXObject watermarkXObject, float height) {
        BackgroundSize size = new BackgroundSize();
        size.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPointValue(WATERMARK_HEIGHT));
        BackgroundPosition position = new BackgroundPosition()
                .setPositionX(BackgroundPosition.PositionX.CENTER)
                .setPositionY(BackgroundPosition.PositionY.BOTTOM)
                .setYShift(UnitValue.createPointValue(WATERMARK_BOTTOM_SHIFT));
        BackgroundImage backgroundImage = new BackgroundImage.Builder()
                .setImage(watermarkXObject)
                .setBackgroundSize(size)
                .setBackgroundPosition(position)
                .setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT))
                .build();

        Cell outer = new Cell();
        outer.add(cardContent);
        outer.setHeight(height);
        outer.setBorder(new SolidBorder(0.75f));
        outer.setPadding(2.74f);
        outer.setKeepTogether(true);
        outer.setBackgroundImage(backgroundImage);
        return outer;
    }

    private Cell blankCell(float height) {
        Cell cell = new Cell();
        cell.setHeight(height);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private Cell plainCell(IBlockElement content) {
        Cell cell = new Cell();
        cell.add(content);
        cell.setBorder(Border.NO_BORDER);
        cell.setPadding(0f);
        cell.setMargin(0f);
        return cell;
    }

    private PdfFormXObject buildQrCodeXObject(PdfDocument pdfDoc) {
        return new BarcodeQRCode(QR_CODE_CONTENT).createFormXObject(ColorConstants.BLACK, pdfDoc);
    }

    /**
     * Reads the BiH-map election watermark and returns a faded, softened, fully-opaque copy that
     * sits faintly behind the card body text.
     * <p>
     * This used to just scale the source PNG's alpha channel down to {@link #WATERMARK_OPACITY}
     * and leave it semi-transparent. That looked right on screen, but printed black-and-white it
     * came out far too dark and swallowed the text under it - some print pipelines don't honor a
     * PNG's alpha/soft-mask correctly and rasterize it closer to full strength. To make the result
     * print-safe regardless of the pipeline, the fade is baked directly into fully-opaque, near-white
     * RGB values here (composited against white at {@link #WATERMARK_OPACITY} strength) instead of
     * left as transparency, and the alpha map is box-blurred first so edges/lines soften into a haze
     * rather than staying crisp.
     */
    private ImageData loadFadedWatermarkImageData() {
        try {
            BufferedImage original = ImageIO.read(new File(WATERMARK_LOGO_PATH));
            int width = original.getWidth();
            int height = original.getHeight();

            float[] alpha = new float[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    alpha[y * width + x] = (original.getRGB(x, y) >>> 24) & 0xFF;
                }
            }
            float[] blurredAlpha = boxBlur(alpha, width, height, WATERMARK_BLUR_RADIUS);

            BufferedImage faded = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = original.getRGB(x, y);
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    float opacityFrac = (blurredAlpha[y * width + x] / 255f) * WATERMARK_OPACITY;
                    int newR = Math.round(255 - opacityFrac * (255 - r));
                    int newG = Math.round(255 - opacityFrac * (255 - g));
                    int newB = Math.round(255 - opacityFrac * (255 - b));
                    faded.setRGB(x, y, 0xFF000000 | (newR << 16) | (newG << 8) | newB);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(faded, "png", baos);
            return ImageDataFactory.create(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Separable box blur (horizontal pass then vertical pass) over a single-channel float map,
     * clamping the averaging window at the image edges instead of wrapping or padding.
     */
    private float[] boxBlur(float[] src, int width, int height, int radius) {
        if (radius <= 0) {
            return src;
        }
        float[] horizontal = new float[src.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float sum = 0f;
                int count = 0;
                for (int dx = -radius; dx <= radius; dx++) {
                    int sx = x + dx;
                    if (sx >= 0 && sx < width) {
                        sum += src[y * width + sx];
                        count++;
                    }
                }
                horizontal[y * width + x] = sum / count;
            }
        }
        float[] result = new float[src.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float sum = 0f;
                int count = 0;
                for (int dy = -radius; dy <= radius; dy++) {
                    int sy = y + dy;
                    if (sy >= 0 && sy < height) {
                        sum += horizontal[sy * width + x];
                        count++;
                    }
                }
                result[y * width + x] = sum / count;
            }
        }
        return result;
    }

    /**
     * Largest font size (within the header's min/max bounds) at which {@code text} still fits on
     * one line within {@code maxWidth}. {@code PdfFont.getWidth()} under-reports how wide the
     * cyrillic-font.ttf glyphs actually render (confirmed by comparing predicted vs. rendered
     * header text), so its measurements get inflated by a safety margin before being checked
     * against the column budget.
     */
    private float fitFontSize(PdfFont font, String text, float maxWidth, boolean isCyrillic) {
        float safetyFactor = isCyrillic ? CYRILLIC_WIDTH_SAFETY_FACTOR : 1f;
        float size = HEADER_FONT_SIZE_MAX;
        while (size > HEADER_FONT_SIZE_MIN && font.getWidth(text, size) * safetyFactor > maxWidth) {
            size -= 0.1f;
        }
        return size;
    }

    /**
     * Largest value font size (wihin the labeled-line min/max bounds) at which {@code label} at
     * {@code labelSize} plus {@code value} together still fit on one line within
     * {@link #LABELED_LINE_MAX_WIDTH}.
     */
    private float fitValueFontSize(PdfFont font, String label, float labelSize, String value) {
        float labelWidth = font.getWidth(label, labelSize);
        float size = LABELED_LINE_VALUE_FONT_SIZE_MAX;
        while (size > LABELED_LINE_VALUE_FONT_SIZE_MIN && labelWidth + font.getWidth(value, size) > LABELED_LINE_MAX_WIDTH) {
            size -= 0.2f;
        }
        return size;
    }

    private ImageData loadCountryLogoImageData() {
        try {
            return ImageDataFactory.create(COUNTRY_LOGO_PATH);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageData loadSignatureImageData() {
        try {
            return ImageDataFactory.create(SIGNATURE_PATH);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfFont loadArialFont() {
        try {
            return PdfFontFactory.createFont(ARIAL_FONT_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PdfFont loadCyrillicFont() {
        try {
            return PdfFontFactory.createFont(CYRILLIC_FONT_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Left-pads the political organization's code with zeros to 5 digits (e.g. "34" -&gt; "00034"). */
    private String padToFiveDigits(String code) {
        if (code == null) {
            return "";
        }
        return code.length() >= 5 ? code : "0".repeat(5 - code.length()) + code;
    }

    private String doConvert(String value, ScriptEnum scriptEnum) {
        return scriptEnum == ScriptEnum.CYRILLIC ? latinToCyrillicConverter.convert(value) : value;
    }

    /**
     * A plain "sr-RS" locale (no script) resolves to Cyrillic collation rules on the JVM, so the
     * Latin case must set "Latn" explicitly - otherwise Latin names sort by Cyrillic ordering
     * (e.g. Đ ends up sorted after Z instead of next to D/Dž, Ć before Č) instead of azbuka/abeceda.
     */
    private Collator getCollatorForScript(ScriptEnum scriptEnum) {
        Locale.Builder localeBuilder = new Locale.Builder().setLanguage("sr").setRegion("RS")
                .setScript(scriptEnum == ScriptEnum.CYRILLIC ? "Cyrl" : "Latn");
        return Collator.getInstance(localeBuilder.build());
    }

    private static boolean isAcceptedForOperationalDocuments(ObserverEntity observer) {
        return Boolean.TRUE.equals(observer.getStatus().getSuccess()) || Boolean.TRUE.equals(observer.getForce());
    }
}
