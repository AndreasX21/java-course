package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.repository.IAccountRepo;
import com.example.demo.service.dtos.AccountDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final IAccountRepo accountRepo;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        var savedAccount = accountRepo.save(accountDto);
        return savedAccount;
    }

    @Override
    public List<AccountDto> getAccounts() {
        return accountRepo.getAllAccounts();
    }

    @Override
    public AccountDto getAccountById(UUID id) {
        return accountRepo.getAccountById(id);
    }

}
