package com.alterego.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.alterego.quest.QuestSpringTest;

@DisplayName("Q22 · Error seragam dengan ProblemDetail (RFC 9457)")
class Q22_ProblemDetailTest extends QuestSpringTest {

    @AfterEach
    void cleanUp() {
        mongo.getCollection("players").deleteMany(new Document("playerId", new Document("$regex", "^quest-")));
    }

    @Test
    @DisplayName("404 -> application/problem+json, detail menyebut apa yang dicari")
    void notFound() throws Exception {
        mvc.perform(get("/api/v1/players/cs2/nino"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.detail").value(containsString("nino")));
    }

    @Test
    @DisplayName("400 validasi -> problem+json dengan properti errors: {field: pesan}")
    void validation() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON)
                        .content(Q20_CreatePlayerTest.VALID.replace("\"QUEST\"", "\"\"").replace("\"mlbb\"", "\"dota2\"")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.nickname").isString())
                .andExpect(jsonPath("$.errors.game").isString());
    }

    @Test
    @DisplayName("400 parameter game tidak dikenal -> problem+json")
    void badParameter() throws Exception {
        mvc.perform(get("/api/v1/players?game=dota2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(containsString("dota2")));
    }

    @Test
    @DisplayName("409 -> problem+json, detail menyebut id yang bentrok")
    void conflict() throws Exception {
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content(Q20_CreatePlayerTest.VALID))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/admin/players").contentType(MediaType.APPLICATION_JSON).content(Q20_CreatePlayerTest.VALID))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value(containsString("quest-new")));
    }
}
