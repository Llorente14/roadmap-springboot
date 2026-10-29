package com.alterego.fondasi;

/**
 * F03 — Method bawaan String.
 */
public final class Names {

    private Names() {
    }

    /**
     * Rapikan tag: buang spasi di tepi, spasi berulang di tengah jadi satu, huruf besar semua.
     * "  ae   nino " -> "AE NINO"
     */
    public static String cleanTag(String raw) {
        throw new UnsupportedOperationException("TODO F03");
    }

    /**
     * Inisial huruf besar dari setiap kata.
     * "Muhammad Nino Pratama" -> "MNP", "  nino   pratama " -> "NP"
     */
    public static String initials(String fullName) {
        throw new UnsupportedOperationException("TODO F03");
    }

    /**
     * Bagian sebelum titik pertama. "AE.NINO" -> "AE", "AE.NINO.ALT" -> "AE".
     * Tidak ada titik -> "" (string kosong).
     */
    public static String teamOf(String tag) {
        throw new UnsupportedOperationException("TODO F03");
    }

    /**
     * Nickname valid: 3–12 karakter, hanya huruf, angka, atau underscore. null -> false.
     * Petunjuk: String punya method yang mencocokkan SELURUH teks dengan regex.
     */
    public static boolean isValidNickname(String nickname) {
        throw new UnsupportedOperationException("TODO F03");
    }

    /** Jumlah huruf vokal (a, i, u, e, o), besar atau kecil. countVowels("YAZUKEE") -> 4 */
    public static int countVowels(String text) {
        throw new UnsupportedOperationException("TODO F03");
    }
}
