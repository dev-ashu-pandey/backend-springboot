package com.seeder.contract.contractservice.entity;

import com.seeder.contract.contractservice.enums.ContractStatus;
import com.seeder.contract.contractservice.enums.ContractType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column
    private ContractType type;
    @Column(name = "monthly_payment")
    private String perPayment;
    @Column
    private String termLength;
    @Column(name = "monthly_interest_rate")
    private String termFees;
    @Column
    private String paymentAmount;
    @Column
    private ContractStatus status;
    @Column(name = "total_available_amount")
    private String totalAvailableAmount;

    @ManyToMany(mappedBy = "contracts")
    Set<CashKick> cashkicks = new HashSet<>();

}
