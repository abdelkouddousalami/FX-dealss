package com.bloomberg.fxdeals.exception;

/**
 * Exception thrown when an FX deal is not found.
 */
public class DealNotFoundException extends RuntimeException {

    public DealNotFoundException(String message) {
        super(message);
    }

    public DealNotFoundException(Long id) {
        super(String.format("FX Deal with ID '%d' not found", id));
    }
}
