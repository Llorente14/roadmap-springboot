package com.alterego.fondasi;

import java.util.ArrayList;

import java.util.List;
import java.util.function.Function;

/**
 * F11 — Interface, lambda, method reference.
 *
 * Bagian 2 (buat sendiri, file baru): class NicknameRule yang implements Rule.
 * Constructor menerima int maxLength. accept -> true kalau panjang nickname <=
 * maxLength.
 */
public final class Rules {

    private Rules() {
    }

    /** Lolos kalau umur >= minAge. Kembalikan lambda. */
    public static Rule minAge(int minAge) {
        return c -> c.age() >= minAge;
    }

    /** Lolos kalau rankPoints >= minPoints. */
    public static Rule minPoints(int minPoints) {

        return c -> c.rankPoints() >= minPoints;
    }

    /** Lolos kalau a DAN b lolos. */
    public static Rule and(Rule a, Rule b) {
        return c -> a.accept(c) && b.accept(c);

    }

    /** Kebalikan dari rule. */
    public static Rule not(Rule rule) {
        return c -> !rule.accept(c);
    }

    /** Calon yang lolos rule, urutan tetap. Boleh pakai loop. */
    public static List<Candidate> select(List<Candidate> candidates, Rule rule) {
        List<Candidate> cList = new ArrayList<>();
        for (Candidate c : candidates) {
            if (rule.accept(c)) {

                cList.add(c);
            }
        }
        return cList;
    }

    /**
     * Function yang mengubah teks jadi huruf besar. Tulis dengan method reference
     * (::).
     */
    public static Function<String, String> upper() {
        Function<String, String> rapi = String::toUpperCase;
        return rapi;
    }

    public static List<String> nickname(List<Candidate> lists) {
        List<String> hasil = new ArrayList<>();
        for (Candidate c : lists) {
            hasil.add(c.nickname());
        }
        return hasil;
    }

    public static Rule allOf(List<Rule> rules) {
        return c -> {
            for (Rule r : rules) {
                if (!r.accept(c)) {
                    return false;
                }
            }
            return true;
        };
    }
}
