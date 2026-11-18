package com.ledger.app;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        Map<UUID, Transaction> transactions = new HashMap<>();

        var tx = new Transaction(UUID.randomUUID(), "Test Transaction", 100.00);

        transactions.put(tx.getId(), tx);

        System.out.println(transactions.get(tx.getId()).getDescription());

    }
}

class Transaction {
    private UUID id;
    private String description;
    private double amount;

    public Transaction(UUID id, String description, double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
