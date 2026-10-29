package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;

import tools.jackson.databind.JsonNode;

@DisplayName("Q19 · GET /api/v1/players/{game}/{id}/achievements (query lewat referensi)")
class Q19_PlayerAchievementsTest extends QuestSpringTest {

    @Test
    @DisplayName("pemain yang direferensikan rosterIds -> prestasinya muncul")
    void linked() throws Exception {
        JsonNode body = getJson("/api/v1/players/mlbb/nino/achievements");

        assertThat(body.isArray()).isTrue();
        assertThat(body).anySatisfy(a -> assertThat(a.path("id").asString()).isEqualTo("fixture-roster-link"));
    }

    @Test
    @DisplayName("pemain ada tapi tidak direferensikan -> array kosong, bukan 404")
    void empty() throws Exception {
        assertThat(getJson("/api/v1/players/mlbb/yazukee/achievements").size()).isZero();
    }

    @Test
    @DisplayName("pemain tidak ada -> 404")
    void unknownPlayer() throws Exception {
        // Pastikan endpoint-nya ada dulu; 404 dari route yang belum dibuat tidak dihitung.
        mvc.perform(get("/api/v1/players/mlbb/nino/achievements")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/players/mlbb/ghost/achievements")).andExpect(status().isNotFound());
    }
}
