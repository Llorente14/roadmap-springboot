package com.alterego.fondasi;

import java.util.List;
import java.util.function.Function;

/**
 * F11 — Interface, lambda, method reference.
 *
 * Bagian 2 (buat sendiri, file baru): class NicknameRule yang implements Rule.
 *   Constructor menerima int maxLength. accept -> true kalau panjang nickname <= maxLength.
 */
public final class Rules {

    private Rules() {
    }

    /** Lolos kalau umur >= minAge. Kembalikan lambda. */
    public static Rule minAge(int minAge) {
        throw new UnsupportedOperationException("TODO F11");
    }

    /** Lolos kalau rankPoints >= minPoints. */
    public static Rule minPoints(int minPoints) {
        throw new UnsupportedOperationException("TODO F11");
    }

    /** Lolos kalau a DAN b lolos. */
    public static Rule and(Rule a, Rule b) {
        throw new UnsupportedOperationException("TODO F11");
    }

    /** Kebalikan dari rule. */
    public static Rule not(Rule rule) {
        throw new UnsupportedOperationException("TODO F11");
    }

    /** Calon yang lolos rule, urutan tetap. Boleh pakai loop. */
    public static List<Candidate> select(List<Candidate> candidates, Rule rule) {
        throw new UnsupportedOperationException("TODO F11");
    }

    /** Function yang mengubah teks jadi huruf besar. Tulis dengan method reference (::). */
    public static Function<String, String> upper() {
        throw new UnsupportedOperationException("TODO F11");
    }
}
