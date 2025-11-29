package com.bloomberg.fxdeals.dto;

import java.math.BigDecimal;

public class DealRequest {
    private String dealUniqueId;
    private String orderingCurrency; // from currency ISO
    private String counterCurrency;  // to currency ISO
    private String dealTimestamp;    // ISO-8601 string
    private BigDecimal amount;

    public String getDealUniqueId() { return dealUniqueId; }
    public void setDealUniqueId(String dealUniqueId) { this.dealUniqueId = dealUniqueId; }
    public String getOrderingCurrency() { return orderingCurrency; }
    public void setOrderingCurrency(String orderingCurrency) { this.orderingCurrency = orderingCurrency; }
    public String getCounterCurrency() { return counterCurrency; }
    public void setCounterCurrency(String counterCurrency) { this.counterCurrency = counterCurrency; }
    public String getDealTimestamp() { return dealTimestamp; }
    public void setDealTimestamp(String dealTimestamp) { this.dealTimestamp = dealTimestamp; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
