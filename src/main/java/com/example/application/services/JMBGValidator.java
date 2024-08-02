package com.example.application.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class JMBGValidator {

    public boolean isValidJMBG(String jmbg) {
        if (jmbg == null || jmbg.length() != 13 || !jmbg.matches("\\d+")) {
            return false;
        }

        // Validate the date part of JMBG
        String datePart = jmbg.substring(0, 7);
        if (!isValidDate(datePart)) {
            return false;
        }

        // Validate checksum
        return isValidChecksum(jmbg);
    }

    private boolean isValidDate(String datePart) {
        String day = datePart.substring(0, 2);
        String month = datePart.substring(2, 4);
        String year = datePart.substring(4, 7);

        // JMBG year is in the format yyy, which needs conversion
        int fullYear = Integer.parseInt(year);
        if (fullYear < 800) { // Adjusting year range
            fullYear += 2000;
        } else {
            fullYear += 1000;
        }

        String formattedDate = String.format("%02d-%02d-%04d", Integer.parseInt(day), Integer.parseInt(month), fullYear);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(formattedDate, formatter);
            return date != null;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidChecksum(String jmbg) {
        int[] coefficients = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int checksum = 0;

        for (int i = 0; i < 12; i++) {
            checksum += Character.getNumericValue(jmbg.charAt(i)) * coefficients[i];
        }

        int remainder = checksum % 11;
        int controlDigit = remainder == 0 ? 0 : 11 - remainder;

        return controlDigit == Character.getNumericValue(jmbg.charAt(12));
    }
}
