package com.deco2800.potatoes.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Least recently used cache. Stores a finite number of key-value pairings, and deals with
 * overflows by evicting the last element to be used.
 */
public class LRUCache<Key, Value> {

    // max number of elements in the cache
    private final int capacity;
    private int size;

    // actual data
    private Map<Key, Value> data;

    // doubly linked list to keep track of which elements have been used most recently
    private int[] forwardLinks;
    private int[] backwardLinks;
    private int head;
    private int tail;

    // relation between the map and the doubly linked list
    Map<Key, Integer> left;
    Map<Integer, Key> right;

    // empty slots within the doubly linked list
    Set<Integer> free;

    /**
     * Create a new cache.
     *
     * @param capacity
     *      The number of elements that can be stored in the cache before it overflows.
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        size = 0;

        data = new HashMap<>();

        forwardLinks = new int[capacity];
        backwardLinks = new int[capacity];

        head = -1;
        tail = -1;

        left = new HashMap<>();
        right = new HashMap<>();

        free = new HashSet<>();
        for (int i = 0; i < capacity; ++i) {
            free.add(i);
        }
    }

    /**
     * Sets a key value pair within the cache. May have the side effect of removing an old
     * key-value pair.
     *
     * @param k
     *      The key being set.
     * @param v
     *      The value being set.
     */
    public void put(Key k, Value v) {
        if (data.containsKey(k)) {
            data.put(k, v);
            shuffleToFront(k);
            return;
        }

        if (size == 0) {
            size = 1;
            data.put(k, v);
            int position = free.iterator().next();
            free.remove(position);

            forwardLinks[position] = -1;
            backwardLinks[position] = -1;
            head = position;
            tail = position;
            left.put(k, position);
            right.put(position, k);
        } else if (size == capacity) {
            Key k2 = right.get(tail);

            data.remove(k2);
            data.put(k, v);

            right.put(tail, k);
            left.remove(k2);
            left.put(k, tail);
            shuffleToFront(k);
        } else {
            size += 1;
            int position = free.iterator().next();
            free.remove(position);

            left.put(k, position);
            right.put(position, k);

            data.put(k, v);

            forwardLinks[tail] = position;
            backwardLinks[position] = tail;
            tail = position;
            shuffleToFront(k);
        }
    }

    /**
     * Gets the value component of a key value pair within the cache.
     *
     * @param k
     *      The key being searched for.
     * @return
     *      The value associated with it. Optional.empty is returned if the key is not currently in
     *      the cache.
     */
    public Optional<Value> get(Key k) {
        if (data.containsKey(k)) {
            shuffleToFront(k);
            return Optional.of(data.get(k));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Shuffles a key to be the least recently accessed pair in the cache.
     *
     * @param k
     *      The key being suffled.
     */
    private void shuffleToFront(Key k) {
        int position = left.get(k);
        if (position == head) {
            // its already the least recently accessed pair
            return;
        }

        backwardLinks[head] = position;
        forwardLinks[position] = head;
        head = position;

        if (head == tail && size > 1) {
            tail = backwardLinks[tail];
        }
    }

    @Override
    public String toString() {
        String output = "LRU Cache: [";
        int position = head;
        while (position != tail) {
            output += "(" + right.get(position) + ", " + data.get(right.get(position)) + "),";
            position = forwardLinks[position];
        }
        return output + "]";
    }
}
