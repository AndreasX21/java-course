package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.service.dtos.AccountDto;

public interface IAccountService {

    AccountDto createAccount(AccountDto accountDto);

    List<AccountDto> getAccounts();

    AccountDto getAccountById(UUID id);
}
