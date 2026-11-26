package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.repository.entities.AccountEntity;

public interface IAccountJpaRepo extends JpaRepository<AccountEntity, UUID> {

}
