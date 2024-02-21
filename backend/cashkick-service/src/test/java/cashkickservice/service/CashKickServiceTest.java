package cashkickservice.service;

import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.dto.ContractDTO;
import com.seeder.cashkick.cashkickservice.dto.UserDTO;
import com.seeder.cashkick.cashkickservice.entity.CashKick;
import com.seeder.cashkick.cashkickservice.entity.Contract;
import com.seeder.cashkick.cashkickservice.entity.User;
import com.seeder.cashkick.cashkickservice.enums.CashKickStatusEnum;
import com.seeder.cashkick.cashkickservice.enums.ContractStatusEnum;
import com.seeder.cashkick.cashkickservice.enums.ContractTypeEnum;
import com.seeder.cashkick.cashkickservice.exception.EntityException;
import com.seeder.cashkick.cashkickservice.exception.EntityNotFoundException;
import com.seeder.cashkick.cashkickservice.repository.ICashKickRepository;
import com.seeder.cashkick.cashkickservice.repository.IContractRepository;
import com.seeder.cashkick.cashkickservice.repository.IUserRepository;
import com.seeder.cashkick.cashkickservice.request.CashKickRequest;
import com.seeder.cashkick.cashkickservice.service.impl.CashKickService;
import com.seeder.cashkick.cashkickservice.utils.DataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CashKickServiceTest {

    @Mock
    private ICashKickRepository cashKickRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IContractRepository contractRepository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DataConverter converter;

    @InjectMocks
    private CashKickService cashKickService;

    private User user;
    private UserDTO userDTO;
    private Contract contract;
    private ContractDTO contractDTO;
    private CashKick cashKick;
    private CashKickDTO cashKickDTO;
    private CashKickRequest cashKickRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID cashkickId = UUID.randomUUID();
        UUID contractId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setCashKickBalance("1000");


        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCashKickBalance(user.getCashKickBalance());

        contract = new Contract();
        contract.setId(contractId);
        contract.setName("Contract 1");
        contract.setType(ContractTypeEnum.Monthly);
        contract.setPerPayment("100");
        contract.setTermLength("12 months");
        contract.setTermFees("1%");
        contract.setPaymentAmount("1200");
        contract.setStatus(ContractStatusEnum.Available);
        contract.setTotalAvailableAmount("5000");

        contractDTO = new ContractDTO();
        contractDTO.setId(contract.getId());
        contractDTO.setName(contract.getName());
        contractDTO.setType(contract.getType());
        contractDTO.setPerPayment(contract.getPerPayment());
        contractDTO.setTermLength(contract.getTermLength());
        contractDTO.setTermFees(contract.getTermFees());
        contractDTO.setPaymentAmount(contract.getPaymentAmount());
        contractDTO.setStatus(contract.getStatus());
        contractDTO.setTotalAvailableAmount(contract.getTotalAvailableAmount());

        cashKick = new CashKick();
        cashKick.setId(cashkickId);
        cashKick.setName("Cash Kick 1");
        cashKick.setStatus(CashKickStatusEnum.Active);
        cashKick.setMaturity(new Date());
        cashKick.setTotalReceivedAmount("2000");
        cashKick.setTotalFinancedAmount("2500");
        cashKick.setUser(user);
        Set<Contract> contracts = new HashSet<>();
        contracts.add(contract);
        cashKick.setContracts(contracts);

        cashKickDTO = new CashKickDTO();
        cashKickDTO.setId(cashKick.getId());
        cashKickDTO.setName(cashKick.getName());
        cashKickDTO.setStatus(cashKick.getStatus());
        cashKickDTO.setMaturity(cashKick.getMaturity());
        cashKickDTO.setTotalReceivedAmount(cashKick.getTotalReceivedAmount());
        cashKickDTO.setTotalFinancedAmount(cashKick.getTotalFinancedAmount());
        cashKickDTO.setUserDTO(userDTO);
        cashKickDTO.setContractDTOS(List.of(contractDTO));

        cashKickRequest = new CashKickRequest();
        cashKickRequest.setName(cashKick.getName());
        cashKickRequest.setStatus(cashKick.getStatus());
        cashKickRequest.setMaturity(cashKickDTO.getMaturity());
        cashKickRequest.setUserId(cashKick.getUser().getId());
        cashKickRequest.setTotalReceivedAmount(cashKickDTO.getTotalReceivedAmount());
        cashKickRequest.setTotalFinancedAmount(cashKickDTO.getTotalFinancedAmount());
        cashKickRequest.setContractIds(cashKick.getContracts().stream().map(Contract::getId).collect(Collectors.toSet()));

    }
    @Test
    void testCreateCashKick() {

        when(userRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(user));
        when(contractRepository.findAllById(any())).thenReturn(Collections.singletonList(contract));
        when(modelMapper.map(any(), any())).thenReturn(cashKick);
        when(cashKickRepository.save(any())).thenReturn(cashKick);
        when(converter.convertToDTO(any())).thenReturn(cashKickDTO);

        CashKickDTO createdCashKickDTO = cashKickService.createCashKick(cashKickRequest);
        assertEquals(cashKickDTO.getId(), createdCashKickDTO.getId());
        assertEquals(cashKickDTO.getName(), createdCashKickDTO.getName());
        assertEquals(cashKickDTO.getMaturity(), createdCashKickDTO.getMaturity());
        assertEquals(cashKickDTO.getStatus(), createdCashKickDTO.getStatus());
        assertEquals(cashKickDTO.getTotalFinancedAmount(), createdCashKickDTO.getTotalFinancedAmount());
        assertEquals(cashKickDTO.getTotalFinancedAmount(), createdCashKickDTO.getTotalFinancedAmount());
        assertEquals(cashKickDTO.getContractDTOS(), createdCashKickDTO.getContractDTOS());
        assertEquals(cashKickDTO.getUserDTO(), createdCashKickDTO.getUserDTO());
    }

    @Test
    void testGetAllCashKicks() {
        List<CashKick> cashKicks = Collections.singletonList(cashKick);
        when(cashKickRepository.findAll()).thenReturn(cashKicks);
        when(converter.convertToDTO(cashKick)).thenReturn(cashKickDTO);

        List<CashKickDTO> result = cashKickService.getAllCashKicks();

        assertEquals(1, result.size());
        assertEquals(cashKick.getId(), result.get(0).getId());
    }

    @Test
    void testContractDTOGetterAndSetter() {
        ContractDTO contractDTO = new ContractDTO();
        UUID id = UUID.randomUUID();
        String name = "Test Contract";
        ContractTypeEnum type = ContractTypeEnum.Monthly;
        String perPayment = "$100";
        String termLength = "12 months";
        String termFees = "1%";
        String paymentAmount = "$1200";
        ContractStatusEnum status = ContractStatusEnum.Available;
        String totalAvailableAmount = "$5000";

        contractDTO.setId(id);
        contractDTO.setName(name);
        contractDTO.setType(type);
        contractDTO.setPerPayment(perPayment);
        contractDTO.setTermLength(termLength);
        contractDTO.setTermFees(termFees);
        contractDTO.setPaymentAmount(paymentAmount);
        contractDTO.setStatus(status);
        contractDTO.setTotalAvailableAmount(totalAvailableAmount);

        assertEquals(id, contractDTO.getId());
        assertEquals(name, contractDTO.getName());
        assertEquals(type, contractDTO.getType());
        assertEquals(perPayment, contractDTO.getPerPayment());
        assertEquals(termLength, contractDTO.getTermLength());
        assertEquals(termFees, contractDTO.getTermFees());
        assertEquals(paymentAmount, contractDTO.getPaymentAmount());
        assertEquals(status, contractDTO.getStatus());
        assertEquals(totalAvailableAmount, contractDTO.getTotalAvailableAmount());
    }

    @Test
    void testUserDTOGetterAndSetter() {
        UserDTO userDTO = new UserDTO();
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john.doe@example.com";
        String cashKickBalance = "$1000";

        userDTO.setId(id);
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setCashKickBalance(cashKickBalance);

        assertEquals(id, userDTO.getId());
        assertEquals(name, userDTO.getName());
        assertEquals(email, userDTO.getEmail());
        assertEquals(cashKickBalance, userDTO.getCashKickBalance());
    }

    @Test
    void testGetAllCashKicksByUserId() {
        List<CashKick> cashKicks = Collections.singletonList(new CashKick());
        List<CashKickDTO> cashKickDTOs = Collections.singletonList(cashKickDTO);

        when(cashKickRepository.findAllByUserId(any(UUID.class))).thenReturn(cashKicks);
        when(converter.convertToDTO(any())).thenReturn(cashKickDTO);

        List<CashKickDTO> retrievedCashKicks = cashKickService.getAllCashKicksByUserId(user.getId());
        assertEquals(cashKickDTOs, retrievedCashKicks);
    }

    @Test
    void testCreateCashKick_UserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            cashKickService.createCashKick(cashKickRequest);
        });
    }


    @Test
    void testCreateCashKick_ExceptionThrown() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        when(contractRepository.findAllById(any())).thenReturn(Collections.singletonList(contract));

        when(modelMapper.map(any(), any())).thenThrow(RuntimeException.class);
        assertThrows(EntityException.class, () -> {
            cashKickService.createCashKick(cashKickRequest);
        });
    }

    @Test
    void testGetAllCashKicks_ExceptionThrown() {
        when(cashKickRepository.findAll()).thenThrow(EntityException.class);

        assertThrows(EntityException.class, () -> {
            cashKickService.getAllCashKicks();
        });
    }

    @Test
    void testGetAllCashKicksByUserId_ExceptionThrown() {
        UUID userId = UUID.randomUUID();
        when(cashKickRepository.findAllByUserId(any(UUID.class))).thenThrow(EntityException.class);

        assertThrows(EntityException.class, () -> cashKickService.getAllCashKicksByUserId(userId));
    }
}
