package com.alterego.fondasi;

import java.util.List;
import java.util.Map;

/**
 * F13 — Generics.
 *
 * Bagian 2 (buat sendiri, file baru): record generik Pair dengan dua type parameter
 *   (komponen: first, second) dan method swap() yang mengembalikan Pair dengan isi tertukar.
 */
public final class Boxes {

    private Boxes() {
    }

    /** Elemen pertama, atau fallback kalau list kosong. Berlaku untuk tipe apa pun. */
    public static <T> T firstOr(List<T> list, T fallback) {
        throw new UnsupportedOperationException("TODO F13");
    }

    /** List berisi item sebanyak times. repeat("GG", 3) -> ["GG", "GG", "GG"] */
    public static <T> List<T> repeat(T item, int times) {
        throw new UnsupportedOperationException("TODO F13");
    }

    /** Tukar kunci dan nilai. {nino=EXP} -> {EXP=nino}. Anggap tidak ada nilai yang kembar. */
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        throw new UnsupportedOperationException("TODO F13");
    }
}
