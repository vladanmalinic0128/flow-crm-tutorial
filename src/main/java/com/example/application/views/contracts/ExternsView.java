package com.example.application.views.contracts;

import com.example.application.entities.ConstraintEntity;
import com.example.application.repositories.ConstraintRepository;
import com.example.application.repositories.MemberRepository;
import com.example.application.services.CollaboratorsXlsxService;
import com.example.application.services.CouncelUpdateXlsxService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@PermitAll
@Route(value = "spoljasnji-saradnici", layout = MainLayout.class)
public class ExternsView extends FormLayout {
    Upload upload = null;
    MemoryBuffer buffer = null;
    boolean isFileEmpty = true;
    private final CollaboratorsXlsxService collaboratorsXlsxService;


    public ExternsView(CollaboratorsXlsxService collaboratorsXlsxService) {
        this.collaboratorsXlsxService = collaboratorsXlsxService;

        addClassName("adding-stack-form");

        upload = configureFileUpload();


        Button executeButton = new Button("Ažuriraj tabelu spoljašnjih saradnika");
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
            //this.updateVotingCouncelsThroughXlsxFile(inputStream, checkbox.getOptionalValue());
            this.updateExterns(inputStream);
        });

        upload.addFileRemovedListener(event -> isFileEmpty = true);


        return upload;
    }

    private void updateExterns(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            // Iterate over each sheet in the workbook
//            modifiedConstraints = collaboratorsXlsxService.createExternCollaborators(workbook);
//
//            for(ConstraintEntity c: modifiedConstraints) {
//                memberRepository.save(c.getMember());
//                constraintRepository.save(c);
//            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void handleExecuteButtonClick() {
//        if(modifiedConstraints == null) {
//            ConfirmDialog confirmDialog = showAlert("Nema fajla", "Molimo Vas da odaberete fajl");
//            confirmDialog.open();
//            return;
//        }
//
//        for(ConstraintEntity c: modifiedConstraints) {
//            memberRepository.save(c.getMember());
//            constraintRepository.save(c);
//        }
    }

    private ConfirmDialog showAlert(String title, String body) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(body);

        dialog.setConfirmText("OK");
        return dialog;
    }
}
