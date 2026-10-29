package com.alterego.warmup;

import java.time.LocalDate;

/**
 * Data prestasi versi latihan (Q03). Sudah jadi.
 * prizeUsd boleh null (di data asli ada turnamen tanpa hadiah tercatat).
 */
public record WarmupAchievement(String id, String game, String placement, LocalDate startDate, Double prizeUsd) {
}
