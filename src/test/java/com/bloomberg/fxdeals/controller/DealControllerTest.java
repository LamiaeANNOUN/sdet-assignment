package com.bloomberg.fxdeals.controller;

import com.bloomberg.fxdeals.dto.RowResult;
import com.bloomberg.fxdeals.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DealControllerTest {

        private MockMvc mockMvc;
        private DealService dealService;

        @BeforeEach
        void setUp() {
                dealService = Mockito.mock(DealService.class);
                DealController controller = new DealController(dealService);
                mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        void testImportDeals_Success() throws Exception {
                RowResult rowResult = new RowResult("D1", true, List.of());
                when(dealService.importDeals(anyList())).thenReturn(List.of(rowResult));

                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "deals.csv",
                                "text/csv",
                                "dealUniqueId,orderingCurrency,counterCurrency,amount,dealTimestamp\nD1,USD,EUR,1000,2025-11-29T12:00:00Z"
                                                .getBytes());

                mockMvc.perform(multipart("/api/deals/import")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isOk());
        }

        @Test
        void testImportDeals_PartialSuccess() throws Exception {
                RowResult successRow = new RowResult("D1", true, List.of());
                RowResult failedRow = new RowResult("D2", false, List.of("amount must be positive"));
                when(dealService.importDeals(anyList())).thenReturn(List.of(successRow, failedRow));

                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "deals.csv",
                                "text/csv",
                                ("dealUniqueId,orderingCurrency,counterCurrency,amount,dealTimestamp\n" +
                                                "D1,USD,EUR,1000,2025-11-29T12:00:00Z\n" +
                                                "D2,USD,EUR,-100,2025-11-29T12:00:00Z").getBytes());

                mockMvc.perform(multipart("/api/deals/import")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(result -> {
                                        int status = result.getResponse().getStatus();
                                        if (status != 200 && status != 207) {
                                                throw new AssertionError(
                                                                "Expected status 200 or 207 but got " + status);
                                        }
                                });
        }

        @Test
        void testImportDeals_ValidationError() throws Exception {
                RowResult failedRow = new RowResult("D2", false,
                                List.of("dealUniqueId missing", "amount must be positive"));
                when(dealService.importDeals(anyList())).thenReturn(List.of(failedRow));

                MockMultipartFile file = new MockMultipartFile(
                                "file",
                                "deals.csv",
                                "text/csv",
                                "dealUniqueId,orderingCurrency,counterCurrency,amount,dealTimestamp\n,,USD,EUR,-100,invalid-timestamp"
                                                .getBytes());

                mockMvc.perform(multipart("/api/deals/import")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isBadRequest());
        }
}
