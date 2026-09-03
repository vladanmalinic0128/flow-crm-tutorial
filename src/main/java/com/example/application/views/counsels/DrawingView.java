package com.example.application.views.counsels;

import com.example.application.entities.ConstraintEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.TitleEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.repositories.ConstraintRepository;
import com.example.application.repositories.PoliticalOrganizationRepository;
import com.example.application.repositories.TitleRepository;
import com.example.application.repositories.VotingCouncelRepository;
import com.example.application.services.LatinToCyrillicConverter;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "bo/zrijebanje", layout = MainLayout.class)
public class DrawingView extends VerticalLayout {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";

    // Column widths for the drawing ("žrijebanje") XLSX print layout, in Excel "characters" - used
    // both to size the columns and to estimate how many lines wrapped text will need, so the row
    // can be made tall enough that wrapped lines don't get clipped/overlapped when printed. This
    // sizing is specific to this table only - other generated XLSX/PDF documents are unaffected.
    // Matches the column widths (in characters) from the reference layout in
    // resources/documents/2026/zapisnik.xlsx, except columns A/B/C: A was widened by 2 characters
    // so codes like "034Б501/ННН" have comfortable room, C was doubled (narrow for its "Шифра
    // политичког субјекта" content) and B was narrowed by the same amount C grew, so the table's
    // total width otherwise matches the reference.
    private static final double COLUMN_CHARS_0 = 15.0;
    private static final double COLUMN_CHARS_1 = 34.14453125;
    private static final double COLUMN_CHARS_2 = 8.28125;
    private static final double COLUMN_CHARS_3 = 66.42578125;
    private static final double COLUMN_CHARS_4 = 14.0;
    private static final double COLUMN_CHARS_5 = 6.7109375;
    // Comfortably taller than the 16pt font actually used in this sheet, so wrapped lines have
    // enough room and don't overlap the row below when printed.
    private static final float WRAPPED_LINE_HEIGHT_POINTS = 22f;
    private final PoliticalOrganizationService politicalOrganizationService;
    private final PoliticalOrganizationRepository politicalOrganizationRepository;
    private final VotingCouncelRepository votingCouncelRepository;
    private final TitleRepository titleRepository;
    private final ConstraintRepository constraintRepository;
    private final LatinToCyrillicConverter latinToCyrillicConverter;
    private List<PoliticalOrganizationEntity> chosenPoliticalOrganizations = new ArrayList<>();

    private VerticalLayout drawingLayout = new VerticalLayout();

    public DrawingView(PoliticalOrganizationService politicalOrganizationService, PoliticalOrganizationRepository politicalOrganizationRepository, VotingCouncelRepository votingCouncelRepository, TitleRepository titleRepository, ConstraintRepository constraintRepository, LatinToCyrillicConverter latinToCyrillicConverter) {
        this.politicalOrganizationService = politicalOrganizationService;
        this.politicalOrganizationRepository = politicalOrganizationRepository;
        this.votingCouncelRepository = votingCouncelRepository;
        this.titleRepository = titleRepository;
        this.constraintRepository = constraintRepository;
        this.latinToCyrillicConverter = latinToCyrillicConverter;
        Button button = new Button("Započnite proces žrijebanja");
        Button buttonMT = new Button("Započnite proces žrijebanja za MT");
        button.setWidth("100%");
        buttonMT.setWidth("100%");
        drawingLayout.setWidth("100%");

        if(constraintRepository.findAll().stream().filter(c -> c.getVotingCouncel().getCode().contains("МТ") == false).count() > 0)
            button.setEnabled(false);
        button.addClickListener(e -> {
            Dialog dialog = createDialog(false);
            dialog.open();
        });

        if(constraintRepository.findAll().stream().filter(c -> c.getVotingCouncel().getCode().contains("МТ")).count() > 0)
            buttonMT.setEnabled(false);
        buttonMT.addClickListener(e -> {
            Dialog dialog = createDialog(true);
            dialog.open();
        });

        this.setWidth("50%");

        this.getStyle().set("margin", "0 auto");
        this.getStyle().set("margin-top", "20px");

        add(button, buttonMT);
        add(drawingLayout);
    }

