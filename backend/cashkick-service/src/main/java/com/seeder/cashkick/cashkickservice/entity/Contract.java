package com.seeder.cashkick.cashkickservice.entity;

import com.seeder.cashkick.cashkickservice.enums.ContractStatusEnum;
import com.seeder.cashkick.cashkickservice.enums.ContractTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column
    private ContractTypeEnum type;
    @Column(name = "monthly_payment")
    private String perPayment;
    @Column(name = "term_length")
    private String termLength;
    @Column(name = "monthly_interest_rate")
    private String termFees;
    @Column(name = "payment_amount")
    private String paymentAmount;
    @Column
    private ContractStatusEnum status;
    @Column(name = "total_available_amount")
    private String totalAvailableAmount;

    @ManyToMany(mappedBy = "contracts")
    private Set<CashKick> cashKicks = new HashSet<>();
}
