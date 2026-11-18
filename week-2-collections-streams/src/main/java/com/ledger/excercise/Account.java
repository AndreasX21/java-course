package com.ledger.excercise;

import java.util.List;

public class Account {
    private final Integer clientId;
    private final Integer bankId;
    private final Integer balance;

    public Account(Integer clientId, Integer bankId, Integer balance) {
        this.clientId = clientId;
        this.bankId = bankId;
        this.balance = balance;
    }

    public static List<Account> all() {
        return List.of(
                new Account(6, 1, 15000),
                new Account(1, 3, 18000),
                new Account(5, 3, 135000),
                new Account(2, 2, 5600),
                new Account(3, 1, 23000),
                new Account(5, 2, 15000),
                new Account(3, 3, 45900),
                new Account(2, 3, 19000),
                new Account(4, 3, 51000),
                new Account(5, 1, 89000),
                new Account(1, 2, 1600),
                new Account(5, 3, 37500),
                new Account(6, 1, 19200),
                new Account(2, 3, 10000),
                new Account(3, 2, 5400),
                new Account(3, 1, 9000),
                new Account(4, 3, 13500),
                new Account(2, 1, 38200),
                new Account(5, 2, 17000),
                new Account(1, 3, 1000),
                new Account(5, 2, 600),
                new Account(6, 1, 16200),
                new Account(2, 2, 10000));
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getBankId() {
        return bankId;
    }

    public Integer getBalance() {
        return balance;
    }
}
