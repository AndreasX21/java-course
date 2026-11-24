package com.ledger.collections.streams;

import java.util.List;
import java.util.function.Consumer;

/**
 * {@code Consumer<T>} takes one value and returns nothing.
 */
public class Consumers {
    public static void main(String[] args) {
        // input: one String. output: nothing.
        Consumer<String> print = System.out::println;

        print.accept("a");

        List.of("a", "bb").forEach(print);
    }
}
