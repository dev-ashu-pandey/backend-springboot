package com.seeder.cashkick.cashkickservice.repository;

import com.seeder.cashkick.cashkickservice.entity.CashKick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICashKickRepository extends JpaRepository<CashKick, UUID> {
    List<CashKick> findAllByUserId(UUID userId);
}
