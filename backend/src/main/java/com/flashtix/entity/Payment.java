package com.flashtix.entity;

import com.flashtix.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", length = 100)
    private Double amount;

    @Column(name = "payment_provider")
    private String paymentProvider;

    @Column(name="transaction_code", length = 100)
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
