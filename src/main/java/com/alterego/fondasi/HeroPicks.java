package com.alterego.fondasi;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * F07 — Map dan Set.
 */
public final class HeroPicks {

    private HeroPicks() {
    }

    /** Hitung berapa kali tiap hero dipilih. ["Fanny", "Ling", "Fanny"] -> {Fanny=2, Ling=1} */
    public static Map<String, Integer> countPicks(List<String> picks) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Hero tanpa duplikat. */
    public static Set<String> uniqueHeroes(List<String> picks) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Jumlah pick seorang hero dari hasil countPicks. Hero tidak ada -> 0. */
    public static int picksOf(Map<String, Integer> counts, String hero) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Hero yang paling sering dipilih. Seri -> yang abjadnya paling awal. List kosong -> "". */
    public static String mostPicked(List<String> picks) {
        throw new UnsupportedOperationException("TODO F07");
    }

    /** Hero yang dipilih di KEDUA list (irisan). */
    public static Set<String> pickedByBoth(List<String> teamA, List<String> teamB) {
        throw new UnsupportedOperationException("TODO F07");
    }
}
