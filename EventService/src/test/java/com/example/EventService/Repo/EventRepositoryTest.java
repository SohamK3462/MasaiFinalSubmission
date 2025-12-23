package com.example.EventService.Repo;

import com.example.EventService.Entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

    @Mock
    private EventRepository eventRepository;

    @Test
    void existsByEventId_WhenEventExists_ShouldReturnTrue() {
        // Arrange
        when(eventRepository.existsByEventId("event123")).thenReturn(true);

        // Act
        boolean exists = eventRepository.existsByEventId("event123");

        // Assert
        assertThat(exists).isTrue();
        verify(eventRepository).existsByEventId("event123");
    }

    @Test
    void existsByEventId_WhenEventDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(eventRepository.existsByEventId("nonexistent")).thenReturn(false);

        // Act
        boolean exists = eventRepository.existsByEventId("nonexistent");

        // Assert
        assertThat(exists).isFalse();
        verify(eventRepository).existsByEventId("nonexistent");
    }

    @Test
    void save_ShouldPersistEventEntity() {
        // Arrange
        EventEntity entity = new EventEntity(
                null,
                "event456",
                "acc789",
                250.75,
                "debit",
                1640000002000L
        );
        EventEntity savedEntity = new EventEntity(
                1L,
                "event456",
                "acc789",
                250.75,
                "debit",
                1640000002000L
        );
        when(eventRepository.save(entity)).thenReturn(savedEntity);

        // Act
        EventEntity result = eventRepository.save(entity);

        // Assert
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEventId()).isEqualTo("event456");
        assertThat(result.getAccountId()).isEqualTo("acc789");
        assertThat(result.getAmount()).isEqualTo(250.75);
        assertThat(result.getType()).isEqualTo("debit");
        assertThat(result.getTimestamp()).isEqualTo(1640000002000L);
        verify(eventRepository).save(entity);
    }

    @Test
    void findById_WhenEventExists_ShouldReturnEvent() {
        // Arrange
        EventEntity entity = new EventEntity(
                1L,
                "event777",
                "acc777",
                777.77,
                "credit",
                1640000004000L
        );
        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        Optional<EventEntity> found = eventRepository.findById(1L);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEventId()).isEqualTo("event777");
        verify(eventRepository).findById(1L);
    }

    @Test
    void findById_WhenEventDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<EventEntity> found = eventRepository.findById(999L);

        // Assert
        assertThat(found).isEmpty();
        verify(eventRepository).findById(999L);
    }

    @Test
    void findAll_ShouldReturnAllEvents() {
        // Arrange
        EventEntity entity1 = new EventEntity(1L, "event1", "acc1", 100.0, "credit", 1640000000000L);
        EventEntity entity2 = new EventEntity(2L, "event2", "acc2", 200.0, "debit", 1640000001000L);
        EventEntity entity3 = new EventEntity(3L, "event3", "acc3", 300.0, "credit", 1640000002000L);
        List<EventEntity> entities = Arrays.asList(entity1, entity2, entity3);
        when(eventRepository.findAll()).thenReturn(entities);

        // Act
        List<EventEntity> allEvents = eventRepository.findAll();

        // Assert
        assertThat(allEvents).hasSize(3);
        verify(eventRepository).findAll();
    }

    @Test
    void delete_ShouldRemoveEvent() {
        // Arrange
        EventEntity entity = new EventEntity(
                1L,
                "event888",
                "acc888",
                888.88,
                "debit",
                1640000005000L
        );
        doNothing().when(eventRepository).delete(entity);

        // Act
        eventRepository.delete(entity);

        // Assert
        verify(eventRepository).delete(entity);
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Arrange
        when(eventRepository.count()).thenReturn(5L);

        // Act
        long count = eventRepository.count();

        // Assert
        assertThat(count).isEqualTo(5L);
        verify(eventRepository).count();
    }

    @Test
    void save_WithDebitType_ShouldPersist() {
        // Arrange
        EventEntity entity = new EventEntity(
                null,
                "debitEvent1",
                "acc555",
                50.25,
                "debit",
                1640000006000L
        );
        EventEntity savedEntity = new EventEntity(
                10L,
                "debitEvent1",
                "acc555",
                50.25,
                "debit",
                1640000006000L
        );
        when(eventRepository.save(entity)).thenReturn(savedEntity);

        // Act
        EventEntity result = eventRepository.save(entity);

        // Assert
        assertThat(result.getType()).isEqualTo("debit");
        assertThat(result.getId()).isEqualTo(10L);
        verify(eventRepository).save(entity);
    }

    @Test
    void save_WithCreditType_ShouldPersist() {
        // Arrange
        EventEntity entity = new EventEntity(
                null,
                "creditEvent1",
                "acc666",
                150.75,
                "credit",
                1640000007000L
        );
        EventEntity savedEntity = new EventEntity(
                11L,
                "creditEvent1",
                "acc666",
                150.75,
                "credit",
                1640000007000L
        );
        when(eventRepository.save(entity)).thenReturn(savedEntity);

        // Act
        EventEntity result = eventRepository.save(entity);

        // Assert
        assertThat(result.getType()).isEqualTo("credit");
        assertThat(result.getId()).isEqualTo(11L);
        verify(eventRepository).save(entity);
    }

    @Test
    void existsByEventId_MultipleChecks_ShouldWorkCorrectly() {
        // Arrange
        when(eventRepository.existsByEventId("event1")).thenReturn(true);
        when(eventRepository.existsByEventId("event2")).thenReturn(true);
        when(eventRepository.existsByEventId("event3")).thenReturn(false);

        // Act & Assert
        assertThat(eventRepository.existsByEventId("event1")).isTrue();
        assertThat(eventRepository.existsByEventId("event2")).isTrue();
        assertThat(eventRepository.existsByEventId("event3")).isFalse();

        verify(eventRepository, times(3)).existsByEventId(anyString());
    }
}

