package com.ledger.transaction;

import java.util.List;

public interface ITransactionProcessor {
    default void processTransaction(TransactionDto transactionDto) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default void processTransactions(List<TransactionDto> transactionDtos) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
