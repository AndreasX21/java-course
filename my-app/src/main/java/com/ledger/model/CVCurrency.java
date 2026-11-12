package com.ledger.model;

public enum CVCurrency {
    USD(1, "United States Dollar"),
    EUR(2, "Euro"),
    CLP(3, "Chilean Peso");

    private int id;
    private String name;

    CVCurrency(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
