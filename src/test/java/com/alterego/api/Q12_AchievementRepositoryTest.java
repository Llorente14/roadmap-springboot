package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.Contract;
import com.alterego.quest.QuestSpringTest;

@DisplayName("Q12 · Document Achievement + embedded Prize + referensi rosterIds")
class Q12_AchievementRepositoryTest extends QuestSpringTest {

    private static final String REPO = "com.alterego.api.achievement.AchievementRepository";

    @BeforeEach
    void insertRawDocuments() {
        mongo.getCollection("achievements").insertMany(List.of(
                doc("t-old", LocalDate.of(2024, 5, 1), "1", 1000.0, List.of("alpha")),
                doc("t-new", LocalDate.of(2026, 2, 1), "2", null, List.of("alpha", "bravo")),
                doc("t-mid", LocalDate.of(2025, 9, 9), "3", 250.5, List.of())));
    }

    @AfterEach
    void cleanUp() {
        mongo.getCollection("achievements").deleteMany(new Document("game", "testgame"));
    }

    @Test
    @DisplayName("findByGameOrderByStartDateDesc: terbaru di atas")
    void byGameNewestFirst() {
        Object repo = Contract.bean(ctx, REPO);

        List<?> list = (List<?>) Contract.call(repo, "findByGameOrderByStartDateDesc", "testgame");

        assertThat(list).extracting(a -> Contract.prop(a, "id")).containsExactly("t-new", "t-mid", "t-old");
    }

    @Test
    @DisplayName("prize adalah objek tertanam (embedded) dan boleh null")
    void embeddedPrize() {
        Object repo = Contract.bean(ctx, REPO);
        List<?> list = (List<?>) Contract.call(repo, "findByGameOrderByStartDateDesc", "testgame");

        assertThat(Contract.prop(list.get(0), "prize")).isNull();
        Object prize = Contract.prop(list.get(1), "prize");
        assertThat(prize).isNotNull();
        assertThat(((Number) Contract.prop(prize, "amount")).doubleValue()).isEqualTo(250.5);
        assertThat(Contract.prop(prize, "currency")).isEqualTo("USD");
    }

    @Test
    @DisplayName("findByGameAndRosterIdsContaining: cari prestasi lewat referensi pemain")
    void byRosterReference() {
        Object repo = Contract.bean(ctx, REPO);

        List<?> alpha = (List<?>) Contract.call(repo, "findByGameAndRosterIdsContaining", "testgame", "alpha");
        List<?> bravo = (List<?>) Contract.call(repo, "findByGameAndRosterIdsContaining", "testgame", "bravo");
        List<?> nobody = (List<?>) Contract.call(repo, "findByGameAndRosterIdsContaining", "testgame", "zulu");

        assertThat(alpha).extracting(a -> Contract.prop(a, "id")).containsExactlyInAnyOrder("t-old", "t-new");
        assertThat(bravo).extracting(a -> Contract.prop(a, "id")).containsExactly("t-new");
        assertThat(nobody).isEmpty();
    }

    private static Document doc(String id, LocalDate start, String placement, Double prize, List<String> roster) {
        return new Document("_id", id)
                .append("game", "testgame")
                .append("division", "Test")
                .append("tournament", "Turnamen " + id)
                .append("tier", "B-Tier")
                .append("startDate", Date.from(start.atStartOfDay().toInstant(ZoneOffset.UTC)))
                .append("endDate", null)
                .append("placement", placement)
                .append("placementLabel", placement)
                .append("prize", prize == null ? null : new Document("amount", prize).append("currency", "USD"))
                .append("rosterIds", roster)
                .append("highlight", false);
    }
}
