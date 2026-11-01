package com.alterego.fondasi;

import java.util.ArrayList;
import java.util.List;

/**
 * F09 — Exception.
 *
 * Bagian 1: isi parseKills dan parseOrZero di bawah.
 *
 * Bagian 2 (buat sendiri):
 * - class ScoreFormatException di package ini (file baru), berupa CHECKED
 * exception.
 * - di class Parser ini: public static int[] parseScore(String score) throws
 * ScoreFormatException
 * "13-7" -> {13, 7}
 * " 2 - 0 " -> {2, 0} (spasi di sekitar angka diabaikan)
 * null, "13", "a-b", "1-2-3" -> lempar ScoreFormatException yang pesannya
 * menyebut input
 */
public final class Parser {

    private Parser() {
    }

    /**
     * Ubah teks menjadi jumlah kill. Spasi di tepi diabaikan.
     * null / kosong / negatif / bukan angka -> IllegalArgumentException.
     * Untuk "bukan angka": tangkap exception bawaan Java, lalu lempar
     * IllegalArgumentException
     * dengan exception bawaan itu sebagai cause.
     */
    public static int parseKills(String text) {
        try {
            int kills = Integer.parseInt(text.trim());
            if (kills < 0) {
                throw new IllegalArgumentException();
            }
            return kills;
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Menangkap:", e);
        }

    }

    /**
     * Seperti parseKills, tapi semua kegagalan menghasilkan 0 (pakai try/catch).
     */
    public static int parseOrZero(String text) {
        try {

            int kills = Integer.parseInt(text.trim());
            return Math.max(kills, 0);
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static int[] parseScore(String score) throws ScoreFormatException {
        if (score == null) {
            throw new ScoreFormatException(score);
        }
        int[] hasil = new int[2];
        String[] temp = score.split("-");
        if (temp.length != 2) {
            throw new ScoreFormatException(score);
        }
        try {
            hasil[0] = Integer.parseInt(temp[0].trim());
            hasil[1] = Integer.parseInt(temp[1].trim());
            return hasil;
        } catch (NumberFormatException e) {
            throw new ScoreFormatException(score);
        }

    }
}
