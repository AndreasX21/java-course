package com.ledger.repo;

import java.util.Optional;
import java.util.UUID;

import com.ledger.account.BaseAccount;

public interface IAccountRepository {

    void save(BaseAccount account);

    Optional<BaseAccount> findById(UUID id);

    void deleteById(UUID id);

}
