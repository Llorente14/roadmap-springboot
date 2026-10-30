package com.alterego.fondasi;

/**
 * F01 — Variabel, tipe data, operator.
 */
public final class MatchMath {

    private MatchMath() {
    }

    /**
     * Persentase menang. winRate(3, 4) -> 75.0, winRate(1, 3) -> 33.33...
     * games = 0 -> 0.0 (jangan sampai membagi dengan nol).
     */
    public static double winRate(int wins, int games) {
        if (games == 0) {
            return 0.0;
        }
        ;
        return wins * 100.0 / games;
    }

    /**
     * KDA = (kills + assists) / deaths. Kalau deaths 0, anggap deaths = 1.
     * kda(5, 2, 0) -> 2.5 (bukan 2!).
     */
    public static double kda(int kills, int deaths, int assists) {
        if (deaths == 0) {
            deaths = 1;
        }
        return (kills * 1.0 + assists) / deaths;
    }

    /** true kalau n genap. */
    public static boolean isEven(int n) {
        if (n % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /** Menit penuh dari jumlah detik. minutes(725) -> 12 */
    public static int minutes(int totalSeconds) {
        return totalSeconds / 60;
    }

    /** Sisa detik setelah menit penuh. seconds(725) -> 5 */
    public static int seconds(int totalSeconds) {
        return totalSeconds % 60;
    }
}
