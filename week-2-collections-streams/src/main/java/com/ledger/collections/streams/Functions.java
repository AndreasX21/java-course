package com.ledger.collections.streams;

import java.util.List;
import java.util.function.Function;

/**
 * {@code Function<T, R>} takes one value and returns a value of another type.
 */
public class Functions {
    public static void main(String[] args) {
        // input: one String. output: Integer.
        Function<String, Integer> getLength = String::length;

        List<Integer> lengths = List.of("a", "bb", "ccc").stream().map(getLength).toList();
        System.out.println(lengths);
    }
}
