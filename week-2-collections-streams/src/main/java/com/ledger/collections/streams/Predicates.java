package com.ledger.collections.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * {@code Predicate<T>} takes one value and returns a boolean.
 */
public class Predicates {
    public static void main(String[] args) {
        // input: one String. output: boolean.
        Predicate<String> isA = s -> s.equals("a");

        List<String> words = new ArrayList<>(List.of("a", "bb", "a"));
        words.removeIf(isA); // true drops the element
        System.out.println(words);

        List<String> onlyA = List.of("a", "bb", "a").stream().filter(isA).toList(); // true keeps the element
        System.out.println(onlyA);
    }
}
