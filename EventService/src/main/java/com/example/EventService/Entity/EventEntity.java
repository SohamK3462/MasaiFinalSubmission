package com.example.EventService.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events", uniqueConstraints = {
        @UniqueConstraint(columnNames = "event_id")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    private String accountId;
    private Double amount;
    private String type;
    private Long timestamp;
}

