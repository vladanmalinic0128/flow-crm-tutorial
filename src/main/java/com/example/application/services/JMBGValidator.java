package com.example.application.services;

import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;

@Service
public class JMBGValidator {
    private static final int JMBG_LENGTH = 13;
    private static final int DATE_PART_LENGTH = 7;
    private static final int CHECKSUM_DIGIT_COUNT = 12;
    private static final int[] CHECKSUM_COEFFICIENTS = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int CHECKSUM_MODULUS = 11;
    // JMBG only stores the last 3 digits of the birth year, so the century has to be guessed:
    // 000-799 is treated as 2000-2799, 800-999 as 1800-1999.
    private static final int SHORT_YEAR_CENTURY_THRESHOLD = 800;
    private static final int YEAR_BASE_FOR_LOW_DIGITS = 2000;
    private static final int YEAR_BASE_FOR_HIGH_DIGITS = 1000;

    public boolean isValidJMBG(String jmbg) {
        if (jmbg == null || jmbg.length() != JMBG_LENGTH || !jmbg.matches("\\d+")) {
            return false;
        }

        return isValidDate(jmbg.substring(0, DATE_PART_LENGTH)) && isValidChecksum(jmbg);
    }

    private boolean isValidDate(String datePart) {
        int day = Integer.parseInt(datePart.substring(0, 2));
        int month = Integer.parseInt(datePart.substring(2, 4));
        int yearDigits = Integer.parseInt(datePart.substring(4, 7));
        int year = yearDigits < SHORT_YEAR_CENTURY_THRESHOLD
                ? YEAR_BASE_FOR_LOW_DIGITS + yearDigits
                : YEAR_BASE_FOR_HIGH_DIGITS + yearDigits;

        try {
            LocalDate date = LocalDate.of(year, month, day);
            return date != null;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean isValidChecksum(String jmbg) {
        int checksum = 0;
        for (int i = 0; i < CHECKSUM_DIGIT_COUNT; i++) {
            checksum += Character.getNumericValue(jmbg.charAt(i)) * CHECKSUM_COEFFICIENTS[i];
        }

        int remainder = checksum % CHECKSUM_MODULUS;
        // Per the official algorithm a remainder of 0 or 1 both resolve to control digit 0.
        int controlDigit = remainder <= 1 ? 0 : CHECKSUM_MODULUS - remainder;

        return controlDigit == Character.getNumericValue(jmbg.charAt(CHECKSUM_DIGIT_COUNT));
    }
}
