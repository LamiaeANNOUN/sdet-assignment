package com.bloomberg.fxdeals.validation;

import com.bloomberg.fxdeals.dto.DealRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DealValidatorTest {
    private final DealValidator v = new DealValidator();

    @Test
    void validDeal() {
        DealRequest r = new DealRequest();
        r.setDealUniqueId("X1");
        r.setOrderingCurrency("USD");
        r.setCounterCurrency("EUR");
        r.setDealTimestamp("2025-11-28T12:00:00Z");
        r.setAmount(BigDecimal.valueOf(100));
        List<String> errors = v.validate(r);
        assertTrue(errors.isEmpty());
    }

    @Test
    void invalidDeal() {
        DealRequest r = new DealRequest();
        r.setDealUniqueId("");
        r.setOrderingCurrency("US");
        r.setCounterCurrency(null);
        r.setDealTimestamp("bad");
        r.setAmount(BigDecimal.valueOf(-1));
        List<String> errors = v.validate(r);
        assertFalse(errors.isEmpty());
        assertTrue(errors.size() >= 3);
    }
}
