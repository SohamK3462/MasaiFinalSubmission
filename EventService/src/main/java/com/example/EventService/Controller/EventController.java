package com.example.EventService.Controller;

import com.example.EventService.Dto.EventRequest;
import com.example.EventService.Service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest request) {
        eventService.processEvent(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

