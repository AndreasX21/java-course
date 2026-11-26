package com.example.demo.service;

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

}
