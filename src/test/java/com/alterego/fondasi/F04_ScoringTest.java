package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F04 · Membuat method sendiri")
class F04_ScoringTest {

    @Test
    @DisplayName("points(kills, assists) = kills x 2 + assists")
    void points() {
        assertThat(Reflect.callStatic("Scoring", "points", 5, 3)).isEqualTo(13);
        assertThat(Reflect.callStatic("Scoring", "points", 0, 0)).isEqualTo(0);
    }

    @Test
    @DisplayName("average dua angka mengembalikan double")
    void averageTwo() {
        assertThat(Reflect.callStatic("Scoring", "average", 3, 4)).isEqualTo(3.5);
    }

    @Test
    @DisplayName("average tiga angka (overloading)")
    void averageThree() {
        assertThat(Reflect.callStatic("Scoring", "average", 3, 4, 8)).isEqualTo(5.0);
        assertThat(Reflect.callStatic("Scoring", "average", 1, 1, 2)).isEqualTo(4.0 / 3);
    }

    @Test
    @DisplayName("label menggabungkan nickname dan poin")
    void label() {
        assertThat(Reflect.callStatic("Scoring", "label", "NINO", 12)).isEqualTo("NINO (12 pts)");
        assertThat(Reflect.callStatic("Scoring", "label", "ACES", 0)).isEqualTo("ACES (0 pts)");
    }
}
