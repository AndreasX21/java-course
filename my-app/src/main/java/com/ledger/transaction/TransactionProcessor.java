package com.ledger.transaction;

import com.ledger.repo.IAccountRepository;
import com.ledger.exception.InsufficientBalanceException;
import java.util.List;
import java.util.Comparator;

public class TransactionProcessor implements ITransactionProcessor {

    private final IAccountRepository accountRepository;

    public TransactionProcessor(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void processTransaction(TransactionDto transactionDto) {
        var senderAccount = accountRepository.findById(transactionDto.senderAccountId());
        var receiverAccount = accountRepository.findById(transactionDto.receiverAccountId());

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            System.out.println("Sender or receiver account not found");
            return;
        }

        try {
            senderAccount.get().debit(transactionDto.amount());
            receiverAccount.get().credit(transactionDto.amount());
        } catch (InsufficientBalanceException e) {
            System.out.println("Transaction " + transactionDto.toString() + " failed");
        }
    }

    @Override
    public void processTransactions(List<TransactionDto> transactionDtos) {
        // sort by timestamp and accounts
        transactionDtos.sort(Comparator.comparing(TransactionDto::timestamp)
                .thenComparing(TransactionDto::id));

        transactionDtos.stream().forEach(this::processTransaction);
    }
}
