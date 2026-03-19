package com.flashtix.repository;

import com.flashtix.entity.Order;
import com.flashtix.common.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.ticketType WHERE o.orderCode = :orderCode")
    Order findByOrderCodeWithTicketType(@Param("orderCode") String orderCode);

    Order findByOrderCode(String orderCode);

    @Query("SELECT o FROM Order o JOIN FETCH o.ticketType WHERE o.bookingCode = :bookingCode")
    Order findByBookingCodeWithTicketType(@Param("bookingCode") String bookingCode);

    Order findByBookingCode(String bookingCode);

    List<Order> findByStatusAndExpiredAtBefore(OrderStatus status, LocalDateTime dateTime);
}
