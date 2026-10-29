package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F06 · Array dan List")
class F06_LineupTest {

    @Test
    @DisplayName("add menyimpan urutan masuk")
    void addKeepsOrder() {
        Lineup lineup = new Lineup();
        assertThat(lineup.add("NINO")).isTrue();
        assertThat(lineup.add("YAZUKEE")).isTrue();

        assertThat(lineup.size()).isEqualTo(2);
        assertThat(lineup.get(0)).isEqualTo("NINO");
        assertThat(lineup.get(1)).isEqualTo("YAZUKEE");
    }

    @Test
    @DisplayName("duplikat ditolak")
    void noDuplicates() {
        Lineup lineup = new Lineup();
        lineup.add("NINO");
        assertThat(lineup.add("NINO")).isFalse();
        assertThat(lineup.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("maksimal 5 pemain")
    void maxFive() {
        Lineup lineup = new Lineup();
        for (String p : List.of("A1", "A2", "A3", "A4", "A5")) {
            assertThat(lineup.add(p)).isTrue();
        }
        assertThat(lineup.add("A6")).isFalse();
        assertThat(lineup.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("remove menghapus dan menggeser urutan")
    void remove() {
        Lineup lineup = new Lineup();
        lineup.add("NINO");
        lineup.add("ACES");
        lineup.add("YAZUKEE");

        assertThat(lineup.remove("ACES")).isTrue();
        assertThat(lineup.remove("GHOST")).isFalse();
        assertThat(lineup.players()).containsExactly("NINO", "YAZUKEE");
    }

    @Test
    @DisplayName("players() tidak bisa dipakai untuk mengubah lineup dari luar")
    void defensiveCopy() {
        Lineup lineup = new Lineup();
        lineup.add("NINO");
        lineup.add("ACES");

        List<String> outside = lineup.players();
        catchThrowable(outside::clear); // boleh melempar exception, boleh juga tidak
        assertThat(lineup.size()).isEqualTo(2);
        assertThat(lineup.players()).containsExactly("NINO", "ACES");
    }

    @Test
    @DisplayName("maxScore pada array")
    void maxScore() {
        assertThat(Lineup.maxScore(new int[] {3, 9, 4})).isEqualTo(9);
        assertThat(Lineup.maxScore(new int[] {-5, -2})).isEqualTo(-2);
        assertThat(Lineup.maxScore(new int[] {})).isZero();
    }

    @Test
    @DisplayName("longNames menyaring dan menjaga urutan, list asli tidak berubah")
    void longNames() {
        List<String> names = new ArrayList<>(List.of("NINO", "YAZUKEE", "ACES", "KIBOY"));
        assertThat(Lineup.longNames(names, 5)).containsExactly("YAZUKEE", "KIBOY");
        assertThat(Lineup.longNames(names, 20)).isEmpty();
        assertThat(names).containsExactly("NINO", "YAZUKEE", "ACES", "KIBOY");
    }
}
