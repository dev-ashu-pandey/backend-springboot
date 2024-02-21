package com.seeder.payment.paymentservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentRequest {
    @NotBlank(message = "Term cannot be blank")
    private String term;

    @NotBlank(message = "Total payback amount cannot be blank")
    @Pattern(regexp = "\\$\\d{1,3}(,\\d{3})*(\\.\\d{2})?", message = "Total payback amount must be in valid format")
    private String totalPaybackAmount;

    @NotNull(message = "User ID cannot be null")
    private UUID userId;
}
