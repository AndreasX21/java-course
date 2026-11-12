package com.ledger.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ledger.account.BaseAccount;

public class AccountRepository implements IAccountRepository {

    private final Map<UUID, BaseAccount> accounts;

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }

    @Override
    public void save(BaseAccount account) {
        accounts.put(account.getId(), account);
    }

    @Override
    public Optional<BaseAccount> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        accounts.remove(id);
    }
}
