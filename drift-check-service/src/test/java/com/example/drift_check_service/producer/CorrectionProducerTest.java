package com.example.drift_check_service.producer;

import com.example.drift_check_service.dto.CorrectionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class CorrectionProducerTest {
    @Test
    void producerCanBeInstantiated() {
        KafkaTemplate<String, CorrectionEvent> kafkaTemplate = mock(KafkaTemplate.class);
        CorrectionProducer producer = new CorrectionProducer(kafkaTemplate);
        assertNotNull(producer);
    }
}
