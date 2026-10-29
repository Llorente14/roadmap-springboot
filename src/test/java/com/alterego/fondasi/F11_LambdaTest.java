package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F11 · Interface, lambda, method reference")
class F11_LambdaTest {

    private static final Candidate RAKA = new Candidate("raka", 17, 3200);
    private static final Candidate DIMAS = new Candidate("dimas_pro_99", 19, 2100);
    private static final Candidate BAYU = new Candidate("bayu", 15, 4100);

    @Test
    @DisplayName("minAge dan minPoints mengembalikan Rule")
    void simpleRules() {
        assertThat(Rules.minAge(16).accept(RAKA)).isTrue();
        assertThat(Rules.minAge(16).accept(BAYU)).isFalse();
        assertThat(Rules.minAge(17).accept(RAKA)).as("batas inklusif").isTrue();
        assertThat(Rules.minPoints(3000).accept(DIMAS)).isFalse();
        assertThat(Rules.minPoints(3200).accept(RAKA)).isTrue();
    }

    @Test
    @DisplayName("and dan not menggabungkan rule lain")
    void combinators() {
        Rule eligible = Rules.and(Rules.minAge(16), Rules.minPoints(3000));
        assertThat(eligible.accept(RAKA)).isTrue();
        assertThat(eligible.accept(DIMAS)).isFalse();
        assertThat(eligible.accept(BAYU)).isFalse();
        assertThat(Rules.not(eligible).accept(BAYU)).isTrue();
    }

    @Test
    @DisplayName("select menyaring dengan rule apa pun, termasuk lambda buatan test")
    void select() {
        List<Candidate> all = List.of(RAKA, DIMAS, BAYU);
        assertThat(Rules.select(all, Rules.minAge(16))).containsExactly(RAKA, DIMAS);
        assertThat(Rules.select(all, c -> c.nickname().startsWith("b"))).containsExactly(BAYU);
        assertThat(Rules.select(all, c -> false)).isEmpty();
    }

    @Test
    @DisplayName("upper() mengembalikan Function")
    void upper() {
        assertThat(Rules.upper().apply("nino")).isEqualTo("NINO");
    }

    @Test
    @DisplayName("class NicknameRule buatanmu implements Rule")
    void nicknameRule() {
        Class<?> type = Reflect.type("NicknameRule");
        assertThat(Rule.class.isAssignableFrom(type)).as("NicknameRule harus implements Rule").isTrue();

        Rule shortNames = (Rule) Reflect.create("NicknameRule", 6);
        assertThat(shortNames.accept(RAKA)).isTrue();
        assertThat(shortNames.accept(DIMAS)).isFalse();
        assertThat(Rules.select(List.of(RAKA, DIMAS, BAYU), Rules.and(shortNames, Rules.minAge(16)))).containsExactly(RAKA);
    }
}
