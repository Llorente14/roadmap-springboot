package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F10 · Sorting")
class F10_SortingTest {

    private static final Entry NINO = new Entry("NINO", 30, 12);
    private static final Entry ACES = new Entry("ACES", 45, 8);
    private static final Entry KIBOY = new Entry("KIBOY", 30, 15);
    private static final Entry ALDO = new Entry("ALDO", 30, 15);

    @Test
    @DisplayName("sortedNicknames abjad tanpa peduli huruf besar/kecil; list asli tetap")
    void sortedNicknames() {
        List<String> input = new ArrayList<>(List.of("nino", "Aces", "YAZUKEE", "kiboy"));
        assertThat(Leaderboard.sortedNicknames(input)).containsExactly("Aces", "kiboy", "nino", "YAZUKEE");
        assertThat(input).containsExactly("nino", "Aces", "YAZUKEE", "kiboy");
    }

    @Test
    @DisplayName("byPointsDesc: poin terbesar dulu; list asli tetap")
    void byPointsDesc() {
        List<Entry> input = new ArrayList<>(List.of(NINO, ACES));
        assertThat(Leaderboard.byPointsDesc(input)).containsExactly(ACES, NINO);
        assertThat(input).containsExactly(NINO, ACES);
    }

    @Test
    @DisplayName("ranked: poin desc, lalu kills desc, lalu nickname abjad")
    void ranked() {
        assertThat(Leaderboard.ranked(List.of(NINO, KIBOY, ACES, ALDO)))
                .extracting(Entry::nickname)
                .containsExactly("ACES", "ALDO", "KIBOY", "NINO");
    }

    @Test
    @DisplayName("top: teratas atau Optional kosong")
    void top() {
        assertThat(Leaderboard.top(List.of(NINO, KIBOY))).contains(KIBOY);
        assertThat(Leaderboard.top(List.of())).isEmpty();
    }
}
