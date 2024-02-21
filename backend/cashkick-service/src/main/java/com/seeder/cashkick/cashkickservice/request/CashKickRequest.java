package com.seeder.cashkick.cashkickservice.request;

import com.seeder.cashkick.cashkickservice.enums.CashKickStatusEnum;
import com.seeder.cashkick.cashkickservice.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
public class CashKickRequest {
    @NotBlank(message = Constant.NAME_BLANK)
    private String name;

    @NotNull(message = Constant.STATUS_NULL)
    private CashKickStatusEnum status;

    @NotNull(message = Constant.MATURITY_NULL)
    private Date maturity;

    @Pattern(regexp = "^\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?$", message = Constant.TOTAL_RECEIVED_AMOUNT_INVALID_FORMAT)
    @NotBlank(message = Constant.TOTAL_RECEIVED_AMOUNT_BLANK)
    private String totalReceivedAmount;

    @Pattern(regexp = "^\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?$", message = Constant.TOTAL_FINANCED_AMOUNT_INVALID_FORMAT)
    @NotBlank(message = Constant.TOTAL_FINANCED_AMOUNT_BLANK)
    private String totalFinancedAmount;

    @NotNull(message = Constant.USER_ID_NULL)
    private UUID userId;

    @NotNull(message = Constant.CONTRACT_ID_NULL)
    @NotEmpty(message = Constant.CONTRACT_ID_EMPTY)
    private Set<UUID> contractIds;
}