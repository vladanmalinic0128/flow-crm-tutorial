package com.example.application.views.observers;


import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.services.CouncelXlsxService;
import com.example.application.services.OverallObserversService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.io.IOException;

@PageTitle("Zbirni spisak posmatraca")
@PermitAll
@Route(value = "zpirni-spisak-posmatraca", layout = MainLayout.class)
public class OverallReportView extends VerticalLayout {
    private final OverallObserversService overallObserversService;
    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    public OverallReportView(OverallObserversService overallObserversService) {
        this.overallObserversService = overallObserversService;
        this.setWidth("800px");
        this.getStyle().set("margin", "0 auto");
        this.setAlignItems(Alignment.CENTER);

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        String longDescription = "Ovde se može preuzeti zbirni spisak posmatrača.";

        Text descriptionText = new Text(longDescription);
        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button saveButton = new Button("Preuzmi", downloadIcon);

        String fileTitle = "zbirni-spisak-posmatraca" + "_" + System.currentTimeMillis() + ".pdf";


        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        saveButton.setWidthFull();

        saveButton.addClickListener(e -> {
//
            Dialog dialog = createDialog();
            dialog.open();
        });

        add(descriptionText, saveButton);

    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private H2 createDialogHeader() {
        H2 headline = new H2("Generisanje zbirnog spiska posmatraca");
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = "zbirni_spisak_posmatraca_" + System.currentTimeMillis() + ".pdf";
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
                stringPath = overallObserversService.generateOverallObserversReport(fileTitle, scripts.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
            if(stringPath != null)
                return overallObserversService.getStream(stringPath);
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
