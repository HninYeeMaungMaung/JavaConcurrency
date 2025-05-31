package com.concurrency.Cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache<K, V> {
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

    public V get(K key, Callable<V> valueLoader) throws Exception {

        return cache.computeIfAbsent(key,  k -> {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                throw new RuntimeException(e); // wrap checked exception
            }
        });
    }
}
