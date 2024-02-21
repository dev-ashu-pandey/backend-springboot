package com.seeder.payment.paymentservice.utils;

import com.seeder.payment.paymentservice.dto.PaymentDTO;
import com.seeder.payment.paymentservice.dto.UserDTO;
import com.seeder.payment.paymentservice.entity.Payment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataConverter {
    private ModelMapper modelMapper;

    @Autowired
    public DataConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PaymentDTO convertToDTO(Payment payment) {
        UserDTO userDTO = modelMapper.map(payment.getUser(), UserDTO.class);
        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
        paymentDTO.setUserDTO(userDTO);
        return paymentDTO;
    }
    public BigDecimal parseCurrency(String currencyValue) {
        return new BigDecimal(currencyValue.replace("$", "").replace(",", ""));
    }
}
