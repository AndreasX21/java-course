package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponseDto;
import com.example.demo.controller.mapper.AccountMapper;
import com.example.demo.service.IAccountService;
import com.example.demo.service.dtos.AccountDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountMapper accountMapper;
    private final IAccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody AccountRequestDto requestDto) {

        AccountDto accountDto = accountMapper.requestDtoToDomainDto(requestDto);
        accountDto = accountService.createAccount(accountDto);
        var responseDto = accountMapper.domainDtoToResponseDto(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

}
