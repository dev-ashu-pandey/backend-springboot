package com.seeder.contract.contractservice.service;

import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.request.ContractRequest;

import java.util.List;

public interface IContractService {
    ContractDTO createContract(ContractRequest contractRequest);
    List<ContractDTO> getAllContracts();
}
