package com.bloomberg.fxdeals.exception;

/**
 * Exception thrown when attempting to save a duplicate FX deal.
 */
public class DuplicateDealException extends RuntimeException {

    private final String dealUniqueId;

    public DuplicateDealException(String dealUniqueId) {
        super(String.format("FX Deal with unique ID '%s' already exists", dealUniqueId));
        this.dealUniqueId = dealUniqueId;
    }

    public String getDealUniqueId() {
        return dealUniqueId;
    }
}
