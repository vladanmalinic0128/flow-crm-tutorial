package com.example.application.views.list;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.services.StackService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.io.InputStream;
import java.util.List;

@PermitAll
@Route(value = "posmatraci/stanje", layout = MainLayout.class)
public class AddingStackForm extends FormLayout {
    private final StackService stackService;
    TextField decisionNumber = new TextField("Unesite broj odluke: ");
    ComboBox<PoliticalOrganizationEntity> organizations = new ComboBox<>("Odaberite politički subjekat: ");
    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");
    DatePicker datePicker = new DatePicker("Odaberite datum: ");

    public AddingStackForm(StackService stackService, List<PoliticalOrganizationEntity> politicalOrganizations) {
        this.stackService = stackService;
        addClassName("adding-stack-form");

        organizations.setItems(politicalOrganizations);
        organizations.setItemLabelGenerator(o -> o.getCode() + ": " + o.getName());

        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        Upload upload = configureFileUpload();

        this.setWidth("100%");
        this.getStyle().set("margin", "0 auto");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );



        add(decisionNumber);
        add(organizations);
        add(scripts);
        add(datePicker);
        add(upload);

        setColspan(decisionNumber, 2);
        setColspan(organizations, 2);
        setColspan(scripts, 2);
        setColspan(datePicker,2);
        setColspan(upload, 2);
    }

    private Upload configureFileUpload() {
        MemoryBuffer buffer = new MemoryBuffer();

        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);

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
            //stackService.addStackThroughXlsxFile(inputStream);
        });

        return upload;
    }
}
