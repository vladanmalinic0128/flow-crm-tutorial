package com.example.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountValidatorTest {

    private final BankAccountValidator validator = new BankAccountValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456"})
    void rejectsNullOrTooShortAccountNumbers(String accountNumber) {
        assertFalse(validator.isValidAccountNumber(accountNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "123456789", "12345678901"})
    void acceptsShortFormAccountNumbersBetween7And11Digits(String accountNumber) {
        assertTrue(validator.isValidAccountNumber(accountNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789012",     // 12 digits - between short and full format, invalid
            "123456789012345",  // 15 digits - one short of full format, invalid
            "12345678901234567" // 17 digits - longer than full format, invalid
    })
    void rejectsAccountNumbersInTheGapOrLongerThanFullFormat(String accountNumber) {
        assertFalse(validator.isValidAccountNumber(accountNumber));
    }

    @Test
    void acceptsFullFormAccountNumberWithCorrectMod97Checksum() {
        // base = 12345678901234, remainder of (base + "00") % 97 is 70, so control digits must be 98 - 70 = 28
        assertTrue(validator.isValidAccountNumber("1234567890123428"));
    }

    @Test
    void rejectsFullFormAccountNumberWithIncorrectMod97Checksum() {
        assertFalse(validator.isValidAccountNumber("1234567890123429"));
    }

    @ParameterizedTest
    @CsvSource({
            "abcdefg, letters within short-form length",
            "1234a67, digit/letter mix within short-form length",
            "123456 7, whitespace within short-form length",
    })
    void rejectsNonDigitCharactersEvenWhenLengthWouldOtherwiseBeValid(String accountNumber, String description) {
        assertFalse(validator.isValidAccountNumber(accountNumber));
    }

    @Test
    void rejectsFullFormAccountNumberWithNonDigitControlDigitsInsteadOfThrowing() {
        // 16 characters total, same as a valid full-form account, but the last two
        // "control" characters aren't digits (this used to throw NumberFormatException).
        assertFalse(validator.isValidAccountNumber("12345678901234XY"));
    }
}