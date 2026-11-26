package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import com.example.demo.service.dtos.AccountDto;

public interface IAccountRepo {

    AccountDto save(AccountDto accountDto);

    List<AccountDto> getAllAccounts();

    AccountDto getAccountById(UUID id);

}
