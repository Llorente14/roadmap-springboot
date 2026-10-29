package com.alterego.fondasi;

/**
 * F05 — Class, field, dan object.
 *
 * Satu Scoreboard mencatat skor ronde satu tim. Dua Scoreboard tidak boleh saling berbagi data.
 * Data harus tetap ada di antara pemanggilan method. Di mana data itu harus disimpan?
 *
 * Bagian 2 (buat sendiri, file baru): record MatchResult(String home, String away, int homeScore, int awayScore)
 *   dengan method winner(): nama tim yang skornya lebih besar, atau "DRAW" kalau sama.
 */
public class Scoreboard {

    /** Catat skor satu ronde. Skor negatif -> IllegalArgumentException. */
    public void add(int points) {
        throw new UnsupportedOperationException("TODO F05");
    }

    /** Jumlah semua skor yang sudah dicatat. */
    public int total() {
        throw new UnsupportedOperationException("TODO F05");
    }

    /** Berapa ronde yang sudah dicatat. */
    public int rounds() {
        throw new UnsupportedOperationException("TODO F05");
    }

    /** Skor ronde tertinggi; belum ada ronde -> 0. */
    public int best() {
        throw new UnsupportedOperationException("TODO F05");
    }

    /** Kosongkan semua catatan. */
    public void reset() {
        throw new UnsupportedOperationException("TODO F05");
    }
}
