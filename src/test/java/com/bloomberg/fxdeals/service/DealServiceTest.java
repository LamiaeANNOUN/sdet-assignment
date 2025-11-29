package com.bloomberg.fxdeals.service;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.dto.RowResult;
import com.bloomberg.fxdeals.repository.DealRepository;
import com.bloomberg.fxdeals.validation.DealValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DealServiceTest {
    @Test
    void importDealsHandlesDuplicatesAndValidation() {
        DealRepository repo = Mockito.mock(DealRepository.class);
        when(repo.existsByDealUniqueId(anyString())).thenReturn(false);

        DealValidator validator = new DealValidator();
        DealService svc = new DealService(repo, validator);

        DealRequest good = new DealRequest();
        good.setDealUniqueId("G1");
        good.setOrderingCurrency("USD");
        good.setCounterCurrency("EUR");
        good.setDealTimestamp("2025-11-28T12:00:00Z");
        good.setAmount(BigDecimal.valueOf(10));

        DealRequest bad = new DealRequest(); // invalid
        bad.setDealUniqueId("");
        bad.setOrderingCurrency("US");
        bad.setCounterCurrency("EU");
        bad.setDealTimestamp("bad");
        bad.setAmount(BigDecimal.valueOf(-5));

        List<RowResult> results = svc.importDeals(List.of(good, bad));
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.isSuccess() && "G1".equals(r.getDealUniqueId())));
        assertTrue(results.stream()
                .anyMatch(r -> !r.isSuccess() && (r.getDealUniqueId() == null || r.getDealUniqueId().isBlank())));
    }
}
