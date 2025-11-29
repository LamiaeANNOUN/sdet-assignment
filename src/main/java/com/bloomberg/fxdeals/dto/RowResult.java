package com.bloomberg.fxdeals.dto;

import java.util.List;

public class RowResult {
    private String dealUniqueId;
    private boolean success;
    private List<String> errors;

    public RowResult() {}
    public RowResult(String dealUniqueId, boolean success, List<String> errors) {
        this.dealUniqueId = dealUniqueId;
        this.success = success;
        this.errors = errors;
    }
    public String getDealUniqueId() { return dealUniqueId; }
    public void setDealUniqueId(String dealUniqueId) { this.dealUniqueId = dealUniqueId; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public java.util.List<String> getErrors() { return errors; }
    public void setErrors(java.util.List<String> errors) { this.errors = errors; }
}
