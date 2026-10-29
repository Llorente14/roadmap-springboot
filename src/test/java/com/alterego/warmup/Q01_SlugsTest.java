package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Q01 · Slug")
class Q01_SlugsTest {

    @ParameterizedTest(name = "\"{0}\" -> \"{1}\"")
    @CsvSource(delimiter = '|', value = {
            "M7 World Championship|m7-world-championship",
            "MPL ID S16|mpl-id-s16",
            "Alter Ego Esports Homura 26/27 Official Jersey - Home|alter-ego-esports-homura-26-27-official-jersey-home",
            "  spasi   di   tepi  |spasi-di-tepi",
            "--Sudah--Ada--Minus--|sudah-ada-minus",
            "Café Ünïcödé|cafe-unicode",
            "ACES|aces"
    })
    @DisplayName("mengubah teks menjadi slug")
    void slug(String input, String expected) {
        assertThat(Slugs.toSlug(input)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "input \"{0}\" ditolak")
    @ValueSource(strings = {"", "   ", "!!!", "///"})
    @DisplayName("teks yang tidak menghasilkan slug ditolak dengan IllegalArgumentException")
    void rejectsEmpty(String input) {
        assertThatThrownBy(() -> Slugs.toSlug(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("null ditolak dengan IllegalArgumentException")
    void rejectsNull() {
        assertThatThrownBy(() -> Slugs.toSlug(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
