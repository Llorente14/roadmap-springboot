package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;

import tools.jackson.databind.JsonNode;

@DisplayName("Q26 · Dokumentasi API dengan springdoc (OpenAPI + Swagger UI)")
class Q26_OpenApiTest extends QuestSpringTest {

    @Test
    @DisplayName("/v3/api-docs memuat endpoint utama")
    void apiDocs() throws Exception {
        JsonNode paths = getJson("/v3/api-docs").path("paths");

        assertThat(paths.has("/api/v1/players")).isTrue();
        assertThat(paths.has("/api/v1/players/{game}/{id}")).isTrue();
        assertThat(paths.has("/api/v1/achievements")).isTrue();
        assertThat(paths.has("/api/v1/stats/{game}")).isTrue();
        assertThat(paths.has("/api/v1/products")).isTrue();
    }

    @Test
    @DisplayName("Swagger UI bisa dibuka")
    void swaggerUi() throws Exception {
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
}
