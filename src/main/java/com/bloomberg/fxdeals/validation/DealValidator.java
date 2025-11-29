package com.bloomberg.fxdeals.validation;

import com.bloomberg.fxdeals.dto.DealRequest;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DealValidator {

    public List<String> validate(DealRequest r) {
        List<String> errors = new ArrayList<>();
        if (r == null) {
            errors.add("Row is null");
            return errors;
        }
        if (r.getDealUniqueId() == null || r.getDealUniqueId().isBlank()) {
            errors.add("dealUniqueId missing");
        }
        if (r.getOrderingCurrency() == null || r.getOrderingCurrency().length() != 3) {
            errors.add("orderingCurrency must be 3-letter ISO code");
        }
        if (r.getCounterCurrency() == null || r.getCounterCurrency().length() != 3) {
            errors.add("counterCurrency must be 3-letter ISO code");
        }
        if (r.getAmount() == null) {
            errors.add("amount missing");
        } else {
            if (r.getAmount().doubleValue() <= 0) {
                errors.add("amount must be positive");
            }
        }
        if (r.getDealTimestamp() == null) {
            errors.add("dealTimestamp missing");
        } else {
            try {
                OffsetDateTime.parse(r.getDealTimestamp());
            } catch (DateTimeParseException ex) {
                errors.add("dealTimestamp must be ISO-8601");
            }
        }
        return errors;
    }
}
