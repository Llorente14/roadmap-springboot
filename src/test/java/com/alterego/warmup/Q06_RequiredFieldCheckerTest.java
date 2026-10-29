package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q06 · Annotation & reflection")
class Q06_RequiredFieldCheckerTest {

    static class PlayerForm {
        @Required
        String id;
        @Required
        String nickname;
        String role;
        @Required
        Integer displayOrder;

        PlayerForm(String id, String nickname, String role, Integer displayOrder) {
            this.id = id;
            this.nickname = nickname;
            this.role = role;
            this.displayOrder = displayOrder;
        }
    }

    @Test
    @DisplayName("@Required harus terbaca saat runtime")
    void retention() {
        Retention retention = Required.class.getAnnotation(Retention.class);
        assertThat(retention)
                .as("Tanpa @Retention yang tepat, anotasi hilang sebelum program berjalan")
                .isNotNull();
        assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("objek lengkap: tidak ada yang hilang")
    void complete() {
        assertThat(RequiredFieldChecker.missing(new PlayerForm("nino", "NINO", null, 1))).isEmpty();
    }

    @Test
    @DisplayName("null dan string kosong/spasi dilaporkan, field tanpa @Required diabaikan")
    void missingFields() {
        assertThat(RequiredFieldChecker.missing(new PlayerForm(null, "   ", null, null)))
                .containsExactly("id", "nickname", "displayOrder");
    }

    @Test
    @DisplayName("objek tanpa @Required sama sekali: list kosong")
    void noAnnotations() {
        assertThat(RequiredFieldChecker.missing("teks biasa")).isEmpty();
    }
}
