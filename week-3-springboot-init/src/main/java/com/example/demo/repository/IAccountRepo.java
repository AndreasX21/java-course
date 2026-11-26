package com.example.demo.repository;

import com.example.demo.service.dtos.AccountDto;

public interface IAccountRepo {

    AccountDto save(AccountDto accountDto);

}
