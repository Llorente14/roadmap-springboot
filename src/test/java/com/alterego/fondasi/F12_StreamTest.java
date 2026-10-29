package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F12 · Stream dasar")
class F12_StreamTest {

    private static final Match M1 = new Match("Erangel", "W", 9);
    private static final Match M2 = new Match("Miramar", "L", 3);
    private static final Match M3 = new Match("Erangel", "W", 14);
    private static final Match M4 = new Match("Sanhok", "L", 6);
    private static final List<Match> ALL = List.of(M1, M2, M3, M4);

    @Test
    @DisplayName("wins menghitung result W")
    void wins() {
        assertThat(MatchStats.wins(ALL)).isEqualTo(2);
        assertThat(MatchStats.wins(List.of())).isZero();
    }

    @Test
    @DisplayName("totalKills dan averageKills")
    void kills() {
        assertThat(MatchStats.totalKills(ALL)).isEqualTo(32);
        assertThat(MatchStats.averageKills(ALL)).isCloseTo(8.0, within(0.001));
        assertThat(MatchStats.averageKills(List.of())).isEqualTo(0.0);
    }

    @Test
    @DisplayName("mapsPlayed unik dan urut abjad")
    void mapsPlayed() {
        assertThat(MatchStats.mapsPlayed(ALL)).containsExactly("Erangel", "Miramar", "Sanhok");
    }

    @Test
    @DisplayName("matchesPerMap memakai groupingBy")
    void perMap() {
        assertThat(MatchStats.matchesPerMap(ALL))
                .containsOnly(Map.entry("Erangel", 2L), Map.entry("Miramar", 1L), Map.entry("Sanhok", 1L));
    }

    @Test
    @DisplayName("bestGame: kills terbanyak atau Optional kosong")
    void bestGame() {
        assertThat(MatchStats.bestGame(ALL)).contains(M3);
        assertThat(MatchStats.bestGame(List.of())).isEmpty();
    }
}
