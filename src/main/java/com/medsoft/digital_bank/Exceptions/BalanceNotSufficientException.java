package com.medsoft.digital_bank.Exceptions;

public class BalanceNotSufficientException extends RuntimeException {


    public BalanceNotSufficientException(String message ) {

        super(message);
    }
}
