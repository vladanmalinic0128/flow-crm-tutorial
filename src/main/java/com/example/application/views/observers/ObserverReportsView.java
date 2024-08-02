package com.example.application.views.observers;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.StackEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.enums.SideEnum;
import com.example.application.services.ObserverPdfService;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.services.StackService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


@Route(value = "posmatraci/izvjestaji", layout = MainLayout.class)
public class ObserverReportsView extends VerticalLayout {
    private final PoliticalOrganizationService politicalOrganizationService;
    private final StackService stackService;
    private final ObserverPdfService observerPdfService;

    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");
    ComboBox<SideEnum> printType = new ComboBox<>("Odaberite tip generisanja");
    DatePicker datePicker = new DatePicker("Odaberite datum: ");

    public ObserverReportsView(PoliticalOrganizationService politicalOrganizationService, StackService stackService, ObserverPdfService observerPdfService) {
        this.politicalOrganizationService = politicalOrganizationService;
        this.stackService = stackService;
        this.observerPdfService = observerPdfService;

        // Main layout
        this.setWidth("100%");
        this.getStyle().set("margin", "0 auto");

        Accordion accordion = new Accordion();
        accordion.setWidth("800px");
        accordion.getStyle().set("margin", "0 auto");

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        printType.setItems(SideEnum.values());
        printType.setItemLabelGenerator(SideEnum::getName);

        for (PoliticalOrganizationEntity entity : politicalOrganizationService.getAll()) {
            // Create a VerticalLayout for each accordion panel
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setWidthFull();

            // Add a HorizontalLayout
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setWidthFull();
            addComponentsToOverallReport(horizontalLayout, entity);

            verticalLayout.add(horizontalLayout);

            // Create a nested Accordion
            Accordion nestedAccordion = new Accordion();
            nestedAccordion.setWidthFull();


            for(StackEntity stack: entity.getStacks()) {
                HorizontalLayout childHorizontalLayout = new HorizontalLayout();
                childHorizontalLayout.setWidthFull();
                AccordionPanel nestedPanel = nestedAccordion.add(stack.getDecisionNumber() + " (" + stack.getDate() + ")", childHorizontalLayout);
                addComponentsToNestedReport(childHorizontalLayout, stack, nestedAccordion, nestedPanel);

                nestedPanel.addThemeVariants(DetailsVariant.FILLED);
            }

            verticalLayout.add(nestedAccordion);

            // Add the VerticalLayout to the main Accordion
            AccordionPanel panel = accordion.add(entity.getCode() + ": " + entity.getName(), verticalLayout);
            //panel.addThemeVariants(DetailsVariant.FILLED);
        }

        add(accordion);
    }

    private void addComponentsToOverallReport(HorizontalLayout horizontalLayout, PoliticalOrganizationEntity entity) {
        Span span = new Span("Zbirni spisak za PS: ");
        span.getStyle().set("margin-top", "8px");
        span.getStyle().set("font-weight", "bold");

        Button pdfButton = new Button("Pdf");
        pdfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        //pdfButton.addClickListener(e -> observerPdfService.downloadOverallPdf(entity));
        pdfButton.addClickListener(e -> showAlertForOverallPdf(entity));

        Button xlsxButton = new Button("Xlsx");
        xlsxButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        //xlsxButton.addClickListener(e -> observerPdfService.downloadOverallXlsx(entity));
        xlsxButton.addClickListener(e -> showAlertForOverallXlsx(entity));

        horizontalLayout.add(span, pdfButton, xlsxButton);
    }

    private void addComponentsToNestedReport(HorizontalLayout horizontalLayout, StackEntity entity, Accordion accordion, AccordionPanel accordionPanel) {
        Button pdfButton = new Button("Generiši akreditacije");
        pdfButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST);
        pdfButton.addClickListener(e -> showAlertForAccreditationsPdf(entity));

        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button acceptedButton = new Button("Izvještaj", downloadIcon);
        acceptedButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        acceptedButton.addClickListener(e -> showAlertForAcceptedObservers(entity));

        // Create a trash icon
        Icon trashIcon = new Icon(VaadinIcon.TRASH);

