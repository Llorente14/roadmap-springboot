package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.alterego.quest.QuestSpringTest;

@DisplayName("Q20 · POST /api/v1/admin/players (validasi + 201 + 409)")
class Q20_CreatePlayerTest extends QuestSpringTest {

    static final String VALID = """
            {"id":"quest-new","game":"mlbb","nickname":"QUEST","fullName":"Quest Player","role":"Roamer",
             "type":"player","status":"active","division":"Alter Ego","joinDate":"2026-10-01","displayOrder":99}
            """;

    @AfterEach
    void cleanUp() {
        mongo.getCollection("players").deleteMany(new Document("playerId", new Document("$regex", "^quest-")));
    }

    @Test
    @DisplayName("body valid -> 201, header Location, body = pemain baru; lalu bisa di-GET")
    void created() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/v1/players/mlbb/quest-new")))
                .andExpect(jsonPath("$.id").value("quest-new"))
                .andExpect(jsonPath("$.joinDate").value("2026-10-01"));

        assertThat(getJson("/api/v1/players/mlbb/quest-new").path("nickname").asString()).isEqualTo("QUEST");
    }

    @Test
    @DisplayName("field opsional (fullName, role, division, joinDate, profileImg) boleh tidak dikirim")
    void optionalFields() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content("""
                        {"id":"quest-min","game":"cs2","nickname":"MIN","type":"staff","status":"active","displayOrder":1}
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("game+id sudah ada -> 409 Conflict, data lama tidak berubah")
    void duplicate() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content(VALID))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID.replace("\"QUEST\"", "\"OVERWRITE\"")))
                .andExpect(status().isConflict());

        assertThat(getJson("/api/v1/players/mlbb/quest-new").path("nickname").asString()).isEqualTo("QUEST");
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(delimiter = '|', value = {
            "nickname kosong|\"nickname\":\"QUEST\"|\"nickname\":\"  \"",
            "nickname > 30 karakter|\"nickname\":\"QUEST\"|\"nickname\":\"ABCDEFGHIJKLMNOPQRSTUVWXYZ012345\"",
            "game tidak dikenal|\"game\":\"mlbb\"|\"game\":\"dota2\"",
            "id bukan slug|\"id\":\"quest-new\"|\"id\":\"Quest New\"",
            "type tidak dikenal|\"type\":\"player\"|\"type\":\"coach\"",
            "status tidak dikenal|\"status\":\"active\"|\"status\":\"retired\"",
            "displayOrder < 1|\"displayOrder\":99|\"displayOrder\":0",
            "joinDate bukan tanggal|\"joinDate\":\"2026-10-01\"|\"joinDate\":\"kemarin\""
    })
    @DisplayName("body tidak valid -> 400, tidak ada yang tersimpan")
    void invalid(String reason, String from, String to) throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content(VALID.replace(from, to)))
                .andExpect(status().isBadRequest());

        assertThat(mongo.getCollection("players").countDocuments(new Document("playerId", new Document("$regex", "^quest-"))))
                .isZero();
    }
}
