package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q24 · GET /api/v1/stats/{game}/check (summary file vs hasil hitung)")
class Q24_SummaryCheckTest extends QuestSpringTest {

    @ParameterizedTest(name = "check {0}")
    @ValueSource(strings = {"mlbb", "pubgm", "cs2"})
    @DisplayName("tiap field: MATCH / MISMATCH / UNKNOWN (summary null)")
    void check(String game) throws Exception {
        List<JsonNode> list = SeedFiles.achievements(game);
        double[] computed = {
                list.stream().filter(a -> "1".equals(a.path("placement").asString())).count(),
                list.stream().filter(a -> "2".equals(a.path("placement").asString())).count(),
                list.stream()
                        .filter(a -> "USD".equals(a.path("prize").path("currency").asString()))
                        .filter(a -> a.path("prize").path("amount").isNumber())
                        .mapToDouble(a -> a.path("prize").path("amount").asDouble()).sum()
        };
        String[] names = {"titles", "runnerUps", "totalPrizeUSD"};
        JsonNode declared = SeedFiles.declaredSummary(game);

        JsonNode body = getJson("/api/v1/stats/" + game + "/check");

        assertThat(body.path("game").asString()).isEqualTo(game);
        JsonNode fields = body.path("fields");
        assertThat(fields.size()).isEqualTo(3);

        boolean anyMismatch = false;
        for (int i = 0; i < names.length; i++) {
            JsonNode field = fields.get(i);
            JsonNode declaredValue = declared.path(names[i]);
            String expectedStatus;
            if (!declaredValue.isNumber()) {
                expectedStatus = "UNKNOWN";
            } else if (Math.abs(declaredValue.asDouble() - computed[i]) < 0.01) {
                expectedStatus = "MATCH";
            } else {
                expectedStatus = "MISMATCH";
                anyMismatch = true;
            }

            assertThat(field.path("name").asString()).isEqualTo(names[i]);
            assertThat(field.path("computed").asDouble()).as(names[i] + ".computed").isCloseTo(computed[i], within(0.01));
            if (declaredValue.isNumber()) {
                assertThat(field.path("declared").asDouble()).as(names[i] + ".declared").isCloseTo(declaredValue.asDouble(), within(0.01));
            } else {
                assertThat(field.path("declared").isNull()).as(names[i] + ".declared harus null").isTrue();
            }
            assertThat(field.path("status").asString()).as(names[i] + ".status").isEqualTo(expectedStatus);
        }
        assertThat(body.path("consistent").asBoolean()).isEqualTo(!anyMismatch);
    }
}
