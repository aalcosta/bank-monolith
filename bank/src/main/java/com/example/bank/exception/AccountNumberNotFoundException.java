package com.example.bank.exception;

public class AccountNumberNotFoundException extends RuntimeException {

    public AccountNumberNotFoundException() {
    }

    public AccountNumberNotFoundException(String message) {
        super(message);
    }

    public AccountNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNumberNotFoundException(Throwable cause) {
        super(cause);
    }
}
