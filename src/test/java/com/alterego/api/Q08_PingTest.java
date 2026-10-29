package com.alterego.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import com.alterego.quest.QuestSpringTest;

@DisplayName("Q08 · Controller pertama: /api/v1/ping")
class Q08_PingTest extends QuestSpringTest {

    @Test
    @DisplayName("GET /api/v1/ping -> 200 JSON {status: ok, app: alterego-api}")
    void ping() throws Exception {
        mvc.perform(get("/api/v1/ping"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.app").value("alterego-api"));
    }

    @Nested
    @TestPropertySource(properties = "spring.application.name=nama-dari-konfigurasi")
    @DisplayName("nama app dibaca dari konfigurasi, bukan ditulis mati di kode")
    class FromConfig extends QuestSpringTest {

        @Test
        @DisplayName("spring.application.name diganti -> field app ikut berubah")
        void appNameFromConfig() throws Exception {
            mvc.perform(get("/api/v1/ping"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.app").value("nama-dari-konfigurasi"));
        }
    }
}
