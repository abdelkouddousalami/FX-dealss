package com.bloomberg.fxdeals.repository;

import com.bloomberg.fxdeals.entity.FxDeal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FxDealRepository {

    private static final Logger logger = LoggerFactory.getLogger(FxDealRepository.class);

    @PersistenceContext(unitName = "fxdealsPU")
    private EntityManager entityManager;

    public FxDeal save(FxDeal deal) {
        logger.debug("Saving FX deal with unique ID: {}", deal.getDealUniqueId());
        entityManager.persist(deal);
        entityManager.flush();
        logger.info("Successfully saved FX deal with ID: {} and unique ID: {}", 
                    deal.getId(), deal.getDealUniqueId());
        return deal;
    }

    public Optional<FxDeal> findById(Long id) {
        logger.debug("Finding FX deal by ID: {}", id);
        FxDeal deal = entityManager.find(FxDeal.class, id);
        return Optional.ofNullable(deal);
    }

    public Optional<FxDeal> findByDealUniqueId(String dealUniqueId) {
        logger.debug("Finding FX deal by unique ID: {}", dealUniqueId);
        try {
            TypedQuery<FxDeal> query = entityManager.createQuery(
                "SELECT f FROM FxDeal f WHERE f.dealUniqueId = :dealUniqueId", 
                FxDeal.class
            );
            query.setParameter("dealUniqueId", dealUniqueId);
            FxDeal deal = query.getSingleResult();
            logger.debug("Found FX deal with unique ID: {}", dealUniqueId);
            return Optional.of(deal);
        } catch (NoResultException e) {
            logger.debug("No FX deal found with unique ID: {}", dealUniqueId);
            return Optional.empty();
        }
    }

    public boolean existsByDealUniqueId(String dealUniqueId) {
        logger.debug("Checking if FX deal exists with unique ID: {}", dealUniqueId);
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(f) FROM FxDeal f WHERE f.dealUniqueId = :dealUniqueId", 
            Long.class
        );
        query.setParameter("dealUniqueId", dealUniqueId);
        Long count = query.getSingleResult();
        boolean exists = count > 0;
        logger.debug("FX deal with unique ID {} exists: {}", dealUniqueId, exists);
        return exists;
    }

    public List<FxDeal> findAll() {
        logger.debug("Retrieving all FX deals");
        TypedQuery<FxDeal> query = entityManager.createQuery(
            "SELECT f FROM FxDeal f ORDER BY f.dealTimestamp DESC", 
            FxDeal.class
        );
        List<FxDeal> deals = query.getResultList();
        logger.debug("Retrieved {} FX deals", deals.size());
        return deals;
    }

    public List<FxDeal> findAll(int offset, int limit) {
        logger.debug("Retrieving FX deals with offset: {} and limit: {}", offset, limit);
        TypedQuery<FxDeal> query = entityManager.createQuery(
            "SELECT f FROM FxDeal f ORDER BY f.dealTimestamp DESC", 
            FxDeal.class
        );
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<FxDeal> deals = query.getResultList();
        logger.debug("Retrieved {} FX deals", deals.size());
        return deals;
    }

    public long count() {
        logger.debug("Counting total FX deals");
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(f) FROM FxDeal f", 
            Long.class
        );
        Long count = query.getSingleResult();
        logger.debug("Total FX deals: {}", count);
        return count;
    }
}
