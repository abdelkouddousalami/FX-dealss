package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.entity.FxDeal;
import com.bloomberg.fxdeals.exception.DealNotFoundException;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.ValidationException;
import com.bloomberg.fxdeals.mapper.FxDealMapper;
import com.bloomberg.fxdeals.repository.FxDealRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FxDealService.
 */
@ExtendWith(MockitoExtension.class)
class FxDealServiceTest {

    @Mock
    private FxDealRepository repository;

    @Mock
    private FxDealMapper mapper;

    @InjectMocks
    private FxDealService service;

    private Validator validator;
    private FxDealRequest validRequest;
    private FxDeal fxDeal;
    private FxDealResponse response;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        service.validator = validator;

        validRequest = new FxDealRequest(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now().minusDays(1),
            new BigDecimal("1000.50")
        );

        fxDeal = new FxDeal(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now().minusDays(1),
            new BigDecimal("1000.50")
        );
        fxDeal.setId(1L);

        response = new FxDealResponse(
            1L,
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now().minusDays(1),
            new BigDecimal("1000.50"),
            LocalDateTime.now()
        );
    }

    @Test
    void testCreateDeal_Success() {
        when(repository.existsByDealUniqueId(anyString())).thenReturn(false);
        when(mapper.toEntity(any(FxDealRequest.class))).thenReturn(fxDeal);
        when(repository.save(any(FxDeal.class))).thenReturn(fxDeal);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(response);

        FxDealResponse result = service.createDeal(validRequest);

        assertNotNull(result);
        assertEquals("DEAL001", result.getDealUniqueId());
        verify(repository, times(1)).existsByDealUniqueId("DEAL001");
        verify(repository, times(1)).save(any(FxDeal.class));
    }

    @Test
    void testCreateDeal_DuplicateDeal() {
        when(repository.existsByDealUniqueId(anyString())).thenReturn(true);

        assertThrows(DuplicateDealException.class, () -> service.createDeal(validRequest));
        verify(repository, times(1)).existsByDealUniqueId("DEAL001");
        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void testCreateDeal_ValidationError_MissingDealId() {
        FxDealRequest invalidRequest = new FxDealRequest(
            null,
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.00")
        );

        assertThrows(ValidationException.class, () -> service.createDeal(invalidRequest));
        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void testCreateDeal_ValidationError_InvalidCurrency() {
        FxDealRequest invalidRequest = new FxDealRequest(
            "DEAL002",
            "US",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.00")
        );

        assertThrows(ValidationException.class, () -> service.createDeal(invalidRequest));
        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void testCreateDeal_ValidationError_SameCurrencies() {
        FxDealRequest invalidRequest = new FxDealRequest(
            "DEAL003",
            "USD",
            "USD",
            LocalDateTime.now(),
            new BigDecimal("1000.00")
        );

        assertThrows(ValidationException.class, () -> service.createDeal(invalidRequest));
        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void testCreateDeal_ValidationError_NegativeAmount() {
        FxDealRequest invalidRequest = new FxDealRequest(
            "DEAL004",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("-100.00")
        );

        assertThrows(ValidationException.class, () -> service.createDeal(invalidRequest));
        verify(repository, never()).save(any(FxDeal.class));
    }

    @Test
    void testGetDealById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(fxDeal));
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(response);

        FxDealResponse result = service.getDealById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetDealById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DealNotFoundException.class, () -> service.getDealById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetDealByUniqueId_Success() {
        when(repository.findByDealUniqueId("DEAL001")).thenReturn(Optional.of(fxDeal));
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(response);

        FxDealResponse result = service.getDealByUniqueId("DEAL001");

        assertNotNull(result);
        assertEquals("DEAL001", result.getDealUniqueId());
        verify(repository, times(1)).findByDealUniqueId("DEAL001");
    }

    @Test
    void testGetDealByUniqueId_NotFound() {
        when(repository.findByDealUniqueId("DEAL001")).thenReturn(Optional.empty());

        assertThrows(DealNotFoundException.class, () -> service.getDealByUniqueId("DEAL001"));
        verify(repository, times(1)).findByDealUniqueId("DEAL001");
    }

    @Test
    void testGetAllDeals_Success() {
        List<FxDeal> deals = Arrays.asList(fxDeal);
        when(repository.findAll()).thenReturn(deals);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(response);

        List<FxDealResponse> results = service.getAllDeals();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetDeals_WithPagination() {
        List<FxDeal> deals = Arrays.asList(fxDeal);
        when(repository.findAll(0, 10)).thenReturn(deals);
        when(mapper.toResponse(any(FxDeal.class))).thenReturn(response);

        List<FxDealResponse> results = service.getDeals(0, 10);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(repository, times(1)).findAll(0, 10);
    }

    @Test
    void testGetTotalCount() {
        when(repository.count()).thenReturn(5L);

        long count = service.getTotalCount();

        assertEquals(5L, count);
        verify(repository, times(1)).count();
    }
}
