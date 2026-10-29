package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q23 · GET /api/v1/stats/{game} (agregasi)")
class Q23_StatsTest extends QuestSpringTest {

    @ParameterizedTest(name = "stats {0}")
    @ValueSource(strings = {"mlbb", "pubgm", "cs2"})
    @DisplayName("angka dihitung dari data prestasi")
    void computed(String game) throws Exception {
        List<JsonNode> list = SeedFiles.achievements(game);
        long titles = list.stream().filter(a -> "1".equals(a.path("placement").asString())).count();
        long runnerUps = list.stream().filter(a -> "2".equals(a.path("placement").asString())).count();
        double prize = list.stream()
                .filter(a -> "USD".equals(a.path("prize").path("currency").asString()))
                .filter(a -> a.path("prize").path("amount").isNumber())
                .mapToDouble(a -> a.path("prize").path("amount").asDouble())
                .sum();
        // Prestasi tanpa startDate (ada di data asli) tidak masuk byYear.
        Map<String, Long> byYear = list.stream()
                .filter(a -> a.path("startDate").isString())
                .collect(Collectors.groupingBy(a -> a.path("startDate").asString().substring(0, 4), TreeMap::new, Collectors.counting()));

        JsonNode body = getJson("/api/v1/stats/" + game);

        assertThat(body.path("game").asString()).isEqualTo(game);
        assertThat(body.path("tournaments").asLong()).isEqualTo(list.size());
        assertThat(body.path("titles").asLong()).isEqualTo(titles);
        assertThat(body.path("runnerUps").asLong()).isEqualTo(runnerUps);
        assertThat(body.path("totalPrizeUSD").asDouble()).isCloseTo(prize, within(0.01));
        byYear.forEach((year, count) ->
                assertThat(body.path("byYear").path(year).asLong()).as("byYear." + year).isEqualTo(count));
        assertThat(body.path("byYear").size()).isEqualTo(byYear.size());
    }

    @Test
    @DisplayName("game tidak dikenal -> 400")
    void unknownGame() throws Exception {
        mvc.perform(get("/api/v1/stats/dota2")).andExpect(status().isBadRequest());
    }
}
