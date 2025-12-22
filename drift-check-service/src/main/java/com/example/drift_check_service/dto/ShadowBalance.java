package com.example.drift_check_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShadowBalance {
    private String accountId;
    private long balance;

    // getters/setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }
}

