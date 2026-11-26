package com.example.demo.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.repository.entities.AccountEntity;
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

    @Override
    public List<AccountDto> getAllAccounts() {
        List<AccountEntity> accountEntities = accountJpaRepo.findAll();
        return accountEntities.stream()
                .map(accountEntityMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountById(UUID id) {
        AccountEntity accountEntity = accountJpaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return accountEntityMapper.entityToDto(accountEntity);
    }

}
