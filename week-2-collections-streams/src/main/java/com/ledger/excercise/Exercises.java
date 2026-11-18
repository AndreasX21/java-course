package com.ledger.excercise;

import java.util.List;
import java.util.Map;

public class Exercises {
    private final List<Client> clients;
    private final List<Account> accounts;
    private final List<Bank> banks;

    public Exercises(List<Client> clients, List<Account> accounts, List<Bank> banks) {
        this.clients = clients;
        this.accounts = accounts;
        this.banks = banks;
    }

    /**
     * Client ids, in the same order as {@link Client#all()}.
     */
    public Integer[] listClientIds() {
        return clients.stream().map(Client::getId).toArray(Integer[]::new);
    }

    /**
     * Client ids sorted by tax number.
     */
    public Integer[] listClientsIdsSortByTaxNumber() {
        return new Integer[] {};
    }

    /**
     * Client names sorted from highest to lowest by the total balance of that
     * client across every bank.
     */
    public String[] sortClientsTotalBalances() {
        return new String[] {};
    }

    /**
     * Bank name to the tax numbers of that bank's clients, sorted alphabetically
     * by client name.
     */
    public Map<String, String[]> banksClientsTaxNumbers() {
        return Map.of();
    }

    /**
     * Balances of clients with more than 25_000 in the bank named SANTANDER,
     * sorted from highest to lowest.
     */
    public Integer[] richClientsBalances() {
        return new Integer[] {};
    }

    /**
     * Bank ids sorted from lowest to highest by the total balance the bank holds.
     */
    public Integer[] banksRankingByTotalBalance() {
        return new Integer[] {};
    }

    /**
     * Bank name to the number of clients who have accounts only in that bank.
     */
    public Map<String, Integer> banksFidelity() {
        return Map.of();
    }

    /**
     * Bank name to the id of the client with the least money in that bank.
     */
    public Map<String, Integer> banksPoorClients() {
        return Map.of();
    }

    /**
     * Add a fictional client and one account at the bank named ESTADO with
     * balance 9000. Do not modify {@code clients} or {@code accounts}. Return
     * the place this client occupies in the ranking from
     * {@link #sortClientsTotalBalances()}.
     */
    public int newClientRanking() {
        return -1;
    }
}
