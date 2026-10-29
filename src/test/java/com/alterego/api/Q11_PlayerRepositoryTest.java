package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.Contract;
import com.alterego.quest.QuestSpringTest;

@DisplayName("Q11 · Document Player + PlayerRepository")
class Q11_PlayerRepositoryTest extends QuestSpringTest {

    private static final String REPO = "com.alterego.api.player.PlayerRepository";

    @BeforeEach
    void insertRawDocuments() {
        // Dokumen mentah sesuai skema di docs/BLUEPRINT.md. Class Player-mu harus bisa membacanya.
        mongo.getCollection("players").insertMany(List.of(
                doc("charlie", "CHARLIE", "active", 3),
                doc("alpha", "ALPHA", "active", 1),
                doc("bravo", "BRAVO", "inactive", 2)));
    }

    @AfterEach
    void cleanUp() {
        mongo.getCollection("players").deleteMany(new Document("game", "testgame"));
    }

    @Test
    @DisplayName("findById memakai _id gabungan \"game:playerId\"")
    void findByCompositeId() {
        Object repo = Contract.bean(ctx, REPO);

        Optional<?> found = (Optional<?>) Contract.call(repo, "findById", "testgame:alpha");

        assertThat(found).isPresent();
        Object player = found.get();
        assertThat(Contract.prop(player, "playerId")).isEqualTo("alpha");
        assertThat(Contract.prop(player, "nickname")).isEqualTo("ALPHA");
        assertThat(Contract.prop(player, "joinDate")).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    @DisplayName("findByGameOrderByDisplayOrderAsc: derived query, urut displayOrder")
    void byGameSorted() {
        Object repo = Contract.bean(ctx, REPO);

        List<?> players = (List<?>) Contract.call(repo, "findByGameOrderByDisplayOrderAsc", "testgame");

        assertThat(players).extracting(p -> Contract.prop(p, "playerId")).containsExactly("alpha", "bravo", "charlie");
    }

    @Test
    @DisplayName("findByGameAndStatusOrderByDisplayOrderAsc: filter status")
    void byGameAndStatus() {
        Object repo = Contract.bean(ctx, REPO);

        List<?> players = (List<?>) Contract.call(repo, "findByGameAndStatusOrderByDisplayOrderAsc", "testgame", "active");

        assertThat(players).extracting(p -> Contract.prop(p, "playerId")).containsExactly("alpha", "charlie");
    }

    private static Document doc(String id, String nickname, String status, int order) {
        return new Document("_id", "testgame:" + id)
                .append("playerId", id)
                .append("game", "testgame")
                .append("nickname", nickname)
                .append("fullName", nickname + " Test")
                .append("role", null)
                .append("type", "player")
                .append("status", status)
                .append("division", "Test")
                .append("joinDate", Date.from(LocalDate.of(2025, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC)))
                .append("displayOrder", order)
                .append("profileImg", null);
    }
}
