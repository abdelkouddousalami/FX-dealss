package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.entity.FxDeal;
import com.bloomberg.fxdeals.exception.DealNotFoundException;
import com.bloomberg.fxdeals.exception.DuplicateDealException;
import com.bloomberg.fxdeals.exception.ValidationException;
import com.bloomberg.fxdeals.mapper.FxDealMapper;
import com.bloomberg.fxdeals.repository.FxDealRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class FxDealService {

    private static final Logger logger = LoggerFactory.getLogger(FxDealService.class);

    @Inject
    FxDealRepository repository;

    @Inject
    FxDealMapper mapper;

    @Inject
    Validator validator;

    @Transactional
    public FxDealResponse createDeal(FxDealRequest request) {
        logger.info("Processing FX deal creation request for unique ID: {}", request.getDealUniqueId());

        validateRequest(request);

        if (repository.existsByDealUniqueId(request.getDealUniqueId())) {
            logger.warn("Duplicate FX deal detected with unique ID: {}", request.getDealUniqueId());
            throw new DuplicateDealException(request.getDealUniqueId());
        }

        FxDeal deal = mapper.toEntity(request);
        FxDeal savedDeal = repository.save(deal);

        logger.info("Successfully created FX deal with ID: {} and unique ID: {}", 
                    savedDeal.getId(), savedDeal.getDealUniqueId());

        return mapper.toResponse(savedDeal);
    }

    public FxDealResponse getDealById(Long id) {
        logger.debug("Retrieving FX deal by ID: {}", id);
        FxDeal deal = repository.findById(id)
            .orElseThrow(() -> new DealNotFoundException(id));
        return mapper.toResponse(deal);
    }

    public FxDealResponse getDealByUniqueId(String dealUniqueId) {
        logger.debug("Retrieving FX deal by unique ID: {}", dealUniqueId);
        FxDeal deal = repository.findByDealUniqueId(dealUniqueId)
            .orElseThrow(() -> new DealNotFoundException(
                String.format("FX Deal with unique ID '%s' not found", dealUniqueId)
            ));
        return mapper.toResponse(deal);
    }

    public List<FxDealResponse> getAllDeals() {
        logger.debug("Retrieving all FX deals");
        List<FxDeal> deals = repository.findAll();
        return deals.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<FxDealResponse> getDeals(int offset, int limit) {
        logger.debug("Retrieving FX deals with offset: {} and limit: {}", offset, limit);
        List<FxDeal> deals = repository.findAll(offset, limit);
        return deals.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }

    public long getTotalCount() {
        return repository.count();
    }

    private void validateRequest(FxDealRequest request) {
        Set<ConstraintViolation<FxDealRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
            logger.error("Validation failed for FX deal request: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (request.getFromCurrencyIso().equals(request.getToCurrencyIso())) {
            logger.error("Invalid currency pair: From and To currencies are the same");
            throw new ValidationException("From Currency and To Currency must be different");
        }

        logger.debug("Validation successful for FX deal request");
    }
}
