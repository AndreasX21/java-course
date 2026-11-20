package com.ledger.collections.set;

import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 217. Contains Duplicate. Return {@code true} when any value in {@code nums} appears at
 * least twice.
 */
public class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int n : nums) {
            if (!seen.add(n)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ContainsDuplicate solution = new ContainsDuplicate();
        System.out.println(solution.containsDuplicate(new int[] { 1, 2, 3, 1 }));
        System.out.println(solution.containsDuplicate(new int[] { 1, 2, 3, 4 }));
    }
}
