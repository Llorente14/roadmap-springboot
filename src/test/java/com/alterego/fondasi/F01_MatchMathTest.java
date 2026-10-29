package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F01 · Variabel, tipe data, operator")
class F01_MatchMathTest {

    @Test
    @DisplayName("winRate dalam persen, termasuk pecahan")
    void winRate() {
        assertThat(MatchMath.winRate(3, 4)).isEqualTo(75.0);
        assertThat(MatchMath.winRate(1, 3)).isCloseTo(33.333, within(0.01));
        assertThat(MatchMath.winRate(0, 5)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("winRate dengan 0 game -> 0.0, bukan error")
    void winRateNoGames() {
        assertThat(MatchMath.winRate(0, 0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("kda memakai pembagian desimal, deaths 0 dianggap 1")
    void kda() {
        assertThat(MatchMath.kda(10, 2, 4)).isEqualTo(7.0);
        assertThat(MatchMath.kda(5, 2, 0)).as("5 / 2 harus 2.5, bukan 2").isEqualTo(2.5);
        assertThat(MatchMath.kda(3, 0, 1)).isEqualTo(4.0);
    }

    @Test
    @DisplayName("isEven memakai sisa bagi")
    void isEven() {
        assertThat(MatchMath.isEven(4)).isTrue();
        assertThat(MatchMath.isEven(7)).isFalse();
        assertThat(MatchMath.isEven(0)).isTrue();
        assertThat(MatchMath.isEven(-2)).isTrue();
    }

    @Test
    @DisplayName("minutes dan seconds dari total detik")
    void duration() {
        assertThat(MatchMath.minutes(725)).isEqualTo(12);
        assertThat(MatchMath.seconds(725)).isEqualTo(5);
        assertThat(MatchMath.minutes(59)).isZero();
        assertThat(MatchMath.seconds(120)).isZero();
    }
}
