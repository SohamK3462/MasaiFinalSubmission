package com.example.drift_check_service.controller;

import com.example.drift_check_service.dto.CbsBalance;
import com.example.drift_check_service.service.DriftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DriftControllerTest {
    private DriftService driftService;
    private DriftController controller;

    @BeforeEach
    void setUp() {
        driftService = Mockito.mock(DriftService.class);
        controller = new DriftController(driftService);
    }

    @Test
    void testCheckDrift() {
        List<CbsBalance> balances = Collections.emptyList();
        ResponseEntity<Void> response = controller.checkDrift(balances);
        assertEquals(200, response.getStatusCode().value());
        verify(driftService, times(1)).checkDrift(balances);
    }

    @Test
    void testManualCorrection() {
        String accountId = "acc123";
        long amount = 100L;
        String type = "CREDIT";
        ResponseEntity<Void> response = controller.manualCorrection(accountId, amount, type);
        assertEquals(200, response.getStatusCode().value());
        verify(driftService, times(1)).manualCorrection(accountId, amount, type);
    }
}
