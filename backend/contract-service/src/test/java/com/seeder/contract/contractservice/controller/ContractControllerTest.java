package com.seeder.contract.contractservice.controller;

import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.request.ContractRequest;
import com.seeder.contract.contractservice.service.IContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private IContractService contractService;

    @InjectMocks
    private ContractController contractController;

    private ContractRequest validContractRequest;
    private ContractDTO expectedContractDTO;

    @BeforeEach
    void setUp() {
        validContractRequest = new ContractRequest();
        expectedContractDTO = new ContractDTO();
    }

    @Test
    void getAllContracts_ReturnsListOfContractDTOs() {
        List<ContractDTO> expectedContracts = Collections.singletonList(expectedContractDTO);
        when(contractService.getAllContracts()).thenReturn(expectedContracts);

        ResponseEntity<List<ContractDTO>> response = contractController.getAllContracts();
        verify(contractService, times(1)).getAllContracts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedContracts, response.getBody());
    }

    @Test
    void createContract_ValidRequest_ReturnsCreatedStatusAndContractDTO() {
        when(contractService.createContract(validContractRequest)).thenReturn(expectedContractDTO);
        ResponseEntity<ContractDTO> response = contractController.createContract(validContractRequest);
        verify(contractService, times(1)).createContract(validContractRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedContractDTO, response.getBody());
    }
}
