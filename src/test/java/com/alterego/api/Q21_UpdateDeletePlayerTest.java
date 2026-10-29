package com.alterego.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.alterego.quest.QuestSpringTest;

@DisplayName("Q21 · PUT & DELETE /api/v1/admin/players/{game}/{id}")
class Q21_UpdateDeletePlayerTest extends QuestSpringTest {

    private static final String UPDATE = """
            {"nickname":"RENAMED","fullName":"Quest Player","role":"Gold Laner","type":"player",
             "status":"inactive","division":"Alter Ego","joinDate":"2026-10-02","displayOrder":7}
            """;

    @BeforeEach
    void createPlayer() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON)
                        .content(Q20_CreatePlayerTest.VALID))
                .andExpect(status().isCreated());
    }

    @AfterEach
    void cleanUp() {
        mongo.getCollection("players").deleteMany(new Document("playerId", new Document("$regex", "^quest-")));
    }

    @Test
    @DisplayName("PUT -> 200 dengan data baru (id & game dari path, tidak dari body)")
    void update() throws Exception {
        mvc.perform(put("/api/v1/admin/players/mlbb/quest-new").contentType(MediaType.APPLICATION_JSON).content(UPDATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("quest-new"))
                .andExpect(jsonPath("$.nickname").value("RENAMED"))
                .andExpect(jsonPath("$.status").value("inactive"));

        mvc.perform(get("/api/v1/players/mlbb/quest-new"))
                .andExpect(jsonPath("$.role").value("Gold Laner"));
    }

    @Test
    @DisplayName("PUT pemain yang tidak ada -> 404 (PUT di API ini tidak membuat data baru)")
    void updateMissing() throws Exception {
        mvc.perform(put("/api/v1/admin/players/mlbb/quest-ghost").contentType(MediaType.APPLICATION_JSON).content(UPDATE))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT body tidak valid -> 400")
    void updateInvalid() throws Exception {
        mvc.perform(put("/api/v1/admin/players/mlbb/quest-new").contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE.replace("\"RENAMED\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE -> 204, lalu GET -> 404, DELETE lagi -> 404")
    void deleteFlow() throws Exception {
        mvc.perform(delete("/api/v1/admin/players/mlbb/quest-new")).andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/players/mlbb/quest-new")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/v1/admin/players/mlbb/quest-new")).andExpect(status().isNotFound());
    }
}
