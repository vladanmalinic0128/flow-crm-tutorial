package com.example.application.views.observers;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.services.ObserverPdfService;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@PermitAll
@Route(value = "posmatraci/prazni-izvjestaji", layout = MainLayout.class)
public class EmptyObserverReportsView extends VerticalLayout {
    private final PoliticalOrganizationService politicalOrganizationService;
    private final ObserverPdfService observerPdfService;

    public EmptyObserverReportsView(PoliticalOrganizationService politicalOrganizationService, ObserverPdfService observerPdfService) {
        this.politicalOrganizationService = politicalOrganizationService;
        this.observerPdfService = observerPdfService;

        setWidth("800px");
        getStyle().set("margin", "0 auto");

        List<PoliticalOrganizationEntity> organizations = politicalOrganizationService.getAll();

        add(buildDownloadAllRow(organizations));

        VerticalLayout list = new VerticalLayout();
        list.setPadding(false);
        list.setSpacing(false);

        for (PoliticalOrganizationEntity organization : organizations) {
            list.add(buildOrganizationRow(organization));
        }

        add(list);
    }

    private HorizontalLayout buildDownloadAllRow(List<PoliticalOrganizationEntity> organizations) {
        Span title = new Span("Prazni izvještaji za sve političke subjekte (" + organizations.size() + ")");

        Button downloadAllButton = new Button("Preuzmi sve (ZIP)", new Icon(VaadinIcon.ARCHIVE));
        downloadAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        String zipFileTitle = "prazni_izvjestaji_svi_" + System.currentTimeMillis() + ".zip";
        Anchor downloadAllAnchor = new Anchor(new StreamResource(zipFileTitle, () -> {
            String filePath = observerPdfService.downloadBlankObserversTemplatesForAllOrganizations(organizations, zipFileTitle);
            return getStream(filePath);
        }), "");

        downloadAllAnchor.getElement().setAttribute("download", true);
        downloadAllAnchor.removeAll();
        downloadAllAnchor.add(downloadAllButton);

        HorizontalLayout row = new HorizontalLayout(title, downloadAllAnchor);
        row.setWidthFull();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        row.addClassNames(LumoUtility.Padding.Bottom.SMALL, LumoUtility.Margin.Bottom.SMALL,
                LumoUtility.Border.BOTTOM, LumoUtility.BorderColor.CONTRAST_10);

        return row;
    }

    private HorizontalLayout buildOrganizationRow(PoliticalOrganizationEntity entity) {
        Span code = new Span(entity.getCode());
        code.addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.TextColor.PRIMARY, LumoUtility.FontWeight.SEMIBOLD,
                LumoUtility.Padding.Horizontal.SMALL, LumoUtility.BorderRadius.SMALL);

        Span name = new Span(entity.getName());

        HorizontalLayout label = new HorizontalLayout(code, name);
        label.setAlignItems(FlexComponent.Alignment.CENTER);

        Button downloadButton = new Button("Preuzmi prazan izvještaj", new Icon(VaadinIcon.DOWNLOAD));
        downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        String fileTitle = "prazan_izvjestaj_" + entity.getCode() + "_" + System.currentTimeMillis() + ".xlsx";
        Anchor downloadAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            String filePath = observerPdfService.downloadBlankObserversTemplate(entity, fileTitle);
            return getStream(filePath);
        }), "");

        downloadAnchor.getElement().setAttribute("download", true);
        downloadAnchor.removeAll();
        downloadAnchor.add(downloadButton);

        HorizontalLayout row = new HorizontalLayout(label, downloadAnchor);
        row.setWidthFull();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        return row;
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
