package com.example.EventService.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank
    private String eventId;

    @NotBlank
    private String accountId;

    @NotNull
    @Positive
    private Double amount;

    @NotBlank
    @Pattern(regexp = "credit|debit")
    private String type;

    @NotNull
    private Long timestamp;
}

