package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

import tools.jackson.databind.JsonNode;

@DisplayName("Q18 · GET /api/v1/achievements (filter + pagination)")
class Q18_AchievementPageTest extends QuestSpringTest {

    /** Urutan kontrak: startDate terbaru dulu, tanpa startDate paling akhir; tanggal sama -> id A-Z. */
    private static final Comparator<JsonNode> ORDER = Comparator
            .comparing((JsonNode a) -> a.path("startDate").isString() ? a.path("startDate").asString() : "").reversed()
            .thenComparing(a -> a.path("id").asString());

    @Test
    @DisplayName("halaman pertama: bentuk PageResponse + urutan benar")
    void firstPage() throws Exception {
        List<JsonNode> all = sorted(SeedFiles.achievements("mlbb"));

        JsonNode body = getJson("/api/v1/achievements?game=mlbb&size=5");

        assertThat(body.path("page").asInt()).isZero();
        assertThat(body.path("size").asInt()).isEqualTo(5);
        assertThat(body.path("totalElements").asLong()).isEqualTo(all.size());
        assertThat(body.path("totalPages").asInt()).isEqualTo((all.size() + 4) / 5);
        assertThat(ids(body.path("content"))).containsExactlyElementsOf(idsOf(all.subList(0, Math.min(5, all.size()))));

        JsonNode item = body.path("content").get(0);
        assertThat(item.has("tournament")).isTrue();
        assertThat(item.has("placement")).isTrue();
        assertThat(item.path("startDate").asString()).matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    @DisplayName("halaman kedua berisi irisan berikutnya")
    void secondPage() throws Exception {
        List<JsonNode> all = sorted(SeedFiles.achievements("mlbb"));

        JsonNode body = getJson("/api/v1/achievements?game=mlbb&page=1&size=5");

        assertThat(body.path("page").asInt()).isEqualTo(1);
        assertThat(ids(body.path("content"))).containsExactlyElementsOf(idsOf(all.subList(5, Math.min(10, all.size()))));
    }

    @Test
    @DisplayName("filter year dan placement (boleh digabung, game opsional)")
    void filters() throws Exception {
        long in2026 = SeedFiles.achievements("mlbb").stream().filter(a -> a.path("startDate").asString().startsWith("2026")).count();
        long champions = SeedFiles.achievements().stream().filter(a -> "1".equals(a.path("placement").asString())).count();

        assertThat(getJson("/api/v1/achievements?game=mlbb&year=2026&size=50").path("totalElements").asLong()).isEqualTo(in2026);
        assertThat(getJson("/api/v1/achievements?placement=1&size=50").path("totalElements").asLong()).isEqualTo(champions);
    }

    @Test
    @DisplayName("default size 10; size di atas 50 dipotong jadi 50")
    void sizeRules() throws Exception {
        assertThat(getJson("/api/v1/achievements").path("size").asInt()).isEqualTo(10);
        assertThat(getJson("/api/v1/achievements?size=500").path("size").asInt()).isEqualTo(50);
    }

    private static List<JsonNode> sorted(List<JsonNode> list) {
        return list.stream().sorted(ORDER).toList();
    }

    private static List<String> idsOf(List<JsonNode> list) {
        return list.stream().map(a -> a.path("id").asString()).toList();
    }

    private static List<String> ids(JsonNode array) {
        List<String> out = new ArrayList<>();
        array.forEach(n -> out.add(n.path("id").asString()));
        return out;
    }
}
