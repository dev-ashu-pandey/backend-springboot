package com.seeder.cashkick.cashkickservice.dto;

import com.seeder.cashkick.cashkickservice.enums.ContractStatusEnum;
import com.seeder.cashkick.cashkickservice.enums.ContractTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ContractDTO {
    private UUID id;
    private String name;
    private ContractTypeEnum type;
    private String perPayment;
    private String termLength;
    private String termFees;
    private String paymentAmount;
    private ContractStatusEnum status;
    private String totalAvailableAmount;

}
