package com.ledger.excercise;

import java.util.List;

public class Client {
    private final Integer id;
    private final String taxNumber;
    private final String name;

    public Client(Integer id, String taxNumber, String name) {
        this.id = id;
        this.taxNumber = taxNumber;
        this.name = name;
    }

    public static List<Client> all() {
        return List.of(
                new Client(1, "86620855", "HECTOR ACUÑA BOLAÑOS"),
                new Client(2, "7317855K", "JESUS RODRIGUEZ ALVAREZ"),
                new Client(3, "73826497", "ANDRES NADAL MOLINA"),
                new Client(4, "88587715", "SALVADOR ARNEDO MANRIQUEZ"),
                new Client(5, "94020190", "VICTOR MANUEL ROJAS LUCAS"),
                new Client(6, "99804238", "MOHAMED FERRE SAMPER"));
    }

    public Integer getId() {
        return id;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getName() {
        return name;
    }
}
