package com.seeder.cashkick.cashkickservice.repository;

import com.seeder.cashkick.cashkickservice.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IContractRepository extends JpaRepository<Contract, UUID> {
}
