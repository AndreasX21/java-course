package com.ledger.collections.streams;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * {@code BinaryOperator<T>} takes two values and returns a value of the same type.
 */
public class BinaryOperators {
    public static void main(String[] args) {
        // input: two Integers. output: Integer.
        BinaryOperator<Integer> sum = Integer::sum;

        int total = List.of(1, 2, 3).stream().reduce(0, sum);
        System.out.println(total);
    }
}
