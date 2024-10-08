package com.example.application.views.counsels;


import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.services.CouncelXlsxService;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "bo/po/ps", layout = MainLayout.class)
public class CouncelsByPoliticalOrganizationView extends VerticalLayout {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final PoliticalOrganizationService politicalOrganizationService;
    private final CouncelXlsxService councelXlsxService;

    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    public CouncelsByPoliticalOrganizationView(PoliticalOrganizationService politicalOrganizationService, CouncelXlsxService councelXlsxService) {
        this.politicalOrganizationService = politicalOrganizationService;
        this.councelXlsxService = councelXlsxService;

        // Main layout
        this.setWidth("100%");
        this.getStyle().set("margin", "0 auto");

        Accordion accordion = new Accordion();
        accordion.setWidth("800px");
        accordion.getStyle().set("margin", "0 auto");

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        for (PoliticalOrganizationEntity entity : politicalOrganizationService.getAllDrawed()) {
            // Add a HorizontalLayout
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            Text description = new Text("Ovde možete preuzeti pozicije za gore navedenu partiju: ");

            Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
            Button xlsxButton = new Button("Preuzmi", downloadIcon);
            Button xlsxMTButton = new Button("Preuzmi MT", downloadIcon);

            xlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_SUCCESS);
            xlsxMTButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_SUCCESS);

            xlsxButton.addClickListener(e -> {
                Dialog dialog = createDialog(entity, false);
                dialog.open();
            });

            xlsxMTButton.addClickListener(e -> {
                Dialog dialog = createDialog(entity, true);
                dialog.open();
            });

            horizontalLayout.add(description, xlsxButton, xlsxMTButton);

            // Add the VerticalLayout to the main Accordion
            AccordionPanel panel = accordion.add(entity.getCode() + ": " + entity.getName(), horizontalLayout);
            //panel.addThemeVariants(DetailsVariant.FILLED);
        }

        add(accordion);
    }

    private Dialog createDialog(PoliticalOrganizationEntity entity, boolean isMT) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayout(dialog, entity, isMT);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private H2 createDialogHeader() {
        H2 headline = new H2("Generisanje biračkih odbora");
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayout(Dialog dialog, PoliticalOrganizationEntity entity, boolean isMT) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = entity.getCode() + "_" + System.currentTimeMillis() + ".xlsx";
        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = councelXlsxService.generateCouncelsByPoliticalOrganization(entity, fileTitle, scripts.getValue(), isMT);
            dialog.close();
            if(stringPath != null)
                return councelXlsxService.getStream(stringPath);
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