    private Dialog createDialog(boolean isMT) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayout(dialog, isMT);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private H2 createDialogHeader() {
        H2 headline = new H2("Odaberite političke subjekte");
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayout(Dialog dialog, boolean isMT) {
        TextArea textArea = new TextArea();
        textArea.setValue("Ovde možete odabrati političke subjekte koji učestvuju na žrijebanju. To možete uraditi klikom na iste.");
        textArea.setHeightFull(); // Set height as desired
        textArea.setReadOnly(true); // Optional: make the TextArea read-only if needed

        MultiSelectListBox<PoliticalOrganizationEntity> listBox = new MultiSelectListBox<>();
        listBox.setItems(politicalOrganizationService.getAll());
        listBox.setRenderer(new ComponentRenderer<>(politicalOrganization -> {
            Span name = new Span(politicalOrganization.getName());
            Span code = new Span(politicalOrganization.getCode());
            code.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout column = new VerticalLayout(name, code);
            column.setPadding(false);
            column.setSpacing(false);

            column.getStyle().set("line-height", "var(--lumo-line-height-m)");

            return column;
        }));

        VerticalLayout fieldLayout = new VerticalLayout(textArea, listBox);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            chosenPoliticalOrganizations = new ArrayList<>();
            dialog.close();
        });
        Button saveButton = new Button("Odaberi", e -> {
            drawingLayout.removeAll();
            chosenPoliticalOrganizations.clear();
            listBox.getSelectedItems().forEach(po -> chosenPoliticalOrganizations.add(po));
            dialog.close();
            choosePositionForPoliticalOrganizations(isMT);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return fieldLayout;
    }

    private void choosePositionForPoliticalOrganizations(boolean isMT) {
        List<ComboBox<PoliticalOrganizationEntity>> comboBoxes = new ArrayList<>();
        for (int i = 1; i <= chosenPoliticalOrganizations.size(); i++) { // Adjust the loop count as needed
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
            horizontalLayout.setWidthFull();

            // Create ComboBox
            ComboBox<PoliticalOrganizationEntity> comboBox = new ComboBox<>("Odaberite politički subjekat na " + i +". poziciji");
            comboBoxes.add(comboBox);
            comboBox.setItems(chosenPoliticalOrganizations);
            comboBox.setItemLabelGenerator(e -> e.getCode() + ": " + e.getName());
            comboBox.setWidthFull();

            // Add components to HorizontalLayout
            horizontalLayout.add(comboBox);

            // Add HorizontalLayout to the main layout
            drawingLayout.add(horizontalLayout);
        }
        Button button = new Button("Generiši biračke odbore");
        String fileTitle = "zrijebanje_" + System.currentTimeMillis() + ".xlsx";
        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {

            generateVotingCouncels(comboBoxes, isMT);
            String stringPath = generateExcelFileForDrawing(fileTitle);
            if(stringPath != null)
                return getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(button);

        button.setWidthFull();
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        drawingLayout.add(saveButtonAnchor);
    }

    private void generateVotingCouncels(List<ComboBox<PoliticalOrganizationEntity>> comboBoxes, boolean isMT) {
        Optional<ComboBox<PoliticalOrganizationEntity>> optionalComboBox = comboBoxes.stream().filter(c -> c.getValue() == null).findFirst();
        if(optionalComboBox.isPresent()) {
            ConfirmDialog confirmDialog = showAlert("Neispravno zrijebanje", "Na poziciji " + (comboBoxes.indexOf(optionalComboBox.get()) + 1) + " nije odabran politički subjekat!");
            confirmDialog.open();
            return;
        }

        Set<PoliticalOrganizationEntity> politicalOrganizationEntitiesSet = comboBoxes.stream().map(c -> c.getValue()).collect(Collectors.toSet());
        List<PoliticalOrganizationEntity> politicalOrganizationEntitiesList = comboBoxes.stream().map(c -> c.getValue()).collect(Collectors.toList());
        if(politicalOrganizationEntitiesSet.size() < this.chosenPoliticalOrganizations.size()) {
            for(PoliticalOrganizationEntity politicalOrganization : this.chosenPoliticalOrganizations) {
                if(politicalOrganizationEntitiesList.stream().filter(po -> po.equals(politicalOrganization)).count() > 1) {
                    ConfirmDialog confirmDialog = showAlert("Neispravno zrijebanje", "Politički subjekat " + politicalOrganization.getCode() +": " + politicalOrganization.getName() + " je odabran više puta");
                    confirmDialog.open();
                    return;
                }
            }
        }

        // drawNumber is the official order established at the regular drawing - MT reuses (a subset
        // of) the same subjects, so it must not renumber them from 1, which would overwrite that
        // record with MT-local numbers and lose the original sequence.
        if(isMT == false) {
            int i = 1; //Pozicija ide od prve!!!
            for(PoliticalOrganizationEntity politicalOrganization: politicalOrganizationEntitiesList) {
                politicalOrganization.setDrawNumber(i);
                politicalOrganizationRepository.save(politicalOrganization);
                i++;
            }
        }

        PoliticalOrganizationEntity gikEntity = politicalOrganizationRepository.findByCode("00000");

        // Voting councils attached to another one (e.g. "LIČNO"/"ODSUSTVO") share their primary
        // council's election board - they don't get their own members/president/deputies.
        List<VotingCouncelEntity> votingCouncels = null;
        if(isMT == false)
            votingCouncels = votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("МТ") == false && vc.getPrimaryVotingCouncel() == null).collect(Collectors.toList());
        else
            votingCouncels = votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("МТ") && vc.getPrimaryVotingCouncel() == null).collect(Collectors.toList());

        int participated = politicalOrganizationEntitiesList.size();

        Optional<TitleEntity> optionalMemberTitle = titleRepository.findById(1L);
        Optional<TitleEntity> optionalMemberDeputyTitle = titleRepository.findById(2L);

        TitleEntity memberTitle = optionalMemberTitle.get();
        TitleEntity memberDeputyTitle = optionalMemberDeputyTitle.get();

        // Rotating draw: each council's real-subject positions continue where the previous
        // council's left off (mod participated), instead of every council restarting from
        // subject #1 - otherwise subjects beyond position numberOfMembers (e.g. the 5th+ of 5)
        // would never be assigned anywhere. Councils with fewer real subjects than positions
        // still get the leftover positions filled with GIK, same as before.
        //
        // For MT, the rotation continues from the subject right after whoever got the regular
        // draw's very last position, instead of restarting at subject #1 - MT positions are a
        // continuation of the same draw, not a separate one.
        int rotationOffset = 0;
        if(isMT) {
            Long nextOrganizationId = findNextOrganizationIdAfterRegularDraw();
            if(nextOrganizationId != null) {
                for(int idx = 0; idx < politicalOrganizationEntitiesList.size(); idx++) {
                    if(nextOrganizationId.equals(politicalOrganizationEntitiesList.get(idx).getId())) {
                        rotationOffset = idx;
                        break;
                    }
                }
            }
        }
        for(VotingCouncelEntity votingCouncel: votingCouncels) {
            int realSlots = Math.min(votingCouncel.getNumberOfMembers(), participated);
            for(int j = 0; j < votingCouncel.getNumberOfMembers(); j++) {
                PoliticalOrganizationEntity assignedOrganization = (j < realSlots)
                        ? politicalOrganizationEntitiesList.get((rotationOffset + j) % participated)
                        : gikEntity;

                ConstraintEntity constraint = new ConstraintEntity();
                constraint.setVotingCouncel(votingCouncel);
                constraint.setPoliticalOrganization(assignedOrganization);
                constraint.setTitle(new TitleEntity());
                constraint.setPosition(j + 1);
                constraint.setTitle(memberTitle);
                constraintRepository.save(constraint);

                ConstraintEntity deputyConstraint = new ConstraintEntity();
                deputyConstraint.setVotingCouncel(votingCouncel);
                deputyConstraint.setPoliticalOrganization(assignedOrganization);
                deputyConstraint.setTitle(new TitleEntity());
                deputyConstraint.setPosition(j + 1);
                deputyConstraint.setTitle(memberDeputyTitle);
                constraintRepository.save(deputyConstraint);
            }
            rotationOffset = (rotationOffset + realSlots) % participated;
        }
    }

