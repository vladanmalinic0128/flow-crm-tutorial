package com.example.application.views.observers;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.StatusEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.StatusRepository;
import com.example.application.services.AccreditationPdfServiceV2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;

@PermitAll
@Route(value = "posmatraci/uredi", layout = MainLayout.class)
public class ObserverManagementView extends VerticalLayout {
    private final ObserverRepository observerRepository;
    private final StatusRepository statusRepository;
    private final AccreditationPdfServiceV2 accreditationPdfServiceV2;

    private final Grid<ObserverEntity> grid = new Grid<>(ObserverEntity.class, false);
    private final ListDataProvider<ObserverEntity> dataProvider;

    public ObserverManagementView(ObserverRepository observerRepository, StatusRepository statusRepository, AccreditationPdfServiceV2 accreditationPdfServiceV2) {
        this.observerRepository = observerRepository;
        this.statusRepository = statusRepository;
        this.accreditationPdfServiceV2 = accreditationPdfServiceV2;

        setSizeFull();

        H2 title = new H2("Posmatrači");

        TextField filter = new TextField();
        filter.setPlaceholder("Pretraga po imenu, prezimenu ili JMBG-u");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setWidth("400px");
        filter.addValueChangeListener(e -> applyFilter(filter.getValue()));

        dataProvider = new ListDataProvider<>(observerRepository.findAll());
        grid.setItems(dataProvider);
        grid.setSizeFull();

        grid.addColumn(ObserverEntity::getLastname).setHeader("Prezime").setSortable(true);
        grid.addColumn(ObserverEntity::getFirstname).setHeader("Ime").setSortable(true);
        grid.addColumn(ObserverEntity::getJmbg).setHeader("JMBG");
        grid.addColumn(ObserverEntity::getCardId).setHeader("Broj isprave");
        grid.addColumn(ObserverEntity::getDocumentIssuingCountry).setHeader("Država izdavanja isprave");
        grid.addColumn(observer -> observer.getStatus() != null ? observer.getStatus().getName() : "")
                .setHeader("Status");
        grid.addColumn(observer -> observer.getStack() != null && observer.getStack().getPoliticalOrganization() != null
                        ? observer.getStack().getPoliticalOrganization().getCode() : "")
                .setHeader("Politički subjekat");
        grid.addColumn(observer -> Boolean.TRUE.equals(observer.getForce()) ? "Da" : "Ne")
                .setHeader("Prisilno prihvaćen");

        grid.addComponentColumn(observer -> {
            Button editButton = new Button("Uredi", new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> openEditDialog(observer));

            Button accreditationButton = new Button("Akreditacija", new Icon(VaadinIcon.DOWNLOAD));
            accreditationButton.addClickListener(e -> openAccreditationDialog(observer));

            return new HorizontalLayout(editButton, accreditationButton);
        }).setHeader("Akcije").setAutoWidth(true);

        HorizontalLayout toolbar = new HorizontalLayout(filter);
        toolbar.setWidthFull();

        add(title, toolbar, grid);
    }

    private void applyFilter(String value) {
        String needle = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        dataProvider.setFilter(observer ->
                needle.isEmpty()
                        || containsIgnoreCase(observer.getFirstname(), needle)
                        || containsIgnoreCase(observer.getLastname(), needle)
                        || containsIgnoreCase(observer.getJmbg(), needle));
    }

    private boolean containsIgnoreCase(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }

    private void openEditDialog(ObserverEntity observer) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Uredi posmatrača");

        TextField firstname = new TextField("Ime");
        firstname.setValue(observer.getFirstname() != null ? observer.getFirstname() : "");

        TextField lastname = new TextField("Prezime");
        lastname.setValue(observer.getLastname() != null ? observer.getLastname() : "");

        TextField jmbg = new TextField("JMBG");
        jmbg.setValue(observer.getJmbg() != null ? observer.getJmbg() : "");

        TextField cardId = new TextField("Broj lične isprave");
        cardId.setValue(observer.getCardId() != null ? observer.getCardId() : "");

        TextField documentIssuingCountry = new TextField("Naziv države koja je izdala ličnu ispravu");
        documentIssuingCountry.setValue(observer.getDocumentIssuingCountry() != null ? observer.getDocumentIssuingCountry() : "");

        IntegerField documentNumber = new IntegerField("Broj dokumenta (akreditacije)");
        if (observer.getDocumentNumber() != null)
            documentNumber.setValue(observer.getDocumentNumber());

        ComboBox<StatusEntity> status = new ComboBox<>("Status");
        status.setItems(statusRepository.findAll());
        status.setItemLabelGenerator(s -> s.getName() != null ? s.getName() : "Uspješno akreditovan");
        status.setValue(observer.getStatus());

        Checkbox force = new Checkbox("Prisilno prihvaćen");
        force.setValue(Boolean.TRUE.equals(observer.getForce()));

        VerticalLayout formLayout = new VerticalLayout(firstname, lastname, jmbg, cardId, documentIssuingCountry, documentNumber, status, force);
        formLayout.setPadding(false);
        formLayout.setWidth("400px");
        dialog.add(formLayout);

        Button saveButton = new Button("Sačuvaj", e -> {
            observer.setFirstname(firstname.getValue());
            observer.setLastname(lastname.getValue());
            observer.setJmbg(jmbg.getValue());
            observer.setCardId(cardId.getValue());
            observer.setDocumentIssuingCountry(documentIssuingCountry.getValue());
            observer.setDocumentNumber(documentNumber.getValue());
            observer.setStatus(status.getValue());
            observer.setForce(force.getValue());
            observerRepository.save(observer);
            dataProvider.refreshItem(observer);
            dialog.close();

            Notification notification = Notification.show("Posmatrač je sačuvan", 2000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton, saveButton);
        dialog.open();
    }

    private void openAccreditationDialog(ObserverEntity observer) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Generisanje akreditacije za " + observer.getFullName());

        ComboBox<ScriptEnum> scripts = new ComboBox<>("Odaberite pismo");
        scripts.setItems(ScriptEnum.values());
        scripts.setItemLabelGenerator(ScriptEnum::getName);
        scripts.setValue(ScriptEnum.CYRILLIC);

        DatePicker datePicker = new DatePicker("Odaberite datum");
        datePicker.setValue(LocalDate.now());

        VerticalLayout layout = new VerticalLayout(scripts, datePicker);
        layout.setPadding(false);
        dialog.add(layout);

        Button generateButton = new Button("Generiši");
        generateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        String fileTitle = "akreditacija_" + safeFileNamePart(observer) + "_" + System.currentTimeMillis() + ".pdf";
        Anchor downloadAnchor = new Anchor(new StreamResource(fileTitle, () -> {
            if (scripts.getValue() == null || datePicker.getValue() == null) {
                Notification notification = Notification.show("Morate odabrati pismo i datum", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return null;
            }
            String path = accreditationPdfServiceV2.downloadSingleAccreditationPdf(observer, datePicker.getValue(), scripts.getValue(), fileTitle);
            dialog.close();
            return getStream(path);
        }), "");

        downloadAnchor.getElement().setAttribute("download", true);
        downloadAnchor.removeAll();
        downloadAnchor.add(generateButton);

        Button cancelButton = new Button("Otkaži", e -> dialog.close());

        dialog.getFooter().add(cancelButton, downloadAnchor);
        dialog.open();
    }

    private String safeFileNamePart(ObserverEntity observer) {
        String raw = (observer.getLastname() != null ? observer.getLastname() : "") + "_" +
                (observer.getFirstname() != null ? observer.getFirstname() : "");
        return raw.replaceAll("[^\\p{L}\\p{N}_]+", "_");
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
