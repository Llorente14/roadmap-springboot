package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.Contract;
import com.alterego.quest.QuestSpringTest;

@DisplayName("Q14 · Seeder idempotent (upsert)")
class Q14_SeedIdempotentTest extends QuestSpringTest {

    private static final String SEEDER = "com.alterego.api.seed.DataSeeder";

    @Test
    @DisplayName("seed() dijalankan dua kali lagi: jumlah dokumen tidak berubah")
    void noDuplicates() {
        long players = mongo.getCollection("players").countDocuments(Q13_SeedLoadTest.SEEDED_PLAYERS);
        long achievements = mongo.getCollection("achievements").countDocuments();
        Object seeder = Contract.bean(ctx, SEEDER);

        Contract.call(seeder, "seed");
        Contract.call(seeder, "seed");

        assertThat(mongo.getCollection("players").countDocuments(Q13_SeedLoadTest.SEEDED_PLAYERS)).isEqualTo(players);
        assertThat(mongo.getCollection("achievements").countDocuments()).isEqualTo(achievements);
    }

    @Test
    @DisplayName("data yang berubah di DB dikembalikan sesuai file seed")
    void upsertRestores() {
        mongo.getCollection("players").updateOne(
                new Document("_id", "mlbb:nino"), new Document("$set", new Document("nickname", "HACKED")));

        Contract.call(Contract.bean(ctx, SEEDER), "seed");

        Document nino = mongo.getCollection("players").find(new Document("_id", "mlbb:nino")).first();
        assertThat(nino.getString("nickname")).isEqualTo("NINO");
    }
}
