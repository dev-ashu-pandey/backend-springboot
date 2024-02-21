package com.seeder.contract.contractservice.repository;

import com.seeder.contract.contractservice.entity.CashKick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICashKickRepository extends JpaRepository<CashKick, UUID> {
}
