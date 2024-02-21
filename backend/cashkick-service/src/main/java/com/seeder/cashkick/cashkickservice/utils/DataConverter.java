package com.seeder.cashkick.cashkickservice.utils;

import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.dto.ContractDTO;
import com.seeder.cashkick.cashkickservice.dto.UserDTO;
import com.seeder.cashkick.cashkickservice.entity.CashKick;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public DataConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CashKickDTO convertToDTO(CashKick cashKick) {
        CashKickDTO cashKickDTO = modelMapper.map(cashKick, CashKickDTO.class);
        UserDTO userDTO = modelMapper.map(cashKick.getUser(), UserDTO.class);
        cashKickDTO.setUserDTO(userDTO);
        List<ContractDTO> contractDTOS = cashKick.getContracts().stream().map(contract -> modelMapper.map(contract, ContractDTO.class)).toList();
        cashKickDTO.setContractDTOS(contractDTOS);
        return cashKickDTO;
    }
    public BigDecimal parseCurrency(String currencyValue) {
        return new BigDecimal(currencyValue.replace("$", "").replace(",", ""));
    }
}
