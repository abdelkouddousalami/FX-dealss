package com.bloomberg.fxdeals.mapper;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.entity.FxDeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FxDealMapper.
 */
class FxDealMapperTest {

    private FxDealMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FxDealMapper();
    }

    @Test
    void testToEntity_Success() {
        FxDealRequest request = new FxDealRequest(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.50")
        );

        FxDeal entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("DEAL001", entity.getDealUniqueId());
        assertEquals("USD", entity.getFromCurrencyIso());
        assertEquals("EUR", entity.getToCurrencyIso());
        assertNotNull(entity.getDealTimestamp());
        assertEquals(new BigDecimal("1000.50"), entity.getDealAmount());
    }

    @Test
    void testToEntity_Null() {
        FxDeal entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToResponse_Success() {
        FxDeal deal = new FxDeal(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.50")
        );
        deal.setId(1L);

        FxDealResponse response = mapper.toResponse(deal);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("DEAL001", response.getDealUniqueId());
        assertEquals("USD", response.getFromCurrencyIso());
        assertEquals("EUR", response.getToCurrencyIso());
        assertNotNull(response.getDealTimestamp());
        assertEquals(new BigDecimal("1000.50"), response.getDealAmount());
    }

    @Test
    void testToResponse_Null() {
        FxDealResponse response = mapper.toResponse(null);
        assertNull(response);
    }
}
