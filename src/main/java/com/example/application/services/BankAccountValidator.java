package com.example.application.services;

import org.springframework.stereotype.Service;

@Service
public class BankAccountValidator {
    public boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber.length() > 11 && accountNumber.length() != 16) {
            return false;
        }

        String baseAccountNumber = accountNumber.substring(0, 14);
        String controlDigits = accountNumber.substring(14);

        if (!baseAccountNumber.matches("\\d+")) {
            return false;
        }

        String numberToValidate = baseAccountNumber + "00";

        long number = Long.parseLong(numberToValidate);
        long controlNumber = Long.parseLong(controlDigits);

        long remainder = number % 97;

        return remainder + controlNumber == 98;
    }
}
