package com.seeder.contract.contractservice.entity;

import com.seeder.contract.contractservice.enums.CashKickStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "cashkick")
@Getter
@Setter
public class CashKick {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private CashKickStatus status;
    @Column
    private Date maturity;
    @Column(name = "total_received_amount")
    private String totalReceivedAmount;
    @Column(name = "total_financed_amount")
    private String totalFinancedAmount;

    @ManyToMany
    @JoinTable(name = "cashkick_has_contracts",
            joinColumns = @JoinColumn(name = "cashkick_id"),
            inverseJoinColumns = @JoinColumn(name = "contract_id"))
    private Set<Contract> contracts = new HashSet<>();
}
