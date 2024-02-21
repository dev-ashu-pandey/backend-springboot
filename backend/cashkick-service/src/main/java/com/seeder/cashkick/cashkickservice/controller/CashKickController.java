package com.seeder.cashkick.cashkickservice.controller;

import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.request.CashKickRequest;
import com.seeder.cashkick.cashkickservice.service.ICashKickService;
import com.seeder.cashkick.cashkickservice.utils.Constant;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(Constant.BASE_URL)
@Slf4j
public class CashKickController {

    private final ICashKickService cashKickService;

    @Autowired
    public CashKickController(ICashKickService cashKickService) {
        this.cashKickService = cashKickService;
    }

    @PostMapping
    public ResponseEntity<CashKickDTO> createCashKick(@RequestBody @Valid CashKickRequest cashKickRequest){
        log.info(Constant.CREATE_CASH_KICK_LOG);
        CashKickDTO cashKickDTO = cashKickService.createCashKick(cashKickRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cashKickDTO);
    }

    @GetMapping(Constant.ALL)
    public ResponseEntity<List<CashKickDTO>> getAllCashKick(){
        log.info(Constant.GET_ALL_CASH_KICKS_LOG);
        List<CashKickDTO> cashKicks = cashKickService.getAllCashKicks();
        return ResponseEntity.ok(cashKicks);
    }

    @GetMapping
    public ResponseEntity<List<CashKickDTO>> getAllCashKickByUserId(@RequestParam UUID userId){
        log.info(Constant.GET_ALL_CASH_KICKS_BY_USER_ID_LOG);
        List<CashKickDTO> cashKicks = cashKickService.getAllCashKicksByUserId(userId);
        return ResponseEntity.ok(cashKicks);
    }
}
