package com.example.application.services;

import org.springframework.stereotype.Service;

@Service
public class BankAccountValidator {
    public static boolean isValidAccountNumber(String accountNumber) {
        // Check if the account number is exactly 16 characters long
        if (accountNumber.length() != 16) {
            return false;
        }

        // Extract the base account number and control digits
        String baseAccountNumber = accountNumber.substring(0, 14);
        String controlDigits = accountNumber.substring(14);

        // Ensure the base account number contains only digits
        if (!baseAccountNumber.matches("\\d+")) {
            return false;
        }

        // Append "00" to the base account number
        String numberToValidate = baseAccountNumber + "00";

        // Convert to a long integer
        long number = Long.parseLong(numberToValidate);

        // Calculate the remainder when divided by 97
        long remainder = number % 97;

        // Validate the remainder
        return remainder == 1;
    }
}
