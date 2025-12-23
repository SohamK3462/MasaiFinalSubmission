package com.example.drift_check_service.service;

import com.example.drift_check_service.client.ShadowLedgerClient;
import com.example.drift_check_service.dto.CbsBalance;
import com.example.drift_check_service.dto.CorrectionEvent;
import com.example.drift_check_service.dto.ShadowBalance;
import com.example.drift_check_service.producer.CorrectionProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriftServiceTest {
    private ShadowLedgerClient ledgerClient;
    private CorrectionProducer producer;
    private DriftService service;

    @BeforeEach
    void setUp() {
        ledgerClient = mock(ShadowLedgerClient.class);
        producer = mock(CorrectionProducer.class);
        service = new DriftService(ledgerClient, producer);
    }

    @Test
    void serviceCanBeInstantiated() {
        assertNotNull(service);
    }

    @Test
    void checkDrift_noDrift_noEventSent() {
        CbsBalance cbs = mock(CbsBalance.class);
        ShadowBalance shadow = mock(ShadowBalance.class);
        when(cbs.getAccountId()).thenReturn("acc1");
        when(cbs.getReportedBalance()).thenReturn(100L);
        when(shadow.getBalance()).thenReturn(100L);
        when(ledgerClient.getShadowBalance("acc1")).thenReturn(shadow);
        service.checkDrift(Collections.singletonList(cbs));
        verify(producer, never()).sendCorrection(any());
    }

    @Test
    void checkDrift_positiveDrift_eventSent() {
        CbsBalance cbs = mock(CbsBalance.class);
        ShadowBalance shadow = mock(ShadowBalance.class);
        when(cbs.getAccountId()).thenReturn("acc2");
        when(cbs.getReportedBalance()).thenReturn(200L);
        when(shadow.getBalance()).thenReturn(100L);
        when(ledgerClient.getShadowBalance("acc2")).thenReturn(shadow);
        service.checkDrift(Collections.singletonList(cbs));
        ArgumentCaptor<CorrectionEvent> captor = ArgumentCaptor.forClass(CorrectionEvent.class);
        verify(producer).sendCorrection(captor.capture());
        CorrectionEvent event = captor.getValue();
        assertEquals("acc2", event.getAccountId());
        assertEquals(100L, event.getAmount());
        assertEquals("credit", event.getType());
    }

    @Test
    void checkDrift_negativeDrift_eventSent() {
        CbsBalance cbs = mock(CbsBalance.class);
        ShadowBalance shadow = mock(ShadowBalance.class);
        when(cbs.getAccountId()).thenReturn("acc3");
        when(cbs.getReportedBalance()).thenReturn(50L);
        when(shadow.getBalance()).thenReturn(100L);
        when(ledgerClient.getShadowBalance("acc3")).thenReturn(shadow);
        service.checkDrift(Collections.singletonList(cbs));
        ArgumentCaptor<CorrectionEvent> captor = ArgumentCaptor.forClass(CorrectionEvent.class);
        verify(producer).sendCorrection(captor.capture());
        CorrectionEvent event = captor.getValue();
        assertEquals("acc3", event.getAccountId());
        assertEquals(50L, event.getAmount());
        assertEquals("debit_reversal", event.getType());
    }

    @Test
    void manualCorrection_sendsEventWithCorrectValues() {
        service.manualCorrection("acc4", 123L, "credit");
        ArgumentCaptor<CorrectionEvent> captor = ArgumentCaptor.forClass(CorrectionEvent.class);
        verify(producer).sendCorrection(captor.capture());
        CorrectionEvent event = captor.getValue();
        assertEquals("acc4", event.getAccountId());
        assertEquals(123L, event.getAmount());
        assertEquals("credit", event.getType());
        assertTrue(event.getEventId().startsWith("CORR-acc4-"));
    }
}
