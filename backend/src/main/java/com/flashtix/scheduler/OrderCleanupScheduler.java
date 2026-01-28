package com.flashtix.scheduler;

import com.flashtix.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupScheduler {

    private final OrderService orderService;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredOrders() {
        log.info("Running expired order cleanup job...");
        orderService.cancelExpiredOrders();
    }
}
