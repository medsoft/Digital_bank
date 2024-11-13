package com.medsoft.digital_bank.Exceptions;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
