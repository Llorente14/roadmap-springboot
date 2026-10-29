package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q03 · Lambda & Stream")
class Q03_AchievementMathTest {

    private final List<WarmupAchievement> data = List.of(
            new WarmupAchievement("a", "mlbb", "1", LocalDate.of(2026, 6, 28), 50690.04),
            new WarmupAchievement("b", "mlbb", "2", LocalDate.of(2026, 1, 25), 150000.0),
            new WarmupAchievement("c", "mlbb", "1", LocalDate.of(2025, 10, 5), null),
            new WarmupAchievement("d", "mlbb", "5-8", LocalDate.of(2025, 3, 1), 1200.5),
            new WarmupAchievement("e", "mlbb", "1-2", LocalDate.of(2024, 7, 7), 0.0));

    @Test
    @DisplayName("countPlacement menghitung placement yang persis sama ('1-2' bukan '1')")
    void countPlacement() {
        assertThat(AchievementMath.countPlacement(data, "1")).isEqualTo(2);
        assertThat(AchievementMath.countPlacement(data, "2")).isEqualTo(1);
        assertThat(AchievementMath.countPlacement(data, "3")).isZero();
    }

    @Test
    @DisplayName("totalPrize melewati hadiah null")
    void totalPrize() {
        assertThat(AchievementMath.totalPrize(data)).isCloseTo(201890.54, within(0.001));
        assertThat(AchievementMath.totalPrize(List.of())).isZero();
    }

    @Test
    @DisplayName("countByYear mengelompokkan per tahun startDate")
    void countByYear() {
        assertThat(AchievementMath.countByYear(data)).isEqualTo(Map.of(2026, 2L, 2025, 2L, 2024, 1L));
    }

    @Test
    @DisplayName("biggestPrize mengabaikan null dan aman untuk list kosong")
    void biggestPrize() {
        assertThat(AchievementMath.biggestPrize(data)).map(WarmupAchievement::id).contains("b");
        assertThat(AchievementMath.biggestPrize(List.of())).isEmpty();
        assertThat(AchievementMath.biggestPrize(List.of(
                new WarmupAchievement("x", "cs2", "1", LocalDate.of(2025, 1, 1), null)))).isEmpty();
    }
}
