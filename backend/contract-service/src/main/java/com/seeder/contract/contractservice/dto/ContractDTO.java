package com.seeder.contract.contractservice.dto;

import com.seeder.contract.contractservice.enums.ContractStatus;
import com.seeder.contract.contractservice.enums.ContractType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ContractDTO {
    private UUID id;
    private String name;
    private ContractType type;
    private String perPayment;
    private String termLength;
    private String termFees;
    private String paymentAmount;
    private ContractStatus status;
    private String totalAvailableAmount;

    private Set<CashKickDTO> cashKickDTOS = new HashSet<>();
}
