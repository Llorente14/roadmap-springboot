package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Nickname urut abjad tanpa peduli huruf besar/kecil. ["nino", "Aces",
     * "YAZUKEE"] -> ["Aces", "nino", "YAZUKEE"]
     */
    public static List<String> sortedNicknames(List<String> nicknames) {
        List<String> list = new ArrayList<>(nicknames);
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;

    }

    /** Poin terbesar dulu. */
    public static List<Entry> byPointsDesc(List<Entry> entries) {
        List<Entry> list = new ArrayList<>(entries);
        list.sort(Comparator.comparingInt(Entry::points).reversed());
        return list;
    }

    /**
     * Poin terbesar dulu; poin sama -> kills terbesar dulu; masih sama -> nickname
     * abjad.
     */
    public static List<Entry> ranked(List<Entry> entries) {
        List<Entry> list = new ArrayList<>(entries);
        Comparator<Entry> filter1 = Comparator.comparingInt(Entry::points).reversed();
        Comparator<Entry> filter2 = Comparator.comparingInt(Entry::kills).reversed();
        Comparator<Entry> filter3 = filter1.thenComparing(filter2).thenComparing(Entry::nickname);
        list.sort(filter3);
        return list;
    }

    /** Entry teratas menurut ranked(). List kosong -> Optional kosong. */
    public static Optional<Entry> top(List<Entry> entries) {

        if (entries == null || entries.isEmpty()) {
            return Optional.empty();
        }
        List<Entry> list = ranked(entries);

        return Optional.of(list.get(0));
    }
}
