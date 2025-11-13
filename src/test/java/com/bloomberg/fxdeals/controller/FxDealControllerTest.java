package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.service.FxDealService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FxDealController.
 */
@ExtendWith(MockitoExtension.class)
class FxDealControllerTest {

    @Mock
    private FxDealService dealService;

    @InjectMocks
    private FxDealController controller;

    private FxDealRequest request;
    private FxDealResponse response;

    @BeforeEach
    void setUp() {
        request = new FxDealRequest(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.50")
        );

        response = new FxDealResponse(
            1L,
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.50"),
            LocalDateTime.now()
        );
    }

    @Test
    void testCreateDeal_Success() {
        when(dealService.createDeal(any(FxDealRequest.class))).thenReturn(response);

        Response result = controller.createDeal(request);

        assertNotNull(result);
        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        verify(dealService, times(1)).createDeal(any(FxDealRequest.class));
    }

    @Test
    void testGetDealById_Success() {
        when(dealService.getDealById(anyLong())).thenReturn(response);

        Response result = controller.getDealById(1L);

        assertNotNull(result);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        verify(dealService, times(1)).getDealById(1L);
    }

    @Test
    void testGetDealByUniqueId_Success() {
        when(dealService.getDealByUniqueId(anyString())).thenReturn(response);

        Response result = controller.getDealByUniqueId("DEAL001");

        assertNotNull(result);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        verify(dealService, times(1)).getDealByUniqueId("DEAL001");
    }

    @Test
    void testGetAllDeals_Success() {
        List<FxDealResponse> deals = Arrays.asList(response);
        when(dealService.getDeals(anyInt(), anyInt())).thenReturn(deals);
        when(dealService.getTotalCount()).thenReturn(1L);

        Response result = controller.getAllDeals(0, 100);

        assertNotNull(result);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("1", result.getHeaderString("X-Total-Count"));
        verify(dealService, times(1)).getDeals(0, 100);
        verify(dealService, times(1)).getTotalCount();
    }

    @Test
    void testGetAllDeals_WithInvalidPagination() {
        List<FxDealResponse> deals = Arrays.asList(response);
        when(dealService.getDeals(anyInt(), anyInt())).thenReturn(deals);
        when(dealService.getTotalCount()).thenReturn(1L);

        // Test with limit > 1000 (should default to 100)
        Response result = controller.getAllDeals(0, 2000);

        assertNotNull(result);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        verify(dealService, times(1)).getDeals(0, 100);
    }

    @Test
    void testHealth() {
        Response result = controller.health();

        assertNotNull(result);
        assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        assertEquals("FX Deals API is running", result.getEntity());
    }
}
