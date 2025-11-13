package com.bloomberg.fxdeals.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FxDealResponse {

    private Long id;
    private String dealUniqueId;
    private String fromCurrencyIso;
    private String toCurrencyIso;
    private LocalDateTime dealTimestamp;
    private BigDecimal dealAmount;
    private LocalDateTime createdAt;

    public FxDealResponse() {
    }

    public FxDealResponse(Long id, String dealUniqueId, String fromCurrencyIso, String toCurrencyIso,
                          LocalDateTime dealTimestamp, BigDecimal dealAmount, LocalDateTime createdAt) {
        this.id = id;
        this.dealUniqueId = dealUniqueId;
        this.fromCurrencyIso = fromCurrencyIso;
        this.toCurrencyIso = toCurrencyIso;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FxDealResponse{" +
                "id=" + id +
                ", dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrencyIso='" + fromCurrencyIso + '\'' +
                ", toCurrencyIso='" + toCurrencyIso + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}
