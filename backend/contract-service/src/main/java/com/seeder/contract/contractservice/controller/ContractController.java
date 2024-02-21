package com.seeder.contract.contractservice.controller;

import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.request.ContractRequest;
import com.seeder.contract.contractservice.service.IContractService;
import com.seeder.contract.contractservice.utils.Constant;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.API_BASE_URL)
@Slf4j
public class ContractController {

    private final IContractService contractService;

    @Autowired
    public ContractController(IContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts() {
        log.info(Constant.LOG_GET_ALL_CONTRACTS);
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@RequestBody @Valid ContractRequest contractRequest) {
        log.info(Constant.LOG_CREATE_CONTRACT);
        return ResponseEntity.status(HttpStatus.CREATED).body(contractService.createContract(contractRequest));
    }
}
