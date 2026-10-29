package com.alterego.warmup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Quest Q03 — Lambda & Stream.
 *
 * Aturan main: tulis tiap method dengan Stream API, tanpa for/while.
 */
public final class AchievementMath {

    private AchievementMath() {
    }

    /** Jumlah prestasi dengan placement persis sama (mis. "1"). */
    public static long countPlacement(List<WarmupAchievement> list, String placement) {
        throw new UnsupportedOperationException("TODO Q03");
    }

    /** Total hadiah USD. prizeUsd null dilewati. */
    public static double totalPrize(List<WarmupAchievement> list) {
        throw new UnsupportedOperationException("TODO Q03");
    }

    /** Jumlah prestasi per tahun startDate. */
    public static Map<Integer, Long> countByYear(List<WarmupAchievement> list) {
        throw new UnsupportedOperationException("TODO Q03");
    }

    /** Prestasi dengan hadiah terbesar. List kosong / semua null = Optional kosong. */
    public static Optional<WarmupAchievement> biggestPrize(List<WarmupAchievement> list) {
        throw new UnsupportedOperationException("TODO Q03");
    }
}
