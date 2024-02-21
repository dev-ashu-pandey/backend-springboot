package com.seeder.contract.contractservice.service;

import com.seeder.contract.contractservice.dto.CashKickDTO;
import com.seeder.contract.contractservice.dto.ContractDTO;
import com.seeder.contract.contractservice.entity.CashKick;
import com.seeder.contract.contractservice.entity.Contract;
import com.seeder.contract.contractservice.enums.CashKickStatus;
import com.seeder.contract.contractservice.enums.ContractStatus;
import com.seeder.contract.contractservice.enums.ContractType;
import com.seeder.contract.contractservice.exception.EntityException;
import com.seeder.contract.contractservice.exception.IllegalEntityArgumentException;
import com.seeder.contract.contractservice.repository.ICashKickRepository;
import com.seeder.contract.contractservice.repository.IContractRepository;
import com.seeder.contract.contractservice.request.ContractRequest;
import com.seeder.contract.contractservice.service.impl.ContractService;
import com.seeder.contract.contractservice.utils.EntityDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private IContractRepository contractRepository;

    @Mock
    private ICashKickRepository cashKickRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EntityDtoConverter converter;

    @InjectMocks
    private ContractService contractService;

    private ContractRequest validContractRequest;
    private Contract contract;

    private CashKick cashKick;
    private ContractDTO contractDTO;
    private CashKickDTO cashKickDTO;
    

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setName("Contract A");
        contract.setType(ContractType.Monthly);
        contract.setPerPayment("$1000");
        contract.setTermLength("12 months");
        contract.setTermFees("$50");
        contract.setPaymentAmount("$12000");
        contract.setStatus(ContractStatus.Available);
        contract.setTotalAvailableAmount("$50000");

        validContractRequest = new ContractRequest();
        validContractRequest.setName(contract.getName());
        validContractRequest.setType(contract.getType());
        validContractRequest.setPerPayment(contract.getPerPayment());
        validContractRequest.setTermLength(contract.getTermLength());
        validContractRequest.setTermFees(contract.getTermFees());
        validContractRequest.setPaymentAmount(contract.getPaymentAmount());
        validContractRequest.setStatus(contract.getStatus());
        validContractRequest.setTotalAvailableAmount(contract.getTotalAvailableAmount());

        contractDTO = new ContractDTO();
        contractDTO.setId(contract.getId());
        contractDTO.setName(validContractRequest.getName());
        contractDTO.setType(validContractRequest.getType());
        contractDTO.setPerPayment(validContractRequest.getPerPayment());
        contractDTO.setTermLength(validContractRequest.getTermLength());
        contractDTO.setTermFees(validContractRequest.getTermFees());
        contractDTO.setPaymentAmount(validContractRequest.getPaymentAmount());
        contractDTO.setStatus(validContractRequest.getStatus());
        contractDTO.setTotalAvailableAmount(validContractRequest.getTotalAvailableAmount());

        cashKick = new CashKick();
        cashKick.setId(UUID.randomUUID());
        cashKick.setName("Example CashKick");
        cashKick.setStatus(CashKickStatus.Pending);
        cashKick.setMaturity(new Date());
        cashKick.setTotalReceivedAmount("1000");
        cashKick.setTotalFinancedAmount("5000");
        cashKick.setContracts(new HashSet<>(List.of(contract)));

        cashKickDTO = new CashKickDTO();
        cashKickDTO.setId(cashKick.getId());
        cashKickDTO.setName(cashKick.getName());
        cashKickDTO.setStatus(cashKick.getStatus());
        cashKickDTO.setMaturity(cashKick.getMaturity());
        cashKickDTO.setTotalReceivedAmount(cashKick.getTotalReceivedAmount());
        cashKickDTO.setTotalFinancedAmount(cashKick.getTotalFinancedAmount());

    }
    @Test
    void testGetterAndSetter() {
        ContractDTO contractDTO = new ContractDTO();

        UUID id = UUID.randomUUID();
        contractDTO.setId(id);
        contractDTO.setName("Contract A");
        contractDTO.setType(ContractType.Monthly);
        contractDTO.setPerPayment("$1000");
        contractDTO.setTermLength("12 months");
        contractDTO.setTermFees("$50");
        contractDTO.setPaymentAmount("$12000");
        contractDTO.setStatus(ContractStatus.Available);
        contractDTO.setTotalAvailableAmount("$50000");

        CashKickDTO cashKickDTO = new CashKickDTO();
        UUID cashKickId = UUID.randomUUID();
        cashKickDTO.setId(cashKickId);
        cashKickDTO.setName("CashKick A");
        Date maturity = new Date();
        cashKickDTO.setMaturity(maturity);
        cashKickDTO.setStatus(CashKickStatus.Pending);
        cashKickDTO.setTotalReceivedAmount("$1000");
        cashKickDTO.setTotalFinancedAmount("$5000");
        Set<CashKickDTO> cashKickDTOS = new HashSet<>();
        cashKickDTOS.add(cashKickDTO);

        contractDTO.setCashKickDTOS(cashKickDTOS);

        assertEquals(id, contractDTO.getId());
        assertEquals("Contract A", contractDTO.getName());
        assertEquals(ContractType.Monthly, contractDTO.getType());
        assertEquals("$1000", contractDTO.getPerPayment());
        assertEquals("12 months", contractDTO.getTermLength());
        assertEquals("$50", contractDTO.getTermFees());
        assertEquals("$12000", contractDTO.getPaymentAmount());
        assertEquals(ContractStatus.Available, contractDTO.getStatus());
        assertEquals("$50000", contractDTO.getTotalAvailableAmount());
        assertEquals(cashKickDTOS, contractDTO.getCashKickDTOS());


        assertEquals(cashKickId, cashKickDTO.getId());
        assertEquals("CashKick A", cashKickDTO.getName());
        assertEquals(CashKickStatus.Pending, cashKickDTO.getStatus());
        assertEquals(maturity, cashKickDTO.getMaturity());
        assertEquals("$1000", cashKickDTO.getTotalReceivedAmount());
        assertEquals("$5000", cashKickDTO.getTotalFinancedAmount());
    }

    @Test
    void createContract_ValidRequest_ReturnsContractDTO() {
        Contract savedContract = new Contract();
        when(modelMapper.map(validContractRequest, Contract.class)).thenReturn(contract);
        when(contractRepository.save(contract)).thenReturn(savedContract);

        ContractDTO expectedContractDTO = new ContractDTO();
        when(modelMapper.map(savedContract, ContractDTO.class)).thenReturn(expectedContractDTO);

        ContractDTO result = contractService.createContract(validContractRequest);

        verify(contractRepository, times(1)).save(any(Contract.class));

        assertEquals(expectedContractDTO.getCashKickDTOS(), result.getCashKickDTOS());
        assertEquals(expectedContractDTO.getId(), result.getId());
        assertEquals(expectedContractDTO.getStatus(), result.getStatus());
        assertEquals(expectedContractDTO.getName(), result.getName());
        assertEquals(expectedContractDTO.getType(), result.getType());
        assertEquals(expectedContractDTO.getPerPayment(), result.getPerPayment());
        assertEquals(expectedContractDTO.getPaymentAmount(), result.getPaymentAmount());
        assertEquals(expectedContractDTO.getTermFees(), result.getTermFees());
    }

    @Test
    void createContract_WithCashKicks_ReturnsContractDTO() {
        List<UUID> uuidList = new ArrayList<UUID>();
        uuidList.add(UUID.randomUUID());
        Set<CashKick> cashKicks = new HashSet<>();
        validContractRequest.setCashKickIds(new HashSet<>(uuidList));
        CashKick cashKick = new CashKick();
        cashKicks.add(cashKick);
        Contract savedContract = new Contract();
        when(modelMapper.map(validContractRequest, Contract.class)).thenReturn(contract);

        when(cashKickRepository.findAllById(anySet())).thenReturn(cashKicks.stream().toList());

        when(contractRepository.save(contract)).thenReturn(savedContract);

        ContractDTO expectedContractDTO = new ContractDTO();
        when(modelMapper.map(savedContract, ContractDTO.class)).thenReturn(expectedContractDTO);
        ContractDTO result = contractService.createContract(validContractRequest);

        verify(cashKickRepository, times(1)).findAllById(validContractRequest.getCashKickIds());
        verify(contractRepository, times(1)).save(any(Contract.class));

        assertEquals(expectedContractDTO.getCashKickDTOS(), result.getCashKickDTOS());
        assertEquals(expectedContractDTO.getId(), result.getId());
        assertEquals(expectedContractDTO.getStatus(), result.getStatus());
        assertEquals(expectedContractDTO.getName(), result.getName());
        assertEquals(expectedContractDTO.getType(), result.getType());
        assertEquals(expectedContractDTO.getPerPayment(), result.getPerPayment());
        assertEquals(expectedContractDTO.getTotalAvailableAmount(), result.getTotalAvailableAmount());
        assertEquals(expectedContractDTO.getTermLength(), result.getTermLength());
    }

    @Test
    void getAllContracts_NoError_ReturnsListOfContractDTOs() {
        Contract contract = new Contract();
        List<Contract> contracts = Collections.singletonList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);

        ContractDTO expectedContractDTO = new ContractDTO();
        List<ContractDTO> expectedContractDTOs = Collections.singletonList(expectedContractDTO);
        when(converter.convertToDTO(any(Contract.class))).thenReturn(expectedContractDTO);

        List<ContractDTO> result = contractService.getAllContracts();

        verify(contractRepository, times(1)).findAll();
        assertEquals(expectedContractDTOs, result);
    }
    @Test
    void createContract_WithEntityException_LogsErrorAndThrowsEntityException() {
        when(modelMapper.map(validContractRequest, Contract.class)).thenThrow(EntityException.class);

        assertThrows(EntityException.class, () -> contractService.createContract(validContractRequest));
    }

    @Test
    void createContract_WithIllegalEntityArgumentException_LogsErrorAndThrowsIllegalEntityArgumentException() {
        when(modelMapper.map(validContractRequest, Contract.class)).thenThrow(IllegalEntityArgumentException.class);

        assertThrows(IllegalEntityArgumentException.class, () -> contractService.createContract(validContractRequest));

    }

    @Test
    void getAllContracts_WithEntityException_LogsErrorAndThrowsEntityException() {
        when(contractRepository.findAll()).thenThrow(EntityException.class);

        assertThrows(EntityException.class, () -> contractService.getAllContracts());
    }

    @Test
    void getAllContracts_WithIllegalEntityArgumentException_LogsErrorAndThrowsIllegalEntityArgumentException() {
        when(contractRepository.findAll()).thenThrow(IllegalEntityArgumentException.class);

        assertThrows(IllegalEntityArgumentException.class, () -> contractService.getAllContracts());

    }
}
