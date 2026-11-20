package com.ledger.collections.map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 1. Two Sum. Return the indexes of the two numbers in {@code nums} that add up to
 * {@code target}. There is one answer, and the same index cannot be used twice.
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[] { seen.get(complement), i };
            }
            seen.put(nums[i], i);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new TwoSum().twoSum(new int[] { 2, 7, 11, 15 }, 9)));
        System.out.println(Arrays.toString(new TwoSum().twoSum(new int[] { 3, 3 }, 6)));
    }
}
