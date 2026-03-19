package com.flashtix.repository;

import com.flashtix.entity.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);

    // PERFORMANCE: Use @EntityGraph instead of JOIN FETCH for pagination
    // JOIN FETCH with Page causes memory issues
    @EntityGraph(attributePaths = { "ticketTypes" })
    Page<Event> findAll(Pageable pageable);
}
