package com.seeder.cashkick.cashkickservice.entity;

import com.seeder.cashkick.cashkickservice.enums.CashKickStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "cashkick")
public class CashKick {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private CashKickStatusEnum status;
    @Column
    private Date maturity;
    @Column(name = "total_received_amount")
    private String totalReceivedAmount;
    @Column(name = "total_financed_amount")
    private String totalFinancedAmount;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "cashkick_contracts", joinColumns = @JoinColumn(name = "cashkick_id"), inverseJoinColumns = @JoinColumn(name = "contract_id"))
    private Set<Contract> contracts = new HashSet<>();
}
