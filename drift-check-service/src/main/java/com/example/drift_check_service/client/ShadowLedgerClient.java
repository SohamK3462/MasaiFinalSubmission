package com.example.drift_check_service.client;




import com.example.drift_check_service.dto.ShadowBalance;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShadowLedgerClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ledgerUrl = "http://localhost:8082/accounts/";

    public ShadowBalance getShadowBalance(String accountId) {
        return restTemplate.getForObject(ledgerUrl + accountId + "/shadow-balance", ShadowBalance.class);
    }
}
