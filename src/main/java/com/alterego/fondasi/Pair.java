package com.alterego.fondasi;

/**
 * Pair
 */
public record Pair<K, V>(K first, V second) {

    public Pair<V, K> swap() {
        return new Pair<V, K>(second, first);
    }
}