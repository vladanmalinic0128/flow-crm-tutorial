package com.example.application.views.counsels;


import com.example.application.entities.MentorEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.MentorRepository;
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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.util.Comparator;
import java.util.stream.Collectors;

@PermitAll
@Route(value = "bo/po/gik", layout = MainLayout.class)
public class CouncelsByMentor extends VerticalLayout {
    public static final String ROOT_PATH = "src/main/resources/generated-documents";
    private final CouncelXlsxService councelXlsxService;
    private final MentorRepository mentorRepository;

    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    public CouncelsByMentor(CouncelXlsxService councelXlsxService, MentorRepository mentorRepository) {
        this.councelXlsxService = councelXlsxService;
        this.mentorRepository = mentorRepository;

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
        generateOverallTableForMobileTeams(accordion);

        for(MentorEntity entity: mentorRepository.findAll().stream().sorted(Comparator.comparing(MentorEntity::getFirstname)).collect(Collectors.toList())) {
            // Add a HorizontalLayout
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            Text description = new Text("Ovde možete preuzeti biračke odbore za gore navedenog člana GIK: ");

            Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
            Icon downloadIcon2 = new Icon(VaadinIcon.DOWNLOAD);
            Button xlsxButton = new Button("Preuzmi", downloadIcon);
            Button xlsxButtonWithPresidents = new Button("Preuzmi sa predsjednicima", downloadIcon2);
            xlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_SUCCESS);
            xlsxButtonWithPresidents.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_SUCCESS);

            xlsxButton.addClickListener(e -> {
                Dialog dialog = createDialog(entity);
                dialog.open();
            });
            xlsxButtonWithPresidents.addClickListener(e -> {
                Dialog dialog = createDialogWithPresidents(entity);
                dialog.open();
            });

            horizontalLayout.add(description, xlsxButton, xlsxButtonWithPresidents);

            // Add the VerticalLayout to the main Accordion
            AccordionPanel panel = accordion.add(entity.getFirstname() + " " + entity.getLastname(), horizontalLayout);
            //panel.addThemeVariants(DetailsVariant.FILLED);
        }

        add(accordion);
    }

    void generateOverallTable(Accordion accordion) {
        // Add a HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);


        MentorEntity mentorEntity = new MentorEntity();
        mentorEntity.setFirstname("ZBIRNI");
        mentorEntity.setLastname("SPISAK");
        Text description = new Text("Ovde možete preuzeti zbirnu tabelu za biračke odbore: ");

        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Icon downloadIcon2 = new Icon(VaadinIcon.DOWNLOAD);
        Button xlsxButton = new Button("Preuzmi", downloadIcon);
        Button xlsxButtonWithPresidents = new Button("Preuzmi sa predsjednicima", downloadIcon2);
        xlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        xlsxButtonWithPresidents.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);

        xlsxButton.addClickListener(e -> {
            Dialog dialog = createDialog(mentorEntity);
            dialog.open();
        });
        xlsxButtonWithPresidents.addClickListener(e -> {
            Dialog dialog = createDialogWithPresidents(mentorEntity);
            dialog.open();
        });

        horizontalLayout.add(description, xlsxButton, xlsxButtonWithPresidents);

        // Add the VerticalLayout to the main Accordion
        AccordionPanel panel = accordion.add(mentorEntity.getFirstname() + " " + mentorEntity.getLastname(), horizontalLayout);
        //panel.addThemeVariants(DetailsVariant.FILLED);
    }

    void generateOverallTableForMobileTeams(Accordion accordion) {
        // Add a HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);


        MentorEntity mentorEntity = new MentorEntity();
        mentorEntity.setId(-1L);
        mentorEntity.setFirstname("ZBIRNI SPISAK");
        mentorEntity.setLastname("MOBILNIH TIMOVA");
        Text description = new Text("Ovde možete preuzeti zbirnu tabelu za mobilne timove: ");

        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Icon downloadIcon2 = new Icon(VaadinIcon.DOWNLOAD);
        Button xlsxButton = new Button("Preuzmi", downloadIcon);
        Button xlsxButtonWithPresidents = new Button("Preuzmi sa predsjednicima", downloadIcon2);
        xlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        xlsxButtonWithPresidents.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);

        xlsxButton.addClickListener(e -> {
            Dialog dialog = createDialog(mentorEntity);
            dialog.open();
        });
        xlsxButtonWithPresidents.addClickListener(e -> {
            Dialog dialog = createDialogWithPresidents(mentorEntity);
            dialog.open();
        });

        horizontalLayout.add(description, xlsxButton, xlsxButtonWithPresidents);

        // Add the VerticalLayout to the main Accordion
        AccordionPanel panel = accordion.add(mentorEntity.getFirstname() + " " + mentorEntity.getLastname(), horizontalLayout);
        //panel.addThemeVariants(DetailsVariant.FILLED);
    }

    private Dialog createDialog(MentorEntity entity) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayout(dialog, entity);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);

        return dialog;
    }

    private Dialog createDialogWithPresidents(MentorEntity entity) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add note");

        dialog.getHeader().add(createDialogHeader());

        VerticalLayout dialogLayout = createDialogLayoutWithPresidents(dialog, entity);
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

    private VerticalLayout createDialogLayout(Dialog dialog, MentorEntity entity) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = entity.getFirstname() + "_" + entity.getLastname() + "_" + System.currentTimeMillis() + ".xlsx";
        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = councelXlsxService.generateCouncelsByMentor(entity, fileTitle, scripts.getValue());
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

    private VerticalLayout createDialogLayoutWithPresidents(Dialog dialog, MentorEntity entity) {
        VerticalLayout fieldLayout = new VerticalLayout(scripts);
        scripts.setValue(ScriptEnum.CYRILLIC);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Zatvori", e -> {
            dialog.close();
        });
        String fileTitle = entity.getFirstname() + "_" + entity.getLastname() + "_" + System.currentTimeMillis() + ".xlsx";
        Button saveButton = new Button("Generiši");

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if(scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                dialog.close();
                return null;
            }
            String stringPath = councelXlsxService.generateCouncelsWithPresidentsByMentor(entity, fileTitle, scripts.getValue());
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
