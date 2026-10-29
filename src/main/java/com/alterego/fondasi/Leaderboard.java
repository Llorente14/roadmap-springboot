package com.alterego.fondasi;

import java.util.List;
import java.util.Optional;

/**
 * F10 — Sorting dengan Comparable dan Comparator.
 *
 * Aturan: list masukan TIDAK boleh berubah urutannya. Kembalikan list baru.
 */
public final class Leaderboard {

    private Leaderboard() {
    }

    /** Nickname urut abjad tanpa peduli huruf besar/kecil. ["nino", "Aces", "YAZUKEE"] -> ["Aces", "nino", "YAZUKEE"] */
    public static List<String> sortedNicknames(List<String> nicknames) {
        throw new UnsupportedOperationException("TODO F10");
    }

    /** Poin terbesar dulu. */
    public static List<Entry> byPointsDesc(List<Entry> entries) {
        throw new UnsupportedOperationException("TODO F10");
    }

    /** Poin terbesar dulu; poin sama -> kills terbesar dulu; masih sama -> nickname abjad. */
    public static List<Entry> ranked(List<Entry> entries) {
        throw new UnsupportedOperationException("TODO F10");
    }

    /** Entry teratas menurut ranked(). List kosong -> Optional kosong. */
    public static Optional<Entry> top(List<Entry> entries) {
        throw new UnsupportedOperationException("TODO F10");
    }
}
