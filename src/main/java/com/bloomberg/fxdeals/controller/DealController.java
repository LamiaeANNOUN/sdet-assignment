package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.dto.RowResult;
import com.bloomberg.fxdeals.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService service;
    private final Logger logger = LoggerFactory.getLogger(DealController.class);

    public DealController(DealService service) {
        this.service = service;
    }

    @PostMapping("/import")
    public ResponseEntity<List<RowResult>> importDeals(@RequestParam("file") MultipartFile file) {
        try {
            List<DealRequest> deals = parseCsv(file);
            List<RowResult> results = service.importDeals(deals);

            boolean allFailed = results.stream().allMatch(r -> !r.isSuccess());
            boolean anyFailed = results.stream().anyMatch(r -> !r.isSuccess());

            if (allFailed) {
                return ResponseEntity.badRequest().body(results); // 400
            } else if (anyFailed) {
                return ResponseEntity.status(207).body(results); // 207 Partial success
            } else {
                return ResponseEntity.ok(results); // 200 Success
            }

        } catch (Exception e) {
            logger.error("Unexpected error importing deals", e);
            List<RowResult> errorResult = List.of(
                    new RowResult(null, false, List.of("Internal server error", e.getMessage())));
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    private List<DealRequest> parseCsv(MultipartFile file) {
        List<DealRequest> deals = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // skip header
                }

                String[] fields = line.split(",");
                if (fields.length < 5)
                    continue;

                DealRequest deal = new DealRequest();
                deal.setDealUniqueId(fields[0].trim().isEmpty() ? null : fields[0].trim());
                deal.setOrderingCurrency(fields[1].trim().isEmpty() ? null : fields[1].trim());
                deal.setCounterCurrency(fields[2].trim().isEmpty() ? null : fields[2].trim());

                try {
                    deal.setAmount(new BigDecimal(fields[3].trim()));
                } catch (Exception e) {
                    deal.setAmount(null);
                }

                String ts = fields[4].trim();
                try {
                    java.time.OffsetDateTime.parse(ts);
                    deal.setDealTimestamp(ts);
                } catch (Exception ex) {
                    deal.setDealTimestamp(null);
                }

                deals.add(deal);
            }
        } catch (Exception e) {
            logger.error("Error reading CSV file", e);
        }
        return deals;
    }
}
