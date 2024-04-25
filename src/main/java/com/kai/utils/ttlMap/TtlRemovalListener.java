package com.kai.utils.ttlMap;

@FunctionalInterface
public interface TtlRemovalListener<K, V> {
    void onRemoval(K key, V v);
}
