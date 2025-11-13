package com.bloomberg.fxdeals.repository;

import com.bloomberg.fxdeals.entity.FxDeal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FxDealRepository.
 */
@ExtendWith(MockitoExtension.class)
class FxDealRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<FxDeal> dealQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private FxDealRepository repository;

    private FxDeal fxDeal;

    @BeforeEach
    void setUp() {
        fxDeal = new FxDeal(
            "DEAL001",
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("1000.50")
        );
        fxDeal.setId(1L);
    }

    @Test
    void testSave() {
        doNothing().when(entityManager).persist(any(FxDeal.class));
        doNothing().when(entityManager).flush();

        FxDeal result = repository.save(fxDeal);

        assertNotNull(result);
        verify(entityManager, times(1)).persist(fxDeal);
        verify(entityManager, times(1)).flush();
    }

    @Test
    void testFindById_Found() {
        when(entityManager.find(FxDeal.class, 1L)).thenReturn(fxDeal);

        Optional<FxDeal> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("DEAL001", result.get().getDealUniqueId());
        verify(entityManager, times(1)).find(FxDeal.class, 1L);
    }

    @Test
    void testFindById_NotFound() {
        when(entityManager.find(FxDeal.class, 1L)).thenReturn(null);

        Optional<FxDeal> result = repository.findById(1L);

        assertFalse(result.isPresent());
        verify(entityManager, times(1)).find(FxDeal.class, 1L);
    }

    @Test
    void testFindByDealUniqueId_Found() {
        when(entityManager.createQuery(anyString(), eq(FxDeal.class))).thenReturn(dealQuery);
        when(dealQuery.setParameter(anyString(), anyString())).thenReturn(dealQuery);
        when(dealQuery.getSingleResult()).thenReturn(fxDeal);

        Optional<FxDeal> result = repository.findByDealUniqueId("DEAL001");

        assertTrue(result.isPresent());
        assertEquals("DEAL001", result.get().getDealUniqueId());
        verify(entityManager, times(1)).createQuery(anyString(), eq(FxDeal.class));
    }

    @Test
    void testExistsByDealUniqueId_True() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean result = repository.existsByDealUniqueId("DEAL001");

        assertTrue(result);
        verify(entityManager, times(1)).createQuery(anyString(), eq(Long.class));
    }

    @Test
    void testExistsByDealUniqueId_False() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);

        boolean result = repository.existsByDealUniqueId("DEAL999");

        assertFalse(result);
        verify(entityManager, times(1)).createQuery(anyString(), eq(Long.class));
    }

    @Test
    void testFindAll() {
        List<FxDeal> deals = Arrays.asList(fxDeal);
        when(entityManager.createQuery(anyString(), eq(FxDeal.class))).thenReturn(dealQuery);
        when(dealQuery.getResultList()).thenReturn(deals);

        List<FxDeal> results = repository.findAll();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(entityManager, times(1)).createQuery(anyString(), eq(FxDeal.class));
    }

    @Test
    void testFindAllWithPagination() {
        List<FxDeal> deals = Arrays.asList(fxDeal);
        when(entityManager.createQuery(anyString(), eq(FxDeal.class))).thenReturn(dealQuery);
        when(dealQuery.setFirstResult(anyInt())).thenReturn(dealQuery);
        when(dealQuery.setMaxResults(anyInt())).thenReturn(dealQuery);
        when(dealQuery.getResultList()).thenReturn(deals);

        List<FxDeal> results = repository.findAll(0, 10);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(dealQuery, times(1)).setFirstResult(0);
        verify(dealQuery, times(1)).setMaxResults(10);
    }

    @Test
    void testCount() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        long count = repository.count();

        assertEquals(5L, count);
        verify(entityManager, times(1)).createQuery(anyString(), eq(Long.class));
    }
}
