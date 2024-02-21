package com.seeder.contract.contractservice.repository;

import com.seeder.contract.contractservice.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IContractRepository extends JpaRepository<Contract, UUID> {

}
