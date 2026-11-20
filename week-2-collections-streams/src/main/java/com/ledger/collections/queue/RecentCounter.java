package com.ledger.collections.queue;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * LeetCode 933. Number of Recent Calls. {@code ping(t)} records a call at time {@code t} and
 * returns how many calls arrived in the window {@code [t - 3000, t]}.
 */
public class RecentCounter {
    private final Queue<Integer> calls = new ArrayDeque<>();

    public int ping(int t) {
        calls.add(t);
        while (calls.peek() < t - 3000) {
            calls.poll();
        }
        return calls.size();
    }

    public static void main(String[] args) {
        RecentCounter counter = new RecentCounter();
        System.out.println(counter.ping(1));
        System.out.println(counter.ping(100));
        System.out.println(counter.ping(3001));
        System.out.println(counter.ping(3002));
    }
}
