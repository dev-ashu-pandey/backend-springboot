package com.seeder.cashkick.cashkickservice.dto;

import com.seeder.cashkick.cashkickservice.enums.CashKickStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CashKickDTO {
    private UUID id;
    private String name;
    private CashKickStatusEnum status;
    private Date maturity;
    private String totalReceivedAmount;
    private String totalFinancedAmount;

    private UserDTO userDTO;
    private List<ContractDTO> contractDTOS;
}
