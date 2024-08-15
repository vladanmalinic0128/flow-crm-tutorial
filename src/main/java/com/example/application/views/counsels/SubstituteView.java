package com.example.application.views.counsels;


import com.example.application.services.CouncelXlsxService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

@PageTitle("Rezervni članovi")
@PermitAll
@Route(value = "rezervni-clanovi", layout = MainLayout.class)
public class SubstituteView extends VerticalLayout {
    private final CouncelXlsxService councelXlsxService;
    public SubstituteView(CouncelXlsxService councelXlsxService) {
        this.councelXlsxService = councelXlsxService;
        this.setWidth("800px");
        this.getStyle().set("margin", "0 auto");
        this.setAlignItems(Alignment.CENTER);


        String longDescription = "Ovde se može preuzeti ažuriran rezervni spisak.";

        Text descriptionText = new Text(longDescription);
        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button saveButton = new Button("Preuzmi", downloadIcon);

        String fileTitle = "rezervni_spisak" + "_" + System.currentTimeMillis() + ".xlsx";


        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        saveButton.setWidthFull();

        Anchor saveButtonAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            String stringPath = councelXlsxService.generateSubstitutes(fileTitle);
            if(stringPath != null)
                return councelXlsxService.getStream(stringPath);
            else
                return null;
        }), "");

        saveButtonAnchor.getElement().setAttribute("download", true);
        saveButtonAnchor.removeAll();
        saveButtonAnchor.add(saveButton);
        saveButtonAnchor.setWidthFull();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        add(descriptionText, saveButtonAnchor);
    }
}
