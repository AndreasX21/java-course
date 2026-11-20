package com.ledger.collections.list;

import java.util.List;
import java.util.stream.IntStream;

/**
 * LeetCode 412. Fizz Buzz. Return the words for 1 through {@code n}. A multiple of 3 and 5 is
 * {@code "FizzBuzz"}, a multiple of 3 is {@code "Fizz"}, a multiple of 5 is {@code "Buzz"}, and
 * anything else is the number as a string.
 */
public class FizzBuzzWithStreams {
    public List<String> fizzBuzz(int n) {
        return IntStream.rangeClosed(1, n)
                .mapToObj(i -> switch (i % 15) {
                case 0 -> "FizzBuzz";
                case 3, 6, 9, 12 -> "Fizz";
                case 5, 10 -> "Buzz";
                default -> Integer.toString(i);
                })
                .toList();
    }

    public static void main(String[] args) {
        System.out.println(new FizzBuzz().fizzBuzz(15));
    }
}
