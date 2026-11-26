package com.example.demo.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.controller.dto.AccountRequestDto;
import com.example.demo.controller.dto.AccountResponseDto;
import com.example.demo.service.dtos.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    AccountDto requestDtoToDomainDto(AccountRequestDto requestDto);

    AccountResponseDto domainDtoToResponseDto(AccountDto domainDto);

}
