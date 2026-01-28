package com.flashtix.repository;

import com.flashtix.entity.Order;
import com.flashtix.common.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderCode(String orderCode);

    List<Order> findByStatusAndExpiredAtBefore(OrderStatus status, LocalDateTime dateTime);
}
