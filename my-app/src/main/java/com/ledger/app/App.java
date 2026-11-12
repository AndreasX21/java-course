package com.ledger.app;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

import com.ledger.account.Account;
import com.ledger.account.BaseAccount;
import com.ledger.model.CVCurrency;
import com.ledger.repo.AccountRepository;
import com.ledger.repo.IAccountRepository;
import com.ledger.transaction.ITransactionProcessor;
import com.ledger.transaction.TransactionProcessor;
import com.ledger.transaction.TransactionDto;

/**
 * Hello world!
 */

public class App {
    public static void main(String[] args) {

        IAccountRepository accountRepository = new AccountRepository();
        ITransactionProcessor transactionProcessor = new TransactionProcessor(accountRepository);

        List<BaseAccount> accounts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            accounts.add(new Account("John_Dow_" + i, CVCurrency.USD));
        }

        for (var account : accounts) {
            account.credit(BigDecimal.valueOf(100));
            accountRepository.save(account);
        }

        List<TransactionDto> transactions = new ArrayList<>();
        IntStream.range(0, 100).forEach(i -> {
            transactions.add(new TransactionDto(
                    UUID.randomUUID(),
                    accounts.get(i % accounts.size()).getId(),
                    accounts.get((i + 1) % accounts.size()).getId(),
                    BigDecimal.valueOf(Math.random() * 35),
                    LocalDateTime.of(2026, 1, 1, 0, 0, 0).plusDays((int) Math.floor(Math.random() * 365))));
        });

        transactionProcessor.processTransactions(transactions);

        accounts.stream().forEach(System.out::println);

    }
}
