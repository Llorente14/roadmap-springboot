package com.alterego.fondasi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * F08 — null dan Optional.
 *
 * Aturan: method yang mengembalikan Optional TIDAK PERNAH mengembalikan null.
 */
public final class Finder {

    private Finder() {
    }

    /** Nama pertama yang diawali prefix. Tidak ada -> Optional kosong. */
    public static Optional<String> firstWithPrefix(List<String> names, String prefix) {
        throw new UnsupportedOperationException("TODO F08");
    }

    /** Tim seorang pemain dari map pemain -> tim. Pemain tidak ada -> Optional kosong. */
    public static Optional<String> teamOf(Map<String, String> playerTeams, String player) {
        throw new UnsupportedOperationException("TODO F08");
    }

    /** Isi Optional, atau "UNKNOWN" kalau kosong. */
    public static String orUnknown(Optional<String> nickname) {
        throw new UnsupportedOperationException("TODO F08");
    }

    /** Panjang nickname di dalam Optional; kosong -> 0. Coba tanpa if. */
    public static int lengthOf(Optional<String> nickname) {
        throw new UnsupportedOperationException("TODO F08");
    }
}
