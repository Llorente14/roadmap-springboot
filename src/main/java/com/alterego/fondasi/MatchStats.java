package com.alterego.fondasi;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * F12 — Stream dasar.
 *
 * Aturan main: tulis tiap method dengan Stream API, tanpa for/while.
 */
public final class MatchStats {

    private MatchStats() {
    }

    /** Jumlah match dengan result "W". */
    public static long wins(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }

    /** Total kills semua match. */
    public static int totalKills(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }

    /** Rata-rata kills; list kosong -> 0.0. */
    public static double averageKills(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }

    /** Nama map yang pernah dimainkan, tanpa duplikat, urut abjad. */
    public static List<String> mapsPlayed(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }

    /** Jumlah match per map. */
    public static Map<String, Long> matchesPerMap(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }

    /** Match dengan kills terbanyak. List kosong -> Optional kosong. */
    public static Optional<Match> bestGame(List<Match> matches) {
        throw new UnsupportedOperationException("TODO F12");
    }
}
