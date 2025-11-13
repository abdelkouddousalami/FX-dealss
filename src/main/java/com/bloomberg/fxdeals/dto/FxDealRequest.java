package com.bloomberg.fxdeals.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FxDealRequest {

    @NotBlank(message = "Deal Unique ID is required")
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency must be a valid 3-letter ISO code (e.g., USD)")
    private String fromCurrencyIso;

    @NotBlank(message = "To Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency must be a valid 3-letter ISO code (e.g., EUR)")
    private String toCurrencyIso;

    @NotNull(message = "Deal timestamp is required")
    @PastOrPresent(message = "Deal timestamp cannot be in the future")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount is required")
    @DecimalMin(value = "0.01", message = "Deal amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Deal amount must have at most 15 integer digits and 2 decimal digits")
    private BigDecimal dealAmount;

    public FxDealRequest() {
    }

    public FxDealRequest(String dealUniqueId, String fromCurrencyIso, String toCurrencyIso, 
                         LocalDateTime dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrencyIso = fromCurrencyIso;
        this.toCurrencyIso = toCurrencyIso;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    public String getDealUniqueId() {
        return dealUniqueId;
    }

    public void setDealUniqueId(String dealUniqueId) {
        this.dealUniqueId = dealUniqueId;
    }

    public String getFromCurrencyIso() {
        return fromCurrencyIso;
    }

    public void setFromCurrencyIso(String fromCurrencyIso) {
        this.fromCurrencyIso = fromCurrencyIso;
    }

    public String getToCurrencyIso() {
        return toCurrencyIso;
    }

    public void setToCurrencyIso(String toCurrencyIso) {
        this.toCurrencyIso = toCurrencyIso;
    }

    public LocalDateTime getDealTimestamp() {
        return dealTimestamp;
    }

    public void setDealTimestamp(LocalDateTime dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    @Override
    public String toString() {
        return "FxDealRequest{" +
                "dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrencyIso='" + fromCurrencyIso + '\'' +
                ", toCurrencyIso='" + toCurrencyIso + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                '}';
    }
}
