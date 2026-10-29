package com.alterego.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alterego.quest.Contract;
import com.alterego.quest.QuestSpringTest;

@DisplayName("Q09 · @ConfigurationProperties")
class Q09_SeedPropertiesTest extends QuestSpringTest {

    private static final String SEED_PROPERTIES = "com.alterego.api.seed.SeedProperties";

    @Test
    @DisplayName("SeedProperties adalah bean yang terisi dari alterego.seed.* (lihat application-test.yaml)")
    void boundFromYaml() {
        Object props = Contract.bean(ctx, SEED_PROPERTIES);

        assertThat(Contract.prop(props, "enabled")).isEqualTo(true);
        assertThat(Contract.prop(props, "location")).isEqualTo("classpath:seed-test/");
    }
}
