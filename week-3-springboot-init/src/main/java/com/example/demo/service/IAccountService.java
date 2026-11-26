package com.example.demo.service;

import com.example.demo.service.dtos.AccountDto;

public interface IAccountRepository {

    AccountDto createAccount(AccountDto accountDto);
}
