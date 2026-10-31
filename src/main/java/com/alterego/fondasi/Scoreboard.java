package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.List;

/**
 * F05 — Class, field, dan object.
 *
 * Satu Scoreboard mencatat skor ronde satu tim. Dua Scoreboard tidak boleh
 * saling berbagi data.
 * Data harus tetap ada di antara pemanggilan method. Di mana data itu harus
 * disimpan?
 *
 * Bagian 2 (buat sendiri, file baru): record MatchResult(String home, String
 * away, int homeScore, int awayScore)
 * dengan method winner(): nama tim yang skornya lebih besar, atau "DRAW" kalau
 * sama.
 */
public class Scoreboard {
    private final List<Integer> pointsTeam = new ArrayList<>();

    /** Catat skor satu ronde. Skor negatif -> IllegalArgumentException. */
    public void add(int points) {
        if (points < 0) {
            throw new IllegalArgumentException();
        }
        pointsTeam.add(points);
    }

    /** Jumlah semua skor yang sudah dicatat. */
    public int total() {
        int totalPoints = 0;
        for (Integer points : pointsTeam) {
            totalPoints += points;
        }
        return totalPoints;
    }

    /** Berapa ronde yang sudah dicatat. */
    public int rounds() {
        return pointsTeam.size();
    }

    /** Skor ronde tertinggi; belum ada ronde -> 0. */
    public int best() {
        int maxPoints = 0;

        for (Integer points : pointsTeam) {
            if (maxPoints < points) {
                maxPoints = points;
            }
        }
        return maxPoints;
    }

    /** Kosongkan semua catatan. */
    public void reset() {
        pointsTeam.removeAll(pointsTeam);
    }
}
