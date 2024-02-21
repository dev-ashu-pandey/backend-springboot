package com.seeder.contract.contractservice.request;

import com.seeder.contract.contractservice.enums.ContractStatus;
import com.seeder.contract.contractservice.enums.ContractType;
import com.seeder.contract.contractservice.utils.Constant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ContractRequest {
    @NotBlank(message = Constant.ERROR_NAME_BLANK)
    private String name;
    private ContractType type;

    @NotNull(message = Constant.ERROR_PER_PAYMENT_NULL)
    @Pattern(regexp = "^\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?$", message = Constant.ERROR_PER_PAYMENT_FORMAT)
    private String perPayment;
    @NotNull(message = Constant.ERROR_TERM_LENGTH_NULL)
    private String termLength;
    @NotNull(message = Constant.ERROR_TERM_FEES_NULL)
    private String termFees;

    @Pattern(regexp = "^\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?$", message = Constant.ERROR_PAYMENT_AMOUNT_FORMAT)
    @NotNull(message = Constant.ERROR_PAYMENT_AMOUNT_NULL)
    private String paymentAmount;
    @NotNull(message = Constant.ERROR_PAYMENT_STATUS_NULL)
    private ContractStatus status;

    @Pattern(regexp = "^\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?$", message = Constant.ERROR_TOTAL_AVAILABLE_AMOUNT_FORMAT)
    @NotNull(message = Constant.ERROR_TOTAL_AVAILABLE_NULL)
    private String totalAvailableAmount;

    private Set<UUID> cashKickIds;
}
