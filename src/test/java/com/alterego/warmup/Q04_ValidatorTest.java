package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q04 · Interface, polymorphism, generics")
class Q04_ValidatorTest {

    private final PlayerValidator validator = new PlayerValidator();

    @Test
    @DisplayName("pemain valid menghasilkan list kosong")
    void valid() {
        assertThat(validator.validate(new WarmupPlayer("nino", "mlbb", "NINO", "EXP Laner", "active", 1))).isEmpty();
        assertThat(validator.validate(new WarmupPlayer("badru", "pubgm", "BADRU", null, "active", 1))).isEmpty();
    }

    @Test
    @DisplayName("setiap aturan yang dilanggar menghasilkan satu error berawalan nama field")
    void allErrors() {
        List<String> errors = validator.validate(new WarmupPlayer("Bad ID!", "dota2", "   ", null, "active", 0));
        assertThat(errors).hasSize(4);
        assertThat(errors).anyMatch(e -> e.startsWith("id:"));
        assertThat(errors).anyMatch(e -> e.startsWith("game:"));
        assertThat(errors).anyMatch(e -> e.startsWith("nickname:"));
        assertThat(errors).anyMatch(e -> e.startsWith("displayOrder:"));
    }

    @Test
    @DisplayName("PlayerValidator bisa dipakai lewat tipe interface (polymorphism)")
    void polymorphism() {
        Validator<WarmupPlayer> asInterface = validator;
        assertThat(asInterface.validate(new WarmupPlayer("", "mlbb", "X", null, "active", 1)))
                .singleElement().asString().startsWith("id:");
    }

    @Test
    @DisplayName("CompositeValidator menjalankan semua validator (termasuk lambda) dan menggabungkan error berurutan")
    void composite() {
        Validator<WarmupPlayer> noStaff = p -> "staff".equals(p.role()) ? List.of("role: staff tidak boleh") : List.of();
        Validator<WarmupPlayer> composite = CompositeValidator.of(validator, noStaff);

        List<String> errors = composite.validate(new WarmupPlayer("ok", "mlbb", "", "staff", "active", 1));

        assertThat(errors).hasSize(2);
        assertThat(errors.get(0)).startsWith("nickname:");
        assertThat(errors.get(1)).isEqualTo("role: staff tidak boleh");
    }

    @Test
    @DisplayName("CompositeValidator generik: bekerja untuk tipe apa pun, bukan hanya WarmupPlayer")
    void compositeGeneric() {
        Validator<String> notEmpty = s -> s.isEmpty() ? List.of("text: kosong") : List.of();
        Validator<String> shortText = s -> s.length() > 5 ? List.of("text: terlalu panjang") : List.of();

        assertThat(CompositeValidator.of(notEmpty, shortText).validate("abc")).isEmpty();
        assertThat(CompositeValidator.of(notEmpty, shortText).validate("panjang sekali")).containsExactly("text: terlalu panjang");
        assertThat(CompositeValidator.<String>of().validate("apa saja")).isEmpty();
    }
}
