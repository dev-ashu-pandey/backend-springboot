package com.seeder.payment.paymentservice.service.impl;

import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.entity.Payment;
import com.seeder.payment.paymentservice.entity.User;
import com.seeder.payment.paymentservice.enums.PaymentStatus;
import com.seeder.payment.paymentservice.exception.EntityException;
import com.seeder.payment.paymentservice.exception.EntityNotFoundException;
import com.seeder.payment.paymentservice.exception.InsufficientBalanceException;
import com.seeder.payment.paymentservice.exception.InvalidParameterException;
import com.seeder.payment.paymentservice.repository.IPaymentRepository;
import com.seeder.payment.paymentservice.repository.IUserRepository;
import com.seeder.payment.paymentservice.request.PaymentRequest;
import com.seeder.payment.paymentservice.service.IPaymentService;
import com.seeder.payment.paymentservice.utils.Constant;
import com.seeder.payment.paymentservice.utils.DataConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
public class PaymentService implements IPaymentService {

    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;
    private final DataConverter converter;

    @Autowired
    public PaymentService(IPaymentRepository paymentRepository, IUserRepository userRepository, DataConverter converter) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    @Transactional
    public List<PaymentDTO> createPayments(PaymentRequest paymentRequest) {
        log.info(Constant.LOG_CREATING_PAYMENTS, paymentRequest.getUserId());
        User user = userRepository.findById(paymentRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException(Constant.ERROR_USER_NOT_FOUND + paymentRequest.getUserId()));
        DecimalFormat currencyFormat = new DecimalFormat("$###,###.##");
        try {
            List<Payment> payments = new ArrayList<>();
            int term = parseInt(paymentRequest.getTerm().split(" ")[0]);
            if (term <= 0) {
                throw new InvalidParameterException("Term must be greater than 0");
            }
            log.debug(Constant.LOG_CALCULATING_TERM, term);
            BigDecimal totalPaybackAmount = converter.parseCurrency(paymentRequest.getTotalPaybackAmount());
            if (totalPaybackAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidParameterException("Total payback amount must be greater than 0");
            }
            BigDecimal cashKickBalance = converter.parseCurrency(user.getCashkickBalance());
            if (totalPaybackAmount.compareTo(cashKickBalance) > 0) {
                throw new InsufficientBalanceException("Insufficient balance in user's cashkick account");
            }
            BigDecimal expectedAmount = totalPaybackAmount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
            user.setCashkickBalance(currencyFormat.format(cashKickBalance.subtract(totalPaybackAmount)));
            BigDecimal outstandingAmount = totalPaybackAmount;
            LocalDate currentDate = LocalDate.now();
            for (int i = 0; i < term; i++) {
                Payment payment = Payment.builder()
                        .dueDate(currentDate.plusMonths(i).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                        .term(paymentRequest.getTerm())
                        .status(PaymentStatus.Upcoming)
                        .totalPaybackAmount(paymentRequest.getTotalPaybackAmount())
                        .expectedAmount("-" + currencyFormat.format(expectedAmount))
                        .outstandingAmount(currencyFormat.format(outstandingAmount))
                        .user(user)
                        .build();
                payments.add(payment);
                outstandingAmount = outstandingAmount.subtract(expectedAmount);
                if (outstandingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
            List<Payment> savedPayments = paymentRepository.saveAll(payments);
            userRepository.save(user);
            log.info(Constant.LOG_PAYMENTS_CREATED_SUCCESSFULLY);
            return savedPayments.stream().map(converter::convertToDTO).toList();
        } catch (Exception e) {
            log.error(Constant.ERROR_CREATING_PAYMENTS, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public List<PaymentDTO> getPaymentsByUserId(UUID userId) {
        log.info(Constant.LOG_FETCHING_PAYMENTS_FOR_USER, userId);
        try {
            List<Payment> payments = paymentRepository.findAllByUserId(userId);
            log.info(Constant.LOG_PAYMENTS_FETCHED_SUCCESSFULLY);
            return payments.stream().map(converter::convertToDTO).toList();
        } catch (Exception e) {
            log.error(Constant.ERROR_FETCHING_PAYMENTS, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        log.info(Constant.LOG_FETCHING_ALL_PAYMENTS);
        try {
            List<Payment> payments = paymentRepository.findAll();
            log.info(Constant.LOG_ALL_PAYMENTS_FETCHED_SUCCESSFULLY);
            return payments.stream().map(converter::convertToDTO).toList();
        } catch (Exception e) {
            log.error(Constant.ERROR_FETCHING_ALL_PAYMENTS, e.getMessage());
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public String deleteById(UUID id) {
            log.info(Constant.LOG_DELETING_PAYMENT_BY_ID, id);
            Optional<Payment> payment = paymentRepository.findById(id);
            if (payment.isPresent()) {
                paymentRepository.deleteById(id);
                return Constant.SUCCESS_PAYMENT_DELETED + id;
            }else {
                throw new EntityNotFoundException(Constant.ERROR_PAYMENT_NOT_FOUND + id);
            }
    }

}
