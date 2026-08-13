package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.entities.StatusEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.ObserverRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ObserverPdfServiceTest {

    @Mock
    private ObserverRepository observerRepository;

    private ObserverPdfService service;

    @BeforeEach
    void setUp() {
        service = new ObserverPdfService(new LatinToCyrillicConverter(), new CyrillicToLatinConverter(), observerRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "true, false, true",
            "false, true, true",
            "false, false, false",
    })
    void isAcceptedForOperationalDocumentsWhenStatusAndForceAreSet(boolean statusSuccess, boolean force, boolean expected) {
        ObserverEntity observer = observerWith("Petar", "Kovac", statusSuccess, force);

        assertEquals(expected, ObserverPdfService.isAcceptedForOperationalDocuments(observer));
        assertEquals(!expected, ObserverPdfService.isRejectedForOperationalDocuments(observer));
    }

    @Test
    void isAcceptedAndIsRejectedForOperationalDocumentsDoNotThrowWhenBothAreNull() {
        // Regression test: the old filters compared "status.getSuccess() == true || force == true"
        // (accepted) and "status.getSuccess() == false && force == false" (rejected). Comparing a
        // null Boolean with a primitive boolean auto-unboxes it and throws NullPointerException,
        // so an observer whose status/force were never set would crash report generation.
        ObserverEntity observer = observerWith("Petar", "Kovac", null, null);

        assertFalse(ObserverPdfService.isAcceptedForOperationalDocuments(observer));
        assertTrue(ObserverPdfService.isRejectedForOperationalDocuments(observer));
    }

    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false",
    })
    void isFormallyAcceptedAndIsFormallyRejectedAreOppositeOfEachOtherForADeterminedStatus(boolean statusSuccess, boolean expectedAccepted) {
        ObserverEntity observer = observerWith("Petar", "Kovac", statusSuccess, false);

        assertEquals(expectedAccepted, ObserverPdfService.isFormallyAccepted(observer));
        assertEquals(!expectedAccepted, ObserverPdfService.isFormallyRejected(observer));
    }

    @Test
    void isFormallyAcceptedAndIsFormallyRejectedDoNotThrowAndAreBothFalseWhenStatusIsUndetermined() {
        // Same class of regression as above: "status.getSuccess() == false" also throws on null.
        // An observer with no determined status yet belongs in neither the accepted nor the
        // rejected decision table rather than crashing report generation.
        ObserverEntity observer = observerWith("Petar", "Kovac", null, false);

        assertFalse(ObserverPdfService.isFormallyAccepted(observer));
        assertFalse(ObserverPdfService.isFormallyRejected(observer));
    }

    @Test
    void getSortedTranslatedObserversFiltersSortsAndTranslatesWithoutMutatingTheOriginalEntity() {
        ObserverEntity petar = observerWith("Petar", "Kovac", true, false);
        ObserverEntity ana = observerWith("Ana", "Babic", true, false);
        ObserverEntity excluded = observerWith("Marko", "Zec", false, false);

        List<ObserverPdfService.TranslatedObserverName> result = service.getSortedTranslatedObservers(
                List.of(petar, ana, excluded), ObserverPdfService::isFormallyAccepted, ScriptEnum.CYRILLIC);

        assertEquals(List.of("БАБИЦ", "КОВАЦ"),
                result.stream().map(ObserverPdfService.TranslatedObserverName::lastname).toList());
        // The report was generated in Cyrillic, but the original, JPA-managed entities (which here
        // come straight from a StackEntity's managed "observers" collection) must be left
        // untouched, or Hibernate could flush the translated name back to the database.
        assertEquals("Petar", petar.getFirstname());
        assertEquals("Kovac", petar.getLastname());
    }

    @Test
    void getSortedTranslatedObserversLeavesLatinNamesUnchangedButUppercased() {
        ObserverEntity petar = observerWith("Petar", "Kovac", true, false);

        List<ObserverPdfService.TranslatedObserverName> result = service.getSortedTranslatedObservers(
                List.of(petar), ObserverPdfService::isFormallyAccepted, ScriptEnum.LATIN);

        assertEquals("PETAR", result.get(0).firstname());
        assertEquals("KOVAC", result.get(0).lastname());
    }

    private String generatedFilePath;

    @AfterEach
    void cleanUpGeneratedFile() {
        if (generatedFilePath != null) {
            new File(generatedFilePath).delete();
        }
    }

    @Test
    void downloadBlankObserversTemplateFillsInOrganizationAndClearsExampleObserverRows() throws IOException {
        PoliticalOrganizationEntity organization = new PoliticalOrganizationEntity();
        organization.setCode("00099");
        organization.setName("TEST STRANKA");

        generatedFilePath = service.downloadBlankObserversTemplate(organization, "observer_pdf_service_test_blank_template.xlsx");

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(generatedFilePath))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Organization cell (row 10, column D in Excel) is filled in with the requesting organization.
            Cell organizationCell = sheet.getRow(9).getCell(3);
            assertEquals("00099 - " + new LatinToCyrillicConverter().convert("TEST STRANKA"), organizationCell.getStringCellValue());

            // The template's example observer (row 21 in Excel, first data row) must be cleared out...
            XSSFRow firstExampleRow = sheet.getRow(20);
            assertBlank(firstExampleRow.getCell(1)); // JMBG
            assertBlank(firstExampleRow.getCell(2)); // document number
            assertBlank(firstExampleRow.getCell(3)); // lastname
            assertBlank(firstExampleRow.getCell(4)); // firstname
            // ...but the row-number placeholder in column A is left alone.
            assertEquals(1, (int) firstExampleRow.getCell(0).getNumericCellValue());
        }
    }

    @Test
    void downloadBlankObserversTemplateColorsTheHeaderRowAndTheOrganizationCell() throws IOException {
        PoliticalOrganizationEntity organization = new PoliticalOrganizationEntity();
        organization.setCode("00099");
        organization.setName("TEST STRANKA");

        generatedFilePath = service.downloadBlankObserversTemplate(organization, "observer_pdf_service_test_blank_template_colors.xlsx");

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(generatedFilePath))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Header row (row 19 in Excel: Р/Б, ЈМБГ, ..., Име) is filled with the template's own
            // theme accent1 color (xl/theme/theme1.xml in Primjer posmatraca.xlsx) - not an
            // invented one.
            for (int c = 0; c <= 4; c++) {
                assertFillColor("156082", 0.0, sheet.getRow(18).getCell(c));
            }

            // The auto-filled organization cell is highlighted with a lighter tint of that same
            // theme color, so it's obviously pre-filled without competing with the header.
            assertFillColor("156082", 0.6, sheet.getRow(9).getCell(3));

            // Wrap-text on the long JMBG header label must survive the color being applied.
            assertTrue(sheet.getRow(18).getCell(1).getCellStyle().getWrapText());
        }
    }

    private static void assertFillColor(String expectedHex, double expectedTint, Cell cell) {
        org.apache.poi.xssf.usermodel.XSSFColor color =
                ((org.apache.poi.xssf.usermodel.XSSFCellStyle) cell.getCellStyle()).getFillForegroundXSSFColor();
        assertEquals(expectedHex, color.getARGBHex().substring(2));
        assertEquals(expectedTint, color.getTint(), 0.0001);
    }

    @Test
    void downloadBlankObserversTemplatesForAllOrganizationsProducesOneEntryPerOrganization() throws IOException {
        PoliticalOrganizationEntity first = new PoliticalOrganizationEntity();
        first.setCode("00099");
        first.setName("TEST STRANKA");

        PoliticalOrganizationEntity second = new PoliticalOrganizationEntity();
        second.setCode("00042");
        second.setName("DRUGA STRANKA");

        generatedFilePath = service.downloadBlankObserversTemplatesForAllOrganizations(
                List.of(first, second), "observer_pdf_service_test_blank_templates.zip");

        List<String> entryNames = new java.util.ArrayList<>();
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(generatedFilePath))) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                entryNames.add(entry.getName());

                // Each entry must itself be a valid, readable xlsx workbook.
                ByteArrayOutputStream entryBytes = new ByteArrayOutputStream();
                zip.transferTo(entryBytes);
                try (XSSFWorkbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(entryBytes.toByteArray()))) {
                    assertEquals(1, workbook.getNumberOfSheets());
                }
            }
        }

        assertEquals(List.of("00099_TEST STRANKA.xlsx", "00042_DRUGA STRANKA.xlsx"), entryNames);
    }

    private static void assertBlank(Cell cell) {
        if (cell != null) {
            assertEquals(CellType.BLANK, cell.getCellType());
        }
    }

    private static ObserverEntity observerWith(String firstname, String lastname, Boolean statusSuccess, Boolean force) {
        StatusEntity status = new StatusEntity();
        status.setSuccess(statusSuccess);

        ObserverEntity observer = new ObserverEntity();
        observer.setFirstname(firstname);
        observer.setLastname(lastname);
        observer.setStatus(status);
        observer.setForce(force);
        return observer;
    }
}
