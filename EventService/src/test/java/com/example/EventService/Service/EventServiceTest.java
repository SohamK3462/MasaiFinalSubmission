package com.example.EventService.Service;

import com.example.EventService.Dto.EventRequest;
import com.example.EventService.Entity.EventEntity;
import com.example.EventService.Repo.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    @Mock
    private KafkaTemplate<String, EventRequest> kafkaTemplate;

    @InjectMocks
    private EventService eventService;

    private EventRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );
    }

    @Test
    void processEvent_WithValidRequest_ShouldSaveEntityAndPublishToKafka() {
        // Arrange
        when(repository.existsByEventId(validRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(validRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).existsByEventId("event123");
        verify(repository).save(entityCaptor.capture());
        verify(kafkaTemplate).send(eq("transactions.raw"), eq("acc456"), eq(validRequest));

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getEventId()).isEqualTo("event123");
        assertThat(savedEntity.getAccountId()).isEqualTo("acc456");
        assertThat(savedEntity.getAmount()).isEqualTo(100.50);
        assertThat(savedEntity.getType()).isEqualTo("credit");
        assertThat(savedEntity.getTimestamp()).isEqualTo(1640000000000L);
    }

    @Test
    void processEvent_WithDebitType_ShouldSaveAndPublish() {
        // Arrange
        EventRequest debitRequest = new EventRequest(
                "event124",
                "acc457",
                50.25,
                "debit",
                1640000001000L
        );
        when(repository.existsByEventId(debitRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(debitRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).save(entityCaptor.capture());
        verify(kafkaTemplate).send(eq("transactions.raw"), eq("acc457"), eq(debitRequest));

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getType()).isEqualTo("debit");
        assertThat(savedEntity.getAmount()).isEqualTo(50.25);
    }

    @Test
    void processEvent_WithDuplicateEventId_ShouldThrowException() {
        // Arrange
        when(repository.existsByEventId(validRequest.getEventId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> eventService.processEvent(validRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicate eventId");

        verify(repository).existsByEventId("event123");
        verify(repository, never()).save(any(EventEntity.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any(EventRequest.class));
    }

    @Test
    void processEvent_WithDifferentAccountIds_ShouldUseCorrectKey() {
        // Arrange
        EventRequest request1 = new EventRequest("evt1", "acc001", 100.0, "credit", 1640000000000L);
        EventRequest request2 = new EventRequest("evt2", "acc002", 200.0, "debit", 1640000001000L);

        when(repository.existsByEventId(anyString())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(request1);
        eventService.processEvent(request2);

        // Assert
        verify(kafkaTemplate).send(eq("transactions.raw"), eq("acc001"), eq(request1));
        verify(kafkaTemplate).send(eq("transactions.raw"), eq("acc002"), eq(request2));
    }

    @Test
    void processEvent_WhenRepositorySaveFails_ShouldNotPublishToKafka() {
        // Arrange
        when(repository.existsByEventId(validRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> eventService.processEvent(validRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");

        verify(repository).save(any(EventEntity.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any(EventRequest.class));
    }

    @Test
    void processEvent_WithLargeAmount_ShouldHandleCorrectly() {
        // Arrange
        EventRequest largeAmountRequest = new EventRequest(
                "event999",
                "acc999",
                999999999.99,
                "credit",
                1640000000000L
        );
        when(repository.existsByEventId(largeAmountRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(largeAmountRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).save(entityCaptor.capture());

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getAmount()).isEqualTo(999999999.99);
    }

    @Test
    void processEvent_WithSmallAmount_ShouldHandleCorrectly() {
        // Arrange
        EventRequest smallAmountRequest = new EventRequest(
                "event001",
                "acc001",
                0.01,
                "debit",
                1640000000000L
        );
        when(repository.existsByEventId(smallAmountRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(smallAmountRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).save(entityCaptor.capture());

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getAmount()).isEqualTo(0.01);
    }

    @Test
    void processEvent_ShouldCallMethodsInCorrectOrder() {
        // Arrange
        when(repository.existsByEventId(validRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(validRequest);

        // Assert - verify order of operations
        var inOrder = inOrder(repository, kafkaTemplate);
        inOrder.verify(repository).existsByEventId("event123");
        inOrder.verify(repository).save(any(EventEntity.class));
        inOrder.verify(kafkaTemplate).send(anyString(), anyString(), any(EventRequest.class));
    }

    @Test
    void processEvent_WithDifferentTimestamps_ShouldPreserveTimestamp() {
        // Arrange
        Long specificTimestamp = 1703347200000L; // Specific date
        EventRequest timestampRequest = new EventRequest(
                "event555",
                "acc555",
                50.0,
                "credit",
                specificTimestamp
        );
        when(repository.existsByEventId(timestampRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(timestampRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).save(entityCaptor.capture());

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getTimestamp()).isEqualTo(specificTimestamp);
    }

    @Test
    void processEvent_EntityShouldHaveNullIdBeforeSave() {
        // Arrange
        when(repository.existsByEventId(validRequest.getEventId())).thenReturn(false);
        when(repository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        eventService.processEvent(validRequest);

        // Assert
        ArgumentCaptor<EventEntity> entityCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(repository).save(entityCaptor.capture());

        EventEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getId()).isNull(); // ID should be null before DB save
    }
}

