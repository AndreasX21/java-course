package com.ledger.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        UUID senderAccountId,
        UUID receiverAccountId,
        BigDecimal amount,
        LocalDateTime timestamp) {

    @Override
    public String toString() {
        return "TransactionDto [id=" + id.toString().substring(0, 8) + ", senderAccountId="
                + senderAccountId.toString().substring(0, 8) + ", receiverAccountId="
                + receiverAccountId.toString().substring(0, 8) + ", amount=" + amount
                + ", timestamp="
                + timestamp + "]";
    }
}
