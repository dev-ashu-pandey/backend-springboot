package com.seeder.payment.paymentservice.controller;

import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.request.PaymentRequest;
import com.seeder.payment.paymentservice.service.IPaymentService;
import com.seeder.payment.paymentservice.utils.Constant;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constant.API_BASE_URL)
@Slf4j
public class PaymentController {

    private final IPaymentService paymentService;

    @Autowired
    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<List<PaymentDTO>> createPayment(@Valid @RequestBody PaymentRequest paymentRequest){
        log.info(Constant.LOG_CREATING_PAYMENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayments(paymentRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDTO>> getAllPayments(){
        log.info(Constant.LOG_FETCHING_ALL_PAYMENTS);
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByUserId(@RequestParam UUID userId){
        log.info(Constant.LOG_FETCHING_PAYMENTS_FOR_USER, userId);
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable UUID id){
        log.info(Constant.LOG_DELETING_PAYMENT_BY_ID, id);
        return ResponseEntity.ok(paymentService.deleteById(id));
    }

}
