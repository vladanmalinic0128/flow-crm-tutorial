package com.example.application.views.observers;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.StackEntity;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.PresidentRepository;
import com.example.application.repositories.StatusRepository;
import com.example.application.services.CyrillicToLatinConverter;
import com.example.application.services.JMBGValidator;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.services.StackService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@PermitAll
@Route(value = "posmatraci/akredituj", layout = MainLayout.class)
public class AddingObserversForm extends FormLayout {
    private final StackService stackService;
    private final PoliticalOrganizationService politicalOrganizationService;
    private final MemberRepository memberRepository;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;
    private final JMBGValidator jmbgValidator;
    private final StatusRepository statusRepository;
    private final ObserverRepository observerRepository;
    private final PresidentRepository presidentRepository;

    private StackEntity stackEntity = new StackEntity();

    TextField decisionNumber = new TextField("Unesite broj odluke: ");
    ComboBox<PoliticalOrganizationEntity> organizations = new ComboBox<>("Odaberite politički subjekat: ");
    DatePicker datePicker = new DatePicker("Odaberite datum: ");

    Upload upload = null;
    MemoryBuffer buffer = null;
    boolean isFileEmpty = true;

    public AddingObserversForm(StackService stackService, PoliticalOrganizationService politicalOrganizationService, JMBGValidator jmbgValidator, CyrillicToLatinConverter cyrillicToLatinConverter, StatusRepository statusRepository, ObserverRepository observerRepository, PresidentRepository presidentRepository, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.presidentRepository = presidentRepository;
        this.stackService = stackService;
        this.politicalOrganizationService = politicalOrganizationService;
        this.jmbgValidator = jmbgValidator;
        this.cyrillicToLatinConverter = cyrillicToLatinConverter;
        this.statusRepository = statusRepository;
        this.observerRepository = observerRepository;
        addClassName("adding-stack-form");

        organizations.setItems(politicalOrganizationService.getAll());
        organizations.setItemLabelGenerator(o -> o.getCode() + ": " + o.getName());
        organizations.setWidth("100%");

        upload = configureFileUpload();

        Button executeButton = new Button("Započni proces akreditacije");
        executeButton.addClickListener(event -> handleExecuteButtonClick());
        executeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //this.setWidth("100%");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        this.setWidth("50%");

        this.getStyle().set("margin", "0 auto");
        this.getStyle().set("margin-top", "20px");

        add(decisionNumber);
        add(organizations);
        add(datePicker);
        add(upload);
        add(executeButton);

        setColspan(decisionNumber, 2);
        setColspan(organizations, 2);
        setColspan(datePicker,2);
        setColspan(upload, 2);
        setColspan(executeButton, 2);
    }

    private Upload configureFileUpload() {
        buffer = new MemoryBuffer();

        Upload upload = new Upload(buffer);
        upload.setDropAllowed(false);
        upload.setWidthFull();

        // Label for upload
        NativeLabel dropEnabledLabel = new NativeLabel("Postavite excel fajl ovde");
        dropEnabledLabel.getStyle().set("font-weight", "600");
        upload.setId("upload-drop-enabled");
        dropEnabledLabel.setFor(upload.getId().get());

        upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        upload.setMaxFiles(1);

        // Style upload component
        upload.getElement().getStyle().set("width", "80%");

        upload.addSucceededListener(event -> {
            InputStream inputStream = buffer.getInputStream();
            this.addStackThroughXlsxFile(inputStream);
        });

        upload.addFileRemovedListener(event -> isFileEmpty = true);


        return upload;
    }

