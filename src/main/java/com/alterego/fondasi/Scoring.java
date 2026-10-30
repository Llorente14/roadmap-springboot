package com.alterego.fondasi;

/**
 * F04 — Membuat method sendiri.
 *
 * Class ini SENGAJA kosong. Tulis sendiri method berikut (semuanya public
 * static):
 *
 * points(int kills, int assists) -> int : kills x 2 + assists
 * average(int a, int b) -> double : rata-rata dua angka
 * average(int a, int b, int c) -> double : rata-rata tiga angka (overloading:
 * nama sama, parameter beda)
 * label(String nickname, int points) -> String : "NINO (12 pts)"
 *
 * Test memanggil method-method ini berdasarkan nama dan jumlah parameter.
 */
public final class Scoring {

    private Scoring() {

    }

    public static int points(int kills, int assists) {
        return kills * 2 + assists;
    }

    public static double average(int a, int b) {
        return (a + b) / 2.0;
    }

    public static double average(int a, int b, int c) {
        return (a + b + c) / 3.0;
    }

    public static String label(String nickname, int points) {
        return nickname + " (" + points + " pts)";
    }
}
