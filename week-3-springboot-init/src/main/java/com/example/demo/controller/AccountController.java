package com.example.demo.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponseDto;
import com.example.demo.controller.mapper.AccountMapper;
import com.example.demo.service.IAccountService;
import com.example.demo.service.dtos.AccountDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountMapper accountMapper;
    private final IAccountService accountService;

    @PostMapping
    @Operation(summary = "Create account", description = "Create account")
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody AccountRequestDto requestDto) {

        AccountDto accountDto = accountMapper.requestDtoToDomainDto(requestDto);
        accountDto = accountService.createAccount(accountDto);
        var responseDto = accountMapper.domainDtoToResponseDto(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Get all accounts")
    public ResponseEntity<List<AccountResponseDto>> getAccounts() {
        List<AccountDto> accountDtos = accountService.getAccounts();
        var responseDtos = accountDtos.stream()
                .map(accountMapper::domainDtoToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Get account by ID")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable UUID id) {
        AccountDto accountDto = accountService.getAccountById(id);
        var responseDto = accountMapper.domainDtoToResponseDto(accountDto);
        return ResponseEntity.ok(responseDto);
    }

}
