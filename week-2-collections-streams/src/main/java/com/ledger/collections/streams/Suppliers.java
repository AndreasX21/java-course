package com.ledger.collections.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@code Supplier<T>} takes no argument and returns a value.
 */
public class Suppliers {
    public static void main(String[] args) {
        // input: nothing. output: String.
        Supplier<String> emptyWord = () -> "empty";

        System.out.println(Optional.empty().orElseGet(emptyWord)); // called, because the Optional is empty
        System.out.println(Optional.of("present").orElseGet(emptyWord)); // not called, a value is already present

        // input: nothing. output: a new ArrayList. toCollection fills that list.
        Supplier<ArrayList<String>> newList = ArrayList::new;
        List<String> words = List.of("a", "bb").stream().collect(Collectors.toCollection(newList));
        System.out.println(words);
    }
}
