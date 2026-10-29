package com.alterego.fondasi;

/**
 * F09 — Exception.
 *
 * Bagian 1: isi parseKills dan parseOrZero di bawah.
 *
 * Bagian 2 (buat sendiri):
 *   - class ScoreFormatException di package ini (file baru), berupa CHECKED exception.
 *   - di class Parser ini: public static int[] parseScore(String score) throws ScoreFormatException
 *       "13-7"    -> {13, 7}
 *       " 2 - 0 " -> {2, 0}   (spasi di sekitar angka diabaikan)
 *       null, "13", "a-b", "1-2-3" -> lempar ScoreFormatException yang pesannya menyebut input
 */
public final class Parser {

    private Parser() {
    }

    /**
     * Ubah teks menjadi jumlah kill. Spasi di tepi diabaikan.
     * null / kosong / negatif / bukan angka -> IllegalArgumentException.
     * Untuk "bukan angka": tangkap exception bawaan Java, lalu lempar IllegalArgumentException
     * dengan exception bawaan itu sebagai cause.
     */
    public static int parseKills(String text) {
        throw new UnsupportedOperationException("TODO F09");
    }

    /** Seperti parseKills, tapi semua kegagalan menghasilkan 0 (pakai try/catch). */
    public static int parseOrZero(String text) {
        throw new UnsupportedOperationException("TODO F09");
    }
}
