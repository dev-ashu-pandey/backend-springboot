package com.seeder.payment.paymentservice.dto;


import com.seeder.payment.paymentservice.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentDTO {
    private UUID id;
    private String dueDate;
    private String term;
    private String totalPaybackAmount;
    private PaymentStatus status;
    private String expectedAmount;
    private String outstandingAmount;
    private UserDTO userDTO;
}
