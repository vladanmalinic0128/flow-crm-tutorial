package com.example.application.services;

import org.springframework.stereotype.Service;

@Service
public class BankAccountValidator {
    private static final int SHORT_FORMAT_MIN_LENGTH = 7;
    private static final int SHORT_FORMAT_MAX_LENGTH = 11;
    private static final int FULL_FORMAT_LENGTH = 16;
    private static final int FULL_FORMAT_BASE_LENGTH = 14;
    private static final int MOD_97 = 97;
    private static final int MOD_97_TARGET = 98;

    public boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || !accountNumber.matches("\\d+")) {
            return false;
        }

        int length = accountNumber.length();
        if (length >= SHORT_FORMAT_MIN_LENGTH && length <= SHORT_FORMAT_MAX_LENGTH) {
            return true;
        }

        return length == FULL_FORMAT_LENGTH && hasValidMod97Checksum(accountNumber);
    }

    private boolean hasValidMod97Checksum(String accountNumber) {
        String baseAccountNumber = accountNumber.substring(0, FULL_FORMAT_BASE_LENGTH);
        String controlDigits = accountNumber.substring(FULL_FORMAT_BASE_LENGTH);

        long remainder = Long.parseLong(baseAccountNumber + "00") % MOD_97;
        long controlNumber = Long.parseLong(controlDigits);

        return remainder + controlNumber == MOD_97_TARGET;
    }
}
