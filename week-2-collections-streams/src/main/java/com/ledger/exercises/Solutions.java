package com.ledger.exercises;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solutions {
    private final List<Client> clients;
    private final List<Account> accounts;
    private final List<Bank> banks;

    public Solutions(List<Client> clients, List<Account> accounts, List<Bank> banks) {
        this.clients = clients;
        this.accounts = accounts;
        this.banks = banks;
    }

    /**
     * 0. Client ids, in the same order as {@link Client#all()}.
     */
    public Integer[] listClientIds() {
        return clients.stream().map(Client::getId).toArray(Integer[]::new);
    }

    /**
     * 1. Client ids sorted by tax number.
     */
    public Integer[] listClientsIdsSortByTaxNumber() {
        return clients.stream()
                .sorted(Comparator.comparing(Client::getTaxNumber))
                .map(Client::getId)
                .toArray(Integer[]::new);
    }

    /**
     * 2. Client names sorted from highest to lowest by the total balance of that
     * client across every bank.
     */
    public String[] sortClientsTotalBalances() {
        return namesByTotalBalance(clients, accounts);
    }

    /**
     * 3. Bank name to the tax numbers of that bank's clients, sorted alphabetically
     * by client name.
     */
    public Map<String, String[]> banksClientsTaxNumbers() {
        Map<Integer, Client> clientById = clients.stream()
                .collect(Collectors.toMap(Client::getId, client -> client));
        return banks.stream().collect(Collectors.toMap(Bank::getName, bank -> accounts.stream()
                .filter(account -> account.getBankId().equals(bank.getId()))
                .map(account -> clientById.get(account.getClientId()))
                .distinct()
                .sorted(Comparator.comparing(Client::getName))
                .map(Client::getTaxNumber)
                .toArray(String[]::new)));
    }

    /**
     * 4. Balances of clients with more than 25_000 in the bank named SANTANDER,
     * sorted from highest to lowest. A client's balance at that bank is the sum
     * of their accounts there.
     */
    public Integer[] richClientsBalances() {
        Integer santanderId = bankId("SANTANDER");
        return accounts.stream()
                .filter(account -> account.getBankId().equals(santanderId))
                .collect(Collectors.groupingBy(Account::getClientId, Collectors.summingInt(Account::getBalance)))
                .values().stream()
                .filter(total -> total > 25_000)
                .sorted(Comparator.reverseOrder())
                .toArray(Integer[]::new);
    }

    /**
     * 5. Bank ids sorted from lowest to highest by the total balance the bank holds.
     */
    public Integer[] banksRankingByTotalBalance() {
        Map<Integer, Integer> totalByBank = totalsBy(Account::getBankId);
        return banks.stream()
                .sorted(Comparator.comparing(bank -> totalByBank.getOrDefault(bank.getId(), 0)))
                .map(Bank::getId)
                .toArray(Integer[]::new);
    }

    /**
     * 6. Bank name to the number of clients who have accounts only in that bank.
     */
    public Map<String, Integer> banksFidelity() {
        Map<Integer, Set<Integer>> banksByClient = accounts.stream()
                .collect(Collectors.groupingBy(
                        Account::getClientId,
                        Collectors.mapping(Account::getBankId, Collectors.toSet())));
        return banks.stream().collect(Collectors.toMap(Bank::getName, bank -> (int) banksByClient.values().stream()
                .filter(bankIds -> bankIds.size() == 1 && bankIds.contains(bank.getId()))
                .count()));
    }

    /**
     * 7. Bank name to the id of the client with the least money in that bank. A
     * client's money in a bank is the sum of their accounts there.
     */
    public Map<String, Integer> banksPoorClients() {
        return banks.stream().collect(Collectors.toMap(Bank::getName, bank -> accounts.stream()
                .filter(account -> account.getBankId().equals(bank.getId()))
                .collect(Collectors.groupingBy(Account::getClientId, Collectors.summingInt(Account::getBalance)))
                .entrySet().stream()
                .min(Comparator.comparingInt(Map.Entry<Integer, Integer>::getValue)
                        .thenComparingInt(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .orElseThrow()));
    }

    /**
     * 8. Add a fictional client and one account at the bank named ESTADO with
     * balance 9000. Do not modify {@code clients} or {@code accounts}. Return
     * the place this client occupies in the ranking from
     * {@link #sortClientsTotalBalances()}.
     */
    public int newClientRanking() {
        Client extra = new Client(7, "11111111", "ANA NUEVA");
        List<Client> withExtra = new ArrayList<>(clients);
        withExtra.add(extra);
        List<Account> withAccount = new ArrayList<>(accounts);
        withAccount.add(new Account(extra.getId(), bankId("ESTADO"), 9000));

        String[] ranking = namesByTotalBalance(withExtra, withAccount);
        return Arrays.asList(ranking).indexOf(extra.getName()) + 1;
    }

    private Integer bankId(String name) {
        return banks.stream()
                .filter(bank -> bank.getName().equals(name))
                .map(Bank::getId)
                .findFirst()
                .orElseThrow();
    }

    private Map<Integer, Integer> totalsBy(Function<Account, Integer> key) {
        return accounts.stream()
                .collect(Collectors.groupingBy(key, Collectors.summingInt(Account::getBalance)));
    }

    private static String[] namesByTotalBalance(List<Client> clients, List<Account> accounts) {
        Map<Integer, Integer> totalByClient = accounts.stream()
                .collect(Collectors.groupingBy(Account::getClientId, Collectors.summingInt(Account::getBalance)));
        return clients.stream()
                .sorted(Comparator.<Client>comparingInt(client -> totalByClient.getOrDefault(client.getId(), 0))
                        .reversed()
                        .thenComparing(Client::getName))
                .map(Client::getName)
                .toArray(String[]::new);
    }

    public static void main(String[] args) {
        Solutions solutions = new Solutions(Client.all(), Account.all(), Bank.all());

        System.out.println("Question 0");
        System.out.println(Arrays.toString(solutions.listClientIds()));

        System.out.println("Question 1");
        System.out.println(Arrays.toString(solutions.listClientsIdsSortByTaxNumber()));

        System.out.println("Question 2");
        System.out.println(Arrays.toString(solutions.sortClientsTotalBalances()));

        System.out.println("Question 3");
        Map<String, String[]> banksClients = solutions.banksClientsTaxNumbers();
        System.out.println(banksClients.keySet().stream()
                .map(key -> key + "=" + Arrays.toString(banksClients.get(key)))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 4");
        System.out.println(Arrays.toString(solutions.richClientsBalances()));

        System.out.println("Question 5");
        System.out.println(Arrays.toString(solutions.banksRankingByTotalBalance()));

        System.out.println("Question 6");
        Map<String, Integer> fidelity = solutions.banksFidelity();
        System.out.println(fidelity.keySet().stream()
                .map(key -> key + "=" + fidelity.get(key))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 7");
        Map<String, Integer> poorClients = solutions.banksPoorClients();
        System.out.println(poorClients.keySet().stream()
                .map(key -> key + "=" + poorClients.get(key))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 8");
        System.out.println(solutions.newClientRanking());
    }
}
