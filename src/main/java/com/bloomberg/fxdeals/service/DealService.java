package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.dto.RowResult;
import com.bloomberg.fxdeals.entity.DealEntity;
import com.bloomberg.fxdeals.repository.DealRepository;
import com.bloomberg.fxdeals.validation.DealValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DealService {
    private final DealRepository repo;
    private final DealValidator validator;
    private final Logger logger = LoggerFactory.getLogger(DealService.class);

    public DealService(DealRepository repo, DealValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public List<RowResult> importDeals(List<DealRequest> requests) {
        List<RowResult> results = new ArrayList<>();

        for (DealRequest r : requests) {
            if (r == null) {
                results.add(new RowResult(null, false, List.of("null request")));
                continue;
            }

            // Validate row
            List<String> errors = validator.validate(r);
            if (!errors.isEmpty()) {
                results.add(new RowResult(r.getDealUniqueId(), false, errors));
                logger.info("Rejected deal {}: validation errors {}", r.getDealUniqueId(), errors);
                continue;
            }

            // Dedup
            if (repo.existsByDealUniqueId(r.getDealUniqueId())) {
                results.add(new RowResult(r.getDealUniqueId(), false, List.of("duplicate")));
                logger.info("Duplicate deal skipped: {}", r.getDealUniqueId());
                continue;
            }

            // Persist row
            try {
                DealEntity e = new DealEntity();
                e.setDealUniqueId(r.getDealUniqueId());
                e.setOrderingCurrency(r.getOrderingCurrency().toUpperCase());
                e.setCounterCurrency(r.getCounterCurrency().toUpperCase());

                // Safe amount parsing
                try {
                    e.setAmount(r.getAmount());
                } catch (Exception ex) {
                    results.add(new RowResult(r.getDealUniqueId(), false, List.of("invalid_amount", ex.getMessage())));
                    logger.info("Invalid amount for deal {}: {}", r.getDealUniqueId(), ex.getMessage());
                    continue;
                }

                // Safe timestamp parsing
                try {
                    e.setDealTimestamp(OffsetDateTime.parse(r.getDealTimestamp()));
                } catch (Exception ex) {
                    results.add(
                            new RowResult(r.getDealUniqueId(), false, List.of("invalid_timestamp", ex.getMessage())));
                    logger.info("Invalid timestamp for deal {}: {}", r.getDealUniqueId(), r.getDealTimestamp());
                    continue;
                }

                // Save row
                repo.saveAndFlush(e);
                results.add(new RowResult(r.getDealUniqueId(), true, null));
            } catch (Exception ex) {
                results.add(new RowResult(r.getDealUniqueId(), false, List.of("persistence_error", ex.getMessage())));
                logger.error("Failed to persist deal {}: {}", r.getDealUniqueId(), ex.getMessage());
            }
        }

        return results;
    }

}
