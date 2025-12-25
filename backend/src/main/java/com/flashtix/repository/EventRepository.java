package com.flashtix.repository;

import com.flashtix.entity.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.ticketTypes")
    Page<Event> findAllWithTicketTypes(Pageable pageable);
}
