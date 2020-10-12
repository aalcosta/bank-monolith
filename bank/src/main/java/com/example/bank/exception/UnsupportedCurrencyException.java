package com.example.bank.exception;

public class UnsupportedCurrencyException extends RuntimeException {

    public UnsupportedCurrencyException() {
    }

    public UnsupportedCurrencyException(String message) {
        super(message);
    }

    public UnsupportedCurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedCurrencyException(Throwable cause) {
        super(cause);
    }
}
