package com.example.drift_check_service.config;

import com.example.drift_check_service.config.KafkaProducerConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaProducerConfigTest {
    @Test
    void configCanBeInstantiated() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        assertNotNull(config);
    }
}

