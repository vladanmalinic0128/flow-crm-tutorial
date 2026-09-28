package com.example.application.views.observers;

import com.example.application.enums.ScriptEnum;
import com.example.application.enums.SideEnum;
import com.example.application.services.AccreditationPdfServiceV2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@PageTitle("Prazne akreditacije")
@PermitAll
@Route(value = "posmatraci/prazne-akreditacije", layout = MainLayout.class)
public class EmptyAccreditationsView extends VerticalLayout {
    private static final int NUMBER_OF_ACCREDITATIONS = 10;

    private final AccreditationPdfServiceV2 accreditationPdfServiceV2;

    private final ComboBox<ScriptEnum> scripts = new ComboBox<>("Pismo");
    private final ComboBox<SideEnum> printType = new ComboBox<>("Tip printa");

    public EmptyAccreditationsView(AccreditationPdfServiceV2 accreditationPdfServiceV2) {
        this.accreditationPdfServiceV2 = accreditationPdfServiceV2;

        setWidth("400px");
        getStyle().set("margin", "0 auto");

        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);
        scripts.setValue(ScriptEnum.CYRILLIC);
        scripts.setAllowCustomValue(false);
        scripts.setRequired(true);
        scripts.setWidthFull();

        printType.setItems(SideEnum.values());
        printType.setItemLabelGenerator(SideEnum::getName);
        printType.setValue(SideEnum.TWO_SIDED);
        printType.setAllowCustomValue(false);
        printType.setRequired(true);
        printType.setWidthFull();

        Button generateButton = new Button("Generiši " + NUMBER_OF_ACCREDITATIONS + " praznih akreditacija", new Icon(VaadinIcon.DOWNLOAD));
        generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateButton.setWidthFull();

        // The file name is fixed when the anchor is created, so it is rebuilt whenever an option
        // changes (and after each download) to keep the name matching the chosen options.
        Anchor downloadAnchor = new Anchor();
        downloadAnchor.getElement().setAttribute("download", true);
        downloadAnchor.add(generateButton);
        downloadAnchor.setWidthFull();
        refreshResource(downloadAnchor);

        scripts.addValueChangeListener(e -> refreshResource(downloadAnchor));
        printType.addValueChangeListener(e -> refreshResource(downloadAnchor));

        add(scripts, printType, downloadAnchor);
    }

    private void refreshResource(Anchor downloadAnchor) {
        if (scripts.getValue() == null || printType.getValue() == null) {
            downloadAnchor.removeHref();
            return;
        }
        ScriptEnum script = scripts.getValue();
        SideEnum side = printType.getValue();
        String fileTitle = "prazne_akreditacije_" + script.name().toLowerCase() + "_" + side.name().toLowerCase()
                + "_" + System.currentTimeMillis() + ".pdf";
        downloadAnchor.setHref(new StreamResource(fileTitle, () -> {
            String path = accreditationPdfServiceV2.downloadEmptyAccreditationsPdf(NUMBER_OF_ACCREDITATIONS, script, side, fileTitle);
            try {
                return new FileInputStream(new File(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
