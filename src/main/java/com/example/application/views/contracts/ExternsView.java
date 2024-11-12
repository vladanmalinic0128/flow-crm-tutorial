package com.example.application.views.contracts;

import com.example.application.entities.ConstraintEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.ConstraintRepository;
import com.example.application.repositories.MemberRepository;
import com.example.application.services.CollaboratorsXlsxService;
import com.example.application.services.CouncelUpdateXlsxService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RolesAllowed("ADMIN")
@Route(value = "spoljasnji-saradnici", layout = MainLayout.class)
public class ExternsView extends FormLayout {
    Upload upload = null;
    MemoryBuffer buffer = null;
    boolean isFileEmpty = true;
    private final CollaboratorsXlsxService collaboratorsXlsxService;

    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    public ExternsView(CollaboratorsXlsxService collaboratorsXlsxService) {
        this.collaboratorsXlsxService = collaboratorsXlsxService;

        addClassName("adding-stack-form");

        upload = configureFileUpload();

        //this.setWidth("100%");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        this.setWidth("50%");

        this.getStyle().set("margin", "0 auto");
        this.getStyle().set("margin-top", "20px");

        add(upload);
        setColspan(upload, 2);

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button contracts = new Button("Ugovori za spoljne saradnike", downloadIcon);

        contracts.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        contracts.setWidthFull();

        contracts.addClickListener(e -> {
            Dialog dialog = createDialogForContracts();
            dialog.open();
        });


        add(contracts);
        setColspan(contracts, 2);

        downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button decision = new Button("Odluka za spoljne saradnike", downloadIcon);

        decision.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_PRIMARY);
        decision.setWidthFull();

        decision.addClickListener(e -> {
            Dialog dialog = createDialogForDecision();
            dialog.open();
        });


        add(decision);
        setColspan(decision, 2);

        downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button bankReport = new Button("Spisak spoljnih saradnika po bankama", downloadIcon);

        bankReport.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        bankReport.setWidthFull();

        bankReport.addClickListener(e -> {
            Dialog dialog = createDialogForBankReport();
            dialog.open();
        });
        add(bankReport);
        setColspan(bankReport, 2);
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
        upload.getElement().getStyle().set("min-width", "80%");

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            collaboratorsXlsxService.updateCollaboratorsThroughXlsxFile(inputStream, true);
        });

        upload.addFileRemovedListener(event -> isFileEmpty = true);


        return upload;
    }

    private Dialog createDialogForContracts() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader("Generisanje ugovora"));

        VerticalLayout dialogLayout = createDialogLayoutForContracts(dialog);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private Dialog createDialogForDecision() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader("Generisanje odluke"));

        VerticalLayout dialogLayout = createDialogLayoutForDecision(dialog);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private Dialog createDialogForBankReport() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader("Generisanje izvjestaja za finansije"));

        VerticalLayout dialogLayout = createDialogLayoutForBankReport(dialog);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private H2 createDialogHeader(String title) {
        H2 headline = new H2(title);
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayoutForContracts(Dialog dialog) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = "eksterni_saradnici_ugovori" + "_" + System.currentTimeMillis() + ".pdf";

        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = null;
            try {
                stringPath = collaboratorsXlsxService.generateContracts(fileTitle, scripts.getValue(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
            if(stringPath != null)
                return collaboratorsXlsxService.getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButtonAnchor);

        return fieldLayout;
    }

    private VerticalLayout createDialogLayoutForDecision(Dialog dialog) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = "eksterni_saradnici_odluka" + "_" + System.currentTimeMillis() + ".docx";

        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = null;
            try {
                stringPath = collaboratorsXlsxService.generateDecision(fileTitle, scripts.getValue(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InvalidFormatException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
            if(stringPath != null)
                return collaboratorsXlsxService.getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButtonAnchor);

        return fieldLayout;
    }

    private VerticalLayout createDialogLayoutForBankReport(Dialog dialog) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = "eksterni_saradnici_finansije" + "_" + System.currentTimeMillis() + ".xlsx";

        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = null;
            try {
                stringPath = collaboratorsXlsxService.generateReportForBanks(fileTitle, scripts.getValue(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
            if(stringPath != null)
                return collaboratorsXlsxService.getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButtonAnchor);

        return fieldLayout;
    }
}
