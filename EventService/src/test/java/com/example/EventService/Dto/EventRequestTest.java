package com.example.EventService.Dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EventRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        EventRequest request = new EventRequest();

        // Assert
        assertThat(request).isNotNull();
    }

    @Test
    void testAllArgsConstructor() {
        // Act
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );

        // Assert
        assertThat(request.getEventId()).isEqualTo("event123");
        assertThat(request.getAccountId()).isEqualTo("acc456");
        assertThat(request.getAmount()).isEqualTo(100.50);
        assertThat(request.getType()).isEqualTo("credit");
        assertThat(request.getTimestamp()).isEqualTo(1640000000000L);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        EventRequest request = new EventRequest();

        // Act
        request.setEventId("event789");
        request.setAccountId("acc999");
        request.setAmount(250.75);
        request.setType("debit");
        request.setTimestamp(1640000001000L);

        // Assert
        assertThat(request.getEventId()).isEqualTo("event789");
        assertThat(request.getAccountId()).isEqualTo("acc999");
        assertThat(request.getAmount()).isEqualTo(250.75);
        assertThat(request.getType()).isEqualTo("debit");
        assertThat(request.getTimestamp()).isEqualTo(1640000001000L);
    }

    @Test
    void testValidRequest_WithCreditType() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidRequest_WithDebitType() {
        // Arrange
        EventRequest request = new EventRequest(
                "event124",
                "acc457",
                50.25,
                "debit",
                1640000001000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidRequest_WithNullEventId() {
        // Arrange
        EventRequest request = new EventRequest(
                null,
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("eventId"));
    }

    @Test
    void testInvalidRequest_WithBlankEventId() {
        // Arrange
        EventRequest request = new EventRequest(
                "",
                "acc456",
                100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("eventId"));
    }

    @Test
    void testInvalidRequest_WithNullAccountId() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                null,
                100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("accountId"));
    }

    @Test
    void testInvalidRequest_WithBlankAccountId() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "",
                100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("accountId"));
    }

    @Test
    void testInvalidRequest_WithNullAmount() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                null,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }

    @Test
    void testInvalidRequest_WithNegativeAmount() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                -100.50,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }

    @Test
    void testInvalidRequest_WithZeroAmount() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                0.0,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }

    @Test
    void testInvalidRequest_WithInvalidType() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "invalid",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void testInvalidRequest_WithBlankType() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void testInvalidRequest_WithNullType() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                null,
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void testInvalidRequest_WithNullTimestamp() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                null
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("timestamp"));
    }

    @Test
    void testValidRequest_WithSmallAmount() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                0.01,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidRequest_WithLargeAmount() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                9999999999.99,
                "credit",
                1640000000000L
        );

        // Act
        Set<ConstraintViolation<EventRequest>> violations = validator.validate(request);

        // Assert
        assertThat(violations).isEmpty();
    }
}

