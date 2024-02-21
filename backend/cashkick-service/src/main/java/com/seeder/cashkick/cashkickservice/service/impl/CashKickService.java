package com.seeder.cashkick.cashkickservice.service.impl;

import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.entity.CashKick;
import com.seeder.cashkick.cashkickservice.entity.Contract;
import com.seeder.cashkick.cashkickservice.entity.User;
import com.seeder.cashkick.cashkickservice.exception.EntityException;
import com.seeder.cashkick.cashkickservice.exception.EntityNotFoundException;
import com.seeder.cashkick.cashkickservice.exception.InsufficientBalanceException;
import com.seeder.cashkick.cashkickservice.exception.InvalidParameterException;
import com.seeder.cashkick.cashkickservice.repository.ICashKickRepository;
import com.seeder.cashkick.cashkickservice.repository.IContractRepository;
import com.seeder.cashkick.cashkickservice.repository.IUserRepository;
import com.seeder.cashkick.cashkickservice.request.CashKickRequest;
import com.seeder.cashkick.cashkickservice.service.ICashKickService;
import com.seeder.cashkick.cashkickservice.utils.Constant;
import com.seeder.cashkick.cashkickservice.utils.DataConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class CashKickService implements ICashKickService {

    private final ICashKickRepository cashKickRepository;
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IContractRepository contractRepository;
    private final DataConverter converter;

    @Autowired
    public CashKickService(ICashKickRepository cashKickRepository, IContractRepository contractRepository,  IUserRepository userRepository, ModelMapper modelMapper, DataConverter converter) {
        this.cashKickRepository = cashKickRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.converter = converter;
        this.contractRepository = contractRepository;
    }

    @Override
    public CashKickDTO createCashKick(CashKickRequest cashkickRequest) {
        log.info(Constant.LOG_CREATING_CASH_KICK + cashkickRequest);
        User user = userRepository.findById(cashkickRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException(Constant.ERROR_USER_NOT_FOUND + cashkickRequest.getUserId()));
        try {
            BigDecimal totalFinancedAmount = converter.parseCurrency(cashkickRequest.getTotalFinancedAmount());
            BigDecimal totalReceivedAmount = converter.parseCurrency(cashkickRequest.getTotalReceivedAmount());

            if (totalReceivedAmount.compareTo(totalFinancedAmount) > 0) {
                throw new InvalidParameterException("Total financed amount must be greater than total received amount");
            }

            BigDecimal cashKickBalance = converter.parseCurrency(user.getCashKickBalance());
            if (cashKickBalance.compareTo(totalFinancedAmount) < 0) {
                throw new InsufficientBalanceException("Insufficient balance in user's cashkick account");
            }
            List<Contract> contracts = contractRepository.findAllById(cashkickRequest.getContractIds());
            CashKick cashKick = modelMapper.map(cashkickRequest, CashKick.class);
            log.debug(Constant.LOG_CONVERTED_CASH_KICK_ENTITY + cashKick);
            cashKick.setUser(user);
            cashKick.setContracts(new HashSet<>(contracts));
            log.debug(Constant.LOG_CONVERTED_CASH_KICK_ENTITY + cashKick);
            CashKick savedCashKick = cashKickRepository.save(cashKick);
            return converter.convertToDTO(savedCashKick);
        } catch (Exception e) {
            throw new EntityException(e.getMessage());
        }
    }

    @Override
    public List<CashKickDTO> getAllCashKicks() {
        log.info(Constant.LOG_GETTING_ALL_CASH_KICKS);
        try {
            List<CashKick> cashKicks = cashKickRepository.findAll();
            log.debug(Constant.LOG_RETRIEVE_CASHKICK, cashKicks.size());
            return cashKicks.stream().map(converter::convertToDTO).toList();
        } catch (EntityException e) {
            throw new EntityException(e.getMessage());
        }
    }


    @Override
    public List<CashKickDTO> getAllCashKicksByUserId(UUID userId) {
        log.info(Constant.LOG_GETTING_CASH_KICKS_BY_USER_ID + userId);
        try {
            List<CashKick> cashKicks = cashKickRepository.findAllByUserId(userId);
            return cashKicks.stream().map(converter::convertToDTO).toList();
        } catch (EntityException e) {
            throw new EntityException(e.getMessage());
        }
    }
}
