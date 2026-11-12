package com.ledger.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.ledger.model.CVCurrency;
import com.ledger.exception.InsufficientBalanceException;

public class Account extends BaseAccount {

    public Account(UUID id, String ownerName, CVCurrency currency, BigDecimal balance) {
        super(id, ownerName, currency, balance);
    }

    public Account(String ownerName, CVCurrency currency) {
        this(UUID.randomUUID(), ownerName, currency, BigDecimal.ZERO);
    }

    @Override
    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        setBalance(getBalance().add(amount));
    }

    @Override
    public void debit(BigDecimal amount) throws InsufficientBalanceException {
        if (getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    this.toString() + " has insufficient balance to debit " + amount);
        }
        setBalance(getBalance().subtract(amount));
    }

}
