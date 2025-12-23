package com.example.EventService.Controller;

import com.example.EventService.Dto.EventRequest;
import com.example.EventService.Service.EventService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void createEvent_WithValidRequest_ShouldReturnAccepted() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                System.currentTimeMillis()
        );

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response = eventController.createEvent(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(eventService, times(1)).processEvent(any(EventRequest.class));
    }

    @Test
    void createEvent_WithValidDebitRequest_ShouldReturnAccepted() {
        // Arrange
        EventRequest request = new EventRequest(
                "event124",
                "acc457",
                50.25,
                "debit",
                System.currentTimeMillis()
        );

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response = eventController.createEvent(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(eventService, times(1)).processEvent(any(EventRequest.class));
    }

    @Test
    void createEvent_WithDifferentAccountIds_ShouldProcessEach() {
        // Arrange
        EventRequest request1 = new EventRequest("evt1", "acc001", 100.0, "credit", 1640000000000L);
        EventRequest request2 = new EventRequest("evt2", "acc002", 200.0, "debit", 1640000001000L);

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response1 = eventController.createEvent(request1);
        ResponseEntity<?> response2 = eventController.createEvent(request2);

        // Assert
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(eventService, times(2)).processEvent(any(EventRequest.class));
    }

    @Test
    void createEvent_WithServiceException_ShouldPropagateException() {
        // Arrange
        EventRequest request = new EventRequest(
                "event123",
                "acc456",
                100.50,
                "credit",
                System.currentTimeMillis()
        );

        doThrow(new IllegalArgumentException("Duplicate eventId"))
                .when(eventService).processEvent(any(EventRequest.class));

        // Act & Assert
        try {
            eventController.createEvent(request);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Duplicate eventId");
        }

        verify(eventService, times(1)).processEvent(any(EventRequest.class));
    }

    @Test
    void createEvent_WithLargeAmount_ShouldProcess() {
        // Arrange
        EventRequest request = new EventRequest(
                "event999",
                "acc999",
                999999999.99,
                "credit",
                System.currentTimeMillis()
        );

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response = eventController.createEvent(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(eventService, times(1)).processEvent(request);
    }

    @Test
    void createEvent_WithSmallAmount_ShouldProcess() {
        // Arrange
        EventRequest request = new EventRequest(
                "event001",
                "acc001",
                0.01,
                "debit",
                System.currentTimeMillis()
        );

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response = eventController.createEvent(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        verify(eventService, times(1)).processEvent(request);
    }

    @Test
    void createEvent_ShouldReturnEmptyBody() {
        // Arrange
        EventRequest request = new EventRequest(
                "event555",
                "acc555",
                50.0,
                "credit",
                System.currentTimeMillis()
        );

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        ResponseEntity<?> response = eventController.createEvent(request);

        // Assert
        assertThat(response.getBody()).isNull();
    }

    @Test
    void createEvent_MultipleRequests_ShouldProcessAll() {
        // Arrange
        EventRequest request1 = new EventRequest("evt1", "acc1", 100.0, "credit", 1640000000000L);
        EventRequest request2 = new EventRequest("evt2", "acc2", 200.0, "debit", 1640000001000L);
        EventRequest request3 = new EventRequest("evt3", "acc3", 300.0, "credit", 1640000002000L);

        doNothing().when(eventService).processEvent(any(EventRequest.class));

        // Act
        eventController.createEvent(request1);
        eventController.createEvent(request2);
        eventController.createEvent(request3);

        // Assert
        verify(eventService, times(3)).processEvent(any(EventRequest.class));
    }
}
