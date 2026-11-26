package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.repository.mappers.AccountEntityMapper;
import com.example.demo.service.dtos.AccountDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepo implements IAccountRepo {

    private final AccountEntityMapper accountEntityMapper;
    private final IAccountJpaRepo accountJpaRepo;

    @Override
    public AccountDto save(AccountDto accountDto) {
        var accountEntity = accountEntityMapper.dtoToEntity(accountDto);
        accountEntity = accountJpaRepo.saveAndFlush(accountEntity);
        return accountEntityMapper.entityToDto(accountEntity);
    }

}
