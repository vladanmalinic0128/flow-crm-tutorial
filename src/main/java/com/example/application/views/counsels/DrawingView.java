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
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "bo/zrijebanje", layout = MainLayout.class)
public class DrawingView extends VerticalLayout {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
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

        int i = 1; //Pozicija ide od prve!!!
        for(PoliticalOrganizationEntity politicalOrganization: politicalOrganizationEntitiesList) {
            politicalOrganization.setDrawNumber(i);
            politicalOrganizationRepository.save(politicalOrganization);
            i++;
        }

        List<VotingCouncelEntity> votingCouncels = null;
        if(isMT == false)
            votingCouncels = votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("МТ") == false).collect(Collectors.toList());
        else
            votingCouncels = votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("МТ")).collect(Collectors.toList());

        if(isMT == false)
            i = 0; //Sluzi da vodi racuna o zrijebanju
        else
            i = findLastConstraint() + 1;
        int participated = politicalOrganizationEntitiesList.size();

        Optional<TitleEntity> optionalMemberTitle = titleRepository.findById(1L);
        Optional<TitleEntity> optionalMemberDeputyTitle = titleRepository.findById(2L);

        TitleEntity memberTitle = optionalMemberTitle.get();
        TitleEntity memberDeputyTitle = optionalMemberDeputyTitle.get();

        for(VotingCouncelEntity votingCouncel: votingCouncels) {
            for(int j = 0; j < votingCouncel.getNumberOfMembers(); j++) {
                ConstraintEntity constraint = new ConstraintEntity();
                constraint.setVotingCouncel(votingCouncel);
                constraint.setPoliticalOrganization(politicalOrganizationEntitiesList.get(i % participated));
                constraint.setTitle(new TitleEntity());
                constraint.setPosition(j + 1);
                constraint.setTitle(memberTitle);
                constraintRepository.save(constraint);

                ConstraintEntity deputyConstraint = new ConstraintEntity();
                deputyConstraint.setVotingCouncel(votingCouncel);
                deputyConstraint.setPoliticalOrganization(politicalOrganizationEntitiesList.get(i % participated));
                deputyConstraint.setTitle(new TitleEntity());
                deputyConstraint.setPosition(j + 1);
                deputyConstraint.setTitle(memberDeputyTitle);
                constraintRepository.save(deputyConstraint);

                i++;
            }
        }
    }

    private int findLastConstraint() {
        Optional<VotingCouncelEntity> lastVotingCouncel  = votingCouncelRepository.findAll().stream().filter(vc -> vc.getCode().contains("Б")).sorted(Comparator.comparing(VotingCouncelEntity::getId).reversed()).findFirst();
        if(lastVotingCouncel.isEmpty())
            return -1;
        Optional<ConstraintEntity> constraint = lastVotingCouncel.get().getConstraints().stream().filter(c -> c.getTitle().getId().equals(1L)).sorted(Comparator.comparing(ConstraintEntity::getPosition).reversed()).findFirst();
        if(constraint.isEmpty())
            return -1;
        return constraint.get().getPoliticalOrganization().getDrawNumber() - 1;
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

        //setovanje sirine kolona
        sheet.setColumnWidth(0, 13 * 256);
        sheet.setColumnWidth(1, 45 * 256);
        sheet.setColumnWidth(2, 11 * 256);
        sheet.setColumnWidth(3, 90 * 256);
        sheet.setColumnWidth(4, 14 * 256);
        sheet.setColumnWidth(5, 8 * 256);

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

        // Create a cell style and set the bold font
        CellStyle regularStyleWithAlignment = workbook.createCellStyle();
        regularStyleWithAlignment.setFont(regularFont);
        regularStyleWithAlignment.setVerticalAlignment(VerticalAlignment.TOP);
        regularStyleWithAlignment.setAlignment(HorizontalAlignment.CENTER);

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
        decisionNumber.setCellValue("Број: 01-01-23");
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
        dateNumber1.setCellValue("Датум: 01.01.2023");
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
        row.createCell(5);
        row.setHeightInPoints(60);
        title.setCellValue("З  А  П  И  С  Н  И  К  \n СА ЈАВНЕ ПРЕЗЕНТАЦИЈЕ ЈАВНЕ ДОДЈЕЛЕ ПОЗИЦИЈА У БИРАЧКИМ ОДБОРИМА У ОИЈ 034Б - БАЊА ЛУКА\nодржаног 07. 08. 2024. године у 15.00 часова, у сали 33 у Градској управи Бања Лука");

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
        row.setHeightInPoints(260);
        description.setCellValue("Јавној презентацији додјелe позиција у бирачким одборима  у основној изборној јединици 034Б-Бања Лука присуствују овлашћени представници политичких субјеката, предсједник и чланови Градске изборне комисије Бања Лука и информатичка подршка. \n" +
                "\n" +
                "Изборна јединица 034Б Бања Лука има    ???       бирачких одбора са 5 чланова и      ???    бирачких одбора са  ??   члана, што је укупно        бирачких одбора са    ???      чланова и исто толико замјеника, што је укупно     ???     позиције за редовна бирачка мјеста. \n" +
                "У распоређивању позиција у бирачким одборима учествује       ????     овјерених политичких субјеката.\n" +
                "Прије презентације јавне додјеле позиција, утврђен је број кругова додјеле за редовна бирачка мјеста према формули из чл.12 Правилника Централне изборне комисије БиХ, који износи 20 . Политички субјекти су заступљени према омјеру учешћа у додјели који је утврдила Централна изборна комисија БиХ (Табела 1.).            ПРОВЈЕРИТИ !!!!!!!!!!!!!!!\n" +
                "\n" +
                "У наставку је табела (Табела 2.) са додијељеним позицијама у бирачким одборима, уз напомену да за сваку додијељену позицију члана бирачког одбора овјерени политички субјекат доставља приједлоге и за члана и за његовог замјеника. \n" +
                "\n");
        description.setCellStyle(regularStyleWithAlignment);
        sheet.addMergedRegion(new CellRangeAddress(
                17,  // start row
                17,  // end row
                0,  // start column
                5   // end column
        ));

        int rowNumber = 21;
        row = sheet.createRow(rowNumber);
        row.createCell(0);
        XSSFCell orderNumber = row.createCell(1);
        orderNumber.setCellValue("Редни број");
        orderNumber.setCellStyle(boldStyleWithAlignment);
        XSSFCell code = row.createCell(2);
        code.setCellValue("Шифра");
        code.setCellStyle(boldStyleWithAlignment);
        XSSFCell name = row.createCell(3);
        name.setCellValue("ПОЛИТИЧКИ СУБЈЕКТИ");
        name.setCellStyle(boldStyleWithAlignment);
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
            regularStyleWithAlignment.setAlignment(HorizontalAlignment.RIGHT);
            orderNumber.setCellStyle(regularStyleWithAlignment);
            regularStyleWithAlignment.setAlignment(HorizontalAlignment.CENTER);
            code = row.createCell(2);
            code.setCellValue(politicalOrganization.getCode());
            code.setCellStyle(regularStyleWithAlignment);
            name = row.createCell(3);
            name.setCellValue(latinToCyrillicConverter.convert(politicalOrganization.getName()));
            name.setCellStyle(regularStyleWithAlignment);

            rowNumber++;
        }

        rowNumber += 5;

        CellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setFont(boldFont);
        borderedStyle.setWrapText(true);
        borderedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        borderedStyle.setAlignment(HorizontalAlignment.CENTER);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);

        row = sheet.createRow(rowNumber);
        row.setHeightInPoints(60);
        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("Шифра бирачког мјеста");

        cell = row.createCell(1);
        row.createCell(2);
        cell.setCellStyle(borderedStyle);
        cell.setCellValue("НАЗИВ БИРАЧКОГ МЈЕСТА / ШИФРА ПОЛИТИЧКОГ СУБЈЕКТА");
        sheet.addMergedRegion(new CellRangeAddress(
                rowNumber,  // start row
                rowNumber,  // end row
                1,  // start column
                2   // end column
        ));

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

        XSSFCellStyle regularStyleWithPurpleBackgroundAndRightAlignment = workbook.createCellStyle();
        regularStyleWithPurpleBackgroundAndRightAlignment.setFont(regularFont);
        regularStyleWithPurpleBackgroundAndRightAlignment.setFillForegroundColor(color);
        regularStyleWithPurpleBackgroundAndRightAlignment.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        regularStyleWithPurpleBackgroundAndRightAlignment.setAlignment(HorizontalAlignment.RIGHT);

        List<VotingCouncelEntity> votingCouncelEntities = votingCouncelRepository.findAll();
        for(VotingCouncelEntity votingCouncel: votingCouncelEntities) {
            row = sheet.createRow(rowNumber);

            cell = row.createCell(0);
            cell.setCellValue(votingCouncel.getCode());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(1);
            cell.setCellValue(votingCouncel.getName());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(2);
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(3);
            cell.setCellValue(votingCouncel.getLocation());
            cell.setCellStyle(regularStyleWithPurpleBackground);

            cell = row.createCell(4);
            //cell.setCellValue(votingCouncel.getNumberOfVoters());
            cell.setCellStyle(regularStyleWithPurpleBackgroundAndRightAlignment);

            cell = row.createCell(5);
            cell.setCellStyle(regularStyleWithPurpleBackgroundAndRightAlignment);
            if(votingCouncel.getNumberOfMembers() == 4)
                cell.setCellValue("4+4");
            else if(votingCouncel.getNumberOfMembers() == 2)
                cell.setCellValue("2+2");

            rowNumber++;
            List<ConstraintEntity> constraintEntities = votingCouncel.getConstraints().stream()
                    .filter(constraint -> constraint.getTitle().getId() == 1)
                    .sorted(Comparator.comparing(ConstraintEntity::getPosition))
                    .collect(Collectors.toList());

            for(ConstraintEntity constraint: constraintEntities) {
                row = sheet.createRow(rowNumber);

                row.createCell(0);

                cell = row.createCell(1);
                cell.setCellValue(constraint.getPoliticalOrganization().getCode());
                cell.setCellStyle(regularStyle);

                row.createCell(2);
                sheet.addMergedRegion(new CellRangeAddress(
                        rowNumber,  // start row
                        rowNumber,  // end row
                        1,  // start column
                        2   // end column
                ));

                cell = row.createCell(3);
                cell.setCellValue(latinToCyrillicConverter.convert(constraint.getPoliticalOrganization().getName()));
                cell.setCellStyle(regularStyle);

                row.createCell(4);
                row.createCell(5);

                sheet.addMergedRegion(new CellRangeAddress(
                        rowNumber,  // start row
                        rowNumber,  // end row
                        3,  // start column
                        5   // end column
                ));

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
        footer2.setCellValue("1. Свим политичким субјектима, ____x.");
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
