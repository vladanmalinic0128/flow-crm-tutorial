package com.example.application.views.list;

import com.example.application.services.PoliticalOrganizationService;
import com.example.application.services.StackService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.io.InputStream;

@PageTitle("Posmatraci | Opstina")
@Tag("div")
@Route(value = "posmatraci/novi", layout = MainLayout.class)
public class ListView extends VerticalLayout {
    private final StackService stackService;
    private final PoliticalOrganizationService politicalOrganizationService;
    public ListView(StackService stackService,
                    PoliticalOrganizationService politicalOrganizationService) {
        this.stackService = stackService;
        this.politicalOrganizationService = politicalOrganizationService;

        Upload upload = configureFileUpload();
        add(upload);

        add(new H1("Hello World!"));

        Button button = new Button("Click me");
        add(button);
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
