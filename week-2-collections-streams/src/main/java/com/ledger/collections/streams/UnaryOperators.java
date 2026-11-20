package com.ledger.collections.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * {@code UnaryOperator<T>} takes one value and returns a value of the same type.
 */
public class UnaryOperators {
    public static void main(String[] args) {
        // input: one String. output: String.
        UnaryOperator<String> toUpper = String::toUpperCase;

        List<String> words = new ArrayList<>(List.of("a", "bb"));
        words.replaceAll(toUpper);
        System.out.println(words);
    }
}
