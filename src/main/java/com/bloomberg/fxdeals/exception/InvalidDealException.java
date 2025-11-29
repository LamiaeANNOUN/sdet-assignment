package com.bloomberg.fxdeals.exception;

import java.util.List;

public class InvalidDealException extends RuntimeException {
    private final List<String> errors;
    public InvalidDealException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    public List<String> getErrors() { return errors; }
}
