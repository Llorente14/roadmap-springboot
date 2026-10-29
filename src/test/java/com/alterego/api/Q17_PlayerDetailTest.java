package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q17 · GET /api/v1/players/{game}/{id}")
class Q17_PlayerDetailTest extends QuestSpringTest {

    @Test
    @DisplayName("pemain ada -> 200 dengan data lengkap")
    void found() throws Exception {
        JsonNode source = SeedFiles.players().stream()
                .filter(p -> "nino".equals(p.path("id").asString()))
                .findFirst().orElseThrow();

        JsonNode body = getJson("/api/v1/players/mlbb/nino");

        assertThat(body.path("nickname").asString()).isEqualTo("NINO");
        assertThat(body.path("fullName").asString()).isEqualTo(source.path("fullName").asString());
        assertThat(body.path("role").asString()).isEqualTo(source.path("role").asString());
    }

    @Test
    @DisplayName("id sama, game beda -> pemain berbeda (aces)")
    void sameIdOtherGame() throws Exception {
        assertThat(getJson("/api/v1/players/mlbb/aces").path("role").asString()).isEqualTo("Head of Esports");
        assertThat(getJson("/api/v1/players/pubgm/aces").path("role").asString()).isEqualTo("Manager");
    }

    @Test
    @DisplayName("joinDate tidak lengkap \"2021-05\" -> disimpan & dikirim sebagai tanggal 1 bulan itu")
    void partialJoinDate() throws Exception {
        assertThat(getJson("/api/v1/players/cs2/whyphy").path("joinDate").asString()).isEqualTo("2021-05-01");
    }

    @Test
    @DisplayName("tidak ada -> 404")
    void notFound() throws Exception {
        // Pastikan endpoint-nya ada dulu; 404 dari route yang belum dibuat tidak dihitung.
        mvc.perform(get("/api/v1/players/mlbb/nino")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/players/cs2/nino")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("game tidak dikenal di path -> 400")
    void unknownGame() throws Exception {
        mvc.perform(get("/api/v1/players/dota2/nino")).andExpect(status().isBadRequest());
    }
}
