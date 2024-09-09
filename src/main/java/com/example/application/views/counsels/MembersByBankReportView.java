package com.example.application.views.counsels;

import com.example.application.entities.VotingCouncelEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.services.ReportsXlsxService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
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

@PageTitle("Članovi po bankama")
@PermitAll
@Route(value = "bo/po/bankama", layout = MainLayout.class)
public class MembersByBankReportView extends VerticalLayout {
    ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");

    private final ReportsXlsxService reportsXlsxService;
    public MembersByBankReportView(ReportsXlsxService reportsXlsxService) {
        this.reportsXlsxService = reportsXlsxService;

        this.setWidth("800px");
        this.getStyle().set("margin", "0 auto");
        this.setAlignItems(Alignment.CENTER);

        //One se ne pojavljuju u glavnom prozoru, vec iskacu kroz popup
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);

        String longDescription = "Ovde se može preuzeti spisak predsjednika, članova i njihovih zamjenika po bankama.";

        Text descriptionText = new Text(longDescription);
        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button showDialogButton = new Button("Preuzmi", downloadIcon);

        showDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        showDialogButton.setWidthFull();

        showDialogButton.addClickListener(e -> {
            Dialog dialog = createDialog();
            dialog.open();
        });


        add(descriptionText, showDialogButton);
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
        H2 headline = new H2("Generisanje rješenja");
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
        String fileTitle = "spisak_po_bankama" + "_" + System.currentTimeMillis() + ".xlsx";

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
                stringPath = reportsXlsxService.generateReportForBanks(fileTitle, scripts.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
            if(stringPath != null)
                return reportsXlsxService.getStream(stringPath);
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
