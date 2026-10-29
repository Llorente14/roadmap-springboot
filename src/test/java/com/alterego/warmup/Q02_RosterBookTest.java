package com.alterego.warmup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Q02 · Record, Optional, Collection")
class Q02_RosterBookTest {

    private RosterBook book;

    @BeforeEach
    void setUp() {
        book = new RosterBook();
        book.add(new WarmupPlayer("yazukee", "mlbb", "YAZUKEE", "Jungler", "active", 2));
        book.add(new WarmupPlayer("nino", "mlbb", "NINO", "EXP Laner", "active", 1));
        book.add(new WarmupPlayer("benched", "mlbb", "BENCHED", "Roamer", "inactive", 3));
        book.add(new WarmupPlayer("aces", "mlbb", "ACES", "Head of Esports", "active", 20));
        book.add(new WarmupPlayer("aces", "pubgm", "ACES", "Manager", "active", 9));
    }

    @Test
    @DisplayName("id yang sama boleh ada di game berbeda (kasus 'aces')")
    void sameIdDifferentGame() {
        assertThat(book.size()).isEqualTo(5);
        assertThat(book.find("mlbb", "aces")).map(WarmupPlayer::role).contains("Head of Esports");
        assertThat(book.find("pubgm", "aces")).map(WarmupPlayer::role).contains("Manager");
    }

    @Test
    @DisplayName("game+id yang sama ditolak dengan IllegalStateException")
    void duplicateRejected() {
        assertThatThrownBy(() -> book.add(new WarmupPlayer("nino", "mlbb", "NINO2", null, "active", 1)))
                .isInstanceOf(IllegalStateException.class);
        assertThat(book.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("find yang tidak ketemu mengembalikan Optional kosong")
    void findMissing() {
        assertThat(book.find("cs2", "nino")).isEmpty();
        assertThat(book.find("mlbb", "nobody")).isEmpty();
    }

    @Test
    @DisplayName("active() hanya status active, urut displayOrder")
    void activeSorted() {
        assertThat(book.active("mlbb"))
                .extracting(WarmupPlayer::id)
                .containsExactly("nino", "yazukee", "aces");
        assertThat(book.active("cs2")).isEmpty();
    }
}
