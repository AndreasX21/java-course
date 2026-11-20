package com.ledger.collections.list;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 412. Fizz Buzz. Return the words for 1 through {@code n}. A multiple of 3 and 5 is
 * {@code "FizzBuzz"}, a multiple of 3 is {@code "Fizz"}, a multiple of 5 is {@code "Buzz"}, and
 * anything else is the number as a string.
 */
public class FizzBuzz {
    public List<String> fizzBuzz(int n) {
        List<String> answer = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                answer.add("FizzBuzz");
            } else if (i % 3 == 0) {
                answer.add("Fizz");
            } else if (i % 5 == 0) {
                answer.add("Buzz");
            } else {
                answer.add(Integer.toString(i));
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println(new FizzBuzz().fizzBuzz(15));
    }
}