        // Create a button with the trash icon and text
        Button deleteButton = new Button("Obriši", trashIcon);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);

        deleteButton.addClickListener(event -> {
            stackService.deleteStack(entity);
            accordion.remove(accordionPanel);
        });


        horizontalLayout.add(pdfButton, acceptedButton, deleteButton);
    }

    private void showAlertForAcceptedObservers(StackEntity entity) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);

        dialog.setHeaderTitle("Generisanje prihvaćenih posmatrača");

        VerticalLayout overallPdfLayout = generateOverallXlsxLayout();
        dialog.add(overallPdfLayout);

        Button saveButton = new Button("Generiši");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String fileTitle = entity.getDecisionNumber().replace("/", "_") + "_" + System.currentTimeMillis() + ".xlsx";

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            String stringPath = handleAcceptedObservers(entity, saveButton, fileTitle);
            dialog.close();
            if(stringPath != null)
                return getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButtonAnchor);

        dialog.open();
    }

    private String handleAcceptedObservers(StackEntity entity, Button saveButton, String fileTitle) {
        if(scripts.getValue() == null) {
            Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return null;
        }
        return observerPdfService.downloadAcceptedObserversXslx(entity, scripts.getValue(), fileTitle);
    }

    private void showAlertForOverallPdf(PoliticalOrganizationEntity entity) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);

        dialog.setHeaderTitle("Generisanje zbirnog obrasca (PDF)");

        VerticalLayout overallPdfLayout = generateOverallPdfLayout();
        dialog.add(overallPdfLayout);

        Button saveButton = new Button("Generiši");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        saveButton.addClickListener(e -> {
            handleOverallPdf(entity, saveButton);
            dialog.close();
        });

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();
    }
    private VerticalLayout generateOverallPdfLayout() {
        VerticalLayout layout = new VerticalLayout();

        layout.add(scripts);

        return layout;
    }
    private void handleOverallPdf(PoliticalOrganizationEntity entity, Button button) {
        try {
            String pdfPath = observerPdfService.downloadOverallPdf(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void showAlertForOverallXlsx(PoliticalOrganizationEntity entity) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);

        dialog.setHeaderTitle("Generisanje zbirnog obrasca (PDF)");

        VerticalLayout overallPdfLayout = generateOverallXlsxLayout();
        dialog.add(overallPdfLayout);

        Button saveButton = new Button("Generiši");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        saveButton.addClickListener(e -> {
            handleOverallXlsx(entity, saveButton);
            dialog.close();
        });

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();
    }
    private VerticalLayout generateOverallXlsxLayout() {
        VerticalLayout layout = new VerticalLayout();

        layout.add(scripts);

        return layout;
    }
    private void handleOverallXlsx(PoliticalOrganizationEntity entity, Button saveButton) {
        try {
            String xlsxPath = observerPdfService.downloadOverallXlsx(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void showAlertForAccreditationsPdf(StackEntity entity) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);

        dialog.setHeaderTitle("Generisanje akreditacija (PDF)");

        VerticalLayout accreditationsPdfLayout = generateAccreditationsPdfLayout();
        dialog.add(accreditationsPdfLayout);


        Button saveButton = new Button("Generiši");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String fileTitle = entity.getDecisionNumber().replace("/", "_") + "_" + System.currentTimeMillis() + ".pdf";

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            String stringPath = handleAccreditationsPdf(entity, saveButton, fileTitle);
            dialog.close();
            if(stringPath != null)
                return getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);


        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButtonAnchor);

        dialog.open();
    }

    private VerticalLayout generateAccreditationsPdfLayout() {
        VerticalLayout layout = new VerticalLayout();

        scripts.setWidthFull();
        printType.setWidthFull();
        datePicker.setWidthFull();

        layout.add(scripts);
        layout.add(printType);
        layout.add(datePicker);


        return layout;
    }

    private String handleAccreditationsPdf(StackEntity entity, Button saveButton, String fileTitle) {
        if(scripts.getValue() == null) {
            Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return null;
        } else if(printType.getValue() == null)  {
            Notification notification = Notification.show("Morate odabrati tip printa", 3000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return null;
        } else if(datePicker.getValue() == null)  {
            Notification notification = Notification.show("Morate odabrati datum", 3000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return null;
        }
        try {
            return observerPdfService.downloadAccreditatationsPdf(entity, datePicker.getValue(), scripts.getValue(), printType.getValue(), fileTitle);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private InputStream getStream(String fileString) {
        File file = new File(fileString);
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

}
