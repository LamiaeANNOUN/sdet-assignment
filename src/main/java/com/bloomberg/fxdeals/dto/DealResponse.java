package com.bloomberg.fxdeals.dto;

public class DealResponse {
    private String dealUniqueId;
    private String status; // ACCEPTED | REJECTED
    private String reason; // null if accepted

    public DealResponse() {}
    public DealResponse(String dealUniqueId, String status, String reason) {
        this.dealUniqueId = dealUniqueId; this.status = status; this.reason = reason;
    }
    public String getDealUniqueId() { return dealUniqueId; }
    public void setDealUniqueId(String dealUniqueId) { this.dealUniqueId = dealUniqueId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
