package com.seeder.cashkick.cashkickservice.repository;

import com.seeder.cashkick.cashkickservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

}
