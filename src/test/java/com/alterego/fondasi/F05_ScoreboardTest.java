package com.alterego.fondasi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("F05 · Class, field, object, record")
class F05_ScoreboardTest {

    @Test
    @DisplayName("data tetap ada di antara pemanggilan method (field, bukan variabel lokal)")
    void remembers() {
        Scoreboard board = new Scoreboard();
        board.add(10);
        board.add(25);
        board.add(5);

        assertThat(board.total()).isEqualTo(40);
        assertThat(board.rounds()).isEqualTo(3);
        assertThat(board.best()).isEqualTo(25);
    }

    @Test
    @DisplayName("scoreboard baru kosong")
    void empty() {
        Scoreboard board = new Scoreboard();
        assertThat(board.total()).isZero();
        assertThat(board.rounds()).isZero();
        assertThat(board.best()).isZero();
    }

    @Test
    @DisplayName("dua object tidak berbagi data (bukan static)")
    void independent() {
        Scoreboard ae = new Scoreboard();
        Scoreboard rival = new Scoreboard();
        ae.add(13);
        rival.add(7);
        rival.add(2);

        assertThat(ae.total()).isEqualTo(13);
        assertThat(rival.total()).isEqualTo(9);
        assertThat(ae.rounds()).isEqualTo(1);
    }

    @Test
    @DisplayName("reset mengosongkan, lalu bisa dipakai lagi")
    void reset() {
        Scoreboard board = new Scoreboard();
        board.add(30);
        board.reset();
        assertThat(board.total()).isZero();
        assertThat(board.best()).isZero();
        board.add(4);
        assertThat(board.total()).isEqualTo(4);
        assertThat(board.rounds()).isEqualTo(1);
    }

    @Test
    @DisplayName("skor negatif ditolak dan tidak tercatat")
    void negative() {
        Scoreboard board = new Scoreboard();
        board.add(3);
        assertThatThrownBy(() -> board.add(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThat(board.total()).isEqualTo(3);
        assertThat(board.rounds()).isEqualTo(1);
    }

    @Test
    @DisplayName("record MatchResult(home, away, homeScore, awayScore) dengan method winner()")
    void matchResult() {
        assertThat(Reflect.type("MatchResult").isRecord())
                .as("MatchResult harus berupa record").isTrue();

        Object win = Reflect.create("MatchResult", "AE", "RRQ", 2, 1);
        Object lose = Reflect.create("MatchResult", "AE", "EVOS", 0, 2);
        Object draw = Reflect.create("MatchResult", "AE", "ONIC", 1, 1);

        assertThat(Reflect.prop(win, "home")).isEqualTo("AE");
        assertThat(Reflect.prop(win, "awayScore")).isEqualTo(1);
        assertThat(Reflect.call(win, "winner")).isEqualTo("AE");
        assertThat(Reflect.call(lose, "winner")).isEqualTo("EVOS");
        assertThat(Reflect.call(draw, "winner")).isEqualTo("DRAW");
    }

    @Test
    @DisplayName("dua record dengan isi sama dianggap equal")
    void recordEquality() {
        Object a = Reflect.create("MatchResult", "AE", "RRQ", 2, 1);
        Object b = Reflect.create("MatchResult", "AE", "RRQ", 2, 1);
        assertThat(a).isEqualTo(b).isNotSameAs(b);
    }
}