    // Id of the political organization that should receive the very next position after the
    // regular draw's last filled slot (drawNumber i+1, if the regular draw's last slot went to
    // drawNumber i) - the MT rotation's starting point.
    //
    // This is computed from the total count of (non-GIK) positions filled across the whole
    // regular draw, mod the number of regular participants - not by looking up "the last
    // council" via voting_councel's row order, which findAll() doesn't guarantee matches the
    // councils' numeric code order (a council can have a lower code but a higher database id).
    // The total-count sum is order-independent, so it's unaffected by that.
    private Long findNextOrganizationIdAfterRegularDraw() {
        List<PoliticalOrganizationEntity> regularParticipants = politicalOrganizationRepository.findAll().stream()
                .filter(po -> po.getDrawNumber() != null && po.getDrawNumber() > 0)
                .sorted(Comparator.comparing(PoliticalOrganizationEntity::getDrawNumber))
                .collect(Collectors.toList());
        if(regularParticipants.isEmpty())
            return null;

        List<VotingCouncelEntity> regularVotingCouncels = votingCouncelRepository.findAll().stream()
                .filter(vc -> vc.getCode().contains("МТ") == false && vc.getPrimaryVotingCouncel() == null)
                .collect(Collectors.toList());

        int regularParticipated = regularParticipants.size();
        int totalRealSlots = regularVotingCouncels.stream()
                .mapToInt(vc -> Math.min(vc.getNumberOfMembers(), regularParticipated))
                .sum();

        int nextIndex = totalRealSlots % regularParticipated;
        return regularParticipants.get(nextIndex).getId();
    }

