package com.example.EventService.Entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventEntityTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        EventEntity entity = new EventEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getEventId()).isNull();
        assertThat(entity.getAccountId()).isNull();
        assertThat(entity.getAmount()).isNull();
        assertThat(entity.getType()).isNull();
        assertThat(entity.getTimestamp()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        // Act
        EventEntity entity = new EventEntity(
                1L,
                "event123",
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );

        // Assert
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getEventId()).isEqualTo("event123");
        assertThat(entity.getAccountId()).isEqualTo("acc456");
        assertThat(entity.getAmount()).isEqualTo(100.50);
        assertThat(entity.getType()).isEqualTo("credit");
        assertThat(entity.getTimestamp()).isEqualTo(1640000000000L);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setId(2L);
        entity.setEventId("event789");
        entity.setAccountId("acc999");
        entity.setAmount(250.75);
        entity.setType("debit");
        entity.setTimestamp(1640000001000L);

        // Assert
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getEventId()).isEqualTo("event789");
        assertThat(entity.getAccountId()).isEqualTo("acc999");
        assertThat(entity.getAmount()).isEqualTo(250.75);
        assertThat(entity.getType()).isEqualTo("debit");
        assertThat(entity.getTimestamp()).isEqualTo(1640000001000L);
    }

    @Test
    void testSetId() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setId(100L);

        // Assert
        assertThat(entity.getId()).isEqualTo(100L);
    }

    @Test
    void testSetEventId() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setEventId("testEventId");

        // Assert
        assertThat(entity.getEventId()).isEqualTo("testEventId");
    }

    @Test
    void testSetAccountId() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setAccountId("testAccountId");

        // Assert
        assertThat(entity.getAccountId()).isEqualTo("testAccountId");
    }

    @Test
    void testSetAmount() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setAmount(999.99);

        // Assert
        assertThat(entity.getAmount()).isEqualTo(999.99);
    }

    @Test
    void testSetType() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setType("credit");

        // Assert
        assertThat(entity.getType()).isEqualTo("credit");
    }

    @Test
    void testSetTimestamp() {
        // Arrange
        EventEntity entity = new EventEntity();

        // Act
        entity.setTimestamp(1234567890L);

        // Assert
        assertThat(entity.getTimestamp()).isEqualTo(1234567890L);
    }

    @Test
    void testEntityWithNullValues() {
        // Act
        EventEntity entity = new EventEntity(
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Assert
        assertThat(entity.getId()).isNull();
        assertThat(entity.getEventId()).isNull();
        assertThat(entity.getAccountId()).isNull();
        assertThat(entity.getAmount()).isNull();
        assertThat(entity.getType()).isNull();
        assertThat(entity.getTimestamp()).isNull();
    }

    @Test
    void testEntityWithDebitType() {
        // Act
        EventEntity entity = new EventEntity(
                5L,
                "evt555",
                "acc555",
                75.25,
                "debit",
                1640000002000L
        );

        // Assert
        assertThat(entity.getType()).isEqualTo("debit");
    }

    @Test
    void testEntityWithLargeAmount() {
        // Act
        EventEntity entity = new EventEntity();
        entity.setAmount(9999999999.99);

        // Assert
        assertThat(entity.getAmount()).isEqualTo(9999999999.99);
    }

    @Test
    void testEntityWithSmallAmount() {
        // Act
        EventEntity entity = new EventEntity();
        entity.setAmount(0.01);

        // Assert
        assertThat(entity.getAmount()).isEqualTo(0.01);
    }
}

