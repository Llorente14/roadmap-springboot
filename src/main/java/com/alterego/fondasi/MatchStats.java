package com.alterego.fondasi;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return matches.stream().filter(b -> b.result().equals("W")).count();
    }

    /** Total kills semua match. */
    public static int totalKills(List<Match> matches) {
        return matches.stream().mapToInt(Match::kills).sum();
    }

    /** Rata-rata kills; list kosong -> 0.0. */
    public static double averageKills(List<Match> matches) {
        return matches.stream().mapToDouble(Match::kills).average().orElse(0.0);
    }

    /** Nama map yang pernah dimainkan, tanpa duplikat, urut abjad. */
    public static List<String> mapsPlayed(List<Match> matches) {
        return matches.stream().map(Match::map).distinct().sorted().toList();
    }

    /** Jumlah match per map. */
    public static Map<String, Long> matchesPerMap(List<Match> matches) {
        return matches.stream().collect(Collectors.groupingBy(Match::map, Collectors.counting()));
    }

    /** Match dengan kills terbanyak. List kosong -> Optional kosong. */
    public static Optional<Match> bestGame(List<Match> matches) {
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        return matches.stream().max(Comparator.comparingInt(Match::kills));
    }
}
