package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("F03 · Method bawaan String")
class F03_NamesTest {

    @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
    @CsvSource(delimiter = '|', value = {
            "  ae   nino |AE NINO",
            "AE NINO|AE NINO",
            "alter    ego   esports|ALTER EGO ESPORTS"
    })
    @DisplayName("cleanTag merapikan spasi dan huruf")
    void cleanTag(String raw, String expected) {
        assertThat(Names.cleanTag(raw)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
    @CsvSource(delimiter = '|', value = {
            "Muhammad Nino Pratama|MNP",
            "  nino   pratama |NP",
            "aces|A"
    })
    @DisplayName("initials mengambil huruf pertama setiap kata")
    void initials(String name, String expected) {
        assertThat(Names.initials(name)).isEqualTo(expected);
    }

    @Test
    @DisplayName("teamOf mengambil bagian sebelum titik pertama")
    void teamOf() {
        assertThat(Names.teamOf("AE.NINO")).isEqualTo("AE");
        assertThat(Names.teamOf("AE.NINO.ALT")).isEqualTo("AE");
        assertThat(Names.teamOf("NINO")).isEmpty();
    }

    @ParameterizedTest(name = "\"{0}\" valid")
    @ValueSource(strings = {"nino", "YAZUKEE", "aces_01", "abc", "abcdefghijkl"})
    @DisplayName("nickname valid")
    void validNickname(String nickname) {
        assertThat(Names.isValidNickname(nickname)).isTrue();
    }

    @ParameterizedTest(name = "\"{0}\" tidak valid")
    @ValueSource(strings = {"", "ab", "abcdefghijklm", "ni no", "nino!", "AE.NINO"})
    @DisplayName("nickname tidak valid")
    void invalidNickname(String nickname) {
        assertThat(Names.isValidNickname(nickname)).isFalse();
    }

    @Test
    @DisplayName("nickname null tidak valid (bukan error)")
    void nullNickname() {
        assertThat(Names.isValidNickname(null)).isFalse();
    }

    @Test
    @DisplayName("countVowels menghitung vokal besar dan kecil")
    void countVowels() {
        assertThat(Names.countVowels("YAZUKEE")).isEqualTo(4);
        assertThat(Names.countVowels("Alter Ego")).isEqualTo(4);
        assertThat(Names.countVowels("xyz")).isZero();
    }
}
