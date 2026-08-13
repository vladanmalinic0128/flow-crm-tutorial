package com.example.application.services;

import com.example.application.entities.ObserverEntity;
import com.example.application.entities.StatusEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.ObserverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverallObserversServiceTest {

    @Mock
    private ObserverRepository observerRepository;

    private OverallObserversService service;

    @BeforeEach
    void setUp() {
        service = new OverallObserversService(observerRepository, new CyrillicToLatinConverter(), new LatinToCyrillicConverter());
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true",
            "true, false, true",
            "false, true, true",
            "false, false, false",
    })
    void isEligibleForReportWhenStatusAndForceAreSet(boolean statusSuccess, boolean force, boolean expected) {
        ObserverEntity observer = observerWith("Petar", "Kovač", statusSuccess, force);

        assertEquals(expected, OverallObserversService.isEligibleForReport(observer));
    }

    @Test
    void isEligibleForReportDoesNotThrowWhenStatusSuccessAndForceAreBothNull() {
        // Regression test: the old filter used "status.getSuccess() == true || observer.getForce() == true".
        // Comparing a null Boolean to a primitive boolean auto-unboxes it and throws
        // NullPointerException, so an observer whose status/force were never explicitly set would
        // crash report generation instead of simply being excluded.
        ObserverEntity observer = observerWith("Petar", "Kovač", null, null);

        assertFalse(OverallObserversService.isEligibleForReport(observer));
    }

    @Test
    void sortsEligibleObserversByLastnameThenFirstnameInTheTargetScript() {
        ObserverEntity petar = observerWith("Petar", "Kovac", true, false);
        ObserverEntity ana = observerWith("Ana", "Babic", true, false);
        ObserverEntity iva = observerWith("Iva", "Ilic", false, true);
        ObserverEntity excluded = observerWith("Marko", "Zec", false, false);
        when(observerRepository.findAll()).thenReturn(List.of(petar, ana, iva, excluded));

        List<OverallObserversService.TranslatedObserver> result =
                service.getEligibleObserversSortedByName(ScriptEnum.LATIN);

        assertEquals(
                List.of("Babic", "Ilic", "Kovac"),
                result.stream().map(OverallObserversService.TranslatedObserver::lastname).toList());
    }

    @Test
    void translatesNamesIntoCyrillicWithoutMutatingTheOriginalEntity() {
        ObserverEntity petar = observerWith("Petar", "Kovac", true, false);
        when(observerRepository.findAll()).thenReturn(List.of(petar));

        List<OverallObserversService.TranslatedObserver> result =
                service.getEligibleObserversSortedByName(ScriptEnum.CYRILLIC);

        assertEquals("Петар", result.get(0).firstname());
        assertEquals("Ковац", result.get(0).lastname());
        // The report was generated for the Cyrillic script, but the original, JPA-managed entity
        // must be left untouched - otherwise Hibernate could flush the translated (wrong) name
        // back to the database on a later, unrelated transaction within the same request.
        assertEquals("Petar", petar.getFirstname());
        assertEquals("Kovac", petar.getLastname());
    }

    @Test
    void leavesLatinNamesUnchangedWhenTargetScriptIsLatin() {
        ObserverEntity petar = observerWith("Petar", "Kovac", true, false);
        when(observerRepository.findAll()).thenReturn(List.of(petar));

        List<OverallObserversService.TranslatedObserver> result =
                service.getEligibleObserversSortedByName(ScriptEnum.LATIN);

        assertEquals("Petar", result.get(0).firstname());
        assertEquals("Kovac", result.get(0).lastname());
    }

    @Test
    void excludesIneligibleObserversFromTheResult() {
        ObserverEntity excluded = observerWith("Marko", "Zec", false, false);
        when(observerRepository.findAll()).thenReturn(List.of(excluded));

        List<OverallObserversService.TranslatedObserver> result =
                service.getEligibleObserversSortedByName(ScriptEnum.LATIN);

        assertTrue(result.isEmpty());
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
