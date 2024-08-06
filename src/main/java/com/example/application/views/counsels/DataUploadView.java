package com.example.application.views.counsels;

import com.example.application.entities.ConstraintEntity;
import com.example.application.repositories.ConstraintRepository;
import com.example.application.repositories.MemberRepository;
import com.example.application.services.CouncelUpdateXlsxService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@PermitAll
@Route(value = "bo/update", layout = MainLayout.class)
public class DataUploadView extends FormLayout {
    Upload upload = null;
    MemoryBuffer buffer = null;
    boolean isFileEmpty = true;

    List<ConstraintEntity> modifiedConstraints = null;
    private final CouncelUpdateXlsxService councelUpdateXlsxService;
    private final MemberRepository memberRepository;
    private final ConstraintRepository constraintRepository;

    public DataUploadView(CouncelUpdateXlsxService councelUpdateXlsxService, MemberRepository memberRepository, ConstraintRepository constraintRepository) {
        this.councelUpdateXlsxService = councelUpdateXlsxService;
        this.memberRepository = memberRepository;
        this.constraintRepository = constraintRepository;

        addClassName("adding-stack-form");

        upload = configureFileUpload();

        Button executeButton = new Button("Ažuriraj tabelu biračkih odbora");
        executeButton.addClickListener(event -> handleExecuteButtonClick());
        executeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //this.setWidth("100%");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        this.setWidth("50%");

        this.getStyle().set("margin", "0 auto");
        this.getStyle().set("margin-top", "20px");

        add(upload);
        add(executeButton);

        setColspan(upload, 2);
        setColspan(executeButton, 2);
    }

    private Upload configureFileUpload() {
        buffer = new MemoryBuffer();

        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);
        upload.setWidthFull();

        // Label for upload
        NativeLabel dropEnabledLabel = new NativeLabel("Postavite excel fajl ovde");
        dropEnabledLabel.getStyle().set("font-weight", "600");
        upload.setId("upload-drop-enabled");
        dropEnabledLabel.setFor(upload.getId().get());

        upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        upload.setMaxFiles(1);

        // Style upload component
        upload.getElement().getStyle().set("width", "80%");

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            this.updateVotingCouncelsThroughXlsxFile(inputStream);
        });

        upload.addFileRemovedListener(event -> isFileEmpty = true);


        return upload;
    }

    private void updateVotingCouncelsThroughXlsxFile(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            // Iterate over each sheet in the workbook
            modifiedConstraints = councelUpdateXlsxService.getModifiedConstraints(workbook);
//            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
//                Sheet sheet = workbook.getSheetAt(sheetIndex);
//                System.out.println("Sheet Name: " + sheet.getSheetName());
//
//                // Iterate over each row in the sheet
//                for (Row row : sheet) {
//                    // Iterate over each cell in the row
//                    for (Cell cell : row) {
//                        String cellValue = getCellValue(cell);
//                        String cellAddress = new CellReference(cell.getRowIndex(), cell.getColumnIndex()).formatAsString();
//                        System.out.println("Cell Address: " + cellAddress + " - Value: " + cellValue);
//                    }
//                }
//            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void handleExecuteButtonClick() {
        if(modifiedConstraints == null) {
            ConfirmDialog confirmDialog = showAlert("Nema fajla", "Molimo Vas da odaberete fajl");
            confirmDialog.open();
            return;
        }

        boolean skipEmptyRows = true;


        for(ConstraintEntity c: modifiedConstraints) {
            memberRepository.save(c.getMember());
            constraintRepository.save(c);
        }
    }

    private ConfirmDialog showAlert(String title, String body) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(body);

        dialog.setConfirmText("OK");
        return dialog;
    }
}
