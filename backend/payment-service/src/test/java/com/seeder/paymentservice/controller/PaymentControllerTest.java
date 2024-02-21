package com.seeder.paymentservice.controller;

import com.seeder.payment.paymentservice.controller.PaymentController;
import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.request.PaymentRequest;
import com.seeder.payment.paymentservice.service.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private IPaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_ValidRequest_ReturnsCreatedResponse() {
        PaymentRequest paymentRequest = new PaymentRequest();
        when(paymentService.createPayments(paymentRequest)).thenReturn(Collections.emptyList());
        ResponseEntity<List<PaymentDTO>> responseEntity = paymentController.createPayment(paymentRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(paymentService, times(1)).createPayments(paymentRequest);
    }

    @Test
    void getAllPayments_ReturnsListOfPayments() {
        List<PaymentDTO> expectedPayments = Collections.emptyList();
        when(paymentService.getAllPayments()).thenReturn(expectedPayments);
        ResponseEntity<List<PaymentDTO>> responseEntity = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPayments, responseEntity.getBody());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void getAllPaymentsByUserId_ReturnsListOfPaymentsForUserId() {
        UUID userId = UUID.randomUUID();
        List<PaymentDTO> expectedPayments = Collections.emptyList();
        when(paymentService.getPaymentsByUserId(userId)).thenReturn(expectedPayments);

        ResponseEntity<List<PaymentDTO>> responseEntity = paymentController.getAllPaymentsByUserId(userId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPayments, responseEntity.getBody());
        verify(paymentService, times(1)).getPaymentsByUserId(userId);
    }
    @Test
    void deletePayment_ValidId_ReturnsOkResponse() {
        UUID paymentId = UUID.randomUUID();
        String successMessage = "Payment deleted successfully with id: " + paymentId;
        when(paymentService.deleteById(paymentId)).thenReturn(successMessage);

        ResponseEntity<String> responseEntity = paymentController.deletePayment(paymentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successMessage, responseEntity.getBody());
        verify(paymentService, times(1)).deleteById(paymentId);
    }
}
