package com.example.drift_check_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CbsBalance {
    private String accountId;
    private long reportedBalance;

    // getters and setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public long getReportedBalance() { return reportedBalance; }
    public void setReportedBalance(long reportedBalance) { this.reportedBalance = reportedBalance; }
}

