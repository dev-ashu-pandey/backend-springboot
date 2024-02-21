package com.seeder.contract.contractservice.service.impl;

import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.entity.CashKick;
import com.seeder.contract.contractservice.entity.Contract;
import com.seeder.contract.contractservice.exception.EntityException;
import com.seeder.contract.contractservice.exception.IllegalEntityArgumentException;
import com.seeder.contract.contractservice.exception.InvalidParameterException;
import com.seeder.contract.contractservice.repository.ICashKickRepository;
import com.seeder.contract.contractservice.repository.IContractRepository;
import com.seeder.contract.contractservice.request.ContractRequest;
import com.seeder.contract.contractservice.service.IContractService;
import com.seeder.contract.contractservice.utils.Constant;
import com.seeder.contract.contractservice.utils.EntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ContractService implements IContractService {

    private final IContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final ICashKickRepository cashKickRepository;
    private final EntityDtoConverter converter;

    public ContractService(IContractRepository contractRepository, ModelMapper modelMapper, ICashKickRepository cashKickRepository, EntityDtoConverter converter) {
        this.contractRepository = contractRepository;
        this.modelMapper = modelMapper;
        this.cashKickRepository = cashKickRepository;
        this.converter = converter;
    }

    @Override
    public ContractDTO createContract(ContractRequest contractRequest) {
        log.info(Constant.LOG_CREATE_CONTRACT_WITH_REQUEST, contractRequest.toString());
        try {
            BigDecimal perPayment = new BigDecimal(contractRequest.getPerPayment().replace("$", "").replace(",", ""));
           log.info("per payment" +perPayment);
            if (perPayment.compareTo(BigDecimal.valueOf(100)) < 0) {
                throw new InvalidParameterException("Per payment amount must be greater than 0");
            }

            int termLength = Integer.parseInt(contractRequest.getTermLength().replaceAll("[^\\d]", ""));
            if (termLength <= 0) {
                throw new InvalidParameterException("Term length must be greater than 0");
            }

            BigDecimal paymentAmount = new BigDecimal(contractRequest.getPaymentAmount().replace("$", "").replace(",", ""));
            if (paymentAmount.compareTo(perPayment) < 0) {
                throw new InvalidParameterException("Payment amount must be greater than per payment amount");
            }
            BigDecimal totalAvailableAmount = new BigDecimal(contractRequest.getTotalAvailableAmount().replace("$", "").replace(",", ""));
            if (totalAvailableAmount.compareTo(paymentAmount) < 0) {
                throw new InvalidParameterException("Total available amount must be greater than payment amount");
            }
            Contract contract = modelMapper.map(contractRequest, Contract.class);
            if ((contractRequest.getCashKickIds() != null)){
                Set<CashKick> cashKicks = new HashSet<>(cashKickRepository.findAllById(contractRequest.getCashKickIds()));
                contract.setCashkicks(cashKicks);
            }
            Contract savedContract = contractRepository.save(contract);
            return modelMapper.map(savedContract, ContractDTO.class);
        } catch (EntityException e) {
            log.error(e.getMessage(), e);
            throw new EntityException(e.getMessage());
        } catch (IllegalEntityArgumentException e) {
            log.error(e.getMessage(), e);
            throw new IllegalEntityArgumentException(e.getMessage());
        }
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        log.info(Constant.LOG_GET_ALL_CONTRACTS);
        try {
            List<Contract> contracts = contractRepository.findAll();
            return contracts.stream().map(converter::convertToDTO).toList();
        } catch (EntityException e) {
            log.error(e.getMessage(), e);
            throw new EntityException(e.getMessage());
        } catch (IllegalEntityArgumentException e) {
            log.error(e.getMessage(), e);
            throw new IllegalEntityArgumentException(e.getMessage());
        }
    }
}
