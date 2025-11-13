package com.bloomberg.fxdeals.exception;

/**
 * Exception thrown when validation fails for FX deal data.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
