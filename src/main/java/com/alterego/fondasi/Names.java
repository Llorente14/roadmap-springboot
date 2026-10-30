package com.alterego.fondasi;

/**
 * F03 — Method bawaan String.
 */
public final class Names {

    private Names() {
    }

    /**
     * Rapikan tag: buang spasi di tepi, spasi berulang di tengah jadi satu, huruf
     * besar semua.
     * " ae nino " -> "AE NINO"
     */
    public static String cleanTag(String raw) {
        return raw.trim().replaceAll("\\s+", " ").toUpperCase();

    }

    /**
     * Inisial huruf besar dari setiap kata.
     * "Muhammad Nino Pratama" -> "MNP", " nino pratama " -> "NP"
     */
    public static String initials(String fullName) {
        String[] cleanText = fullName.trim().replaceAll("//s+", " ").toUpperCase().split("\\s+");
        StringBuilder nickname = new StringBuilder();
        for (int i = 0; i < cleanText.length; i++) {

            nickname.append(cleanText[i].substring(0, 1));
        }

        return nickname.toString();
    }

    /**
     * Bagian sebelum titik pertama. "AE.NINO" -> "AE", "AE.NINO.ALT" -> "AE".
     * Tidak ada titik -> "" (string kosong).
     */
    public static String teamOf(String tag) {
        int titik = tag.indexOf(".");
        if (titik == -1) {
            return "";
        }
        return tag.substring(0, titik);
    }

    /**
     * Nickname valid: 3–12 karakter, hanya huruf, angka, atau underscore. null ->
     * false.
     * Petunjuk: String punya method yang mencocokkan SELURUH teks dengan regex.
     */
    public static boolean isValidNickname(String nickname) {
        if (nickname == "" || nickname == null) {
            return false;
        }
        nickname.replaceAll("//s+", "");
        return nickname.matches("[a-zA-Z0-9_]{3,12}");
    }

    /**
     * Jumlah huruf vokal (a, i, u, e, o), besar atau kecil. countVowels("YAZUKEE")
     * -> 4
     */
    public static int countVowels(String text) {
        int angka = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if ("aiueo".indexOf(c) != -1) {
                angka++;
            }
        }
        return angka;
    }
}
