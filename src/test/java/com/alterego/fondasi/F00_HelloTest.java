package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F00 · Merah ke hijau")
class F00_HelloTest {

    @Test
    @DisplayName("greet menyapa dengan nama")
    void greet() {
        assertThat(Hello.greet("Axel")).isEqualTo("Halo, Axel!");
        assertThat(Hello.greet("NINO")).isEqualTo("Halo, NINO!");
    }

    @Test
    @DisplayName("sum menjumlahkan dua angka")
    void sum() {
        assertThat(Hello.sum(2, 3)).isEqualTo(5);
        assertThat(Hello.sum(-4, 4)).isZero();
    }
}
