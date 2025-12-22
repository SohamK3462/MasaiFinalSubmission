package com.example.EventService.Repo;

import com.example.EventService.Entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByEventId(String eventId);
}
