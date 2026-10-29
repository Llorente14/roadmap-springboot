package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q16 · GET /api/v1/players (DTO + filter)")
class Q16_PlayerListTest extends QuestSpringTest {

    @Test
    @DisplayName("?game=mlbb -> semua pemain mlbb, urut displayOrder, bentuk JSON sesuai kontrak")
    void byGame() throws Exception {
        List<JsonNode> expected = SeedFiles.players().stream()
                .filter(p -> "mlbb".equals(p.path("game").asString()))
                .sorted(Comparator.comparingInt(p -> p.path("displayOrder").asInt()))
                .toList();

        JsonNode body = getJson("/api/v1/players?game=mlbb");

        assertThat(body.isArray()).isTrue();
        assertThat(body.size()).isEqualTo(expected.size());
        assertThat(ids(body)).containsExactlyElementsOf(expected.stream().map(p -> p.path("id").asString()).toList());

        JsonNode first = body.get(0);
        JsonNode source = expected.get(0);
        assertThat(first.path("id").asString()).isEqualTo(source.path("id").asString());
        assertThat(first.path("game").asString()).isEqualTo("mlbb");
        assertThat(first.path("nickname").asString()).isEqualTo(source.path("nickname").asString());
        assertThat(first.path("joinDate").asString())
                .as("joinDate dikirim sebagai \"yyyy-MM-dd\"")
                .isEqualTo(source.path("joinDate").asString());
        assertThat(first.has("_id")).as("detail database (_id) tidak boleh bocor ke API").isFalse();
        assertThat(first.has("playerId")).as("pakai nama field 'id' di API, bukan 'playerId'").isFalse();
    }

    @Test
    @DisplayName("tanpa filter -> semua pemain dari semua game")
    void all() throws Exception {
        JsonNode body = getJson("/api/v1/players");
        assertThat(body.size()).isEqualTo(SeedFiles.players().size());
    }

    @Test
    @DisplayName("?status=inactive -> filter status bekerja")
    void byStatus() throws Exception {
        long expected = SeedFiles.players().stream()
                .filter(p -> "mlbb".equals(p.path("game").asString()) && "inactive".equals(p.path("status").asString()))
                .count();
        assertThat(getJson("/api/v1/players?game=mlbb&status=inactive").size()).isEqualTo(expected);
    }

    @Test
    @DisplayName("game tidak dikenal -> 400")
    void unknownGame() throws Exception {
        mvc.perform(get("/api/v1/players?game=dota2")).andExpect(status().isBadRequest());
    }

    private static List<String> ids(JsonNode array) {
        List<String> out = new ArrayList<>();
        array.forEach(n -> out.add(n.path("id").asString()));
        return out;
    }
}
