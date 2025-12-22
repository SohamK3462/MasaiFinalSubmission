package com.example.EventService.Service;

import com.example.EventService.Dto.EventRequest;
import com.example.EventService.Entity.EventEntity;
import com.example.EventService.Repo.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final String TOPIC = "transactions.raw";

    private final EventRepository repository;
    private final KafkaTemplate<String, EventRequest> kafkaTemplate;

    @Transactional
    public void processEvent(EventRequest request) {

        // Idempotency check
        if (repository.existsByEventId(request.getEventId())) {
            throw new IllegalArgumentException("Duplicate eventId");
        }

        // Save for traceability
        EventEntity entity = new EventEntity(
                null,
                request.getEventId(),
                request.getAccountId(),
                request.getAmount(),
                request.getType(),
                request.getTimestamp()
        );

        repository.save(entity);

        // Publish to Kafka
        kafkaTemplate.send(TOPIC, request.getAccountId(), request);
    }
}

