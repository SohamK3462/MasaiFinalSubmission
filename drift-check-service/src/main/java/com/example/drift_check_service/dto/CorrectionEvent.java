package com.example.drift_check_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionEvent {
    private String eventId;
    private String accountId;
    private String type; // credit or debit_reversal
    private long amount;


}

