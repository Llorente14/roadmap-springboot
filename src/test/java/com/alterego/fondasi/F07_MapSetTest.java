package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F07 · Map dan Set")
class F07_MapSetTest {

    private static final List<String> PICKS = List.of("Fanny", "Ling", "Fanny", "Lancelot", "Ling", "Fanny");

    @Test
    @DisplayName("countPicks menghitung per hero")
    void countPicks() {
        assertThat(HeroPicks.countPicks(PICKS))
                .containsOnly(Map.entry("Fanny", 3), Map.entry("Ling", 2), Map.entry("Lancelot", 1));
        assertThat(HeroPicks.countPicks(List.of())).isEmpty();
    }

    @Test
    @DisplayName("uniqueHeroes membuang duplikat")
    void unique() {
        assertThat(HeroPicks.uniqueHeroes(PICKS)).containsExactlyInAnyOrder("Fanny", "Ling", "Lancelot");
    }

    @Test
    @DisplayName("picksOf: hero tidak ada -> 0")
    void picksOf() {
        Map<String, Integer> counts = Map.of("Fanny", 3);
        assertThat(HeroPicks.picksOf(counts, "Fanny")).isEqualTo(3);
        assertThat(HeroPicks.picksOf(counts, "Ling")).isZero();
    }

    @Test
    @DisplayName("mostPicked: terbanyak, seri -> abjad, kosong -> \"\"")
    void mostPicked() {
        assertThat(HeroPicks.mostPicked(PICKS)).isEqualTo("Fanny");
        assertThat(HeroPicks.mostPicked(List.of("Ling", "Fanny", "Ling", "Fanny"))).isEqualTo("Fanny");
        assertThat(HeroPicks.mostPicked(List.of())).isEmpty();
    }

    @Test
    @DisplayName("pickedByBoth mengambil irisan")
    void intersection() {
        assertThat(HeroPicks.pickedByBoth(List.of("Fanny", "Ling", "Tigreal"), List.of("Ling", "Tigreal", "Ling", "Hayabusa")))
                .containsExactlyInAnyOrder("Ling", "Tigreal");
        assertThat(HeroPicks.pickedByBoth(List.of("Fanny"), List.of("Ling"))).isEmpty();
    }

    @Test
    @DisplayName("HeroPool menyimpan data di dalam object antar pemanggilan")
    void pool() {
        HeroPool pool = new HeroPool();
        assertThat(pool.register("Fanny", "Assassin")).isTrue();
        assertThat(pool.register("Tigreal", "Tank")).isTrue();
        assertThat(pool.register("Ling", "Assassin")).isTrue();

        assertThat(pool.size()).isEqualTo(3);
        assertThat(pool.roleOf("Tigreal")).isEqualTo("Tank");
        assertThat(pool.heroesWithRole("Assassin")).containsExactlyInAnyOrder("Fanny", "Ling");
        assertThat(pool.heroesWithRole("Mage")).isEmpty();
    }

    @Test
    @DisplayName("HeroPool: nama ganda tidak menimpa, hero tak dikenal -> \"unknown\"")
    void poolDuplicateAndMissing() {
        HeroPool pool = new HeroPool();
        pool.register("Fanny", "Assassin");
        assertThat(pool.register("Fanny", "Tank")).isFalse();
        assertThat(pool.roleOf("Fanny")).isEqualTo("Assassin");
        assertThat(pool.roleOf("Nobody")).isEqualTo("unknown");
        assertThat(pool.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("dua HeroPool tidak berbagi data")
    void poolsIndependent() {
        HeroPool a = new HeroPool();
        HeroPool b = new HeroPool();
        a.register("Fanny", "Assassin");
        assertThat(b.size()).isZero();
        assertThat(b.roleOf("Fanny")).isEqualTo("unknown");
    }
}
