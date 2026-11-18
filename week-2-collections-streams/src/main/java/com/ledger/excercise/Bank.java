package com.ledger.excercise;

import java.util.List;

public class Bank {
    private final Integer id;
    private final String name;

    public Bank(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Bank> all() {
        return List.of(
                new Bank(1, "SANTANDER"),
                new Bank(2, "CHILE"),
                new Bank(3, "ESTADO"));
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
