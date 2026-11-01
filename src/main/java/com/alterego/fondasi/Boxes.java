package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * F13 — Generics.
 *
 * Bagian 2 (buat sendiri, file baru): record generik Pair dengan dua type
 * parameter
 * (komponen: first, second) dan method swap() yang mengembalikan Pair dengan
 * isi tertukar.
 */
public final class Boxes {

    private Boxes() {
    }

    /**
     * Elemen pertama, atau fallback kalau list kosong. Berlaku untuk tipe apa pun.
     */
    public static <T> T firstOr(List<T> list, T fallback) {
        if (list.isEmpty()) {
            return fallback;
        }
        return list.get(0);
    }

    /** List berisi item sebanyak times. repeat("GG", 3) -> ["GG", "GG", "GG"] */
    public static <T> List<T> repeat(T item, int times) {
        List<T> lists = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            lists.add(item);
        }
        return lists;
    }

    /**
     * Tukar kunci dan nilai. {nino=EXP} -> {EXP=nino}. Anggap tidak ada nilai yang
     * kembar.
     */
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        Map<V, K> listBaru = new HashMap<>();
        for (Map.Entry<K, V> item : map.entrySet()) {
            listBaru.put(item.getValue(), item.getKey());

        }
        return listBaru;
    }
}