    private ConfirmDialog showAlert(String title, String body) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(body);

        dialog.setConfirmText("OK");
        return dialog;
    }

    private String generateExcelFileForDrawing(String fileTitle) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("БО-жријебање");

        //setovanje sirine kolona (poklapa se sa resources/documents/2026/zapisnik.xlsx)
        sheet.setColumnWidth(0, (int) Math.round(COLUMN_CHARS_0 * 256));
        sheet.setColumnWidth(1, (int) Math.round(COLUMN_CHARS_1 * 256));
        sheet.setColumnWidth(2, (int) Math.round(COLUMN_CHARS_2 * 256));
        sheet.setColumnWidth(3, (int) Math.round(COLUMN_CHARS_3 * 256));
        sheet.setColumnWidth(4, (int) Math.round(COLUMN_CHARS_4 * 256));
        sheet.setColumnWidth(5, (int) Math.round(COLUMN_CHARS_5 * 256));

        //Podešavanje štampe: pejzažna orijentacija, uklapanje po širini na jednu stranicu
        //(visina/broj stranica nije ograničen, jer tabela ima mnogo redova)
        sheet.setFitToPage(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        printSetup.setFitWidth((short) 1);
        printSetup.setFitHeight((short) 0);
        sheet.setMargin(Sheet.LeftMargin, 0.3);
        sheet.setMargin(Sheet.RightMargin, 0.3);
        sheet.setMargin(Sheet.TopMargin, 0.4);
        sheet.setMargin(Sheet.BottomMargin, 0.4);

        // Create a regular font
        Font regularFont = workbook.createFont();
        regularFont.setBold(false);
        regularFont.setFontHeightInPoints((short) 16);
        // Create a cell style and set the bold font
        CellStyle regularStyle = workbook.createCellStyle();
        regularStyle.setFont(regularFont);

        // Create a bold font
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 16);
        // Create a cell style and set the bold font
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);

        // Create a cell style and set the bold font
        CellStyle boldStyleWithAlignment = workbook.createCellStyle();
        boldStyleWithAlignment.setFont(boldFont);
        boldStyleWithAlignment.setVerticalAlignment(VerticalAlignment.TOP);
        boldStyleWithAlignment.setAlignment(HorizontalAlignment.CENTER);
        boldStyleWithAlignment.setWrapText(true);

        // Create a cell style and set the bold font
        CellStyle regularStyleWithAlignment = workbook.createCellStyle();
        regularStyleWithAlignment.setFont(regularFont);
        regularStyleWithAlignment.setVerticalAlignment(VerticalAlignment.TOP);
        regularStyleWithAlignment.setAlignment(HorizontalAlignment.CENTER);
        regularStyleWithAlignment.setWrapText(true);

        //Dodavanje imena entiteta
        XSSFRow row = sheet.createRow(1);
        XSSFCell entityCell = row.createCell(0);
        row.createCell(1);
        entityCell.setCellValue("РЕПУБЛИКА СРПСКА");
        entityCell.setCellStyle(boldStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                1,  // start row
                1,  // end row
                0,  // start column
                1   // end column
        ));

        //Dodavanje imena grada
        row = sheet.createRow(2);
        XSSFCell cityCell = row.createCell(0);
        row.createCell(1);
        cityCell.setCellValue("Град Бања Лука");
        cityCell.setCellStyle(boldStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                2,  // start row
                2,  // end row
                0,  // start column
                1   // end column
        ));

        //Dodavanje imena grada
        row = sheet.createRow(3);
        XSSFCell gikCell = row.createCell(0);
        row.createCell(1);
        gikCell.setCellValue("Градска изборна комисија");
        gikCell.setCellStyle(boldStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                3,  // start row
                3,  // end row
                0,  // start column
                1   // end column
        ));

        //Dodavanje broja odluke
        row = sheet.createRow(6);
        XSSFCell decisionNumber = row.createCell(0);
         row.createCell(1);
        decisionNumber.setCellValue("Број: 01-03-1/26-85");
        decisionNumber.setCellStyle(regularStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                6,  // start row
                6,  // end row
                0,  // start column
                1   // end column
        ));

        //Dodavanje datuma
        row = sheet.createRow(7);
        XSSFCell dateNumber1 = row.createCell(0);
        row.createCell(1);
        dateNumber1.setCellValue("Датум: 13.08.2026");
        dateNumber1.setCellStyle(regularStyle);
        sheet.addMergedRegion(new CellRangeAddress(
                7,  // start row
                7,  // end row
                0,  // start column
                1   // end column
        ));

        //Dodavanje datuma
        row = sheet.createRow(12);
        XSSFCell title = row.createCell(0);
        row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        row.createCell(4);
        row.setHeightInPoints(60f);
        title.setCellValue("З  А  П  И  С  Н  И  К  \n СА ЈАВНЕ ПРЕЗЕНТАЦИЈЕ ЈАВНЕ ДОДЈЕЛЕ ПОЗИЦИЈА У БИРАЧКИМ ОДБОРИМА У ОИЈ 034Б - БАЊА ЛУКА\nодржаног 13. 08. 2026. године у 20.00 часова, у сали 33 у Градској управи Бања Лука");

        title.setCellStyle(boldStyleWithAlignment);
        sheet.addMergedRegion(new CellRangeAddress(
                12,  // start row
                12,  // end row
                0,  // start column
                5   // end column
        ));

        //Dodavanje datuma
        row = sheet.createRow(17);
        XSSFCell description = row.createCell(0);
        row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        row.createCell(4);
        row.createCell(5);
        row.setHeightInPoints(260.1f);
        description.setCellValue("Јавној презентацији додјелe позиција у бирачким одборима  у основној изборној јединици 034Б-Бања Лука присуствују овлашћени представници политичких субјеката, предсједник и чланови Градске изборне комисије Бања Лука и информатичка подршка. \n" +
                "\n" +
                "Изборна јединица 034Б Бања Лука има 256 бирачких одбора са 4 члана и 5 бирачких одбора са 2 члана, што је укупно 261 бирачки одбор са 1034 члана и исто толико замјеника, што је укупно 2068 позицијa за редовна бирачка мјеста. \n" +
                "У распоређивању позиција у бирачким одборима учествује 16 овјерених политичких субјеката, који су добили мандате у тренутку потврђивања резултата Општих избора 2022. године.\n" +
                "Централна изборна комисија БиХ доставила је свим изборним комисијама листу политичких субјеката који, у складу са одредбом члана 2.19 став (7) Изборног закона БиХ, учествују у жријебању и имају право достављати приједлоге за чланове бирачких одбора. Након жријебања, утврђен је редослијед политичких субјеката за попуњавање позиција у бирачким одборима (Табела 1.).            \n" +
                "\n");
        description.setCellStyle(regularStyleWithAlignment);
        sheet.addMergedRegion(new CellRangeAddress(
                17,  // start row
                17,  // end row
                0,  // start column
                5   // end column
        ));

        CellStyle tableOneHeaderStyle = workbook.createCellStyle();
        tableOneHeaderStyle.cloneStyleFrom(boldStyleWithAlignment);
        tableOneHeaderStyle.setBorderBottom(BorderStyle.THIN);
        tableOneHeaderStyle.setBorderTop(BorderStyle.THIN);
        tableOneHeaderStyle.setBorderLeft(BorderStyle.THIN);
        tableOneHeaderStyle.setBorderRight(BorderStyle.THIN);

        CellStyle tableOneOrderStyle = workbook.createCellStyle();
        tableOneOrderStyle.cloneStyleFrom(regularStyleWithAlignment);
        tableOneOrderStyle.setAlignment(HorizontalAlignment.CENTER);
        tableOneOrderStyle.setBorderBottom(BorderStyle.THIN);
        tableOneOrderStyle.setBorderTop(BorderStyle.THIN);
        tableOneOrderStyle.setBorderLeft(BorderStyle.THIN);
        tableOneOrderStyle.setBorderRight(BorderStyle.THIN);

        CellStyle tableOneBodyStyle = workbook.createCellStyle();
        tableOneBodyStyle.cloneStyleFrom(regularStyleWithAlignment);
        tableOneBodyStyle.setBorderBottom(BorderStyle.THIN);
        tableOneBodyStyle.setBorderTop(BorderStyle.THIN);
        tableOneBodyStyle.setBorderLeft(BorderStyle.THIN);
        tableOneBodyStyle.setBorderRight(BorderStyle.THIN);

        // Political organization codes are always a 5-digit number with leading zeros
        // (e.g. "00027", "00000") - stored as a real number with a "00000" format so the
        // leading zeros survive and Excel doesn't flag it as "number stored as text", and
        // never wrapped so the code always stays on one line.
        CellStyle politicalOrgCodeStyle = workbook.createCellStyle();
        politicalOrgCodeStyle.cloneStyleFrom(tableOneBodyStyle);
        politicalOrgCodeStyle.setWrapText(false);
        politicalOrgCodeStyle.setAlignment(HorizontalAlignment.CENTER);
        politicalOrgCodeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        politicalOrgCodeStyle.setDataFormat(workbook.createDataFormat().getFormat("00000"));

        int rowNumber = 21;
        row = sheet.createRow(rowNumber);
        row.createCell(0);
        XSSFCell orderNumber = row.createCell(1);
        orderNumber.setCellValue("Редни број");
        orderNumber.setCellStyle(tableOneHeaderStyle);
        XSSFCell code = row.createCell(2);
        code.setCellValue("Шифра");
        code.setCellStyle(tableOneHeaderStyle);
        XSSFCell name = row.createCell(3);
        name.setCellValue("ПОЛИТИЧКИ СУБЈЕКТИ");
        name.setCellStyle(tableOneHeaderStyle);
        rowNumber++;

        int startRowNumber = rowNumber;
        List<PoliticalOrganizationEntity> politicalOrganizationEntities = politicalOrganizationService.getAll().stream()
                .filter(po -> po.getDrawNumber() != null && po.getDrawNumber() > 0)
                .sorted(Comparator.comparing(PoliticalOrganizationEntity::getDrawNumber))
                .collect(Collectors.toList());
        for(PoliticalOrganizationEntity politicalOrganization: politicalOrganizationEntities) {
            row = sheet.createRow(rowNumber);
            row.createCell(0);
            orderNumber = row.createCell(1);
            orderNumber.setCellValue(politicalOrganization.getDrawNumber() + ".");
            orderNumber.setCellStyle(tableOneOrderStyle);
            code = row.createCell(2);
            code.setCellValue(Integer.parseInt(politicalOrganization.getCode()));
            code.setCellStyle(politicalOrgCodeStyle);
            name = row.createCell(3);
            name.setCellValue(latinToCyrillicConverter.convert(politicalOrganization.getName()));
            name.setCellStyle(tableOneBodyStyle);

            rowNumber++;
        }

        rowNumber += 2;

        row = sheet.createRow(rowNumber);
        XSSFCell tableTwoDescription = row.createCell(0);
        row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        row.createCell(4);
        row.createCell(5);
        row.setHeightInPoints(49.5f);
        tableTwoDescription.setCellValue("У наставку је табела (Табела 2.) са додијељеним позицијама у бирачким одборима, уз напомену да за сваку додијељену позицију члана бирачког одбора овјерени политички субјекат доставља приједлоге и за члана и за његовог замјеника.");
        tableTwoDescription.setCellStyle(regularStyleWithAlignment);
        sheet.addMergedRegion(new CellRangeAddress(
                rowNumber,  // start row
                rowNumber,  // end row
                0,  // start column
                5   // end column
        ));

        rowNumber += 2;

        CellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setFont(boldFont);
        borderedStyle.setWrapText(true);
        borderedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        borderedStyle.setAlignment(HorizontalAlignment.CENTER);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);

        int votingCouncelHeaderRowIndex = rowNumber;
        row = sheet.createRow(rowNumber);
        row.setHeightInPoints(60);
        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("Шифра бирачког мјеста");

        cell = row.createCell(1);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("НАЗИВ БИРАЧКОГ МЈЕСТА / ШИФРА ПОЛИТИЧКОГ СУБЈЕКТА");
        row.createCell(2).setCellStyle(borderedStyle);
        CellRangeAddress nameHeaderRange = new CellRangeAddress(
                rowNumber,  // start row
                rowNumber,  // end row
                1,  // start column
                2   // end column
        );
        sheet.addMergedRegion(nameHeaderRange);
        // Border on a merged range's individual cell styles alone leaves the merge's outer edge
        // unclosed (e.g. the right border is missing) - RegionUtil draws it across the whole range.
        RegionUtil.setBorderTop(BorderStyle.THIN, nameHeaderRange, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, nameHeaderRange, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, nameHeaderRange, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, nameHeaderRange, sheet);

        cell = row.createCell(3);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("ЛОКАЦИЈА БИРАЧКОГ МЈЕСТА / НАЗИВ ПОЛИТИЧКОГ СУБЈЕКАТА");

        cell = row.createCell(4);
        cell.setCellStyle(borderedStyle);
        //cell.setCellValue("БРОЈ БИРАЧА");

        cell = row.createCell(5);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("БО");

        rowNumber++;

        CellStyle regularStyleWithPurple = workbook.createCellStyle();
        regularStyleWithPurple.setFont(regularFont);
        XSSFCellStyle regularStyleWithPurpleBackground = (XSSFCellStyle) regularStyleWithPurple; // Cast to XSSFCellStyle for XSSF workbooks
        XSSFColor color = new XSSFColor(new java.awt.Color(204, 204, 255), null);
        regularStyleWithPurpleBackground.setFillForegroundColor(color);
        regularStyleWithPurpleBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        regularStyleWithPurpleBackground.setWrapText(true);
        regularStyleWithPurpleBackground.setVerticalAlignment(VerticalAlignment.TOP);

        XSSFCellStyle regularStyleWithPurpleBackgroundAndRightAlignment = workbook.createCellStyle();
        regularStyleWithPurpleBackgroundAndRightAlignment.setFont(regularFont);
        regularStyleWithPurpleBackgroundAndRightAlignment.setFillForegroundColor(color);
        regularStyleWithPurpleBackgroundAndRightAlignment.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        regularStyleWithPurpleBackgroundAndRightAlignment.setAlignment(HorizontalAlignment.RIGHT);
        regularStyleWithPurpleBackgroundAndRightAlignment.setVerticalAlignment(VerticalAlignment.TOP);

        CellStyle regularStyleWrapped = workbook.createCellStyle();
        regularStyleWrapped.cloneStyleFrom(regularStyle);
        regularStyleWrapped.setWrapText(true);
        regularStyleWrapped.setVerticalAlignment(VerticalAlignment.TOP);

        // Same reasoning as politicalOrgCodeStyle above - a real number formatted "00000" so
        // leading zeros survive, never wrapped so the code stays on one line.
        CellStyle candidateCodeStyle = workbook.createCellStyle();
        candidateCodeStyle.cloneStyleFrom(regularStyle);
        candidateCodeStyle.setWrapText(false);
        candidateCodeStyle.setAlignment(HorizontalAlignment.CENTER);
        candidateCodeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        candidateCodeStyle.setDataFormat(workbook.createDataFormat().getFormat("00000"));

        // Councils attached to another one aren't shown separately - see the note above.
        List<VotingCouncelEntity> votingCouncelEntities = votingCouncelRepository.findAll().stream()
                .filter(vc -> vc.getPrimaryVotingCouncel() == null)
                .collect(Collectors.toList());
        for(VotingCouncelEntity votingCouncel: votingCouncelEntities) {
            row = sheet.createRow(rowNumber);

            cell = row.createCell(0);
            cell.setCellValue(votingCouncel.getCode());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(1);
            cell.setCellValue(votingCouncel.getDisplayName());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(2);
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(3);
            cell.setCellValue(votingCouncel.getLocation());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(4);
            cell.setCellValue(votingCouncel.getTotalNumberOfVoters());
            cell.setCellStyle(regularStyleWithPurpleBackgroundAndRightAlignment);

            cell = row.createCell(5);
            cell.setCellStyle(regularStyleWithPurpleBackgroundAndRightAlignment);
            if(votingCouncel.getNumberOfMembers() == 4)
                cell.setCellValue("4+4");
            else if(votingCouncel.getNumberOfMembers() == 2)
                cell.setCellValue("2+2");

            growRowHeightToFitWrappedText(row, Map.of(1, COLUMN_CHARS_1, 3, COLUMN_CHARS_3));

            rowNumber++;
            List<ConstraintEntity> constraintEntities = votingCouncel.getConstraints().stream()
                    .filter(constraint -> constraint.getTitle().getId() == 1)
                    .sorted(Comparator.comparing(ConstraintEntity::getPosition))
                    .collect(Collectors.toList());

            for(ConstraintEntity constraint: constraintEntities) {
                row = sheet.createRow(rowNumber);

                row.createCell(0);

                cell = row.createCell(1);
                cell.setCellValue(Integer.parseInt(constraint.getPoliticalOrganization().getCode()));
                cell.setCellStyle(candidateCodeStyle);

                row.createCell(2);
                sheet.addMergedRegion(new CellRangeAddress(
                        rowNumber,  // start row
                        rowNumber,  // end row
                        1,  // start column
                        2   // end column
                ));

                cell = row.createCell(3);
                cell.setCellValue(latinToCyrillicConverter.convert(constraint.getPoliticalOrganization().getName()));
                cell.setCellStyle(regularStyleWrapped);

                row.createCell(4);
                row.createCell(5);

                sheet.addMergedRegion(new CellRangeAddress(
                        rowNumber,  // start row
                        rowNumber,  // end row
                        3,  // start column
                        5   // end column
                ));

                growRowHeightToFitWrappedText(row, Map.of(
                        1, COLUMN_CHARS_1 + COLUMN_CHARS_2,
                        3, COLUMN_CHARS_3 + COLUMN_CHARS_4 + COLUMN_CHARS_5));

                rowNumber++;
            }

            row = sheet.createRow(rowNumber);
            sheet.addMergedRegion(new CellRangeAddress(
                    rowNumber,  // start row
                    rowNumber,  // end row
                    1,  // start column
                    5   // end column
            ));
            rowNumber++;
        }

        //Ponavljanje zaglavlja tabele ("Šifra bir. mjesta" i sl.) na svakoj odštampanoj stranici
        sheet.setRepeatingRows(new CellRangeAddress(votingCouncelHeaderRowIndex, votingCouncelHeaderRowIndex, 0, 5));

        rowNumber += 5;

        row = sheet.createRow(rowNumber);
        XSSFCell footer1 = row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        footer1.setCellStyle(regularStyle);
        footer1.setCellValue("Записник са додјеле  позиција у бирачким одборима у основној изборној јединици 034Б - Бања Лука, доставља се:");
        rowNumber++;

        row = sheet.createRow(rowNumber);
        XSSFCell footer2 = row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        footer2.setCellStyle(regularStyle);
        footer2.setCellValue("1. Свим политичким субјектима, " + politicalOrganizationEntities.size() + "x.");
        rowNumber++;

        row = sheet.createRow(rowNumber);
        XSSFCell footer3 = row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        footer3.setCellStyle(regularStyle);
        footer3.setCellValue("2. Централној изборној комисији БиХ, на тражење");
        rowNumber++;

        row = sheet.createRow(rowNumber);
        XSSFCell footer4 = row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        footer4.setCellStyle(regularStyle);
        footer4.setCellValue("3. Попис аката.");
        rowNumber++;

        row = sheet.createRow(rowNumber);
        XSSFCell footer5 = row.createCell(1);
        row.createCell(2);
        row.createCell(3);
        footer5.setCellStyle(regularStyle);
        footer5.setCellValue("4. Евиденција Градске изборне комисије.");
        rowNumber++;

        rowNumber+=2;
        row = sheet.createRow(rowNumber);
        XSSFCell footer6 = row.createCell(3);
        footer6.setCellValue("П Р Е Д С Ј Е Д Н И К");
        footer6.setCellStyle(boldStyleWithAlignment);

        rowNumber+=2;
        row = sheet.createRow(rowNumber);
        XSSFCell footer7 = row.createCell(3);
        footer7.setCellValue("Дубравко Малинић");
        footer7.setCellStyle(boldStyleWithAlignment);

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

    /**
     * Excel auto-fits row height for wrapped text when the file is opened, but that isn't reliable
     * enough for printing straight away - a row that's too short lets a wrapped line spill into the
     * row below it. This estimates how many lines each given cell's text will wrap to (based on its
     * column width in characters) and grows the row height to fit the tallest one, so printed text
     * never overlaps between rows. Only touches the row when wrapping is actually needed.
     *
     * @param columnCharWidths column index (the merged range's first column) -> that column's
     *                         (or merged range's combined) width in characters
     */
    private void growRowHeightToFitWrappedText(Row row, Map<Integer, Double> columnCharWidths) {
        Workbook workbook = row.getSheet().getWorkbook();
        int defaultCharWidth = SheetUtil.getDefaultCharWidth(workbook);
        DataFormatter formatter = new DataFormatter();
        int maxLines = 1;
        boolean sawStringContent = false;
        for (Map.Entry<Integer, Double> entry : columnCharWidths.entrySet()) {
            Cell cell = row.getCell(entry.getKey());
            if (cell == null || cell.getCellType() != CellType.STRING)
                continue;
            String value = cell.getStringCellValue();
            if (value == null || value.isEmpty())
                continue;
            // getCellWidth measures the widest single line of the cell's text with its actual font
            // via AWT metrics - the same mechanism POI uses for autoSizeColumn - instead of assuming
            // a fixed chars-per-line ratio, which over- or under-counts depending on glyph width and
            // font size. But it collapses any hard line breaks ("\n") already in the text down to
            // that one widest line, so a 3-line title measures the same as its longest line alone.
            // Each "\n" segment is measured (and wrapped) separately here and the per-segment line
            // counts are summed, or a hard-broken cell ends up the same height as a single long line.
            int lines = 0;
            for (String segment : value.split("\n", -1)) {
                if (segment.isEmpty()) {
                    lines += 1;
                    continue;
                }
                cell.setCellValue(segment);
                double requiredWidthChars = SheetUtil.getCellWidth(cell, defaultCharWidth, formatter, false);
                lines += (int) Math.ceil(requiredWidthChars / entry.getValue());
            }
            cell.setCellValue(value);
            maxLines = Math.max(maxLines, lines);
            sawStringContent = true;
        }
        // Always (not just when wrapping to >1 line) - the sheet's default row height (15.75pt)
        // is shorter than this table's 16pt font, so even a single-line row needs its height set
        // explicitly or the text gets clipped top/bottom.
        if (sawStringContent) {
            row.setHeightInPoints(maxLines * WRAPPED_LINE_HEIGHT_POINTS);
        }
    }

    private InputStream getStream(String fileString) {
        File file = new File(fileString);
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

}
