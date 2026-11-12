package com.ledger.account;

import java.math.BigDecimal;
import java.util.UUID;

import com.ledger.exception.InsufficientBalanceException;
import com.ledger.model.CVCurrency;

public abstract class BaseAccount {
    private final UUID id;
    private final String ownerName;
    private final CVCurrency currency;
    private BigDecimal balance;

    public BaseAccount(UUID id, String ownerName, CVCurrency currency, BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = balance;
    }

    /**
     * Credit the account by the given amount.
     *
     * @param amount the amount to credit
     */
    public abstract void credit(BigDecimal amount);

    /**
     * Debit the account by the given amount. If the account has insufficient
     * balance, throw an InsufficientBalanceException.
     *
     * @param amount the amount to debit
     * @throws InsufficientBalanceException if the account has insufficient balance
     */
    public abstract void debit(BigDecimal amount) throws InsufficientBalanceException;

    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public CVCurrency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return "Account [id=" + id.toString().substring(0, 8) + ", ownerName=" + ownerName
                + ", currency=" + currency
                + ", balance=" + balance
                + "]";
    }

}