    private void addStackThroughXlsxFile(InputStream inputStream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getLastRowNum();

            List<ObserverEntity> observers = new ArrayList<>();

            for(int r=1; r<=rows; r++) {
                XSSFRow row = sheet.getRow(r);
                int cols = row.getLastCellNum();

                if(row.getLastCellNum() < 5)
                    continue;

                XSSFCell cell = row.getCell(0);
                int number = cell != null ? (int)cell.getNumericCellValue() : -1;

                cell = row.getCell(1);
                String jmbg = cell != null ? cell.getStringCellValue() : "";

                cell = row.getCell(2);
                String cardid = cell != null ? cell.getStringCellValue() : "";

                cell = row.getCell(3);
                String lastname = cell != null ? cell.getStringCellValue() : "";

                cell = row.getCell(4);
                String firstname =cell != null ? cell.getStringCellValue() : "";

                ObserverEntity observer = new ObserverEntity();
                observer.setJmbg(jmbg);
                observer.setCardId(cardid);
                observer.setFirstname(cyrillicToLatinConverter.convert(firstname));
                observer.setLastname(cyrillicToLatinConverter.convert(lastname));
                observer.setForce(false);
                observer.setDocumentNumber(number);

                if(jmbgValidator.isValidJMBG(observer.getJmbg()) == false)
                    observer.setStatus(statusRepository.findById(2));
                else if(observerRepository.existsByJmbg(observer.getJmbg()))
                    observer.setStatus(statusRepository.findById(3));
                else if(memberRepository.existsByJmbg(observer.getJmbg()))
                    observer.setStatus(statusRepository.findById(4));
                else if(observer.getCardId().isBlank())
                    observer.setStatus(statusRepository.findById(5));
                else if(observer.getFirstname().isBlank())
                    observer.setStatus(statusRepository.findById(6));
                else if(observer.getLastname().isBlank())
                    observer.setStatus(statusRepository.findById(7));
                else if(observer.getDocumentNumber() == -1)
                    observer.setStatus(statusRepository.findById(8));
                else if(presidentRepository.existsByJmbg(observer.getJmbg()))
                    observer.setStatus(statusRepository.findById(9));
                else
                    observer.setStatus(statusRepository.findById(1));
                observers.add(observer);

            }
            if(observers.size() < 1) {
                Notification notification = Notification.show("Nema detektovanih posmatrača u fajlu ili fajl nije naveden", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            isFileEmpty = false;
            stackEntity.setObservers(observers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleExecuteButtonClick() {
        String decisionNumberString = decisionNumber.getValue();
        if(decisionNumberString.isBlank()) {
            ConfirmDialog confirmDialog = showAlert("Neispavan broj odluke", "Broj odluke je obavezno polje");
            confirmDialog.open();
            return;
        }

        PoliticalOrganizationEntity chosenOrganization = organizations.getValue();
        if(chosenOrganization == null) {
            ConfirmDialog confirmDialog = showAlert("Neispravan politički subjekat", "Politički subjekat nije odabran.");
            confirmDialog.open();
            return;
        }
        if(isFileEmpty) {
            ConfirmDialog confirmDialog = showAlert("Dodajte validan fajl", "Fajl nije dodan ili nema navedenih posmatrača u fajlu");
            confirmDialog.open();
            return;
        }

        LocalDate date = datePicker.getValue();

        // Define the date range
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 1);

        // Check if the date is within the range
        boolean isWithinRange = !date.isBefore(startDate) && !date.isAfter(endDate);

        if(!isWithinRange) {
            ConfirmDialog confirmDialog = showAlert("Neispravan datum", "Odaberite datum između dana raspisivanja i dana održavanja izbora");
            confirmDialog.open();
            return;
        }

        if(upload == null) {
            ConfirmDialog confirmDialog = showAlert("Neregularan upload", "Upload ne radi na ocekivan nacin");
            confirmDialog.open();
            return;
        }



        stackEntity.setDecisionNumber(decisionNumberString);
        stackEntity.setDate(Date.valueOf(date));
        stackEntity.setPoliticalOrganization(chosenOrganization);

        Dialog dialog = createDialog();
        dialog.open();
    }

    private ConfirmDialog showAlert(String title, String body) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(body);

        dialog.setConfirmText("OK");
        return dialog;
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
        H2 headline = new H2("Nevalidni posmatrači");
        headline.addClassName("draggable");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em")
                .set("font-weight", "bold").set("cursor", "move")
                .set("padding", "var(--lumo-space-m) 0").set("flex", "1");

        return headline;
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        TextArea textArea = new TextArea();
        textArea.setValue("Ovde možete vidjeti spisak navalidnih posmatrača, kao i razlog zbog kojeg se smatraju nevalidnim. Ukoliko želite da budu dodani, dovoljno je selektovati ih.");
        textArea.setHeightFull(); // Set height as desired
        textArea.setReadOnly(true); // Optional: make the TextArea read-only if needed

        MultiSelectListBox<ObserverEntity> listBox = new MultiSelectListBox<>();
        listBox.setItems(stackEntity.getObservers().stream().filter(o -> o.getStatus().getId() != 1).collect(Collectors.toList()));
        listBox.setRenderer(new ComponentRenderer<>(observer -> {
            Span name = new Span(observer.getFullName());
            Span profession = new Span(observer.getStatus().getName());
            profession.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout column = new VerticalLayout(name, profession);
            column.setPadding(false);
            column.setSpacing(false);

            column.getStyle().set("line-height", "var(--lumo-line-height-m)");


            return column;
        }));

        VerticalLayout fieldLayout = new VerticalLayout(textArea, listBox);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "600px").set("max-width", "100%");

        Button cancelButton = new Button("Otkaži", e -> {
            dialog.close();
            resetForm();


        });
        Button saveButton = new Button("Akredituj posmatrače", e -> {
            listBox.getSelectedItems().forEach(o -> o.setForce(true));

            Set<ObserverEntity> observers = stackEntity.getObservers().stream().collect(Collectors.toSet());
            observers.forEach(o -> o.setStack(stackEntity));
            stackService.getStackRepository().save(stackEntity);
            for(ObserverEntity observer: observers)
                observerRepository.save(observer);
            dialog.close();
            resetForm();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return fieldLayout;
    }

    private void resetForm() {
        this.decisionNumber.clear();
        this.datePicker.clear();
        this.organizations.clear();
        this.stackEntity = new StackEntity();
        this.upload.clearFileList();

    }
}
