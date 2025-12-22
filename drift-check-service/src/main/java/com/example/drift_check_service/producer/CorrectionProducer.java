package com.example.drift_check_service.producer;


import com.example.drift_check_service.dto.CorrectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CorrectionProducer {

    private static final Logger logger = LoggerFactory.getLogger(CorrectionProducer.class);
    private final KafkaTemplate<String, CorrectionEvent> kafkaTemplate;
    private final String topic = "transactions.corrections";

    public CorrectionProducer(KafkaTemplate<String, CorrectionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCorrection(CorrectionEvent event) {
        kafkaTemplate.send(topic, event.getAccountId(), event);
        logger.info("Sent correction event: {}", event);
    }
}

