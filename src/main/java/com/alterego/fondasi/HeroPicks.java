package com.alterego.fondasi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * F07 — Map dan Set.
 */
public final class HeroPicks {

    private HeroPicks() {
    }

    /**
     * Hitung berapa kali tiap hero dipilih. ["Fanny", "Ling", "Fanny"] -> {Fanny=2,
     * Ling=1}
     */
    public static Map<String, Integer> countPicks(List<String> picks) {
        Map<String, Integer> cpick = new HashMap<>();
        for (String name : picks) {
            cpick.put(name, cpick.getOrDefault(name, 0) + 1);

        }
        return cpick;
    }

    /** Hero tanpa duplikat. */
    public static Set<String> uniqueHeroes(List<String> picks) {
        // Manual
        // Set<String> unique = new HashSet<>();
        // for (String name : picks) {
        // unique.add(name);
        // }
        // return unique;

        // Auto
        return new HashSet<>(picks);
    }

    /** Jumlah pick seorang hero dari hasil countPicks. Hero tidak ada -> 0. */
    public static int picksOf(Map<String, Integer> counts, String hero) {
        // if(counts.containsKey(hero)){
        // return 0;
        // }
        return counts.getOrDefault(hero, 0);
    }

    /**
     * Hero yang paling sering dipilih. Seri -> yang abjadnya paling awal. List
     * kosong -> "".
     */
    public static String mostPicked(List<String> picks) {
        if (picks.size() == 0 || picks.isEmpty()) {
            return "";
        }
        Map<String, Integer> heroPick = countPicks(picks);

        String heroMax = "";
        int maxScore = 0;
        for (Map.Entry<String, Integer> hero : heroPick.entrySet()) {
            String currHero = hero.getKey();
            int currScore = hero.getValue();
            if (maxScore < currScore) {
                maxScore = currScore;
                heroMax = currHero;

            } else if (maxScore == currScore) {
                if (heroMax.compareToIgnoreCase(currHero) > 0) {
                    maxScore = currScore;
                    heroMax = currHero;
                }
            }
        }
        return heroMax;

    }

    /** Hero yang dipilih di KEDUA list (irisan). */
    public static Set<String> pickedByBoth(List<String> teamA, List<String> teamB) {
        Set<String> irisan = new HashSet<>(teamA);
        irisan.retainAll(teamB);

        return irisan;
    }
}
