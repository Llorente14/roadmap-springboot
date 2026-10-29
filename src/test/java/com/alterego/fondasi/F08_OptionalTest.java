package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F08 · null dan Optional")
class F08_OptionalTest {

    @Test
    @DisplayName("firstWithPrefix: ketemu -> isi, tidak -> Optional kosong (bukan null)")
    void firstWithPrefix() {
        List<String> names = List.of("NINO", "NOVA", "ACES");
        assertThat(Finder.firstWithPrefix(names, "NO")).contains("NOVA");
        assertThat(Finder.firstWithPrefix(names, "N")).contains("NINO");
        assertThat(Finder.firstWithPrefix(names, "Z")).isNotNull().isEmpty();
        assertThat(Finder.firstWithPrefix(List.of(), "N")).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("teamOf membungkus hasil Map.get yang bisa null")
    void teamOf() {
        Map<String, String> teams = Map.of("nino", "AE", "kiboy", "RRQ");
        assertThat(Finder.teamOf(teams, "nino")).contains("AE");
        assertThat(Finder.teamOf(teams, "ghost")).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("orUnknown memberi nilai cadangan")
    void orUnknown() {
        assertThat(Finder.orUnknown(Optional.of("NINO"))).isEqualTo("NINO");
        assertThat(Finder.orUnknown(Optional.empty())).isEqualTo("UNKNOWN");
    }

    @Test
    @DisplayName("lengthOf mengubah isi Optional")
    void lengthOf() {
        assertThat(Finder.lengthOf(Optional.of("YAZUKEE"))).isEqualTo(7);
        assertThat(Finder.lengthOf(Optional.empty())).isZero();
    }
}
