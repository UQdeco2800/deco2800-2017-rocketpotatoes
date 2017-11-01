package com.deco2800.potatoes.util;

import java.util.Optional;

import static org.junit.Assert.*;

import org.junit.Test;

public class LRUCacheTest {

    @Test
    public void insertSomeThings() {
        int cap = 20;

        LRUCache<Integer, String> cache = new LRUCache<>(cap);
        for (int i = 0; i < cap; ++i) {
            cache.put(i, new Integer(i).toString());
        }

        for (int i = 0; i < cap; ++i) {
            assertEquals(Optional.of(new Integer(i).toString()), cache.get(i));
        }
    }

    @Test
    public void insertTooManyThings() {
        int cap = 20;
        int diff = 5;

        LRUCache<Integer, String> cache = new LRUCache<>(cap);
        for (int i = 0; i < cap + diff; ++i) {
            cache.put(i, new Integer(i).toString());
        }

        for (int i = 0; i < cap; ++i) {
            assertEquals(Optional.of(new Integer(i + diff).toString()), cache.get(i + diff));
        }

        for (int i = 0; i < diff; ++i) {
            assertEquals(Optional.empty(), cache.get(i));
        }
    }
}
