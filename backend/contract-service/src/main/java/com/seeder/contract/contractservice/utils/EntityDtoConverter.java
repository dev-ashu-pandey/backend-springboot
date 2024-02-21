package com.seeder.contract.contractservice.utils;

import com.seeder.contract.contractservice.dto.CashKickDTO;
import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.entity.Contract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityDtoConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public EntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ContractDTO convertToDTO(Contract contract) {
        ContractDTO contractDTO = modelMapper.map(contract, ContractDTO.class);
        if (contract.getCashkicks() != null) {
            Set<CashKickDTO> cashKickDTOS = contract.getCashkicks().stream().map(cashkick-> modelMapper.map(cashkick,CashKickDTO.class)).collect(Collectors.toSet());
            contractDTO.setCashKickDTOS(cashKickDTOS);
        }
        return contractDTO;
    }
}
