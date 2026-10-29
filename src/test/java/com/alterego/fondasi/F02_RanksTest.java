package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("F02 · if/else, loop, switch")
class F02_RanksTest {

    @ParameterizedTest(name = "{0} poin -> {1}")
    @CsvSource({
            "0,Warrior", "999,Warrior", "1000,Elite", "1999,Elite", "2000,Master",
            "3500,Epic", "3999,Epic", "4000,Legend", "9000,Legend"
    })
    @DisplayName("tierOf memeriksa batas dengan benar")
    void tierOf(int points, String tier) {
        assertThat(Ranks.tierOf(points)).isEqualTo(tier);
    }

    @Test
    @DisplayName("sumUpTo menjumlahkan 1..n")
    void sumUpTo() {
        assertThat(Ranks.sumUpTo(4)).isEqualTo(10);
        assertThat(Ranks.sumUpTo(1)).isEqualTo(1);
        assertThat(Ranks.sumUpTo(100)).isEqualTo(5050);
        assertThat(Ranks.sumUpTo(0)).isZero();
        assertThat(Ranks.sumUpTo(-3)).isZero();
    }

    @Test
    @DisplayName("countdown membangun teks dengan loop")
    void countdown() {
        assertThat(Ranks.countdown(3)).isEqualTo("3 2 1 GO");
        assertThat(Ranks.countdown(1)).isEqualTo("1 GO");
        assertThat(Ranks.countdown(0)).isEqualTo("GO");
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource({
            "Jungler,JG", "Roamer,RM", "Gold Laner,GL", "EXP Laner,EXP", "Mid Laner,MID", "Coach,?"
    })
    @DisplayName("roleShort memetakan role ke singkatan")
    void roleShort(String role, String expected) {
        assertThat(Ranks.roleShort(role)).isEqualTo(expected);
    }
}
