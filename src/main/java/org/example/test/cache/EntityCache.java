package org.example.test.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class EntityCache<K, V> {
    Map<K, V> cacheMap = new ConcurrentHashMap<>();

    private static final int MAX_SIZE = 100;

    public void put(K key, V value) {
        cacheMap.put(key, value);
        if (cacheMap.size() >= MAX_SIZE) {
            cacheMap.clear();
        }
    }

    public V get(K key) {
        return cacheMap.get(key);
    }

    public void clear() {
        cacheMap.clear();
    }
}
