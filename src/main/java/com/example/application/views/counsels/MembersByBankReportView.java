package com.example.application.views.counsels;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Članovi po bankama")
@PermitAll
@Route(value = "bo/po/bankama", layout = MainLayout.class)
public class MembersByBankReportView extends VerticalLayout {
    public MembersByBankReportView() {
        this.setWidth("800px");
        this.getStyle().set("margin", "0 auto");
        this.setAlignItems(Alignment.CENTER);


        String longDescription = "Ovde se može preuzeti spisak članova i njihovih zamjenika po bankama.";

        Text descriptionText = new Text(longDescription);
        Icon downloadIcon = new Icon(VaadinIcon.DOWNLOAD);
        Button showDialogButton = new Button("Preuzmi", downloadIcon);
        showDialogButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        showDialogButton.setWidthFull();

        showDialogButton.addClickListener(e -> {

        });


        add(descriptionText, showDialogButton);
    }
}
