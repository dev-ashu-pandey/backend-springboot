package cashkickservice.controller;

import com.seeder.cashkick.cashkickservice.controller.CashKickController;
import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.request.CashKickRequest;
import com.seeder.cashkick.cashkickservice.service.ICashKickService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
class CashKickControllerTest {

    @Mock
    private ICashKickService cashKickService;

    @InjectMocks
    private CashKickController cashKickController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCashKick() {
        CashKickRequest cashKickRequest = new CashKickRequest();
        CashKickDTO cashKickDTO = new CashKickDTO();

        when(cashKickService.createCashKick(any(CashKickRequest.class))).thenReturn(cashKickDTO);
        ResponseEntity<CashKickDTO> response = cashKickController.createCashKick(cashKickRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cashKickDTO, response.getBody());
        verify(cashKickService, times(1)).createCashKick(cashKickRequest);
    }

    @Test
    void testGetAllCashKick() {
        List<CashKickDTO> cashKicks = Collections.singletonList(new CashKickDTO());
        when(cashKickService.getAllCashKicks()).thenReturn(cashKicks);

        ResponseEntity<List<CashKickDTO>> response = cashKickController.getAllCashKick();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cashKicks, response.getBody());

        verify(cashKickService, times(1)).getAllCashKicks();
    }

    @Test
    void testGetAllCashKickByUserId() {
        UUID userId = UUID.randomUUID();
        List<CashKickDTO> cashKicks = Collections.singletonList(new CashKickDTO());

        when(cashKickService.getAllCashKicksByUserId(userId)).thenReturn(cashKicks);

        ResponseEntity<List<CashKickDTO>> response = cashKickController.getAllCashKickByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cashKicks, response.getBody());

        verify(cashKickService, times(1)).getAllCashKicksByUserId(userId);
    }
}
