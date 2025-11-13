package com.bloomberg.fxdeals.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "fx_deals", indexes = {
    @Index(name = "idx_deal_unique_id", columnList = "deal_unique_id", unique = true),
    @Index(name = "idx_deal_timestamp", columnList = "deal_timestamp")
})
public class FxDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Deal Unique ID is required")
    @Column(name = "deal_unique_id", nullable = false, unique = true, length = 100)
    private String dealUniqueId;

    @NotBlank(message = "From Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From Currency must be a valid 3-letter ISO code")
    @Column(name = "from_currency_iso", nullable = false, length = 3)
    private String fromCurrencyIso;

    @NotBlank(message = "To Currency ISO Code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To Currency must be a valid 3-letter ISO code")
    @Column(name = "to_currency_iso", nullable = false, length = 3)
    private String toCurrencyIso;

    @NotNull(message = "Deal timestamp is required")
    @PastOrPresent(message = "Deal timestamp cannot be in the future")
    @Column(name = "deal_timestamp", nullable = false)
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount is required")
    @DecimalMin(value = "0.01", message = "Deal amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Deal amount format is invalid")
    @Column(name = "deal_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal dealAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FxDeal() {
    }

    public FxDeal(String dealUniqueId, String fromCurrencyIso, String toCurrencyIso, 
                  LocalDateTime dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrencyIso = fromCurrencyIso;
        this.toCurrencyIso = toCurrencyIso;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FxDeal fxDeal = (FxDeal) o;
        return Objects.equals(dealUniqueId, fxDeal.dealUniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dealUniqueId);
    }

    @Override
    public String toString() {
        return "FxDeal{" +
                "id=" + id +
                ", dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrencyIso='" + fromCurrencyIso + '\'' +
                ", toCurrencyIso='" + toCurrencyIso + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
