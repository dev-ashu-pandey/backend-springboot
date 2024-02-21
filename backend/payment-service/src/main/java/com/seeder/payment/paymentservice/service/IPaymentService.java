package com.seeder.payment.paymentservice.service;

import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.request.PaymentRequest;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
    List<PaymentDTO> createPayments(PaymentRequest paymentRequest);

    List<PaymentDTO> getPaymentsByUserId(UUID userId);
    List<PaymentDTO> getAllPayments();

    String deleteById(UUID id);
}
