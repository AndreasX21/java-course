package com.ledger.collections.streams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * {@code Comparator<T>} takes two values and returns an int.
 */
public class Comparators {
    public static void main(String[] args) {
        // input: two Strings. output: int. negative means the first is smaller.
        Comparator<String> byLength = Comparator.comparing(String::length);

        List<String> words = new ArrayList<>(List.of("ccc", "a", "bb"));
        words.sort(byLength);
        System.out.println(words);
    }
}
