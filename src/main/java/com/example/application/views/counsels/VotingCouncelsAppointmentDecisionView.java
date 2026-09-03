package com.example.application.views.counsels;

import com.example.application.enums.ScriptEnum;
import com.example.application.services.ObserverPdfService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@PageTitle("Odluka o imenovanju BO")
@PermitAll
@Route(value = "bo/odluka", layout = MainLayout.class)
public class VotingCouncelsAppointmentDecisionView extends VerticalLayout {
    private final ObserverPdfService observerPdfService;

    public VotingCouncelsAppointmentDecisionView(ObserverPdfService observerPdfService) {
        this.observerPdfService = observerPdfService;

        H2 title = new H2("Odluka o imenovanju članova i zamjenika članova biračkih odbora");

        Button openButton = new Button("Generiši odluku");
        openButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        openButton.addClickListener(e -> openScriptDialog());

        setSizeFull();
        add(title, openButton);

        openScriptDialog();
    }

    private void openScriptDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Odaberite pismo");

        ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);
        scripts.setValue(ScriptEnum.CYRILLIC);

        VerticalLayout layout = new VerticalLayout(scripts);
        layout.setPadding(false);
        dialog.add(layout);

        Button generateButton = new Button("Generiši");
        generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String fileTitle = "odluka_biracki_odbori_" + System.currentTimeMillis() + ".docx";
        Anchor downloadAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if (scripts.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return null;
            }
            try {
                String path = observerPdfService.generateVotingCouncelsAppointmentDecision(scripts.getValue(), fileTitle);
                dialog.close();
                return getStream(path);
            } catch (Exception ex) {
                Notification notification = Notification.show("Greška prilikom generisanja odluke: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return null;
            }
        }), "");

        downloadAnchor.getElement().setAttribute("download", true);
        downloadAnchor.removeAll();
        downloadAnchor.add(generateButton);

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton, downloadAnchor);
        dialog.open();
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
