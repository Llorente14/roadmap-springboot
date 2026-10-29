package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.QuestSpringTest;
import com.alterego.quest.SeedFiles;

@DisplayName("Q13 · Seeder: JSON -> MongoDB saat aplikasi start")
class Q13_SeedLoadTest extends QuestSpringTest {

    /** Hanya data seed, tanpa data buatan test lain (id berawalan "quest-" / game "testgame"). */
    static final Document SEEDED_PLAYERS = Document.parse(
            "{ game: { $in: ['mlbb','pubgm','cs2'] }, playerId: { $not: { $regex: '^quest-' } } }");

    @Test
    @DisplayName("semua pemain dari seed/rosters/*.json tersimpan")
    void allPlayers() {
        long expected = SeedFiles.players().size();
        assertThat(mongo.getCollection("players").countDocuments(SEEDED_PLAYERS)).isEqualTo(expected);
    }

    @Test
    @DisplayName("semua prestasi dari seed/achievements/*.json tersimpan")
    void allAchievements() {
        long expected = SeedFiles.achievements().size();
        assertThat(mongo.getCollection("achievements").countDocuments(
                new Document("game", new Document("$in", List.of("mlbb", "pubgm", "cs2"))))).isEqualTo(expected);
    }

    @Test
    @DisplayName("_id gabungan: 'aces' di mlbb dan pubgm tersimpan sebagai dua dokumen")
    void compositeIds() {
        Document mlbbAces = mongo.getCollection("players").find(new Document("_id", "mlbb:aces")).first();
        Document pubgmAces = mongo.getCollection("players").find(new Document("_id", "pubgm:aces")).first();

        assertThat(mlbbAces).isNotNull();
        assertThat(pubgmAces).isNotNull();
        assertThat(mlbbAces.getString("role")).isEqualTo("Head of Esports");
        assertThat(pubgmAces.getString("role")).isEqualTo("Manager");
    }

    @Test
    @DisplayName("field tersimpan dengan tipe yang benar (tanggal = Date, bukan String)")
    void typedFields() {
        Document nino = mongo.getCollection("players").find(new Document("_id", "mlbb:nino")).first();

        assertThat(nino).isNotNull();
        assertThat(nino.getString("playerId")).isEqualTo("nino");
        assertThat(nino.getString("nickname")).isEqualTo("NINO");
        assertThat(nino.get("joinDate")).isInstanceOf(Date.class);
        assertThat(nino.get("displayOrder")).isInstanceOf(Integer.class);

        Document whyphy = mongo.getCollection("players").find(new Document("_id", "cs2:whyphy")).first();
        assertThat(whyphy).as("cs2:whyphy punya joinDate \"2021-05\" (tidak lengkap) dan tetap harus tersimpan").isNotNull();
        assertThat(whyphy.get("joinDate")).isInstanceOf(Date.class);

        Document m7 = mongo.getCollection("achievements").find(new Document("_id", "m7-world-championship")).first();
        assertThat(m7).isNotNull();
        assertThat(m7.get("startDate")).isInstanceOf(Date.class);
        assertThat(m7.get("prize", Document.class).get("amount")).isInstanceOf(Double.class);
    }
}
