package com.example.drift_check_service.client;

import com.example.drift_check_service.client.ShadowLedgerClient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;
import com.example.drift_check_service.dto.ShadowBalance;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;

class ShadowLedgerClientTest {
    @Test
    void clientCanBeInstantiated() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ShadowLedgerClient client = new ShadowLedgerClient(restTemplate);
        assertNotNull(client);
    }

    @Test
    void getShadowBalance_callsRestTemplateWithCorrectUrlAndReturnsResult() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        ShadowLedgerClient client = new ShadowLedgerClient(restTemplate);
        String accountId = "acc123";
        ShadowBalance expected = new ShadowBalance();
        String expectedUrl = "http://localhost:8082/accounts/acc123/shadow-balance";
        when(restTemplate.getForObject(expectedUrl, ShadowBalance.class)).thenReturn(expected);
        ShadowBalance result = client.getShadowBalance(accountId);
        assertSame(expected, result);
        verify(restTemplate).getForObject(expectedUrl, ShadowBalance.class);
    }
}
