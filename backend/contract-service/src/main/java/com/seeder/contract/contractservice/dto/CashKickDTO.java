package com.seeder.contract.contractservice.dto;

import com.seeder.contract.contractservice.enums.CashKickStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CashKickDTO {
    private UUID id;
    private String name;
    private CashKickStatus status;
    private Date maturity;
    private String totalReceivedAmount;
    private String totalFinancedAmount;
}
