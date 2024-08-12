package com.example.application.views.counsels;

import com.example.application.entities.MentorEntity;
import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.VotingCouncelRepository;
import com.example.application.services.ReportsPdfService;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.util.Comparator;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "rjesenja", layout = MainLayout.class)
public class ReportsView extends VerticalLayout {
    private final VotingCouncelRepository votingCouncelRepository;
    private final ReportsPdfService reportsPdfService;
    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    public ReportsView(VotingCouncelRepository votingCouncelRepository, ReportsPdfService reportsPdfService) {
        this.votingCouncelRepository = votingCouncelRepository;
        this.reportsPdfService = reportsPdfService;
        // Main layout
        this.setWidth("100%");
        this.getStyle().set("margin", "0 auto");

        Accordion accordion = new Accordion();
        accordion.setWidth("800px");
        accordion.getStyle().set("margin", "0 auto");

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        generateOverallTable(accordion);

        for(VotingCouncelEntity entity: votingCouncelRepository.findAll().stream().sorted(Comparator.comparing(VotingCouncelEntity::getCode)).collect(Collectors.toList())) {
            // Add a HorizontalLayout
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            Text description = new Text("Ovde možete preuzeti rješenje za gore navedeni birački odbor: ");

            Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
            Button pdfButton = new Button("Preuzmi", downloadIcon);
            pdfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_ERROR);

            pdfButton.addClickListener(e -> {
//
                Dialog dialog = createDialog(entity);
                dialog.open();
            });

            horizontalLayout.add(description, pdfButton);

            // Add the VerticalLayout to the main Accordion
            AccordionPanel panel = accordion.add(entity.getCode() + ": " + entity.getName(), horizontalLayout);
            //panel.addThemeVariants(DetailsVariant.FILLED);
        }

        add(accordion);
    }

    private void generateOverallTable(Accordion accordion) {
        // Add a HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        Text description = new Text("Ovde možete preuzeti sva rješenja na jednom mjestu: ");

        VotingCouncelEntity votingCouncel = new VotingCouncelEntity();
        votingCouncel.setCode("034B");
        votingCouncel.setName("Zbirna rješenja u jednom fajlu");

        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button pdfButton = new Button("Preuzmi", downloadIcon);
        pdfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);

        pdfButton.addClickListener(e -> {
//
            Dialog dialog = createDialog(votingCouncel);
            dialog.open();
        });

        horizontalLayout.add(description, pdfButton);

        // Add the VerticalLayout to the main Accordion
        AccordionPanel panel = accordion.add(votingCouncel.getCode() + ": " + votingCouncel.getName(), horizontalLayout);
    }

    private Dialog createDialog(VotingCouncelEntity entity) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayout(dialog, entity);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private H2 createDialogHeader() {
        H2 headline = new H2("Generisanje rješenja");
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayout(Dialog dialog, VotingCouncelEntity entity) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = entity.getCode() + "_" + System.currentTimeMillis() + ".pdf";
        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = reportsPdfService.generateReportByCode(entity, fileTitle, scripts.getValue());
            dialog.close();
            if(stringPath != null)
                return reportsPdfService.getStream(stringPath);
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
