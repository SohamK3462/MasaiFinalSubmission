package com.example.drift_check_service.service;




import com.example.drift_check_service.client.ShadowLedgerClient;
import com.example.drift_check_service.dto.CbsBalance;
import com.example.drift_check_service.dto.CorrectionEvent;
import com.example.drift_check_service.dto.ShadowBalance;

import com.example.drift_check_service.producer.CorrectionProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DriftService {

    private final ShadowLedgerClient ledgerClient;
    private final CorrectionProducer producer;
    private final AtomicInteger counter = new AtomicInteger(1);

    public DriftService(ShadowLedgerClient ledgerClient, CorrectionProducer producer) {
        this.ledgerClient = ledgerClient;
        this.producer = producer;
    }

    public void checkDrift(List<CbsBalance> balances) {

        for (CbsBalance cbs : balances) {
            ShadowBalance shadow = ledgerClient.getShadowBalance(cbs.getAccountId());

            long delta = cbs.getReportedBalance() - shadow.getBalance();
            if (delta == 0) continue; // no drift

            CorrectionEvent event = new CorrectionEvent();
            event.setEventId("CORR-" + cbs.getAccountId() + "-" + counter.getAndIncrement());
            event.setAccountId(cbs.getAccountId());
            event.setAmount(Math.abs(delta));
            event.setType(delta > 0 ? "credit" : "debit_reversal");

            producer.sendCorrection(event);
        }
    }

    public void manualCorrection(String accountId, long amount, String type) {
        CorrectionEvent event = new CorrectionEvent();
        event.setEventId("CORR-" + accountId + "-" + counter.getAndIncrement());
        event.setAccountId(accountId);
        event.setAmount(amount);
        event.setType(type);
        producer.sendCorrection(event);
    }
}

