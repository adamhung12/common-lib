package me.xethh.libs.toolkits.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class LRUCache<K,V> implements Cache<K,V> {
    private LRUMap<K,V> map;

    public static <K,V> LRUCache<K,V> of(int maxCache, Function<K, V> transform){
        return new LRUCache<K, V>(maxCache) {
            @Override
            public Function<K, V> getValueProvider() {
                return transform;
            }

        };
    }
    public static <K,V> LRUCache<K,V> of(LRUCache<K,V> cache){
        return cache;
    }
    public LRUCache(int maxCache) {
        map = new LRUMap(maxCache);
    }

    @Override
    public V getInternal(K key) {
        return map.get(key);
    }

    @Override
    public void cacheInternal(K key, V value) {
        map.put(key,value);
    }

    @Override
    public int size() {
        return map.size();
    }

    public LRUMap<K,V> map() {
        return map;
    }

    public static class LRUMap<K,V> extends LinkedHashMap<K,V> {
        private final int maxEntries;
        public static final int DEFAULT_INITIAL_CAPACITY = 16;
        public static final float DEFAULT_LOAD_FACTOR = 0.75f;

        public LRUMap(int initialCapacity,
                        float loadFactor,
                        int maxEntries) {
            super(initialCapacity, loadFactor, true);
            this.maxEntries = maxEntries;
        }

        public LRUMap(int initialCapacity,
                        int maxEntries) {
            this(Math.min(initialCapacity,maxEntries), DEFAULT_LOAD_FACTOR, maxEntries);
        }

        public LRUMap(int maxEntries) {
            this(Math.min(DEFAULT_INITIAL_CAPACITY,maxEntries), maxEntries);
        }

        // not very useful constructor
        public LRUMap(Map<? extends K, ? extends V> m,
                        int maxEntries) {
            this(m.size(), maxEntries);
            putAll(m);
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }

        public int getMaxEntries() {
            return maxEntries;
        }
    }

    @Override
    public String toString() {
        return "LRUCache{" +
                "map=" + map +
                '}';
    }
}
