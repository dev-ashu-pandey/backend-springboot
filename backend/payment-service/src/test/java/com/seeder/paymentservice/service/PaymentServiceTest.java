package com.seeder.paymentservice.service;

import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.dto.UserDTO;
import com.seeder.payment.paymentservice.entity.Payment;
import com.seeder.payment.paymentservice.entity.User;

import com.seeder.payment.paymentservice.enums.PaymentStatus;
import com.seeder.payment.paymentservice.exception.EntityException;
import com.seeder.payment.paymentservice.exception.EntityNotFoundException;
import com.seeder.payment.paymentservice.repository.IPaymentRepository;
import com.seeder.payment.paymentservice.repository.IUserRepository;
import com.seeder.payment.paymentservice.request.PaymentRequest;

import com.seeder.payment.paymentservice.service.impl.PaymentService;
import com.seeder.payment.paymentservice.utils.Constant;
import com.seeder.payment.paymentservice.utils.DataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private IPaymentRepository paymentRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private DataConverter converter;

    @InjectMocks
    private PaymentService paymentService;
    private PaymentRequest paymentRequest;
    private Payment payment;
    private PaymentDTO paymentDTO;
    private User user;
    private UserDTO userDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID userId = UUID.randomUUID();

        payment = Payment.builder()
                .id(UUID.randomUUID())
                .term("12 months")
                .totalPaybackAmount("$1200")
                .status(PaymentStatus.Upcoming)
                .outstandingAmount("$2,000,000")
                .expectedAmount("$10,000")
                .build();

        paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setTerm(payment.getTerm());
        paymentDTO.setTotalPaybackAmount(payment.getTotalPaybackAmount());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setOutstandingAmount(payment.getOutstandingAmount());
        paymentDTO.setDueDate(payment.getDueDate());
        paymentDTO.setExpectedAmount(payment.getExpectedAmount());

        paymentRequest = new PaymentRequest();
        paymentRequest.setTerm(payment.getTerm());
        paymentRequest.setTotalPaybackAmount(payment.getTotalPaybackAmount());
        paymentRequest.setUserId(userId);

        user = new User();
        user.setId(userId);
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("password");
        user.setCashkickBalance("$100,000");
        user.setPayments(List.of(payment));
        payment.setUser(user);

        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCashkickBalance(user.getCashkickBalance());
        paymentDTO.setUserDTO(userDTO);
    }

    @Test
    void createPayments_ValidRequest_ReturnsListOfPaymentDTO() {
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        BigDecimal totalPaybackAmount = BigDecimal.valueOf(100.00);
        BigDecimal cashKickBalance = BigDecimal.valueOf(1000.00);
        when(converter.parseCurrency(paymentRequest.getTotalPaybackAmount())).thenReturn(totalPaybackAmount);
        when(converter.parseCurrency(user.getCashkickBalance())).thenReturn(cashKickBalance);
        when(paymentRepository.save(any())).then(invocation -> {
            Payment payment = invocation.getArgument(0);

            payment.setTotalPaybackAmount(totalPaybackAmount.toString());
            return payment;
        });

        List<PaymentDTO> paymentDTOs = paymentService.createPayments(paymentRequest);

        assertNotNull(paymentDTOs);
    }


    @Test
    void createPayments_UserNotFound_ThrowsEntityNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> paymentService.createPayments(paymentRequest));
        assertTrue(exception.getMessage().contains(Constant.ERROR_USER_NOT_FOUND));
    }
    @Test
    void testPaymentDTOGettersAndSetters() {
        PaymentDTO paymentDTO = new PaymentDTO();
        UUID id = UUID.randomUUID();
        String dueDate = "2024-02-13";
        String term = "12 months";
        String totalPaybackAmount = "$1200";
        PaymentStatus status = PaymentStatus.Upcoming;
        String expectedAmount = "$1000";
        String outstandingAmount = "$200";

        paymentDTO.setId(id);
        paymentDTO.setDueDate(dueDate);
        paymentDTO.setTerm(term);
        paymentDTO.setTotalPaybackAmount(totalPaybackAmount);
        paymentDTO.setStatus(status);
        paymentDTO.setExpectedAmount(expectedAmount);
        paymentDTO.setOutstandingAmount(outstandingAmount);

        assertEquals(id, paymentDTO.getId());
        assertEquals(dueDate, paymentDTO.getDueDate());
        assertEquals(term, paymentDTO.getTerm());
        assertEquals(totalPaybackAmount, paymentDTO.getTotalPaybackAmount());
        assertEquals(status, paymentDTO.getStatus());
        assertEquals(expectedAmount, paymentDTO.getExpectedAmount());
        assertEquals(outstandingAmount, paymentDTO.getOutstandingAmount());
        assertNull(paymentDTO.getUserDTO());
    }

    @Test
    void testUserDTOGettersAndSetters() {
        UserDTO userDTO = new UserDTO();
        UUID id = UUID.randomUUID();
        String name = "John";
        String email = "john@example.com";
        String cashkickBalance = "$1000";

        userDTO.setId(id);
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setCashkickBalance(cashkickBalance);

        assertEquals(id, userDTO.getId());
        assertEquals(name, userDTO.getName());
        assertEquals(email, userDTO.getEmail());
        assertEquals(cashkickBalance, userDTO.getCashkickBalance());
    }

    @Test
    void testPaymentGettersAndSetters() {
        Payment payment = new Payment();
        UUID id = UUID.randomUUID();
        String dueDate = "2024-02-13";
        String term = "12 months";
        String totalPaybackAmount = "$1200";
        PaymentStatus status = PaymentStatus.Upcoming;
        String expectedAmount = "$1000";
        String outstandingAmount = "$200";

        payment.setId(id);
        payment.setDueDate(dueDate);
        payment.setTerm(term);
        payment.setTotalPaybackAmount(totalPaybackAmount);
        payment.setStatus(status);
        payment.setExpectedAmount(expectedAmount);
        payment.setOutstandingAmount(outstandingAmount);

        assertEquals(id, payment.getId());
        assertEquals(dueDate, payment.getDueDate());
        assertEquals(term, payment.getTerm());
        assertEquals(totalPaybackAmount, payment.getTotalPaybackAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(expectedAmount, payment.getExpectedAmount());
        assertEquals(outstandingAmount, payment.getOutstandingAmount());
        assertNull(payment.getUser());
    }

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        UUID id = UUID.randomUUID();
        String name = "John";
        String email = "john@example.com";
        String cashkickBalance = "$1000";
        List<Payment> payments = new ArrayList<>();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setCashkickBalance(cashkickBalance);
        user.setPayments(payments);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(cashkickBalance, user.getCashkickBalance());
        assertEquals(payments, user.getPayments());
    }

    @Test
    void createPayments_ExceptionThrown_ReturnsEntityException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        when(paymentRepository.save(any())).thenThrow(new RuntimeException("Database connection failed"));
        EntityException exception = assertThrows(EntityException.class, () -> paymentService.createPayments(paymentRequest));
        assertNotNull(exception.getMessage());
    }

    @Test
    void getPaymentsByUserId_ValidUserId_ReturnsListOfPaymentDTO() {
        UUID userId = UUID.randomUUID();
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentRepository.findAllByUserId(userId)).thenReturn(payments);
        when(converter.convertToDTO(any(Payment.class))).thenReturn(new PaymentDTO());

        List<PaymentDTO> paymentDTOs = paymentService.getPaymentsByUserId(userId);

        assertNotNull(paymentDTOs);
        assertEquals(1, paymentDTOs.size());
    }

    @Test
    void getAllPayments_ReturnsListOfPaymentDTO() {
        List<Payment> payments = new ArrayList<>();
        when(paymentRepository.findAll()).thenReturn(payments);
        when(converter.convertToDTO(any(Payment.class))).thenReturn(new PaymentDTO());

        List<PaymentDTO> paymentDTOs = paymentService.getAllPayments();

        assertNotNull(paymentDTOs);
        assertEquals(0, paymentDTOs.size());
    }

    @Test
    void getPaymentsByUserId_ExceptionThrown_ReturnsEntityException() {
        UUID userId = UUID.randomUUID();
        when(paymentRepository.findAllByUserId(userId)).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(EntityException.class, () -> paymentService.getPaymentsByUserId(userId));
    }

    @Test
    void getAllPayments_ExceptionThrown_ReturnsEntityException() {
        when(paymentRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(EntityException.class, () -> paymentService.getAllPayments());
    }
    @Test
    void deleteById_PaymentFound_ReturnsSuccessMessage() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setId(id);

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        String result = paymentService.deleteById(id);

        verify(paymentRepository, times(1)).deleteById(id);
        assertEquals(Constant.SUCCESS_PAYMENT_DELETED + id, result);
    }

    @Test
    void deleteById_PaymentNotFound_ThrowsEntityNotFoundException() {
        UUID id = UUID.randomUUID();

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> paymentService.deleteById(id));
        assertTrue(exception.getMessage().contains(Constant.ERROR_PAYMENT_NOT_FOUND));
    }
}
