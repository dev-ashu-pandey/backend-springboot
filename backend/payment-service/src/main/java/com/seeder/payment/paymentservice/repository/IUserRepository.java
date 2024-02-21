package com.seeder.payment.paymentservice.repository;

import com.seeder.payment.paymentservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
