package com.seeder.cashkick.cashkickservice.service;

import com.seeder.cashkick.cashkickservice.dto.CashKickDTO;
import com.seeder.cashkick.cashkickservice.request.CashKickRequest;

import java.util.List;
import java.util.UUID;

public interface ICashKickService {
    CashKickDTO createCashKick(CashKickRequest cashkickRequest);

    List<CashKickDTO> getAllCashKicks();
    List<CashKickDTO> getAllCashKicksByUserId(UUID userId);
}
