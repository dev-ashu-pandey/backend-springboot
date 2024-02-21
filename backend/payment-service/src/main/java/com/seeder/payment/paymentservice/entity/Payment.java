package com.seeder.payment.paymentservice.entity;

import com.seeder.payment.paymentservice.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "due_date")
    private String dueDate;
    @Column
    private String term;
    @Column(name = "total_payback_amount")
    private String totalPaybackAmount;
    @Column
    private PaymentStatus status;
    @Column
    private String expectedAmount;
    @Column
    private String outstandingAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
