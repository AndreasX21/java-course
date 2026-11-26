package com.example.demo.repository.mappers;

import org.mapstruct.Mapper;

import com.example.demo.repository.entities.AccountEntity;
import com.example.demo.service.dtos.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountEntityMapper {

    AccountEntity dtoToEntity(AccountDto accountDto);

    AccountDto entityToDto(AccountEntity accountEntity);

}
