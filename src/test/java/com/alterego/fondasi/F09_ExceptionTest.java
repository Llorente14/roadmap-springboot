package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("F09 · Exception")
class F09_ExceptionTest {

    @Test
    @DisplayName("parseKills membaca angka dan mengabaikan spasi tepi")
    void parseKills() {
        assertThat(Parser.parseKills("12")).isEqualTo(12);
        assertThat(Parser.parseKills("  7 ")).isEqualTo(7);
        assertThat(Parser.parseKills("0")).isZero();
    }

    @ParameterizedTest(name = "\"{0}\" ditolak")
    @ValueSource(strings = {"", "   ", "-3"})
    @DisplayName("kosong atau negatif -> IllegalArgumentException")
    void parseKillsRejects(String text) {
        assertThatThrownBy(() -> Parser.parseKills(text)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("null -> IllegalArgumentException (bukan NullPointerException)")
    void parseKillsNull() {
        assertThatThrownBy(() -> Parser.parseKills(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("bukan angka -> IllegalArgumentException dengan NumberFormatException sebagai cause")
    void parseKillsWrapsCause() {
        assertThatThrownBy(() -> Parser.parseKills("tujuh"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("parseOrZero menelan kegagalan dengan try/catch")
    void parseOrZero() {
        assertThat(Parser.parseOrZero("5")).isEqualTo(5);
        assertThat(Parser.parseOrZero("abc")).isZero();
        assertThat(Parser.parseOrZero(null)).isZero();
        assertThat(Parser.parseOrZero("-1")).isZero();
    }

    @Test
    @DisplayName("ScoreFormatException buatanmu adalah CHECKED exception")
    void customExceptionIsChecked() {
        Class<?> type = Reflect.type("ScoreFormatException");
        assertThat(Exception.class.isAssignableFrom(type)).as("harus turunan Exception").isTrue();
        assertThat(RuntimeException.class.isAssignableFrom(type)).as("checked = BUKAN turunan RuntimeException").isFalse();
    }

    @Test
    @DisplayName("parseScore memecah \"13-7\" menjadi dua angka")
    void parseScore() {
        assertThat((int[]) Reflect.callStatic("Parser", "parseScore", "13-7")).containsExactly(13, 7);
        assertThat((int[]) Reflect.callStatic("Parser", "parseScore", " 2 - 0 ")).containsExactly(2, 0);
    }

    @ParameterizedTest(name = "\"{0}\" -> ScoreFormatException")
    @ValueSource(strings = {"13", "a-b", "1-2-3", ""})
    @DisplayName("format salah -> ScoreFormatException yang menyebut input")
    void parseScoreRejects(String input) {
        assertThatThrownBy(() -> Reflect.callStatic("Parser", "parseScore", input))
                .satisfies(e -> assertThat(e.getClass().getSimpleName()).isEqualTo("ScoreFormatException"))
                .hasMessageContaining(input);
    }

    @Test
    @DisplayName("parseScore(null) -> ScoreFormatException")
    void parseScoreNull() {
        assertThatThrownBy(() -> Reflect.callStatic("Parser", "parseScore", (Object) null))
                .satisfies(e -> assertThat(e.getClass().getSimpleName()).isEqualTo("ScoreFormatException"));
    }
}
