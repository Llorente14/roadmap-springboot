package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

@DisplayName("Q15 · Seed rusak = aplikasi menolak start dengan pesan yang jelas")
class Q15_SeedValidationTest {

    @Test
    @DisplayName("seed-broken/rosters/broken.json (nickname kosong) -> start gagal, pesan menyebut file dan field")
    void failsFast() {
        assertThatThrownBy(() -> new SpringApplicationBuilder(AlteregoApiApplication.class)
                .profiles("test")
                .web(WebApplicationType.NONE)
                .run("--alterego.seed.location=classpath:seed-broken/")
                .close())
                .satisfies(error -> {
                    StringBuilder messages = new StringBuilder();
                    for (Throwable t = error; t != null; t = t.getCause()) {
                        messages.append(t.getMessage()).append('\n');
                    }
                    assertThat(messages.toString())
                            .as("Pesan error (termasuk cause) harus menyebut nama file dan field yang salah")
                            .contains("broken.json")
                            .contains("nickname");
                });
    }
}
