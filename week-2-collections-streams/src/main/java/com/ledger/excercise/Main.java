package com.ledger.excercise;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Exercises exercises = new Exercises(Client.all(), Account.all(), Bank.all());

        System.out.println("Question 0");
        System.out.println(Arrays.toString(exercises.listClientIds()));

        System.out.println("Question 1");
        System.out.println(Arrays.toString(exercises.listClientsIdsSortByTaxNumber()));

        System.out.println("Question 2");
        System.out.println(Arrays.toString(exercises.sortClientsTotalBalances()));

        System.out.println("Question 3");
        Map<String, String[]> banksClients = exercises.banksClientsTaxNumbers();
        System.out.println(banksClients.keySet().stream()
                .map(key -> key + "=" + Arrays.toString(banksClients.get(key)))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 4");
        System.out.println(Arrays.toString(exercises.richClientsBalances()));

        System.out.println("Question 5");
        System.out.println(Arrays.toString(exercises.banksRankingByTotalBalance()));

        System.out.println("Question 6");
        Map<String, Integer> fidelity = exercises.banksFidelity();
        System.out.println(fidelity.keySet().stream()
                .map(key -> key + "=" + fidelity.get(key))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 7");
        Map<String, Integer> poorClients = exercises.banksPoorClients();
        System.out.println(poorClients.keySet().stream()
                .map(key -> key + "=" + poorClients.get(key))
                .collect(Collectors.joining(", ", "{", "}")));

        System.out.println("Question 8");
        System.out.println(exercises.newClientRanking());
    }
}
