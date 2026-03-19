package com.flashtix.repository;

import com.flashtix.common.enums.TicketStatus;
import com.flashtix.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByOrder_BookingCode(String bookingCode);

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByOrder_BookingCodeAndStatus(String bookingCode, TicketStatus status);

    Long countByOrder_BookingCodeAndStatus(String bookingCode, TicketStatus status);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.order WHERE t.ticketCode IN :ticketCodes")
    List<Ticket> findByTicketCodeInWithOrder(@Param("ticketCodes") List<String> ticketCodes);

    /**
     * Batch update tickets to CHECKED_IN status
     * High performance: Single UPDATE query for multiple tickets
     */
    @Modifying
    @Query("UPDATE Ticket t SET t.status = :status, t.checkedInAt = :checkedInAt, t.checkedInBy = :checkedInBy " +
            "WHERE t.ticketCode IN :ticketCodes AND t.status = com.flashtix.common.enums.TicketStatus.ACTIVE")
    int bulkUpdateTicketStatus(@Param("ticketCodes") List<String> ticketCodes,
            @Param("status") TicketStatus status,
            @Param("checkedInAt") LocalDateTime checkedInAt,
            @Param("checkedInBy") String checkedInBy);
}
