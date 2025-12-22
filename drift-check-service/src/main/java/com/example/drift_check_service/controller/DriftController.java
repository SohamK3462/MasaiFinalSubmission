package com.example.drift_check_service.controller;


import com.example.drift_check_service.dto.CbsBalance;
import com.example.drift_check_service.service.DriftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drift")
public class DriftController {

    private final DriftService driftService;

    public DriftController(DriftService driftService) {
        this.driftService = driftService;
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkDrift(@RequestBody List<CbsBalance> balances) {
        driftService.checkDrift(balances);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/correct/{accountId}")
    public ResponseEntity<Void> manualCorrection(
            @PathVariable String accountId,
            @RequestParam long amount,
            @RequestParam String type
    ) {
        driftService.manualCorrection(accountId, amount, type);
        return ResponseEntity.ok().build();
    }
}

