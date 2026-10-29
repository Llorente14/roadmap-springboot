package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Q05 · Exception & parsing harga")
class Q05_PriceParserTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}..{2}")
    @CsvSource(delimiter = '|', value = {
            "Rp585.000|585000|585000",
            "Rp22.500|22500|22500",
            "Rp10.000|10000|10000",
            "Rp325.000 - Rp335.000|325000|335000",
            "  Rp1.250.000  |1250000|1250000"
    })
    @DisplayName("format harga dari data asli")
    void parses(String raw, long min, long max) throws Exception {
        assertThat(PriceParser.parse(raw)).isEqualTo(new PriceRange(min, max));
    }

    @ParameterizedTest(name = "\"{0}\" ditolak")
    @ValueSource(strings = {"gratis", "Rp", "585.000", "Rp58a.000", "Rp335.000 - Rp325.000", "Rp1 - Rp2 - Rp3", ""})
    @DisplayName("input tidak valid -> InvalidPriceException yang pesannya memuat input")
    void rejects(String raw) {
        assertThatThrownBy(() -> PriceParser.parse(raw))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining(raw.trim());
    }

    @Test
    @DisplayName("null ditolak dengan InvalidPriceException")
    void rejectsNull() {
        assertThatThrownBy(() -> PriceParser.parse(null)).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("InvalidPriceException harus unchecked (turunan RuntimeException)")
    void unchecked() {
        assertThat(RuntimeException.class.isAssignableFrom(InvalidPriceException.class))
                .as("Baca beda checked vs unchecked exception, lalu ubah superclass InvalidPriceException")
                .isTrue();
    }
}
