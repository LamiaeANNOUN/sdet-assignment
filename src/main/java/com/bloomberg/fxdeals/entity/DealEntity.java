package com.bloomberg.fxdeals.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "deals", uniqueConstraints = {
    @UniqueConstraint(name = "uk_deal_external_id", columnNames = {"deal_unique_id"})
})
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "deal_unique_id", nullable = false)
    private String dealUniqueId;

    @Column(name = "ordering_currency", length = 3, nullable = false)
    private String orderingCurrency;

    @Column(name = "counter_currency", length = 3, nullable = false)
    private String counterCurrency;

    @Column(name = "deal_timestamp", nullable = false)
    private OffsetDateTime dealTimestamp;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDealUniqueId() { return dealUniqueId; }
    public void setDealUniqueId(String dealUniqueId) { this.dealUniqueId = dealUniqueId; }
    public String getOrderingCurrency() { return orderingCurrency; }
    public void setOrderingCurrency(String orderingCurrency) { this.orderingCurrency = orderingCurrency; }
    public String getCounterCurrency() { return counterCurrency; }
    public void setCounterCurrency(String counterCurrency) { this.counterCurrency = counterCurrency; }
    public OffsetDateTime getDealTimestamp() { return dealTimestamp; }
    public void setDealTimestamp(OffsetDateTime dealTimestamp) { this.dealTimestamp = dealTimestamp; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
