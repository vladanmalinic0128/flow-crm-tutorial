package com.example.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JMBGValidatorTest {

    private final JMBGValidator validator = new JMBGValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "010100100005",     // 12 digits - one short
            "01010010000056",  // 14 digits - one too many
            "010100a000005"     // 13 characters, but contains a non-digit character
    })
    void rejectsNullWrongLengthOrNonDigitInput(String jmbg) {
        assertFalse(validator.isValidJMBG(jmbg));
    }

    @Test
    void rejectsNonExistentCalendarDate() {
        // 30th of February does not exist in any year.
        assertFalse(validator.isValidJMBG("3002001000012"));
    }

    @Test
    void rejectsInvalidMonth() {
        assertFalse(validator.isValidJMBG("0113001000012"));
    }

    @Test
    void rejectsFebruary29thOnNonLeapYear() {
        // 2005 is not a leap year.
        assertFalse(validator.isValidJMBG("2902005000012"));
    }

    @Test
    void acceptsFebruary29thOnLeapYearWithCorrectChecksum() {
        // 29.02.2004, 2004 is a leap year, and the checksum digit is correct.
        assertTrue(validator.isValidJMBG("2902004000006"));
    }

    @Test
    void acceptsValidJmbgWithOrdinaryChecksumRemainder() {
        // Checksum of the first 12 digits % 11 == 5, control digit 6.
        assertTrue(validator.isValidJMBG("0101001000056"));
    }

    @Test
    void rejectsJmbgWithWrongControlDigit() {
        // Same as above but with the control digit changed from 6 to 7.
        assertFalse(validator.isValidJMBG("0101001000057"));
    }

    @Test
    void acceptsValidJmbgWhereChecksumRemainderIsZero() {
        // Checksum of the first 12 digits % 11 == 0, control digit 0.
        assertTrue(validator.isValidJMBG("0101001000080"));
    }

    @Test
    void acceptsValidJmbgWhereChecksumRemainderIsOne() {
        // Regression test: checksum of the first 12 digits % 11 == 1, which per the official
        // algorithm also resolves to control digit 0 (both remainder 0 and 1 map to 0).
        // The previous implementation computed 11 - 1 = 10 here, a control digit that can never
        // match any single digit, so every JMBG landing on this remainder was wrongly rejected.
        assertTrue(validator.isValidJMBG("0101001000030"));
    }
}
